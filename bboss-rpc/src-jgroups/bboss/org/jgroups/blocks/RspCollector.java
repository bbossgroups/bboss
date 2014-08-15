// $Id: RspCollector.java,v 1.4 2007/02/16 09:06:57 belaban Exp $

package bboss.org.jgroups.blocks;

import bboss.org.jgroups.Address;
import bboss.org.jgroups.View;


public interface RspCollector {
    void receiveResponse(Object response_value, Address sender);
    void suspect(Address mbr);
    void viewChange(View new_view);
}
