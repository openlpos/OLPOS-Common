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
 * Start cash tender dialog.
 *
 * @author  Quentin Olson
 * @see EjTender
 */
public class CashTender extends PosEvent {

    /** Simple constructor */
    public CashTender() {
        initTransition();
    }

    /** Constructor sets the context*/
    public CashTender(PosContext context) {
        setContext(context);
        initTransition();
    }

    /**
     * Load the cash tender dialog (default) or one of
     * the following dialogs depending on parameter:
     *  0 - CashTender
     *  1 - CreditTender
     *  2 - GiftTender 
     */
    public void engage(int value) {

		
		String dialog;

		switch (value) {
			case 1 :
				dialog = "CreditTender";
				break;

			case 2 :
				dialog = "GiftTender";
				break;

			default :
				dialog = "CashTender";
				break;
		}
		
        context().eventStack().loadDialog(dialog, context());
        context().eventStack().nextEvent();
    }

    /** Validate transistions state */
    private Hashtable transistions;

    private void initTransition() {
        transistions = new Hashtable();
        transistions.put(FirstItem.eventName(), new Boolean(true));
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

    private static String eventname = "CashTender";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }
}


