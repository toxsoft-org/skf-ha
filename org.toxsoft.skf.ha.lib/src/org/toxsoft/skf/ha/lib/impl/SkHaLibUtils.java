package org.toxsoft.skf.ha.lib.impl;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.ha.lib.*;
import org.toxsoft.uskat.core.api.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.api.ugwis.*;
import org.toxsoft.uskat.core.impl.*;

/**
 * Initialization and utility methods.
 *
 * @author mvk
 */
public class SkHaLibUtils {

  /**
   * Information about class to be defined in {@link ISkHaService}.
   *
   * @param dto {@link IDtoClassInfo} - class definition
   * @param objCreator {@link ISkObjectCreator} - object creator or null
   * @author hazard157
   */
  record BuiltinClassDef ( IDtoClassInfo dto, ISkObjectCreator<?> objCreator ) {

    public BuiltinClassDef( IDtoClassInfo dto, ISkObjectCreator<?> objCreator ) {
      TsNullArgumentRtException.checkNull( dto );
      this.dto = dto;
      this.objCreator = objCreator;
    }

    public String classId() {
      return dto.id();
    }
  }

  /**
   * Classes to be created by {@link ISkHaService}.
   */
  public static final IList<BuiltinClassDef> BUILTIN_CLASS_DEFS = new ElemArrayList<>( ///
      new BuiltinClassDef( ISkHaServiceConstants.CLSINF_CLUSTER, SkHaCluster.CREATOR ) ///
  );

  /**
   * Core handler to register all registered Sk-connection bound {@link ISkUgwiKind} when connection opens.
   */
  private static final ISkCoreExternalHandler coreRegistrationHandler = aCoreApi -> {
    // ISkHaService haService = aCoreApi.getService( ISkHaService.SERVICE_ID );
  };

  /**
   * The plugin initialization must be called before any action to access classes in this plugin.
   */
  public static void initialize() {
    // TsValobjUtils.registerKeeperIfNone( ESkAlarmSeverity.KEEPER_ID, ESkAlarmSeverity.KEEPER );
    SkCoreUtils.registerSkServiceCreator( SkHaService.CREATOR );
    SkCoreUtils.registerCoreApiHandler( coreRegistrationHandler );
  }

  /**
   * No subclasses.
   */
  private SkHaLibUtils() {
    // nop
  }

}
