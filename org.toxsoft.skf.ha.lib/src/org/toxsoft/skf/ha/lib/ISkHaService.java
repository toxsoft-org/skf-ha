package org.toxsoft.skf.ha.lib;

import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.gw.skid.*;
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

@SuppressWarnings( { "nls", "javadoc" } )
public interface ISkHaService
    extends ISkService {

  /**
   * Service identifier.
   */
  String SERVICE_ID = ISkHardConstants.SK_SYSEXT_SERVICE_ID_PREFIX + ".HighAvailability";

  /**
   * Returns clusters.
   *
   * @return {@link IStridablesList}&lt;{@link ISkHaCluster}&gt; cluster list.
   */
  IStridablesList<ISkHaCluster> clusters();

  /**
   * Returns the IDs of the cluster owners.
   *
   * @return {@link IStridablesList}&lt;{@link ISkHaCluster}&gt; IDs of the cluster owners.
   */
  ISkidList clusterOwnerIds();

  /**
   * Returns clusters by cluster owner ID.
   *
   * @param aOwnerId {@link Skid} ID of cluster owner. {@link Skid#NONE}: returns clusters without owner.
   * @return {@link IStridablesList}&lt;{@link ISkHaCluster}&gt; cluster list.
   * @see {@link ISkHaCluster#owner()}, {@link ISkHaCluster#setOwner(Skid)}
   */
  IStridablesList<ISkHaCluster> clusters( Skid aOwnerId );

  /**
   * Create/update the cluster.
   * <p>
   * The ID of primary member is added to the cluster if it has not yet been added.
   *
   * @param aClusterId String cluster ID.
   * @param aPrimaryMember {@link Skid} primary cluster member ID.
   * @return {@link ISkHaCluster} the created cluster.
   */
  ISkHaCluster defineCluster( String aClusterId, Skid aPrimaryMember );

  /**
   * Remove a cluster.
   * <p>
   * Does nothing if the cluster does not exists.
   *
   * @param aClusterId String cluster ID.
   * @return boolean <b>true</b> the cluster removed. <b>false</b> the cluster does not exist.
   */
  boolean removeCluster( String aClusterId );

  // ------------------------------------------------------------------------------------
  // Service support

  /**
   * Returns the service eventer.
   *
   * @return {@link ITsEventer} - the service eventer
   */
  ITsEventer<ISkHaServiceListener> eventer();

}
