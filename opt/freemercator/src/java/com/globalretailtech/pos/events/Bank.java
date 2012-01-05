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

import com.globalretailtech.pos.context.*;

/**
 * Start a bank transaction, paid-in, paid-out....
 * This one is a little different than the other ej type
 * operations. The bank type is supplied by the key value,
 * this tells us which dialog to load.
 *
 * @author  Quentin Olson
 * @see EjBank
 */
public class Bank extends PosEvent {

    /** Simple constructor */
    public Bank() {
		initTransition();
    }

    /** Constructor sets the context*/
    public Bank(PosContext context) {
        setContext(context);
		initTransition();
    }

    /**
     * Load the cash tender dialog
     */
    public void engage(int value) {

        if (value == 1) {
            context().eventStack().loadDialog("PaidIn", context());
        } else {
            context().eventStack().loadDialog("PaidOut", context());
        }
        PosDialogEvent e = (PosDialogEvent) context().eventStack().event();
        context().eventStack().nextEvent();
    }

	/** Validate transistions state */
	private Hashtable transistions;

	private void initTransition() {
		transistions = new Hashtable();
		transistions.put(RegisterOpen.eventName(), new Boolean(true));
	}



    /** Validate transistions state */
    public boolean validTransition(String event) {
		if (transistions.get(event) != null)
			return ((Boolean)transistions.get(event)).booleanValue();
		else
			return false;
    }

    /** Clear key implementation for this class */
    public void clear() {
    }

    private static String eventname = "Bank";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }
}


