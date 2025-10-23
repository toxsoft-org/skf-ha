package org.toxsoft.skf.ha.lib;

import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.api.cmdserv.*;
import org.toxsoft.uskat.core.api.objserv.*;

/**
 * The cluster of {@link ISkHaService}.
 *
 * @author mvk
 */
public interface ISkHaCluster
    extends ISkObject {

  /**
   * The Sk-class identifier.
   */
  String CLASS_ID = ISkHaServiceConstants.CLSID_CLUSTER;

  /**
   * Returns the cluster owner ID.
   *
   * @return {@link Skid} ID of the cluster. {@link Skid#NONE}: the cluster has no owner.
   */
  Skid owner();

  /**
   * Define/update the cluster owner ID.
   * <p>
   * FIXME cluster without owner - what it is, maybe cluster owner is mandatory?
   *
   * @param aOwnerId {@link Skid} - owner SKID or {@link Skid#NONE} for no owner.
   * @return {@link Skid} - owner SKID before the method call
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed {@link ISkHaServiceValidator#canSetOwner(ISkHaCluster, Skid)}
   */
  Skid setOwner( Skid aOwnerId );

  /**
   * Returns the ID of the primary cluster member.
   *
   * @return {@link Skid} - primary member ID
   */
  Skid primaryMember();

  /**
   * Returns the IDs of cluster members.
   *
   * @return {@link ISkidList} - the non-empty list IDs of cluster members, contains at least {@link #primaryMember()})
   */
  ISkidList memberIds();

  /**
   * Add a new member to the cluster.
   * <p>
   * Does nothing if the member does already is in this cluster.
   *
   * @param aMemberId {@link Skid} - ID of member to be added
   * @return boolean <b>true</b> - the member added, <b>false</b> the member already exists
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed {@link ISkHaServiceValidator#canAddMember(ISkHaCluster, Skid)}
   */
  boolean addMember( Skid aMemberId );

  /**
   * Remove the member from the cluster.
   * <p>
   * Does nothing if the member does not exists.
   *
   * @param aMemberId {@link Skid} member ID.
   * @return boolean - <b>true</b> the member removed, <b>false</b> the member does not exists
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed {@link ISkHaServiceValidator#canRemoveMember(ISkHaCluster, Skid)}
   */
  boolean removeMember( Skid aMemberId );

  /**
   * Sends a command to change the primary member of the cluster.
   * <p>
   * FIXME why this method if there is {@link ISkHaService#defineCluster(String, Skid)} ? <br>
   * FIXME мутно для меня - нде-то основной элемент кластера через метод, а где-то через команду...
   *
   * @param aMemberId {@link Skid} member ID.
   * @return {@link ISkCommand} command.
   */
  ISkCommand cmdChangePrimaryMember( Skid aMemberId );

}
