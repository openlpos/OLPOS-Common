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


import com.globalretailtech.pos.context.*;
import com.globalretailtech.pos.ej.*;

/**
 * Void the current sale (EJ). Sets EJ state to VOID
 * and saves the records. Assume external applications
 * will use EJ header flag.
 *
 * @author  Quentin Olson
 */
public class VoidSale extends PosEvent {

    /** Simple constructor */
    public VoidSale() {
    }

    /** Constructor sets the context*/
    public VoidSale(PosContext context) {
        setContext(context);
    }

    /**
     * Get the EJ header and change the state to
     * voided, save the transaction and start a new one.
     */
    public void engage(int value) {

		if ( ! context().trainingMode()){
			EjHeader trans = (EjHeader) context().currEj().elementAt(0);
			trans.transHeader().updateState(com.globalretailtech.data.Transaction.VOIDED);

			for (int i = 0; i < context().currEj().size(); i++) {

				EjLine line = (EjLine) context().currEj().elementAt(i);
				line.save();
			}
		}
		
		context().setCustomer(null);
		context().setSaleMod(null);
        context().receipt().clear();
		context().eventStack().clearPending();
		context().eventStack().pushEvent( new RegisterOpen (context()));
		context().eventStack().pushEvent( new StartTransaction(context()));
		context().eventStack().nextEvent();
    }

    /** Validate transistions state */
    public boolean validTransition(String event) {
        return true;
    }

    /** Clear key implementation for this class */
    public void clear() {
    }

    private static String eventname = "VoidSale";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }
}


