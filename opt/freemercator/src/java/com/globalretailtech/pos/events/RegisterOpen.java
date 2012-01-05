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

import com.globalretailtech.util.*;
import com.globalretailtech.pos.context.*;
import com.globalretailtech.pos.events.PosEvent;
import com.globalretailtech.data.PosTotal;
import com.globalretailtech.data.Total;
import com.globalretailtech.data.Transaction;

/**
 * Initial state for most transactions, also used by FirstItem to
 * get the prompt text.
 *
 * @author  Quentin Olson
 * @see FirstItem
 */
public class RegisterOpen extends PosEvent {

    /** The register open prompt from the context. */
    public String promptText() {
        return context().posParameters().getString("RegOpen");
    }

    /** Simple constructor */
    public RegisterOpen() {
    }

    /** Constructor sets the context*/
    public RegisterOpen(PosContext context) {
        setContext(context);
    }

    /**
     * Just update the dialogs and displays. For
     * displays like reciepts this clears.
     */
    public void engage(int value) {

        context().homeGuis();
        context().eventStack().clearPending();
        context().eventStack().setEvent(this);
        context().operPrompt().update(this);
        context().receipt().update(this);

        // set the transaction type to sales.

        if (context().transType() != Transaction.SALES) {
            context().setTransType(Transaction.SALES);
            Transaction trans = (Transaction) context().currTrans().dataRecord();
            trans.updateStateAndType(Transaction.IN_PROGRESS, Transaction.SALES);
        }

        // check for high cash drawer amount

        double cashPickup = context().posParameters().getDouble("CashPickup");

        if (cashPickup > 0.0) {
            String fetchSpec = PosTotal.getBySiteAndPos(context().siteID(), context().posNo());
            Vector results = Application.dbConnection().fetch(new PosTotal(), fetchSpec);

            if (results.size() > 0) {
                PosTotal posTotal = (PosTotal) results.elementAt(0);
                for (int i = 0; i < posTotal.totals().size(); i++) {

                    Total total = (Total) posTotal.totals().elementAt(i);
                    if (total.totalType() == Total.CASH_IN_DRAWER) {
                        if (total.totalAmount() > cashPickup) {
                            context().eventStack().pushEvent(new PosError(context(), PosError.CASH_EXCEEDED));
                            context().operPrompt().update((PosError) context().eventStack().event());
                        }
                    }
                }
            }

        }
    }

    /** Validate transistions state */
    public boolean validTransition(String event) {
        return true;
    }

    /** Clear key implementation for this class */
    public void clear() {
    }

    private static String eventname = "RegisterOpen";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }
}


