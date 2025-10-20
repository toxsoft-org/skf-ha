package org.toxsoft.skf.ha.lib;

import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.uskat.core.*;

@SuppressWarnings( { "javadoc" } )
/**
 * Listener to the changes in {@link ISkHaService}.
 *
 * @author mvk
 */
public interface ISkHaServiceListener {

  /**
   * Called when any change in objects occur.
   *
   * @param aCoreApi {@link ISkCoreApi} - the event source
   * @param aOp {@link ECrudOp} - the kind of change
   * @param aClusterId String - affected cluster ID or <code>null</code> for batch changes {@link ECrudOp#LIST}
   */
  void onClustersChanged( ISkCoreApi aSource, ECrudOp aOp, String aClusterId );
}
