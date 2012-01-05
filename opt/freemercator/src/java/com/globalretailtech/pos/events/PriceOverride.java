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
import com.globalretailtech.data.TransItem;

/**
 * Start cash tender.
 *
 * @author  Quentin Olson
 */
public class PriceOverride extends PosNumberDialog {

    public static final int GET_AMOUNT = 1;

    private double amount;
    private String prompttext;

    public String promptText() {
        return prompttext;
    }

    public double amount() {
        return amount;
    }

    public void setPromptText(String value) {
        prompttext = value;
    }

    public void setAmount(double value) {
        amount = value;
    }

    /** Simple constructor */
    public PriceOverride() {
    }

    /** Constructor sets the context*/
    public PriceOverride(PosContext context) {
        setContext(context);
    }

    /**
     * Load the cash tender dialog
     */
    public void engage(int value) {

        // We need an item to modify.

        if (context().lastItem() == null)
            return;
        type = PosNumberDialog.CURRENCY;

        // this test tells us if this was invoked from a key press.

        if (state() == 0) {

            setPromptText(context().posParameters().getString("PromptAmount"));
            states().pushState(1);
            context().eventStack().pushEvent(this);
            context().operPrompt().update(this);
        } else {

            setAmount(context().inputDouble());
            if (amountIsValid()) {
                popState();
            } else {
                return;
            }

            context().clearInput();

            TransItem transItem = context().lastItem().transItem();

			double origAmount = transItem.amount();
			double extraAmount = amount()-origAmount;
			
            transItem.setAmount( context().posMath().mult(transItem.quantity(),origAmount));
            transItem.setExtAmount(context().posMath().mult(transItem.quantity(), extraAmount));
            transItem.setState(TransItem.PRICE_OVERRIDE);
            context().operPrompt().update(context().lastItem());
            context().receipt().update(context().lastItem());

            setAmount(0.0);
            
            // remove itself from the stack
            PosEvent event = context().eventStack().event();
            if ( event instanceof PriceOverride )
            	context().eventStack().popEvent();
            	
            context().eventStack().nextEvent();
        }
    }

    private boolean amountIsValid() {
        return true;
    }

    private int type;

    /** Implemntation of type for PosNumberDialog */
    public int type() {
        return type;
    }

    /** Validate transistions state */
    public boolean validTransition(String event) {
        return true;
    }

    /** Clear key implementation for this class */
    public void clear() {
    }

    private static String eventname = "PriceOverride";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }
}


