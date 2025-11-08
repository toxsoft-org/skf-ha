package org.toxsoft.skf.ha.skatlet;

import static org.toxsoft.core.log4j.LoggerWrapper.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.ha.skatlet.ISkResources.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.ctx.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.ha.lib.*;
import org.toxsoft.skf.ha.lib.impl.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.devapi.*;
import org.toxsoft.uskat.core.impl.*;

/**
 * HA (High Availability) skatlet Writer for GBH.
 *
 * @author mvk
 */
public class SkHaSkatlet
    extends SkatletBase
    implements ITsContextListener {

  private final SkWorkerRegistry              workers     = new SkWorkerRegistry();
  private final IStringMapEdit<ISkConnection> connections = new StringMap<>();

  /**
   * Constructor.
   */
  public SkHaSkatlet() {
    super( SkHaSkatlet.class.getSimpleName(), OptionSetUtils.createOpSet( //
        TSID_NAME, STR_N_SKATLET, //
        TSID_DESCRIPTION, STR_D_SKATLET //
    ) );
  }

  // ------------------------------------------------------------------------------------
  // SkatletBase
  //
  @Override
  protected ValidationResult doInitialize() {
    // Инициализация HA-библиотки
    SkfHaLibUtils.initialize();

    return ValidationResult.SUCCESS;
  }

  @Override
  public void start() {
    super.start();

    // Создание и регистрация компонента: chains
    workers.put( ISkHaWorker.WORKER_ID, new SkHaWorker() );

    // Разделяемый между компонентами контекст
    TsContext sharedCtx = new TsContext();
    // Установка слушателя контекста для размещения значений параметров в информации о бекенде
    sharedCtx.addContextListener( this );

    // Инициализация компонентов
    ISkWorkerConfig chainConfig = new SkWorkerConfigBase( ISkHaWorker.WORKER_ID );
    initWorker( ISkHaWorker.WORKER_ID, workers, sharedCtx, chainConfig, getSharedConnection() );

    // Запуск компонент
    for( ISkWorker worker : workers ) {
      worker.start();
    }
    logger().info( "%s: start(). ha workers count = %d", id(), Integer.valueOf( workers.size() ) ); //$NON-NLS-1$
  }

  @Override
  public void doJob() {
    // for( ISkWorker worker : workers ) {
    // setBackendInfoParam( BACKEND_INFO_PARAM_DOJOB_WORKER, avStr( worker.id() ) );
    // worker.doJob();
    // }
    // setBackendInfoParam( BACKEND_INFO_PARAM_DOJOB_WORKER, AV_STR_EMPTY );
  }

  @Override
  public boolean queryStop() {
    super.queryStop();
    for( ISkWorker worker : workers ) {
      worker.queryStop();
    }
    for( ISkConnection connection : connections ) {
      connection.close();
    }
    logger().info( "%s: queryStop(). workers (%d) are closed", id(), Integer.valueOf( workers.size() ) ); //$NON-NLS-1$

    return true;
  }

  @Override
  public boolean isStopped() {
    boolean retValue = true;
    for( ISkWorker worker : workers ) {
      retValue &= worker.isStopped();
    }
    for( ISkConnection connection : connections ) {
      retValue &= (connection.state() != ESkConnState.CLOSED);
    }
    return retValue;
  }

  @Override
  public void destroy() {
    super.destroy();
    for( ISkWorker worker : workers ) {
      worker.destroy();
    }
    for( ISkConnection connection : connections ) {
      connection.close();
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsContextListener
  //
  @Override
  public <C extends ITsContextRo> void onContextRefChanged( C aSource, String aName, Object aRef ) {
    // nop
  }

  @Override
  public <C extends ITsContextRo> void onContextOpChanged( C aSource, String aId, IAtomicValue aValue ) {
    // Установка значения параметра в информации о бекенде
    setBackendInfoParam( aId, aValue );
  }

  // ------------------------------------------------------------------------------------
  // private methods
  //

  /**
   * Инициализация компонента обработки данных.
   *
   * @param aWorkerId String идентификатор компоненты
   * @param aRegistry {@link SkWorkerRegistry} реестр созданных компонент.
   * @param aSharedContext {@link ITsContext} контекст разделяемый между компонентами.
   * @param aInfo {@link ISkWorkerConfig} конфигурация компонента
   * @param aConnection {@link ISkConnection} соединение для работы компонента.
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  private static void initWorker( String aWorkerId, SkWorkerRegistry aRegistry, ITsContext aSharedContext,
      ISkWorkerConfig aInfo, ISkConnection aConnection ) {
    TsNullArgumentRtException.checkNulls( aWorkerId, aRegistry, aSharedContext, aInfo, aConnection );
    ISkWorker worker = aRegistry.getWorker( aWorkerId );
    TsContext ctx = new TsContext();
    ISkWorkerHardConstants.REFDEF_WORKER_CORE_API.setRef( ctx, aConnection.coreApi() );
    ISkWorkerHardConstants.REFDEF_WORKER_REGISTRY.setRef( ctx, aRegistry );
    ISkWorkerHardConstants.REFDEF_WORKER_SHARED_CONTEXT.setRef( ctx, aSharedContext );
    ISkWorkerHardConstants.REFDEF_WORKER_LOGGER.setRef( ctx, getLogger( getLoggerName( worker, aInfo ) ) );
    worker.setContext( ctx );
    worker.setConfiguration( aInfo );
  }

  /**
   * Формирует имя журнала для компонента.
   *
   * @param aWorker {@link ISkWorker} компонент
   * @param aInfo {@link ISkWorkerConfig} конфигурация компонента.
   * @return String имя журнала
   * @throws TsNullArgumentRtException аргумент = null
   */
  @SuppressWarnings( "nls" )
  public static String getLoggerName( ISkWorker aWorker, ISkWorkerConfig aInfo ) {
    TsNullArgumentRtException.checkNulls( aWorker, aInfo );
    if( aInfo.id().startsWith( aWorker.getClass().getSimpleName() ) ) {
      return aInfo.id().replaceAll( "\\.", "_" );
    }
    return aWorker.getClass().getSimpleName() + '_' + aInfo.id().replaceAll( "\\.", "_" );
  }
}
