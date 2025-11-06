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

  String MSG_WORKER_STARTED = "HA cluster worker started. count = %d";
  String MSG_CMD_RECEVIED   = "executeCommand(...): receive command = %s";

  // ------------------------------------------------------------------------------------
  // errors & warnings
  //

  String ERR_UNKNOWN_CMD_RECEVIED = "executeCommand(...): unknown command: class = %s, cmd = %s";
  String ERR_CLUSTER_NOT_FOUND    = "executeCommand(...): cluster '%s' does not exist.";
}
