package org.toxsoft.skf.ha.lib.impl;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.skf.ha.lib.ISkHaServiceConstants.*;
import static org.toxsoft.skf.ha.lib.l10n.ISkHaServiceSharedResources.*;
import static org.toxsoft.uskat.core.ISkHardConstants.*;

import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.diff.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.ha.lib.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.rtdserv.*;
import org.toxsoft.uskat.core.devapi.*;
import org.toxsoft.uskat.core.impl.*;
import org.toxsoft.uskat.core.impl.dto.*;

/**
 * {@link ISkHaService} implementation.
 *
 * @author mvk
 */
public class SkHaService
    extends AbstractSkService
    implements ISkHaService {

  /**
   * Service creator singleton.
   */
  public static final ISkServiceCreator<AbstractSkService> CREATOR = SkHaService::new;

  /**
   * {@link ISkHaService#svs()} implementation.
   */
  private static class Svs
      extends AbstractTsValidationSupport<ISkHaServiceValidator>
      implements ISkHaServiceValidator {

    @Override
    public ISkHaServiceValidator validator() {
      return this;
    }

    @Override
    public ValidationResult canCreateCluster( String aClusterId, String aName, String aDescription, Skid aPrimaryMember,
        ISkidList aMembers ) {
      TsNullArgumentRtException.checkNulls( aClusterId, aName, aDescription, aPrimaryMember, aMembers );
      StridUtils.checkValidIdPath( aClusterId );
      ValidationResult vr = ValidationResult.SUCCESS;
      for( ISkHaServiceValidator v : validatorsList() ) {
        vr = ValidationResult.firstNonOk( vr,
            v.canCreateCluster( aClusterId, aName, aDescription, aPrimaryMember, aMembers ) );
        if( vr.isError() ) {
          break;
        }
      }
      return vr;
    }

    @Override
    public ValidationResult canRemoveCluster( String aClusterId ) {
      StridUtils.checkValidIdPath( aClusterId );
      ValidationResult vr = ValidationResult.SUCCESS;
      for( ISkHaServiceValidator v : validatorsList() ) {
        vr = ValidationResult.firstNonOk( vr, v.canRemoveCluster( aClusterId ) );
        if( vr.isError() ) {
          break;
        }
      }
      return vr;
    }

    @Override
    public ValidationResult canEditCluster( ISkHaCluster aCluster, String aName, String aDescription ) {
      TsNullArgumentRtException.checkNulls( aCluster, aName, aDescription );
      ValidationResult vr = ValidationResult.SUCCESS;
      for( ISkHaServiceValidator v : validatorsList() ) {
        vr = ValidationResult.firstNonOk( vr, v.canEditCluster( aCluster, aName, aDescription ) );
        if( vr.isError() ) {
          break;
        }
      }
      return vr;
    }

    @Override
    public ValidationResult canSetOwner( ISkHaCluster aCluster, Skid aOwnerId ) {
      TsNullArgumentRtException.checkNulls( aCluster, aOwnerId );
      ValidationResult vr = ValidationResult.SUCCESS;
      for( ISkHaServiceValidator v : validatorsList() ) {
        vr = ValidationResult.firstNonOk( vr, v.canSetOwner( aCluster, aOwnerId ) );
        if( vr.isError() ) {
          break;
        }
      }
      return vr;
    }

    @Override
    public ValidationResult canAddMember( ISkHaCluster aCluster, Skid aMemberId ) {
      TsNullArgumentRtException.checkNulls( aCluster, aMemberId );
      ValidationResult vr = ValidationResult.SUCCESS;
      for( ISkHaServiceValidator v : validatorsList() ) {
        vr = ValidationResult.firstNonOk( vr, v.canAddMember( aCluster, aMemberId ) );
        if( vr.isError() ) {
          break;
        }
      }
      return vr;
    }

    @Override
    public ValidationResult canRemoveMember( ISkHaCluster aCluster, Skid aMemberId ) {
      TsNullArgumentRtException.checkNulls( aCluster, aMemberId );
      ValidationResult vr = ValidationResult.SUCCESS;
      for( ISkHaServiceValidator v : validatorsList() ) {
        vr = ValidationResult.firstNonOk( vr, v.canRemoveMember( aCluster, aMemberId ) );
        if( vr.isError() ) {
          break;
        }
      }
      return vr;
    }
  }

  /**
   * {@link ISkHaService#eventer()} implementation.
   *
   * @author mvk
   */
  private class Eventer
      extends AbstractTsEventer<ISkHaServiceListener> {

    private ECrudOp op        = null;
    private String  clusterId = null;

    @Override
    protected boolean doIsPendingEvents() {
      return op != null;
    }

    @Override
    protected void doFirePendingEvents() {
      reallyFire( op, clusterId );
    }

    @Override
    protected void doClearPendingEvents() {
      op = null;
    }

    void reallyFire( ECrudOp aOp, String aClusterId ) {
      for( ISkHaServiceListener l : listeners() ) {
        l.onClustersChanged( coreApi(), aOp, aClusterId );
      }
    }

    void fireClusterChanged( ECrudOp aOp, String aClusterId ) {
      if( isFiringPaused() ) {
        if( op == null ) { // first event to remember
          op = aOp;
          clusterId = aClusterId;
        }
        else { // second and further events generate LIST event
          op = ECrudOp.LIST;
          clusterId = null;
        }
      }
      else {
        reallyFire( aOp, aClusterId );
      }
    }

  }

  private final Svs     svs     = new Svs();
  private final Eventer eventer = new Eventer();

  private final ClassClaimingCoreValidator claimingValidator = new ClassClaimingCoreValidator();

  private final IStringMapEdit<ISkReadCurrDataChannel>  chReadPrimaries  = new StringMap<>();
  private final IStringMapEdit<ISkWriteCurrDataChannel> chWritePrimaries = new StringMap<>();

  /**
   * Translates objects change events to the {@link ISkHaServiceListener}.
   */
  private final ISkObjectServiceListener objectServiceListener = ( aCoreApi, aOp, aSkid ) -> {
    if( aSkid == null ) {
      reopenAllRtdataChannels();
      eventer.fireClusterChanged( ECrudOp.LIST, null );
      return;
    }
    switch( aOp ) {
      case CREATE:
      case EDIT:
      case REMOVE: {
        if( aSkid.classId().equals( CLSID_CLUSTER ) ) {
          reopenAllRtdataChannels();
          eventer.fireClusterChanged( aOp, aSkid.strid() );
        }
        break;
      }
      case LIST: {
        break; // already processed when aSkid = null
      }
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  };

  /**
   * Builtin validator.
   */
  private final ISkHaServiceValidator builtinValidator = new ISkHaServiceValidator() {

    @Override
    public ValidationResult canCreateCluster( String aClusterId, String aName, String aDescription, Skid aPrimaryMember,
        ISkidList aMembers ) {
      ISkHaCluster cluster = findCluster( aClusterId );
      if( cluster != null ) {
        return ValidationResult.error( ERR_CLUSTER_DOES_ALREADY_EXIST );
      }
      ISkObjectService objService = objServ();
      if( objService.find( aPrimaryMember ) == null ) {
        return ValidationResult.error( ERR_MEMBER_NOT_FOUND, aPrimaryMember );
      }
      for( Skid member : aMembers ) {
        if( objService.find( member ) == null ) {
          return ValidationResult.error( ERR_MEMBER_NOT_FOUND, member );
        }
      }
      return ValidationResult.SUCCESS;
    }

    @Override
    public ValidationResult canRemoveCluster( String aClusterId ) {
      ISkHaCluster cluster = findCluster( aClusterId );
      if( cluster == null ) {
        return ValidationResult.error( ERR_CLUSTER_NOT_FOUND );
      }
      return ValidationResult.SUCCESS;
    }

    @Override
    public ValidationResult canEditCluster( ISkHaCluster aCluster, String aName, String aDescription ) {
      return ValidationResult.SUCCESS;
    }

    @Override
    public ValidationResult canSetOwner( ISkHaCluster aCluster, Skid aOwnerId ) {
      ISkObjectService objService = objServ();
      if( objService.find( aOwnerId ) == null ) {
        return ValidationResult.error( ERR_MEMBER_NOT_FOUND, aOwnerId );
      }
      return ValidationResult.SUCCESS;
    }

    @Override
    public ValidationResult canAddMember( ISkHaCluster aCluster, Skid aMemberId ) {
      ISkObjectService objService = objServ();
      if( objService.find( aMemberId ) == null ) {
        return ValidationResult.error( ERR_MEMBER_NOT_FOUND, aMemberId );
      }
      return ValidationResult.SUCCESS;
    }

    @Override
    public ValidationResult canRemoveMember( ISkHaCluster aCluster, Skid aMemberId ) {
      if( !aCluster.memberIds().hasElem( aMemberId ) ) {
        return ValidationResult.error( ERR_MEMBER_NOT_FOUND, aMemberId );
      }
      if( aCluster.primaryMember().equals( aMemberId ) ) {
        return ValidationResult.error( ERR_REMOVE_PRIMARY );
      }
      return ValidationResult.SUCCESS;
    }

  };

  /**
   * Constructor.
   *
   * @param aCoreApi {@link IDevCoreApi} - owner core API implementation
   */
  public SkHaService( IDevCoreApi aCoreApi ) {
    super( SERVICE_ID, aCoreApi );
  }

  // ------------------------------------------------------------------------------------
  // AbstractSkService
  //

  @Override
  protected void doInit( ITsContextRo aArgs ) {
    // create classes and register object creators
    for( SkfHaLibUtils.BuiltinClassDef b : SkfHaLibUtils.BUILTIN_CLASS_DEFS ) {
      sysdescr().defineClass( b.dto() );
      if( b.objCreator() != null ) {
        objServ().registerObjectCreator( b.classId(), b.objCreator() );
      }
    }
    // claim on self classes
    sysdescr().svs().addValidator( claimingValidator );
    objServ().svs().addValidator( claimingValidator );

    // register builtin validator
    svs.addValidator( builtinValidator );

    // listen to object service to fire ISkHaServiceListener.onXxx() events
    objServ().eventer().addListener( objectServiceListener );
    // initialize RTdata: PRIMARY for all clusters
    reopenAllRtdataChannels();
  }

  @Override
  protected void doClose() {
    closeChannels( chReadPrimaries );
    closeChannels( chWritePrimaries );
  }

  @Override
  protected boolean doIsClassClaimedByService( String aClassId ) {
    return switch( aClassId ) {
      case CLSID_CLUSTER -> true;
      default -> false;
    };
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private static IDtoFullObject makeDtoFullObject( String aClusterId, String aName, String aDescription,
      Skid aPrimaryMember, ISkidList aMembers ) {
    TsNullArgumentRtException.checkNulls( aClusterId, aName, aDescription, aPrimaryMember, aMembers );
    Skid skid = new Skid( CLSID_CLUSTER, aClusterId );
    DtoFullObject dto = new DtoFullObject( skid );
    dto.attrs().setStr( AID_NAME, aName );
    dto.attrs().setStr( AID_DESCRIPTION, aDescription );
    dto.links().map().put( LNKID_PRIMARY, new SkidList( aPrimaryMember ) );
    dto.links().map().put( LNKID_MEMBERS, aMembers );
    return dto;
  }

  private void pauseCoreValidation() {
    sysdescr().svs().pauseValidator( claimingValidator );
    objServ().svs().pauseValidator( claimingValidator );
  }

  private void resumeCoreValidation() {
    sysdescr().svs().resumeValidator( claimingValidator );
    objServ().svs().resumeValidator( claimingValidator );
  }

  /**
   * Brings channels for PRIMARY RTdata up do date with the current list of clusters.
   * <p>
   * Removes channels for removed clusters, adds for new clusters.
   */
  private void reopenAllRtdataChannels() {
    // determine difference between old channels and new (according to current list of cluster)
    IStringList oldClusterStrids = new SortedStringLinkedBundleList( chReadPrimaries.keys() );
    IStringList newClusterStrids = new SortedStringLinkedBundleList( clusters().ids() );
    IMapEdit<EDiffNature, IListEdit<String>> diff = DiffUtils.compareLists( oldClusterStrids, newClusterStrids );
    IList<String> stridsToRemove = diff.findByKey( EDiffNature.LEFT );
    IList<String> stridsToAdd = diff.findByKey( EDiffNature.RIGHT );
    // remove channels for removed clusters
    for( String clusterId : stridsToRemove ) {
      chReadPrimaries.removeByKey( clusterId ).close();
      chWritePrimaries.removeByKey( clusterId ).close();
    }
    // add channels, step 1: create GWIDs lists
    GwidList glPrimaries = new GwidList();
    for( String clusterId : stridsToAdd ) {
      glPrimaries.add( Gwid.createRtdata( CLSID_CLUSTER, clusterId, RTDID_PRIMARY ) );
    }
    IMap<Gwid, ISkReadCurrDataChannel> mapR;
    IMap<Gwid, ISkWriteCurrDataChannel> mapW;
    // add channels, step 2: open and add PRIMARY channels
    mapR = rtdService().createReadCurrDataChannels( glPrimaries );
    mapW = rtdService().createWriteCurrDataChannels( glPrimaries );
    for( Gwid g : mapR.keys() ) {
      chReadPrimaries.put( g.strid(), mapR.getByKey( g ) );
      chWritePrimaries.put( g.strid(), mapW.getByKey( g ) );
    }
  }

  private static void closeChannels( IStringMapEdit<? extends ISkRtdataChannel> aMap ) {
    while( !aMap.isEmpty() ) {
      aMap.removeByKey( aMap.keys().first() ).close();
    }
  }

  private void addRtdataChannelsForCluster( String aClusterId ) {
    IMap<Gwid, ISkReadCurrDataChannel> mapR;
    IMap<Gwid, ISkWriteCurrDataChannel> mapW;
    // add primary channels
    Gwid gwidPrimary = Gwid.createRtdata( CLSID_CLUSTER, aClusterId, RTDID_PRIMARY );
    mapR = rtdService().createReadCurrDataChannels( new GwidList( gwidPrimary ) );
    chReadPrimaries.put( aClusterId, mapR.values().first() );
    mapW = rtdService().createWriteCurrDataChannels( new GwidList( gwidPrimary ) );
    chWritePrimaries.put( aClusterId, mapW.values().first() );
  }

  private void removeRtdataChannelsOfCluster( String aClusterId ) {
    chReadPrimaries.removeByKey( aClusterId ).close();
    chWritePrimaries.removeByKey( aClusterId ).close();
  }

  // ------------------------------------------------------------------------------------
  // ISkHaService
  //

  @Override
  public IStridablesList<ISkHaCluster> clusters() {
    IList<ISkHaCluster> ll = objServ().listObjs( CLSID_CLUSTER, true ); // aIncludeSubclasses = true
    return new StridablesList<>( ll );
  }

  @Override
  public ISkidList clusterOwnerIds() {
    SkidList retValue = new SkidList();
    for( ISkHaCluster cluster : clusters() ) {
      Skid ownerId = cluster.owner();
      if( !retValue.hasElem( ownerId ) ) {
        retValue.add( ownerId );
      }
    }
    return retValue;
  }

  @Override
  public IStridablesList<ISkHaCluster> clusters( Skid aOwnerId ) {
    StridablesList<ISkHaCluster> retValue = new StridablesList<>();
    for( ISkHaCluster cluster : clusters() ) {
      Skid ownerId = cluster.owner();
      if( ownerId.equals( aOwnerId ) ) {
        retValue.add( cluster );
      }
    }
    return retValue;
  }

  @Override
  public ISkHaCluster findCluster( String aClusterId ) {
    StridUtils.checkValidIdPath( aClusterId );
    return objServ().find( new Skid( CLSID_CLUSTER, aClusterId ) );

  }

  @Override
  public ISkHaCluster createCluster( String aClusterId, String aName, String aDescription, Skid aPrimaryMember,
      ISkidList aMembers ) {
    TsNullArgumentRtException.checkNulls( aClusterId, aName, aDescription, aPrimaryMember, aMembers );
    StridUtils.checkValidIdPath( aClusterId );
    ISkHaCluster cluster = findCluster( aClusterId );
    if( cluster != null ) {
      return cluster;
    }
    TsValidationFailedRtException
        .checkError( svs.validator().canCreateCluster( aClusterId, aName, aDescription, aPrimaryMember, aMembers ) );
    SkidList members = new SkidList( aMembers );
    if( !members.hasElem( aPrimaryMember ) ) {
      members.add( aPrimaryMember );
    }
    IDtoFullObject dto = makeDtoFullObject( aClusterId, aName, aDescription, aPrimaryMember, members );
    pauseCoreValidation();
    try {
      cluster = DtoFullObject.defineFullObject( coreApi(), dto );
      addRtdataChannelsForCluster( aClusterId );
      chWritePrimaries.getByKey( aClusterId ).setValue( avValobj( aPrimaryMember ) );
      return cluster;
    }
    finally {
      resumeCoreValidation();
    }
  }

  @Override
  public boolean removeCluster( String aClusterId ) {
    StridUtils.checkValidIdPath( aClusterId );
    TsValidationFailedRtException.checkError( svs.validator().canRemoveCluster( aClusterId ) );
    ISkHaCluster skCluster = findCluster( aClusterId );
    if( skCluster == null ) {
      return false;
    }
    removeRtdataChannelsOfCluster( aClusterId );
    pauseCoreValidation();
    try {
      objServ().removeObject( skCluster.skid() );
    }
    finally {
      resumeCoreValidation();
    }
    return true;
  }

  @Override
  public ITsValidationSupport<ISkHaServiceValidator> svs() {
    return svs;
  }

  @Override
  public ITsEventer<ISkHaServiceListener> eventer() {
    return eventer;
  }

}
