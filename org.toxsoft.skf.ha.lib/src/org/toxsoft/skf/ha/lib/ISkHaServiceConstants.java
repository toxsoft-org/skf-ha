package org.toxsoft.skf.ha.lib;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.ha.lib.l10n.ISkHaServiceSharedResources.*;
import static org.toxsoft.uskat.core.ISkHardConstants.*;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.gw.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.impl.dto.*;

/**
 * Hard-coded constants of the high availability service.
 *
 * @author hazard157
 */
public interface ISkHaServiceConstants {

  /**
   * ID of class {@link #CLSINF_CLUSTER}.
   */
  String CLSID_CLUSTER = SK_ID + ".ha.Cluster"; //$NON-NLS-1$

  /**
   * ID of rivet {@link #RVTINF_OWNER}.
   */
  String RVTID_OWNER = "rvtOwner"; //$NON-NLS-1$

  /**
   * ID of RT-data {@link #RTDINF_PRIMARY}.
   * <p>
   * FIXME maybe primary member must be also a rivet?
   */
  String RTDID_PRIMARY = "rtdPrimary"; //$NON-NLS-1$

  /**
   * ID of command {@link #CMDINF_PRIMARY}.
   */
  String CMDID_SET_PRIMARY = "cmdSetPrimary"; //$NON-NLS-1$

  /**
   * ID of command {@link #CMDARGINF_PRIMARY_ID}.
   */
  String CMDARGID_PRIMARY_ID = "cmdArgPrimaryId"; //$NON-NLS-1$

  /**
   * ID of event {@link #EVINF_PRIMARY_CHANGED}.
   */
  String EVID_PRIMARY_CHANGED = "evPrimaryChanged"; //$NON-NLS-1$

  /**
   * ID of parameter {@link #EVPRMINF_CLUSTER_ID} of the event {@link #EVINF_PRIMARY_CHANGED}.
   */
  String EVPRMID_CLUSTER_ID = "clusterId"; //$NON-NLS-1$

  /**
   * ID of parameter {@link #EVPRMIP_OLD_PRIMARY_ID} of the event {@link #EVINF_PRIMARY_CHANGED}.
   */
  String EVPRMID_OLD_PRIMARY_ID = "oldPrimaryId"; //$NON-NLS-1$

  /**
   * ID of parameter {@link #EVPRMINF_NEW_PRIMARY_ID} of the event {@link #EVINF_PRIMARY_CHANGED}.
   */
  String EVPRMID_NEW_PRIMARY_ID = "newPrimaryId"; //$NON-NLS-1$

  /**
   * Rivet of {@link #CLSINF_CLUSTER}: owner object SKID.
   */
  IDtoLinkInfo RVTINF_OWNER = null; // FIXME define INFO using ENG_XXX localization

  /**
   * RTdat of {@link #CLSINF_CLUSTER}: primary member SKID.
   */
  IDtoRtdataInfo RTDINF_PRIMARY = null; // FIXME define INFO using ENG_XXX localization

  /**
   * Command to {@link ISkHaCluster}: request to change primary member.
   */
  IDtoCmdInfo CMDINF_PRIMARY = null; // FIXME define INFO using ENG_XXX localization

  /**
   * Argument of {@link #CMDINF_PRIMARY}: SKID of the new primary member.
   */
  IDtoCmdInfo CMDARGINF_PRIMARY_ID = null; // FIXME define INFO using ENG_XXX localization

  /**
   * EVent of {@link ISkHaCluster}: primary member has been changed.
   */
  IDtoEventInfo EVINF_PRIMARY_CHANGED = null; // FIXME define INFO using ENG_XXX localization

  /**
   * Parameter of {@link #EVINF_PRIMARY_CHANGED}: SKID of changed cluster.
   */
  IDataDef EVPRMINF_CLUSTER_ID = null; // FIXME define INFO using ENG_XXX localization

  /**
   * Parameter of {@link #EVINF_PRIMARY_CHANGED}: old (previous) SKID of the primary member.
   */
  IDataDef EVPRMIP_OLD_PRIMARY_ID = null; // FIXME define INFO using ENG_XXX localization

  /**
   * Parameter of {@link #EVINF_PRIMARY_CHANGED}: new (current) SKID of the primary member.
   */
  IDataDef EVPRMINF_NEW_PRIMARY_ID = null; // FIXME define INFO using ENG_XXX localization

  /**
   * USkat implementation class of {@link ISkHaCluster}.
   */
  IDtoClassInfo CLSINF_CLUSTER = DtoClassInfo.create( CLSID_CLUSTER, IGwHardConstants.GW_ROOT_CLASS_ID, ///
      OptionSetUtils.createOpSet( ///
          TSID_NAME, ENG_CLUSTER_CLASS, ///
          TSID_DESCRIPTION, ENG_CLUSTER_CLASS_D, ///
          ISkHardConstants.OPDEF_SK_IS_SOURCE_CODE_DEFINED_CLASS, AV_TRUE, ///
          ISkHardConstants.OPDEF_SK_IS_SOURCE_USKAT_SYSEXT_CLASS, AV_TRUE ///
      ), ///
      RVTINF_OWNER, ///
      RTDINF_PRIMARY, ///
      CMDINF_PRIMARY, ///
      EVINF_PRIMARY_CHANGED ///
  );
}
