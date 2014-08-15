package bboss.org.jgroups.util;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Bela Ban
 * @version $Id$
 */
public class MyConcurrentLinkedQueue<T> extends ConcurrentLinkedQueue<T> {
    private static final long serialVersionUID=8832199198382745902L;

    public T poll(long timeout) {
        long target_time=System.currentTimeMillis() + timeout;
        T retval;

        do {
            retval=poll();
            if(retval != null)
                return retval;
        }
        while(target_time < System.currentTimeMillis());

        return retval;
    }
}
