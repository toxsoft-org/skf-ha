package org.toxsoft.skf.ha.lib;

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

  // TODO develop API

}
