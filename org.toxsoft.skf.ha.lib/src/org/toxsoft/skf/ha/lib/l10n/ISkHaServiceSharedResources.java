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

  String ERR_PRIMARY_IS_NONE = "Primary member cannot have ID NONE.";

  String ERR_CLUSTER_NOT_FOUND = "The cluster does not exist.";

  String ERR_MEMBER_IS_NONE = "Cluster member cannot have ID NONE.";

  String ERR_MEMBER_NOT_FOUND = "The member does not exist.";

  String ERR_REMOVE_PRIMARY = "Cannot remove a cluster member that is the primary member of the cluster.";
}
