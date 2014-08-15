// $Id: EXAMPLE.java,v 1.11 2009/12/11 13:00:40 belaban Exp $

package bboss.org.jgroups.protocols;

import java.io.Serializable;
import java.util.Vector;

import bboss.org.jgroups.Address;
import bboss.org.jgroups.Event;
import bboss.org.jgroups.Message;
import bboss.org.jgroups.View;
import bboss.org.jgroups.annotations.Unsupported;
import bboss.org.jgroups.stack.Protocol;


class ExampleHeader implements Serializable {
    private static final long serialVersionUID=-8802317525466899597L;
    // your variables

    ExampleHeader() {
    }

    public String toString() {
        return "[EXAMPLE: <variables> ]";
    }
}


/**
 * Example of a protocol layer. Contains no real functionality, can be used as a template.
 */
@Unsupported
public class EXAMPLE extends Protocol {
    final Vector<Address> members=new Vector<Address>();


    /**
     * Just remove if you don't need to reset any state
     */
    public static void reset() {
    }


    public Object up(Event evt) {
        switch(evt.getType()) {

            case Event.MSG:
                Message msg=(Message)evt.getArg();
                // Do something with the event, e.g. extract the message and remove a header.
                // Optionally pass up
                break;
        }

        return up_prot.up(evt);            // Pass up to the layer above us
    }


    public Object down(Event evt) {

        switch(evt.getType()) {
            case Event.TMP_VIEW:
            case Event.VIEW_CHANGE:
                Vector<Address> new_members=((View)evt.getArg()).getMembers();
                synchronized(members) {
                    members.removeAllElements();
                    if(new_members != null && !new_members.isEmpty())
                        for(int i=0; i < new_members.size(); i++)
                            members.addElement(new_members.elementAt(i));
                }
                return down_prot.down(evt);

            case Event.MSG:
                Message msg=(Message)evt.getArg();
                // Do something with the event, e.g. add a header to the message
                // Optionally pass down
                break;
        }

        return down_prot.down(evt);          // Pass on to the layer below us
    }


}
