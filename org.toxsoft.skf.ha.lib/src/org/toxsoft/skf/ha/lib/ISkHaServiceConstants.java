package org.toxsoft.skf.ha.lib;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.ha.lib.l10n.ISkHaServiceSharedResources.*;
import static org.toxsoft.uskat.core.ISkHardConstants.*;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
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
   * ID of class {@link #CLSINF_CLUSTER}.
   */
  String CLSID_CLUSTER = SK_ID + ".ha.Cluster";

  /**
   * ID of link {@link #LNKINF_OWNER}.
   */
  String LNKID_PRIMARY = "lnkPrimary";

  /**
   * ID of link {@link #LNKINF_OWNER}.
   */
  String LNKID_OWNER = "lnkOwner";

  /**
   * ID of link {@link #LNKINF_MEMBERS}.
   */
  String LNKID_MEMBERS = "lnkMembers";

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
   * Link of {@link #CLSINF_CLUSTER}: primary member of the cluster.
   * <p>
   * FIXME define INFO using ENG_XXX localization
   */
  IDtoLinkInfo LNKINF_PRIMARY = DtoLinkInfo.create2( LNKID_PRIMARY, ///
      new StringArrayList( IGwHardConstants.GW_ROOT_CLASS_ID ), ///
      // aMaxCount = 1, aIsExactCount = true, aIsEmptyProhibited = true, aIsDuplicatesProhibited = true
      new CollConstraint( 1, true, true, true ), ///
      TSID_NAME, "Primary", TSID_DESCRIPTION, "Primary member of the cluster" );

  /**
   * Link of {@link #CLSINF_CLUSTER}: owner object SKID.
   * <p>
   * FIXME define INFO using ENG_XXX localization
   */
  IDtoLinkInfo LNKINF_OWNER = DtoLinkInfo.create2( LNKID_OWNER, ///
      new StringArrayList( IGwHardConstants.GW_ROOT_CLASS_ID ), ///
      // aMaxCount = 1, aIsExactCount = false, aIsEmptyProhibited = false, aIsDuplicatesProhibited = true
      new CollConstraint( 1, false, false, true ), ///
      TSID_NAME, "Owner", TSID_DESCRIPTION, "Owner object SKID" );

  /**
   * Link of {@link #CLSINF_CLUSTER}: cluster members SKIDs.
   * <p>
   * FIXME define INFO using ENG_XXX localization
   */
  IDtoLinkInfo LNKINF_MEMBERS = DtoLinkInfo.create2( LNKID_MEMBERS, ///
      new StringArrayList( IGwHardConstants.GW_ROOT_CLASS_ID ), ///
      // aMaxCount = 0, aIsExactCount = false, aIsEmptyProhibited = false, aIsDuplicatesProhibited = true
      new CollConstraint( 0, false, false, true ), ///
      TSID_NAME, "Members", TSID_DESCRIPTION, "Cluster members SKIDs" );

  /**
   * RTdat of {@link #CLSINF_CLUSTER}: primary member SKID.
   * <p>
   * FIXME define INFO using ENG_XXX localization
   */
  IDtoRtdataInfo RTDINF_PRIMARY = DtoRtdataInfo.create2( RTDID_PRIMARY, DT_SKID, true, true, false, 1_000L, ///
      TSID_NAME, "Primary", TSID_DESCRIPTION, "Primary member SKID" );

  /**
   * Argument of {@link #CMDINF_PRIMARY}: SKID of the new primary member.
   * <p>
   * FIXME define INFO using ENG_XXX localization
   */
  IDataDef CMDARGINF_PRIMARY_ID = DataDef.create3( CMDARGID_PRIMARY_ID, DT_SKID, ///
      TSID_NAME, "Primary", ///
      TSID_DESCRIPTION, "SKID of the new primary member.", TSID_IS_MANDATORY, AV_TRUE ///
  );

  /**
   * Command to {@link ISkHaCluster}: request to change primary member.
   * <p>
   * FIXME define INFO using ENG_XXX localization
   */
  IDtoCmdInfo CMDINF_PRIMARY = DtoCmdInfo.create2( CMDID_SET_PRIMARY, OptionSetUtils.createOpSet( ///
      TSID_NAME, "Change primary.", //
      TSID_DESCRIPTION, "Request to change primary member." ), ///
      CMDARGINF_PRIMARY_ID //
  );

  /**
   * Parameter of {@link #EVINF_PRIMARY_CHANGED}: old (previous) SKID of the primary member.
   * <p>
   * FIXME define INFO using ENG_XXX localization
   */
  IDataDef EVPRMINF_OLD_PRIMARY_ID = DataDef.create3( EVPRMID_OLD_PRIMARY_ID, DT_SKID, ///
      TSID_NAME, "Old", ///
      TSID_DESCRIPTION, "Old (previous) SKID of the primary member.", TSID_IS_MANDATORY, AV_TRUE ///
  );

  /**
   * Parameter of {@link #EVINF_PRIMARY_CHANGED}: new (current) SKID of the primary member.
   * <p>
   * FIXME define INFO using ENG_XXX localization
   */
  IDataDef EVPRMINF_NEW_PRIMARY_ID = DataDef.create3( EVPRMID_NEW_PRIMARY_ID, DT_SKID, ///
      TSID_NAME, "New", ///
      TSID_DESCRIPTION, "New (current) SKID of the primary member.", TSID_IS_MANDATORY, AV_TRUE ///
  );

  /**
   * EVent of {@link ISkHaCluster}: primary member has been changed.
   * <p>
   * FIXME define INFO using ENG_XXX localization
   */
  IDtoEventInfo EVINF_PRIMARY_CHANGED = DtoEventInfo.create1( EVID_PRIMARY_CHANGED, true, ///
      new StridablesList<>( ///
          EVPRMINF_OLD_PRIMARY_ID, ///
          EVPRMINF_NEW_PRIMARY_ID ///
      ), //
      OptionSetUtils.createOpSet( ///
          TSID_NAME, "Primary changed", ///
          TSID_DESCRIPTION, "Primary member has been changed" ///
      ) );

  /**
   * USkat implementation class of {@link ISkHaCluster}.
   * <p>
   * FIXME define INFO using ENG_XXX localization
   */
  IDtoClassInfo CLSINF_CLUSTER = DtoClassInfo.create( CLSID_CLUSTER, IGwHardConstants.GW_ROOT_CLASS_ID, ///
      OptionSetUtils.createOpSet( ///
          TSID_NAME, STR_CLUSTER_CLASS, ///
          TSID_DESCRIPTION, STR_CLUSTER_CLASS_D, ///
          ISkHardConstants.OPDEF_SK_IS_SOURCE_CODE_DEFINED_CLASS, AV_TRUE, ///
          ISkHardConstants.OPDEF_SK_IS_SOURCE_USKAT_SYSEXT_CLASS, AV_TRUE ///
      ), ///
      LNKINF_PRIMARY, ///
      LNKINF_OWNER, ///
      LNKINF_MEMBERS, ///
      RTDINF_PRIMARY, ///
      CMDINF_PRIMARY, ///
      EVINF_PRIMARY_CHANGED ///
  );
}
