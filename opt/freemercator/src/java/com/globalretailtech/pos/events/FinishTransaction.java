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
import com.globalretailtech.pos.events.PosEvent;

/**
 * Initial state for most transactions, also used by FirstItem to
 * get the prompt text.
 *
 * @author  Quentin Olson
 * @see FirstItem
 */
public class FinishTransaction extends PosEvent {

    /** Simple constructor */
    public FinishTransaction() {
    }

    /** Constructor sets the context*/
    public FinishTransaction(PosContext context) {
        setContext(context);
    }

    /**
     * Load the cash tender dialog
     */
    public void engage(int value) {

        // Check if there is an outstanding balance,
        // if there is show balance due and remain in
        // the totalling state

        if (context().currEj().ejBalance() > 0.0) {
            context().clearInput();
            return;
        }

        context().currEj().complete();

        // start new transaction

        context().eventStack().nextEvent();

    }

    /** Validate transistions state */
    public boolean validTransition(String event) {
        return true;
    }

    /** Clear key implementation for this class */
    public void clear() {
    }

    private static String eventname = "FinishTransaction";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }
}


