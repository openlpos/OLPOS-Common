/*
 * Copyright (C) 2001 Global Retail Technology, LLC
 * <http://www.globalretailtech.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package jpos.services;

import jpos.*;
import jpos.events.*;

/**
 *
 * @author  Quentin Olson
 * @see 
 */
public class EventCallbacksAdapter implements EventCallbacks
{
	org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger (EventCallbacksAdapter.class.getName()); 
    public void fireDataEvent(DataEvent e)
    {}

    public void fireDirectIOEvent(DirectIOEvent e)
    {}
    public void fireErrorEvent(ErrorEvent e)
    {}
    public void fireOutputCompleteEvent(OutputCompleteEvent e)
    {}
    public void fireStatusUpdateEvent(StatusUpdateEvent e)
    {}

    public BaseControl getEventSource(){
		logger.warn ("return NULL as getEventSource()");
		return null;
    }
}


