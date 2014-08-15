// $Id: SIZE.java,v 1.26 2009/09/21 09:57:25 belaban Exp $

package bboss.org.jgroups.protocols;

import java.util.Vector;

import bboss.org.jgroups.Event;
import bboss.org.jgroups.Message;
import bboss.org.jgroups.annotations.Property;
import bboss.org.jgroups.annotations.Unsupported;
import bboss.org.jgroups.stack.Protocol;
import bboss.org.jgroups.util.ExposedByteArrayOutputStream;
import bboss.org.jgroups.util.ExposedDataOutputStream;
import bboss.org.jgroups.util.Util;


/**
 * Protocol which prints out the real size of a message. To do this, the message
 * is serialized into a byte buffer and its size read. Don't use this layer in
 * a production stack since the costs are high (just for debugging).
 * 
 * @author Bela Ban June 13 2001
 */
@Unsupported
public class SIZE extends Protocol {
    final Vector members=new Vector();
    @Property
    boolean print_msg=false;
    @Property
    boolean raw_buffer=false; // just print size of message buffer

    /** Min size in bytes above which msgs should be printed */
    @Property
    long min_size=0;

    final ExposedByteArrayOutputStream out_stream=new ExposedByteArrayOutputStream(65535);
    final ExposedDataOutputStream out=new ExposedDataOutputStream(out_stream);


    public void init() {
    }


    public Object up(Event evt) {
        
        switch(evt.getType()) {

        case Event.MSG:
            Message msg=(Message)evt.getArg();
            int payload_size=msg.getLength();

            if(raw_buffer) {
                if(log.isTraceEnabled())
                    log.trace("size of message buffer is " + payload_size + ", " + numHeaders(msg) + " headers");
            }
            else {
                int serialized_size=sizeOf(msg);
                if(serialized_size > min_size) {
                    if(log.isTraceEnabled())
                        log.trace("size of serialized message is " + serialized_size +
                                  ", " + numHeaders(msg) + " headers");

                }
            }
            if(print_msg) {
                if(log.isTraceEnabled())
                    log.trace("headers are " + msg.printHeaders() + ", payload size=" + payload_size);
            }
            break;
        }

        return up_prot.up(evt);            // pass up to the layer above us
    }


    public Object down(Event evt) {
        Message msg;
        int payload_size=0, serialized_size;

        switch(evt.getType()) {

            case Event.MSG:
            msg=(Message)evt.getArg();
            payload_size=msg.getLength();

            if(raw_buffer) {
                if(log.isTraceEnabled())
                    log.trace("size of message buffer is " + payload_size + ", " + numHeaders(msg) + " headers");
            }
            else {
                serialized_size=sizeOf(msg);
                if(serialized_size > min_size) {
                    if(log.isTraceEnabled())
                        log.trace("size of serialized message is " + serialized_size + ", " + numHeaders(msg) + " headers");

                }
            }
            if(print_msg) {
                if(log.isTraceEnabled())
                    log.trace("headers are " + msg.printHeaders() + ", payload size=" + payload_size);
            }
            break;
        }

        return down_prot.down(evt);          // Pass on to the layer below us
    }


    int sizeOf(Message msg) {

        synchronized(out_stream) {
            try {
                out_stream.reset();
                out.reset();
                msg.writeTo(out);
                out.flush();
                return out_stream.size();
            }
            catch(Exception e) {
                return 0;
            }
            finally {
                Util.close(out);
            }
        }
    }

    int numHeaders(Message msg) {
        if(msg == null)
            return 0;
        return msg.getNumHeaders();
    }


}
