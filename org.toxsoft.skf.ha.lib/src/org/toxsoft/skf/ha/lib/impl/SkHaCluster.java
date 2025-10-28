package org.toxsoft.skf.ha.lib.impl;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.skf.ha.lib.ISkHaServiceConstants.*;
import static org.toxsoft.uskat.core.ISkHardConstants.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.ha.lib.*;
import org.toxsoft.uskat.core.api.cmdserv.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.users.*;
import org.toxsoft.uskat.core.impl.*;
import org.toxsoft.uskat.core.impl.dto.*;

/**
 * {@link ISkHaCluster} implementation
 *
 * @author mvk
 */
class SkHaCluster
    extends SkObject
    implements ISkHaCluster {

  /**
   * The creator singleton.
   */
  static final ISkObjectCreator<SkHaCluster> CREATOR = SkHaCluster::new;

  /**
   * Constructor.
   *
   * @param aSkid {@link Skid} - the object SKID
   */
  SkHaCluster( Skid aSkid ) {
    super( aSkid );
  }

  // ------------------------------------------------------------------------------------
  // ISkCluster
  //
  @Override
  public void editCluster( String aName, String aDescription ) {
    TsNullArgumentRtException.checkNulls( aName, aDescription );
    ISkHaService service = (ISkHaService)coreApi().services().getByKey( ISkHaService.SERVICE_ID );
    TsValidationFailedRtException.checkError( service.svs().validator().canEditCluster( this, aName, aDescription ) );
    IOptionSetEdit attrs = new OptionSet( attrs() );
    attrs.setStr( AID_NAME, aName );
    attrs.setStr( AID_DESCRIPTION, aDescription );
    coreApi().objService().defineObject( new DtoObject( skid(), attrs, rivets().map() ) );
  }

  @Override
  public Skid owner() {
    ISkidList list = rivets().getSkidList( RVTID_OWNER );
    return (list.size() > 0 ? list.first() : Skid.NONE);
  }

  @Override
  public Skid setOwner( Skid aOwnerId ) {
    TsNullArgumentRtException.checkNull( aOwnerId );
    ISkHaService service = (ISkHaService)coreApi().services().getByKey( ISkHaService.SERVICE_ID );
    TsValidationFailedRtException.checkError( service.svs().validator().canSetOwner( this, aOwnerId ) );
    Skid retValue = owner();
    if( retValue.equals( aOwnerId ) ) {
      return retValue;
    }
    IStringMapEdit<ISkidList> rivets = new StringMap<>( rivets().map() );
    rivets.put( RVTID_OWNER, new SkidList( aOwnerId ) );
    coreApi().objService().defineObject( new DtoObject( skid(), attrs(), rivets ) );
    return retValue;
  }

  @Override
  public Skid primaryMember() {
    return readRtdataIfOpen( RTDID_PRIMARY ).asValobj();
  }

  @Override
  public ISkidList memberIds() {
    ISkidList list = rivets().getSkidList( RVTID_MEMBERS );
    return list;
  }

  @Override
  public boolean addMember( Skid aMemberId ) {
    TsNullArgumentRtException.checkNull( aMemberId );
    ISkHaService service = (ISkHaService)coreApi().services().getByKey( ISkHaService.SERVICE_ID );
    TsValidationFailedRtException.checkError( service.svs().validator().canAddMember( this, aMemberId ) );
    SkidList members = new SkidList( memberIds() );
    if( aMemberId == Skid.NONE || members.hasElem( aMemberId ) ) {
      return false;
    }
    members.add( aMemberId );
    IStringMapEdit<ISkidList> rivets = new StringMap<>( rivets().map() );
    rivets.put( RVTID_MEMBERS, members );
    coreApi().objService().defineObject( new DtoObject( skid(), attrs(), rivets ) );
    return true;
  }

  @Override
  public boolean removeMember( Skid aMemberId ) {
    TsNullArgumentRtException.checkNull( aMemberId );
    ISkHaService service = (ISkHaService)coreApi().services().getByKey( ISkHaService.SERVICE_ID );
    TsValidationFailedRtException.checkError( service.svs().validator().canRemoveMember( this, aMemberId ) );
    SkidList members = new SkidList( memberIds() );
    if( aMemberId == Skid.NONE || !members.hasElem( aMemberId ) ) {
      return false;
    }
    members.remove( aMemberId );
    IStringMapEdit<ISkidList> rivets = new StringMap<>( rivets().map() );
    rivets.put( RVTID_MEMBERS, members );
    coreApi().objService().defineObject( new DtoObject( skid(), attrs(), rivets ) );
    return true;
  }

  @Override
  public ISkCommand requestPrimaryMemberChange( Skid aMemberId ) {
    TsNullArgumentRtException.checkNull( aMemberId );
    // TODO: need command validation (the member is not already primary & does exists)
    IOptionSet args = OptionSetUtils.createOpSet( ///
        CMDARGID_PRIMARY_ID, avValobj( aMemberId ) ///
    );
    ISkLoggedUserInfo userInfo = coreApi().getCurrentUserInfo();
    Gwid cmdGwid = Gwid.createCmd( CLASS_ID, skid().strid(), CMDID_SET_PRIMARY );
    return coreApi().cmdService().sendCommand( cmdGwid, userInfo.userSkid(), args );
  }

}
