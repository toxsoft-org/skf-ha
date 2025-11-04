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

  String ERR_CREATE_CLUSTER_PRIMARY_NONE = "Create cluster %s: primary member cannot have ID NONE.";

  String ERR_UPDATE_CLUSTER_PRIMARY_NONE = "Update cluster %s: primary member cannot have ID NONE.";

  String ERR_REMOVE_NON_CLUSTER = "Remove cluster %s: cluster not found.";

  String ERR_ADD_MEMBER_NONE = "Adding a member to cluster %s: member cannot have ID NONE.";

  String ERR_REMOVE_NON_MEMBER = "Removing member from cluster %s : %s is not member.";

}
