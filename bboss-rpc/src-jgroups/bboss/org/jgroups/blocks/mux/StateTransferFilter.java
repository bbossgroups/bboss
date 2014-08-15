/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc. and individual contributors
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


/**
 * Allows an component that integrates with MuxUpHandler to accept or reject
 * partial state transfer events. If multiple StateTransferFilters are associated
 * with the same MuxUpHandler, but they are mutually exclusive in terms of which
 * <code>state_id</code>'s they will accept, then the MuxUpHandler can direct
 * the state transfer event to the appropriate handler.
 *
 * @author Brian Stansberry
 * 
 * @version $Id: MuxRpcDispatcher.java,v 1.1 2010/04/13 17:57:07 ferraro Exp $
 */
public interface StateTransferFilter {
   
    /**
     * Returns <code>true</code> if a state transfer event associated with a
     * given <code>state_id</code> should be passed to this component's UpHandler.
     * 
     * @param state_id the id of the partial state transfer
     * 
     * @return <code>true</code> if a state transfer event associated with a
     * given <code>state_id</code> should be passed to this component's UpHandler
     */
    boolean accepts(String state_id);
    
}
