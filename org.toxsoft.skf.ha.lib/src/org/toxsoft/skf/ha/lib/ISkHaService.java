package org.toxsoft.skf.ha.lib;

import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.*;

/**
 * The HA (High Availability) service.
 * <p>
 * Service maintains deployed system hardware ad software components with monitoring and debugging data with som other
 * additional functionality.
 *
 * @author hazard157
 */

public interface ISkHaService
    extends ISkService {

  /**
   * Service identifier.
   */
  String SERVICE_ID = ISkHardConstants.SK_SYSEXT_SERVICE_ID_PREFIX + ".HighAvailability"; //$NON-NLS-1$

  /**
   * Returns clusters.
   *
   * @return {@link IStridablesList}&lt;{@link ISkHaCluster}&gt; - clusters list.
   */
  IStridablesList<ISkHaCluster> clusters();

  /**
   * Returns the IDs of the cluster owners.
   *
   * @return {@link IStridablesList}&lt;{@link ISkHaCluster}&gt; - IDs of the cluster owners.
   */
  ISkidList clusterOwnerIds();

  /**
   * Returns clusters by cluster owner ID.
   *
   * @param aOwnerId {@link Skid} ID of cluster owner. {@link Skid#NONE}: returns clusters without owner.
   * @return {@link IStridablesList}&lt;{@link ISkHaCluster}&gt; cluster list.
   * @see ISkHaCluster#owner()
   * @see ISkHaCluster#setOwner(Skid)
   */
  IStridablesList<ISkHaCluster> clusters( Skid aOwnerId );

  /**
   * Find a cluster.
   *
   * @param aClusterId String - cluster ID.
   * @return {@link ISkHaCluster} - the cluster or null if cluster does not exist
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  ISkHaCluster findCluster( String aClusterId );

  /**
   * Create/update the cluster.
   * <p>
   * List of member may be empty list, it may contain or not the <code>aPrimaryMember</code> object. Anyway
   * <code>aPrimaryMember</code> will be added to the cluster members and set as primary member.
   *
   * @param aClusterId String - cluster ID.
   * @param aName String - cluster name
   * @param aDescription String - cluster description
   * @param aPrimaryMember {@link Skid} - primary cluster member ID
   * @param aMembers {@link ISkidList} - the initial members of the cluster, may be an empty list
   * @return {@link ISkHaCluster} - the created cluster
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed
   *           {@link ISkHaServiceValidator#canCreateCluster(String, String, String, Skid, ISkidList)}
   */
  ISkHaCluster createCluster( String aClusterId, String aName, String aDescription, Skid aPrimaryMember,
      ISkidList aMembers );

  /**
   * Remove a cluster.
   * <p>
   * Does nothing if the cluster does not exists.
   *
   * @param aClusterId String = cluster ID
   * @return boolean - <b>true</b> the cluster removed. <b>false</b> the cluster does not exist.
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed {@link ISkHaServiceValidator#canRemoveCluster(String)}
   */
  boolean removeCluster( String aClusterId );

  // ------------------------------------------------------------------------------------
  // Service support

  /**
   * Returns the service validator.
   *
   * @return {@link ITsValidationSupport}&lt;{@link ISkHaServiceValidator}&gt; - the service validator
   */
  ITsValidationSupport<ISkHaServiceValidator> svs();

  /**
   * Returns the service eventer.
   *
   * @return {@link ITsEventer} - the service eventer
   */
  ITsEventer<ISkHaServiceListener> eventer();

}
