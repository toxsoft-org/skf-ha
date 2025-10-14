package org.toxsoft.skf.ha.skatlet;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.ha.skatlet.ISkResources.*;

import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.ctx.impl.*;
import org.toxsoft.core.tslib.bricks.threadexec.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.impl.*;

/**
 * HA (High Availability) skatlet Writer for GBH.
 *
 * @author mvk
 */
public class SkHaSkatlet
    extends SkatletBase {

  @SuppressWarnings( "unused" )
  private ITsThreadExecutor threadExecutor;
  // private SkHaProcessor haProcessor;
  private ISkConnection connection;

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
  protected ValidationResult doSetContext( ITsContextRo aEnviron ) {
    super.doSetContext( aEnviron );
    connection = createConnection( getClass().getSimpleName(), new TsContext() );
    ISkCoreApi coreApi = connection.coreApi();
    threadExecutor = SkThreadExecutorService.getExecutor( coreApi );
    // haProcessor = new SkHaProcessor( coreApi, LoggerWrapper.getLogger( SkHaProcessor.class ) );
    return ValidationResult.SUCCESS;
  }

  @Override
  public void start() {
    super.start();
    // threadExecutor.syncExec( () -> haProcessor.start() );
    logger().info( MSG_HA_PROCESSOR_IS_RUNNING, id() );
  }

  @Override
  public void doJob() {
    super.doJob();
    // threadExecutor.syncExec( () -> haProcessor.doJob() );
  }

  @Override
  public boolean queryStop() {
    super.queryStop();
    // threadExecutor.syncExec( () -> haProcessor.queryStop() );
    SkThreadExecutorService.getExecutor( connection.coreApi() ).syncExec( () -> connection.close() );

    // return haProcessor.isStopped();
    return true;
  }

  @Override
  public boolean isStopped() {
    // return haProcessor.isStopped();
    return true;
  }

  @Override
  public void destroy() {
    super.destroy();
    // threadExecutor.syncExec( () -> haProcessor.destroy() );
  }
}
