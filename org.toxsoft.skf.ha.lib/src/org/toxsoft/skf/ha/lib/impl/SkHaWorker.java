package org.toxsoft.skf.ha.lib.impl;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.skf.ha.lib.ISkHaServiceConstants.*;
import static org.toxsoft.skf.ha.lib.impl.ISkResources.*;
import static org.toxsoft.skf.ha.lib.l10n.ISkHaServiceSharedResources.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.ha.lib.*;
import org.toxsoft.uskat.core.api.cmdserv.*;
import org.toxsoft.uskat.core.api.evserv.*;
import org.toxsoft.uskat.core.devapi.*;
import org.toxsoft.uskat.core.impl.*;
import org.toxsoft.uskat.core.utils.*;

/**
 * {@link ISkHaWorker} implementation.
 *
 * @author mvk
 */
public class SkHaWorker
    extends SkWorkerBase<ISkWorkerConfig>
    implements ISkHaWorker, ISkCommandExecutor {

  /**
   * Конструктор.
   *
   * @throws TsNullArgumentRtException аргумент = null
   */
  public SkHaWorker() {
    super( WORKER_ID );
  }

  // ------------------------------------------------------------------------------------
  // SitrolWorkerBase
  //

  @SuppressWarnings( "boxing" )
  @Override
  protected void doWorkerStart() {
    ISkHaService haService = coreApi().getService( ISkHaService.SERVICE_ID );
    // Инициализация кластеров
    for( ISkHaCluster cluster : haService.clusters() ) {
      // Primary
      Skid primaryId = cluster.primaryMember();
      // Первый член кластера является primary (реализация)
      cluster.writeRtdataIfOpen( RTDID_PRIMARY, avValobj( primaryId ) );
    }
    // Определение исполнителя команд установки в кластере primary-member
    SkCommandUtils.defineCmdExecutor( coreApi(), this, CLSID_CLUSTER );
    // Конфигурация текущих данных поездов
    logger().info( MSG_WORKER_STARTED, haService.clusters().size() );
  }

  @Override
  protected void doWorkerConfigChanged( ISkWorkerConfig aPrevConfig, ISkWorkerConfig aNewConfig ) {
    // nop
  }

  @Override
  protected void doWorkerOnCurrData( IMap<Gwid, IAtomicValue> aNewValues ) {
    // nop
  }

  @Override
  protected void doWorkerDoJob() {
    // nop
  }

  @Override
  protected void doWorkerQueryStop() {
    // Дерегистрация исполнителя команд
    SkCommandUtils.removeCmdExecutor( coreApi(), SkHaWorker.this );
  }

  // ------------------------------------------------------------------------------------
  // ISkCommandExecutor
  //

  @Override
  public void executeCommand( IDtoCommand aCmd ) {
    logger().info( MSG_CMD_RECEVIED, aCmd );
    long currTime = System.currentTimeMillis();
    ISkHaService haService = coreApi().getService( ISkHaService.SERVICE_ID );
    String cmdClassId = aCmd.cmdGwid().classId();
    String clusterId = aCmd.cmdGwid().strid();
    String cmdId = aCmd.cmdGwid().propId();
    Gwid execAuthorId = Gwid.createObj( coreApi().getCurrentUserInfo().userSkid() );
    SkCommandState retValue = null;
    try {
      switch( cmdClassId ) {
        case CLSID_CLUSTER:
          switch( cmdId ) {
            case CMDID_SET_PRIMARY:
              ISkHaCluster cluster = haService.findCluster( clusterId );
              if( cluster == null ) {
                String cause = String.format( ERR_CLUSTER_NOT_FOUND, clusterId );
                retValue = new SkCommandState( currTime, ESkCommandState.FAILED, cause, execAuthorId );
                logger().error( cause );
                break;
              }
              Skid oldPrimaryId = cluster.primaryMember();
              Skid newPrimaryId = aCmd.argValues().getValobj( CMDARGID_PRIMARY_ID );
              if( oldPrimaryId.equals( newPrimaryId ) ) {
                retValue = new SkCommandState( currTime, ESkCommandState.SUCCESS );
                return;
              }
              // set primary. aRemovedSkids = null
              coreApi().linkService().defineLink( cluster.skid(), LNKID_PRIMARY, null, new SkidList( newPrimaryId ) );
              // set RtData
              cluster.writeRtdataIfOpen( RTDID_PRIMARY, avValobj( newPrimaryId ) );
              // fire event
              Gwid eventGwid = Gwid.createEvent( cluster.skid(), EVID_PRIMARY_CHANGED );
              IOptionSetEdit params = new OptionSet();
              params.setValobj( EVPRMINF_OLD_PRIMARY_ID, oldPrimaryId );
              params.setValobj( EVPRMINF_NEW_PRIMARY_ID, newPrimaryId );
              SkEvent event = new SkEvent( currTime, eventGwid, params );
              coreApi().eventService().fireEvent( event );
              retValue = new SkCommandState( currTime, ESkCommandState.SUCCESS );
              break;
            default:
              String cause = String.format( ERR_UNKNOWN_CMD_RECEVIED, cmdClassId, aCmd );
              retValue = new SkCommandState( currTime, ESkCommandState.FAILED, cause, execAuthorId );
              logger().error( cause );
              break;
          }
          break;
        default:
          String cause = String.format( ERR_UNKNOWN_CMD_RECEVIED, cmdClassId, aCmd );
          retValue = new SkCommandState( currTime, ESkCommandState.FAILED, cause, execAuthorId );
          logger().error( cause );
      }
    }
    catch( Throwable e ) {
      String cause = e.getLocalizedMessage();
      retValue = new SkCommandState( currTime, ESkCommandState.FAILED, cause, execAuthorId );
      logger().error( ERR_UNEXPECTED, aCmd, cause );
    }
    coreApi().cmdService().changeCommandState( new DtoCommandStateChangeInfo( aCmd.instanceId(), retValue ) );
  }

  @Override
  public ValidationResult canExecuteCommand( Gwid aCmdGwid, Skid aAuthorSkid, IOptionSet aArgs ) {
    String cmdClassId = aCmdGwid.classId();
    String cmdObjId = aCmdGwid.strid();
    String cmdId = aCmdGwid.propId();
    ISkHaService haService = coreApi().getService( ISkHaService.SERVICE_ID );
    switch( cmdClassId ) {
      case CLSID_CLUSTER:
        switch( cmdId ) {
          case CMDID_SET_PRIMARY: {
            Skid newPrimaryId = aArgs.getValobj( CMDARGID_PRIMARY_ID );
            ISkHaCluster cluster = haService.findCluster( cmdObjId );
            if( cluster.primaryMember().equals( newPrimaryId ) ) {
              return ValidationResult.error( ERR_IS_ALREADY_PRIMARY, newPrimaryId );
            }
            if( !cluster.memberIds().hasElem( newPrimaryId ) ) {
              return ValidationResult.error( ERR_IS_NOT_MEMBER, newPrimaryId );
            }
            break;
          }
          default:
            break;
        }
        return ValidationResult.SUCCESS;
      default:
        break;
    }
    return ValidationResult.SUCCESS;
  }

  // ------------------------------------------------------------------------------------
  // private methods
  //

}
