package org.toxsoft.skf.ha.lib;

import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.uskat.core.*;

/**
 * Listener to the changes in {@link ISkHaService}.
 *
 * @author mvk
 */
public interface ISkHaServiceListener {

  /**
   * Called when any change in cluster occur.
   *
   * @param aCoreApi {@link ISkCoreApi} - the event source
   * @param aOp {@link ECrudOp} - the kind of change
   * @param aClusterId String - affected cluster ID or <code>null</code> for batch changes {@link ECrudOp#LIST}
   */
  void onClustersChanged( ISkCoreApi aCoreApi, ECrudOp aOp, String aClusterId );
}
