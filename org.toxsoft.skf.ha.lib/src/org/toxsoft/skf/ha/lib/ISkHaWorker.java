package org.toxsoft.skf.ha.lib;

import org.toxsoft.skf.ha.lib.impl.*;
import org.toxsoft.uskat.core.devapi.*;

/**
 * Компонент обработки данных службы {@link ISkHaService}.
 *
 * @author mvk
 */
public interface ISkHaWorker
    extends ISkWorker {

  /**
   * The worker ID.
   */
  String WORKER_ID = SkHaWorker.class.getSimpleName();
}
