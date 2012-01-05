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
 * Save the current EJ.
 *
 * @author  Quentin Olson
 */
public class SaveEj extends PosEvent {

    /** Simple constructor */
    public SaveEj() {
        initTransition();
    }

    /** Constructor sets the context*/
    public SaveEj(PosContext context) {
        setContext(context);
        initTransition();
    }

    /**
     * Goes through the EJ and calls the save abstract method
     * for each object. Then starts a new transaction.
     */
    public void engage(int value) {

        for (int i = 0; i < context().currEj().size(); i++) {

            EjLine line = (EjLine) context().currEj().elementAt(i);
            line.save();
        }

        EjHeader header = (EjHeader) context().currEj().elementAt(0);
        Transaction trans = (Transaction) header.dataRecord();
        trans.updateState(Transaction.SUSPEND);

        context().receipt().clear();

		new StartTransaction(context()).engage(0);
		context().receipt().update(new RegisterOpen(context()));  //update display

    }

    /** Validate transistions state */
    private Hashtable transistions;

    private void initTransition() {
        transistions = new Hashtable();
        transistions.put(FirstItem.eventName(), new Boolean(true));
        transistions.put(EjTender.eventName(), new Boolean(true));
        transistions.put(EjCheckTender.eventName(), new Boolean(true));
        transistions.put(EjCCTender.eventName(), new Boolean(true));
        transistions.put(EjAltCurrTender.eventName(), new Boolean(true));
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

    private static String eventname = "SaveEj";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }
}


