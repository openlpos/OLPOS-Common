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

import java.util.Hashtable;


import com.globalretailtech.data.Transaction;
import com.globalretailtech.pos.context.*;
import com.globalretailtech.pos.ej.*;
/**
 * Set the return sale state, sets the oontext sign ().
 *
 * @author  Quentin Olson
 */
public class ReturnSale extends PosEvent {

    /** Simple constructor */
    public ReturnSale() {
        initTransition();
    }

    /** Constructor sets the context*/
    public ReturnSale(PosContext context) {
        setContext(context);
        initTransition();
    }

    /**
     * Gets the current header EJ record, sets the type to RETURN and
     * toggles the sign in the context.
     */
    public void engage(int value) {

        EjHeader trans = (EjHeader) context().currEj().elementAt(0);
        trans.transHeader().updateStateAndType(Transaction.IN_PROGRESS, Transaction.RETURN);
        context().toggleSign();

    }

    /** Validate transistions state */
    private Hashtable transistions;

    private void initTransition() {
        transistions = new Hashtable();
        transistions.put(RegisterOpen.eventName(), new Boolean(true));
    }

    public boolean validTransition(String event) {
        if (transistions.get(event) != null)
            return true;
        else
            return false;
    }

    /** Clear key implementation for this class */
    public void clear() {
    }

    private static String eventname = "ReturnSale";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }
}


