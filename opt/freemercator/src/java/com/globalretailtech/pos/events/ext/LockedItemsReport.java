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

import com.globalretailtech.pos.events.PosEvent;


import com.globalretailtech.pos.context.*;

/**
 * Performs locked items report on oper display,
 * POSPrinter and FiscalPrinter (not implemented 07/02/03)
 *
 * @author  Igor Semenko
 */
public class LockedItemsReport extends PosEvent {

    /** Simple constructor */
    public LockedItemsReport() {
    }

    /** Constructor sets the context*/
    public LockedItemsReport(PosContext context) {
        setContext(context);
    }

    /**
     * 
     */
    public void engage(int value) {

		context().receipt().update(this);
        
    }

    /** Validate transistions state */
    public boolean validTransition(String event) {
        return true;
    }

    /** Clear key implementation for this class */
    public void clear() {
    }

    private static String eventname = "LockedItemsReport";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }
}


