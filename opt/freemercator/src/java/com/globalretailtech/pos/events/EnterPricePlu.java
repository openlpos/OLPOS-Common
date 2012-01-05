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
import com.globalretailtech.pos.ej.*;
import com.globalretailtech.data.Plu;
import com.globalretailtech.data.Item;

/**
 * Start cash tender.
 *
 * @author  Quentin Olson
 */
public class EnterPricePlu extends PosNumberDialog {

    public static final int GET_AMOUNT = 1;
    public static final int DO_LOOKUP = 2;

    private int plu;
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
    public EnterPricePlu() {
    }

    /** Constructor sets the context*/
    public EnterPricePlu(PosContext context) {
        setContext(context);
    }

    /**
     * Load the cash tender dialog
     */
    public void engage(int value) {

        if (states().pendingSize() == 0) {

            plu = value;
            states().pushState(GET_AMOUNT);
            setPromptText(context().posParameters().getString("PromptAmount"));
            type = PosNumberDialog.CURRENCY;
            context().eventStack().pushEvent(this);
            context().operPrompt().update(this);
            return;
        }

        switch (state()) {

            case GET_AMOUNT:  // get the amount and save it

                setAmount(context().inputDouble());
                if (amountIsValid()) {
                    popState();
                } else {
                    return;
                }
                context().clearInput();

                String fetchSpec = Plu.getByPlu(plu);
                Vector tmp = Application.dbConnection().fetch(new Plu(), fetchSpec);

                Item item = null;
                if (tmp.size() > 0) {  // Item found

                    Plu plu = (Plu) tmp.elementAt(0);  // Get the item
                    item = plu.item();
                }

                if (item == null) {
                    return;
                }

                item.setAmount(amount());
                EjItem ejItem = new EjItem(item);
                ejItem.setContext(context());
                ejItem.engage(plu);
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

    private static String eventname = "EnterPricePlu";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }
}


