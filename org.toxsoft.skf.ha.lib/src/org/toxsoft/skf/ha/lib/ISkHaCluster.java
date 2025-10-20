package org.toxsoft.skf.ha.lib;

import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.api.cmdserv.*;
import org.toxsoft.uskat.core.api.objserv.*;

/**
 * The cluster of {@link ISkHaService}.
 *
 * @author mvk
 */
@SuppressWarnings( { "nls", "javadoc" } )
public interface ISkHaCluster
    extends ISkObject {

  String LNKID_OWNER   = "lnkOwner";
  String RTDID_PRIMARY = "rtdPrimary";

  String CMDID_SET_PRIMARY   = "cmdSetPrimary";
  String CMDARGID_PRIMARY_ID = "cmdArgPrimaryId";

  String EVID_PRIMARY_CHANGED    = "evPrimaryChanged";
  String EVPRMID_CLUSTER_ID      = "evPrmClusterId";
  String EVPRMID_PREV_PRIMARY_ID = "evPrmPrevPrimaryId";
  String EVPRMID_NEW_PRIMARY_ID  = "evPrmNewPrimaryId";

  /**
   * Returns the cluster owner ID.
   *
   * @return {@link Skid} ID of the cluster. {@link Skid#NONE}: the cluster has no owner.
   */
  Skid owner();

  /**
   * Define/update the cluster owner ID.
   *
   * @param aOwnerId {@link Skid} ID of the cluster. {@link Skid#NONE}: the cluster has no owner.
   */
  Skid setOwner( Skid aOwnerId );

  /**
   * Returns the ID of the primary cluster member.
   *
   * @return {@link Skid} member ID.
   */
  Skid primaryMember();

  /**
   * Returns the IDs of cluster members.
   *
   * @return {@link ISkidList} the IDs of cluster members. The list cannot be empty (aleast {@link #primaryMemberId()}).
   */
  ISkidList memberIds();

  /**
   * Add a new member to the cluster.
   * <p>
   * Does nothing if the member does already exists.
   *
   * @param aMemberId {@link Skid} member ID.
   * @return boolean <b>true</b> the member added. <b>false</b> the member does already exists.
   */
  boolean addMember( Skid aMemberId );

  /**
   * Remove a member from the cluster.
   * <p>
   * Does nothing if the member does not exists.
   *
   * @param aMemberId {@link Skid} member ID.
   * @return boolean <b>true</b> the member removed. <b>false</b> the member does not exist.
   * @throws TsIllegalArgumentRtException unable to remove primary cluster member.
   */
  boolean removeMember( Skid aMemberId );

  /**
   * Sends a command to change the primary member of the cluster.
   *
   * @param aMemberId {@link Skid} member ID.
   * @return {@link ISkCommand} command.
   */
  ISkCommand cmdChangePrimaryMember( Skid aMemberId );

}
