package org.toxsoft.skf.ha.gui;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;

/**
 * The library quant.
 *
 * @author hazard157
 */
public class QuantSkHaGui
    extends AbstractQuant {

  /**
   * Constructor.
   */
  public QuantSkHaGui() {
    super( QuantSkHaGui.class.getSimpleName() );
  }

  // ------------------------------------------------------------------------------------
  // AbstractQuant
  //

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    ISkHaGuiConstants.init( aWinContext );
  }

}
