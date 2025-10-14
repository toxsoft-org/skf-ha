package org.toxsoft.skf.ha.lib;

import org.toxsoft.core.tslib.coll.*;
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
   * @param aOwnerId
   * @return
   * @see {@link ISkHaCluster#owner()}, {@link ISkHaCluster#setOwner(Skid)}
   */
  IList<ISkHaCluster> clusters( Skid aOwnerId );

  IList<ISkHaCluster> clusters();

  ISkHaCluster defineCluster( Skid aClusterId );

  boolean removeCluster( Skid aClusterId );

  // TODO develop API

}
