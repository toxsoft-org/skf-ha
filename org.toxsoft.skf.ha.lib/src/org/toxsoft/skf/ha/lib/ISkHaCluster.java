package org.toxsoft.skf.ha.lib;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.uskat.core.api.cmdserv.*;
import org.toxsoft.uskat.core.api.objserv.*;

@SuppressWarnings( { "nls", "javadoc" } )
public interface ISkHaCluster
    extends ISkObject {

  String LNKID_OWNER = "lnkOwner";
  String RTDID_MAIN  = "rtdMain";

  String CMDID_SET_MAIN   = "cmdSetMain";
  String CMDARGID_MAIN_ID = "cmdArgMainId";

  Skid owner();

  Skid setOwner( Skid aOwnerId );

  ISkHaMember main();

  IList<ISkHaMember> members();

  boolean addMember( ISkHaMember aMember );

  boolean removeMember( Skid aMemberId );

  ISkCommand cmdSetMain( Skid aMemberId );

}
