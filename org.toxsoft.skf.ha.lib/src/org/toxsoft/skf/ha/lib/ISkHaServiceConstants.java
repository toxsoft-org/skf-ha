package org.toxsoft.skf.ha.lib;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.ha.lib.l10n.ISkHaServiceSharedResources.*;
import static org.toxsoft.uskat.core.ISkHardConstants.*;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.gw.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.impl.dto.*;

/**
 * Hard-coded constants of the high availability service.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
public interface ISkHaServiceConstants {

  /**
   * Maximum number of members in a cluster.
   */
  int CLUSTER_MEMBERS_MAX_SIZE = 32;

  /**
   * ID of class {@link #CLSINF_CLUSTER}.
   */
  String CLSID_CLUSTER = SK_ID + ".ha.Cluster";

  /**
   * ID of rivet {@link #RVTINF_OWNER}.
   */
  String RVTID_PRIMARY = "rvtPrimary";

  /**
   * ID of rivet {@link #RVTINF_OWNER}.
   */
  String RVTID_OWNER = "rvtOwner";

  /**
   * ID of rivet {@link #RVTINF_MEMBERS}.
   */
  String RVTID_MEMBERS = "rvtMembers";

  /**
   * ID of RT-data {@link #RTDINF_PRIMARY}.
   */
  String RTDID_PRIMARY = "rtdPrimary";

  /**
   * ID of command {@link #CMDINF_PRIMARY}.
   */
  String CMDID_SET_PRIMARY = "cmdSetPrimary";

  /**
   * ID of command {@link #CMDARGINF_PRIMARY_ID}.
   */
  String CMDARGID_PRIMARY_ID = "cmdArgPrimaryId";

  /**
   * ID of event {@link #EVINF_PRIMARY_CHANGED}.
   */
  String EVID_PRIMARY_CHANGED = "evPrimaryChanged";

  /**
   * ID of parameter {@link #EVPRMINF_OLD_PRIMARY_ID} of the event {@link #EVINF_PRIMARY_CHANGED}.
   */
  String EVPRMID_OLD_PRIMARY_ID = "oldPrimaryId";

  /**
   * ID of parameter {@link #EVPRMINF_NEW_PRIMARY_ID} of the event {@link #EVINF_PRIMARY_CHANGED}.
   */
  String EVPRMID_NEW_PRIMARY_ID = "newPrimaryId";

  /**
   * Rivet of {@link #CLSINF_CLUSTER}: primary member of the cluster.
   */
  IDtoRivetInfo RVTINF_PRIMARY = DtoRivetInfo.create2( RVTID_PRIMARY, ISkHaCluster.CLASS_ID, 1,    ///
      TSID_NAME, "Primary", TSID_DESCRIPTION, "Primary member of the cluster" );                   // FIXME define INFO
                                                                                                   // using ENG_XXX //
                                                                                                   // localization
  /**
   * Rivet of {@link #CLSINF_CLUSTER}: owner object SKID.
   */
  IDtoRivetInfo RVTINF_OWNER   = DtoRivetInfo.create2( RVTID_OWNER, ISkHaCluster.CLASS_ID, 1,      ///
      TSID_NAME, "Owner", TSID_DESCRIPTION, "Owner object SKID" );                                 // FIXME define INFO
                                                                                                   // using ENG_XXX
                                                                                                   // localization

  /**
   * Rivet of {@link #CLSINF_CLUSTER}: cluster members SKIDs.
   */
  IDtoRivetInfo RVTINF_MEMBERS = DtoRivetInfo.create2( RVTID_MEMBERS, ISkHaCluster.CLASS_ID, CLUSTER_MEMBERS_MAX_SIZE, ///
      TSID_NAME, "Members", TSID_DESCRIPTION, "Cluster members SKIDs" ); // FIXME define INFO using ENG_XXX localization

  /**
   * RTdat of {@link #CLSINF_CLUSTER}: primary member SKID.
   */
  IDtoRtdataInfo RTDINF_PRIMARY = DtoRtdataInfo.create2( RTDID_PRIMARY, DT_SKID, true, true, false, 1_000L, ///
      TSID_NAME, "Primary", TSID_DESCRIPTION, "Primary member SKID" ); // FIXME define INFO using ENG_XXX localization

  /**
   * Argument of {@link #CMDINF_PRIMARY}: SKID of the new primary member.
   */
  IDataDef CMDARGINF_PRIMARY_ID = DataDef.create3( CMDARGID_PRIMARY_ID, DT_SKID, ///
      TSID_NAME, "Primary", ///
      TSID_DESCRIPTION, "SKID of the new primary member.", TSID_IS_MANDATORY, AV_TRUE ///
  ); // FIXME define INFO using ENG_XXX localization

  /**
   * Command to {@link ISkHaCluster}: request to change primary member.
   */
  IDtoCmdInfo CMDINF_PRIMARY = DtoCmdInfo.create2( CMDID_SET_PRIMARY, OptionSetUtils.createOpSet( ///
      TSID_NAME, "Change primary.", //
      TSID_DESCRIPTION, "Request to change primary member." ), ///
      CMDARGINF_PRIMARY_ID //
  ); // FIXME define INFO using ENG_XXX localization

  /**
   * Parameter of {@link #EVINF_PRIMARY_CHANGED}: old (previous) SKID of the primary member.
   */
  IDataDef EVPRMINF_OLD_PRIMARY_ID = DataDef.create3( EVPRMID_OLD_PRIMARY_ID, DT_SKID, ///
      TSID_NAME, "Old", ///
      TSID_DESCRIPTION, "Old (previous) SKID of the primary member.", TSID_IS_MANDATORY, AV_TRUE ///
  ); // FIXME define INFO using ENG_XXX localization

  /**
   * Parameter of {@link #EVINF_PRIMARY_CHANGED}: new (current) SKID of the primary member.
   */
  IDataDef EVPRMINF_NEW_PRIMARY_ID = DataDef.create3( EVPRMID_NEW_PRIMARY_ID, DT_SKID, ///
      TSID_NAME, "New", ///
      TSID_DESCRIPTION, "New (current) SKID of the primary member.", TSID_IS_MANDATORY, AV_TRUE ///
  ); // FIXME define INFO using ENG_XXX localization

  /**
   * EVent of {@link ISkHaCluster}: primary member has been changed.
   */
  IDtoEventInfo EVINF_PRIMARY_CHANGED = DtoEventInfo.create1( EVID_PRIMARY_CHANGED, true, ///
      new StridablesList<>( ///
          EVPRMINF_OLD_PRIMARY_ID, ///
          EVPRMINF_NEW_PRIMARY_ID ///
      ), //
      OptionSetUtils.createOpSet( ///
          TSID_NAME, "Primary changed", ///
          TSID_DESCRIPTION, "Primary member has been changed" ///
      ) ); // FIXME define INFO using ENG_XXX localization

  /**
   * USkat implementation class of {@link ISkHaCluster}.
   */
  IDtoClassInfo CLSINF_CLUSTER = DtoClassInfo.create( CLSID_CLUSTER, IGwHardConstants.GW_ROOT_CLASS_ID, ///
      OptionSetUtils.createOpSet( ///
          TSID_NAME, STR_CLUSTER_CLASS, ///
          TSID_DESCRIPTION, STR_CLUSTER_CLASS_D, ///
          ISkHardConstants.OPDEF_SK_IS_SOURCE_CODE_DEFINED_CLASS, AV_TRUE, ///
          ISkHardConstants.OPDEF_SK_IS_SOURCE_USKAT_SYSEXT_CLASS, AV_TRUE ///
      ), ///
      RVTINF_PRIMARY, ///
      RVTINF_OWNER, ///
      RVTINF_MEMBERS, ///
      RTDINF_PRIMARY, ///
      CMDINF_PRIMARY, ///
      EVINF_PRIMARY_CHANGED ///
  );
}
