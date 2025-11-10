package org.toxsoft.skf.ha.lib.impl;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ISkResources {

  // ------------------------------------------------------------------------------------
  // messages
  //

  String MSG_WORKER_STARTED = "HA cluster worker started. cluster count = %d";
  String MSG_CMD_RECEVIED   = "executeCommand(...): receive command = %s";

  // ------------------------------------------------------------------------------------
  // errors & warnings
  //

  String ERR_UNKNOWN_CMD_RECEVIED = "executeCommand(...): unknown command: class = %s, cmd = %s";
  String ERR_UNEXPECTED           = "executeCommand(...): unexpected error: aCmd = %s, cause = %s";
}
