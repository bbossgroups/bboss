package bboss.org.jgroups.blocks;

import bboss.org.jgroups.Address;

public interface ConnectionMap<V extends Connection> {
    
    V getConnection(Address dest) throws Exception;            
    
}
