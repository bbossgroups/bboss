package bboss.org.jgroups.protocols;

import java.util.Date;

import bboss.org.jgroups.Event;
import bboss.org.jgroups.Message;
import bboss.org.jgroups.annotations.Property;
import bboss.org.jgroups.annotations.Unsupported;
import bboss.org.jgroups.conf.ClassConfigurator;
import bboss.org.jgroups.protocols.pbcast.GMS;
import bboss.org.jgroups.stack.Protocol;
import bboss.org.jgroups.util.Util;

/**
 * Discards 2 JOIN-REQs then accepts 1, then discards 2 more and so on
 * @author Bela Ban
 * @version $Id: DELAY_JOIN_REQ.java,v 1.6 2010/03/05 09:04:54 belaban Exp $
 */
@Unsupported
public class DELAY_JOIN_REQ extends Protocol {
    
    @Property
    private long delay=4000;

    private static final short gms_id=ClassConfigurator.getProtocolId(GMS.class);

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay=delay;
    }

    public Object up(final Event evt) {
        switch(evt.getType()) {
            case Event.MSG:
                Message msg=(Message)evt.getArg();
                final GMS.GmsHeader hdr=(GMS.GmsHeader)msg.getHeader(gms_id);
                if(hdr != null) {
                    switch(hdr.getType()) {
                        case GMS.GmsHeader.JOIN_REQ:
                        case GMS.GmsHeader.JOIN_REQ_WITH_STATE_TRANSFER:
                            System.out.println(new Date() + ": delaying JOIN-REQ by " + delay + " ms");
                            Thread thread=new Thread() {
                                public void run() {
                                    Util.sleep(delay);
                                    System.out.println(new Date() + ": sending up delayed JOIN-REQ by " + hdr.getMember());
                                    up_prot.up(evt);
                                }
                            };
                            thread.start();
                            return null;
                    }
                }
                break;
        }
        return up_prot.up(evt);
    }
}
