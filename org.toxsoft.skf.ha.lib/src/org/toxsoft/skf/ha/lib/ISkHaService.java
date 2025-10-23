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
   * Create/update the cluster.
   * <p>
   * The ID of primary member is added to the cluster if it has not yet been added.
   * <p>
   * FIXME question: how to create new cluster without primary member? <br>
   * FIXME question: how to edit existing cluster not changing existing primary member?
   *
   * @param aClusterId String - cluster ID.
   * @param aPrimaryMember {@link Skid} - primary cluster member ID
   * @return {@link ISkHaCluster} - the created cluster
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed {@link ISkHaServiceValidator#canDefineCluster(String, Skid)}
   */
  ISkHaCluster defineCluster( String aClusterId, Skid aPrimaryMember );

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
