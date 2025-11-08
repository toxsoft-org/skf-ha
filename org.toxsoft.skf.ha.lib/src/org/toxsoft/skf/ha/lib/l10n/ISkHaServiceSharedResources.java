package org.toxsoft.skf.ha.lib.l10n;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( { "nls", "javadoc" } )
public interface ISkHaServiceSharedResources {

  String STR_CLUSTER_CLASS = "Cluster";

  String STR_CLUSTER_CLASS_D = "Cluster of system hardware and software components";

  String ERR_CLUSTER_DOES_ALREADY_EXIST = "The cluster does already exist.";

  String ERR_CLUSTER_NOT_FOUND = "The cluster does not exist.";

  String ERR_MEMBER_NOT_FOUND = "The member '%s' does not exist.";

  String ERR_REMOVE_PRIMARY = "Cannot remove a cluster member that is the primary member of the cluster.";
}
