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

import java.util.Vector;
import java.util.Hashtable;

import com.globalretailtech.util.*;
import com.globalretailtech.pos.context.*;
import com.globalretailtech.pos.events.PosEvent;
import com.globalretailtech.data.*;
import org.apache.log4j.Logger;

/**
 * Initial state for most transactions, also used by FirstItem to
 * get the prompt text.
 *
 * @author  Quentin Olson
 * @see FirstItem
 */
public class AltCurrTender extends PosEvent {

    static Logger logger = Logger.getLogger(AltCurrTender.class);

    /** Simple constructor */
    public AltCurrTender() {
        initTransition();
    }

    /** Constructor sets the context*/
    public AltCurrTender(PosContext context) {
        initTransition();
        setContext(context);
    }

    /**
     * Load the cash tender dialog
     */
    public void engage(int value) {

        // The type of alternate currency is passed as the value to engage ()

        String fetchSpec = Currency.getByID(value);
        Vector v = Application.dbConnection().fetch(new Currency(), fetchSpec);

        if (v.size() == 1) {

            context().setAltCurrency((Currency) v.elementAt(0));

            // Set up and push the current dialog on the stack

        } else {
            logger.warn("Alternate Currency ID not found " + value);
            return;
        }

        context().eventStack().loadDialog("AltCurrTender", context());
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

    private static String eventname = "AltCurrTender";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }
}


