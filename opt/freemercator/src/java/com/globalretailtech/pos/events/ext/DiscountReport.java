/*
* Copyright (C) 2003 Igor Semenko
* igorsemenko@yahoo.com
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
*
*/
package com.globalretailtech.pos.events.ext;

import jpos.JposException;

import com.globalretailtech.pos.events.PosError;
import com.globalretailtech.pos.events.PosEvent;


import com.globalretailtech.pos.context.*;

/**
 * Performs discount report on fiscal printer
 *
 * @author  Igor Semenko
 */
public class DiscountReport extends PosEvent {

    /** Simple constructor */
    public DiscountReport() {
    }

    /** Constructor sets the context*/
    public DiscountReport(PosContext context) {
        setContext(context);
    }

    /**
     * 
     */
    public void engage(int value) {

        // if there is a balance due, don't open the cash drawer
        try {
			context().fiscalPrinter().discReport();
		} catch (JposException e) {
			PosError error = new PosError (context(), e);
			context().eventStack().pushEvent(error);
			context().operPrompt().update(error);
		}
        
    }

    /** Validate transistions state */
    public boolean validTransition(String event) {
        return true;
    }

    /** Clear key implementation for this class */
    public void clear() {
    }

    private static String eventname = "DiscountReport";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }
}


