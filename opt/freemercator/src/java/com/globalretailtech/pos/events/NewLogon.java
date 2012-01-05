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


import com.globalretailtech.data.Transaction;
import com.globalretailtech.pos.ej.*;
import com.globalretailtech.pos.context.*;
import com.globalretailtech.pos.events.PosEvent;
import org.apache.log4j.Logger;

/**
 * Creates a new logon context. Clears diplays, saves the transaction header
 * as a logon type transaction, calls context startrans, which
 * starts a new sales transacion.
 *
 * @author  Quentin Olson
 */
public class NewLogon extends PosEvent {

    static Logger logger = Logger.getLogger(NewLogon.class);

    /** Simple constructor for dynamic load */
    public NewLogon() {
    }

    /** Constructor sets context */
    public NewLogon(PosContext context) {
        setContext(context);
    }

    /**
     * Creates a new logon pos context.
     * <ul>
     * <li>Enable keys</li>
     * <li>Updates transaction header</li>
     * <li>Calls context nextEvent ()</li>
     * </ul>
     */
    public void engage(int value) {

        context().enableKeys();
        EjHeader line = (EjHeader) context().currEj().elementAt(0);

        if (line.transHeader().updateLogon(Transaction.COMPLETE, Transaction.LOGON, context().user())) {
            context().eventStack().nextEvent();
        } else {
            logger.warn("Failed to create logon record in NewLogon.");
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

    /** Return staic name. */
    private static String eventname = "NewLogon";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    public static String eventName() {
        return eventname;
    }
}


