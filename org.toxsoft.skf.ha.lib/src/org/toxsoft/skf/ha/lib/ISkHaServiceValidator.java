package org.toxsoft.skf.ha.lib;

import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.gw.skid.*;

/**
 * Validates possible changes in the {@link ISkHaService}.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ISkHaServiceValidator {

  ValidationResult canCreateCluster( String aClusterId, String aName, String aDescription, Skid aPrimaryMember,
      ISkidList aMembers );

  ValidationResult canRemoveCluster( String aClusterId );

  ValidationResult canEditCluster( ISkHaCluster aClaster, String aName, String aDescription );

  ValidationResult canSetOwner( ISkHaCluster aClaster, Skid aOwnerId );

  ValidationResult canAddMember( ISkHaCluster aClaster, Skid aMemberId );

  ValidationResult canRemoveMember( ISkHaCluster aClaster, Skid aMemberId );

}
