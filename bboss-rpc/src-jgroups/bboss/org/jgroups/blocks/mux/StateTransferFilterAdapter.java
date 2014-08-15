/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package bboss.org.jgroups.blocks.mux;

import bboss.org.jgroups.Event;
import bboss.org.jgroups.UpHandler;

/**
 * Supports the {@link UpHandler} interface and the {@link StateTransferFilter}
 * interface by delegating to two independent objects each of which implements
 * one of the interfaces.
 * 
 * @author Brian Stansberry
 * @version $Revision$
 */
public class StateTransferFilterAdapter implements UpHandler, StateTransferFilter
{
    private final UpHandler upHandlerDelegate;
    private final StateTransferFilter filterDelegate;
   
    public StateTransferFilterAdapter(UpHandler upHandlerDelegate, StateTransferFilter filterDelegate) {
      
        if (upHandlerDelegate == null) {
            throw new IllegalArgumentException("upHandlerDelegate is null");
        }
        if (filterDelegate == null) {
            throw new IllegalArgumentException("filterDelegate is null");
        }
      
        this.upHandlerDelegate = upHandlerDelegate;
        this.filterDelegate = filterDelegate;
    }
   

    /**
     * Invokes on the UpHandler delegate.
     *
     * @see bboss.org.jgroups.UpHandler#up(bboss.org.jgroups.Event)
     */
//    @Override
    public Object up(Event evt)
    {
        return upHandlerDelegate.up(evt);
    }

    /**
     * Invokes on the StateTransferFilter delegate.
     * 
     * @see bboss.org.jgroups.blocks.mux.StateTransferFilter#accepts(java.lang.String)
     */
//    @Override
    public boolean accepts(String stateId)
    {
        return filterDelegate.accepts(stateId);
    }

}
