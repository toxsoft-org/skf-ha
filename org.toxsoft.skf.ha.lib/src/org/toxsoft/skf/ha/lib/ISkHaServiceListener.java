package org.toxsoft.skf.ha.lib;

import org.toxsoft.core.tslib.coll.helpers.*;

@SuppressWarnings( { "javadoc" } )
public interface ISkHaServiceListener {

  void onClusterDefinition( ISkHaService aSource, ECrudOp aOp, String aClusterId );
}
