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

package com.globalretailtech.pos.events;


import com.globalretailtech.pos.ej.*;
import com.globalretailtech.pos.gui.PosGui;
import com.globalretailtech.data.Transaction;
import com.globalretailtech.data.Employee;

import org.apache.log4j.Logger;

/**
 * Calls PosContext logOff () to destroy the current context.
 *
 * @author  Quentin Olson
 */

public class LogOff extends PosEvent {

    static Logger logger = Logger.getLogger(LogOff.class);

    /** Simple constructor for dynamic load */
    public LogOff() {
    }

    /**
     * Logs off the current user.
     * Note: currently does not create a
     * Log off transaction. Calls PosContext
     * logOff ().
     */
    public void engage(int value) {

        Transaction t = ((EjHeader) context().currEj().elementAt(0)).transHeader();
        t.setTransType(Transaction.LOGOFF);
        t.updateStateAndType(Transaction.COMPLETE, Transaction.LOGOFF);

        //update logoff time in DB

        Employee employee = context().employee();
        employee.logoff();


        // Close any guis that might exist.

        for (int i = 0; i < context().guis().size(); i++) {
            PosGui gui = (PosGui) context().guis().elementAt(i);
            gui.close();
        }
    }

    /** Validate transistions state */
    public boolean validTransition(String event) {
        return true;
    }

    /** Clear key implementation for this class */
    public void clear() {
    }

    /** Always return true. */
    public boolean checkProfile(int event) {
        return true;
    }

    private static String eventname = "LogOff";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }
}


