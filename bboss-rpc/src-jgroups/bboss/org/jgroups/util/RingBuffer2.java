package bboss.org.jgroups.util;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

/**
 * 
 * @author Bela Ban
 * @version $Id: RingBuffer.java,v 1.10 2010/02/21 12:27:53 belaban Exp $
 */
public class RingBuffer2<T> {
    private final ConcurrentLinkedQueue<T>  queue;
    private final int                       capacity;
    private final AtomicInteger             size=new AtomicInteger(0);

    @SuppressWarnings("unchecked")
    public RingBuffer2(int capacity) {
        queue=new ConcurrentLinkedQueue<T>();
        this.capacity=capacity;
    }

    /**
     * Adds an elements into the buffer. Blocks if full
     * @param el
     */
    public void add(T el) {
        if(el == null)
            throw new IllegalArgumentException("null element");
        int counter=0;
        while(size.get() + 1 > capacity) {
            if(counter > 5)
                LockSupport.parkNanos(10);
            else
                counter++;
        }
        queue.add(el);
        size.incrementAndGet();
    }

    public T remove() {
        T el=queue.poll();
        if(el != null)
            size.decrementAndGet();
        return el;
    }

    public String dumpNonNullElements() {
        StringBuilder sb=new StringBuilder();
        for(T ref: queue)
            if(ref != null)
                sb.append(ref + " ");
        return sb.toString();
    }

    public int size() {
        return queue.size();
    }

    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append(size() + " elements");
        if(size() < 100) {
            sb.append(": ").append(dumpNonNullElements());
        }

        return sb.toString();
    }
}
