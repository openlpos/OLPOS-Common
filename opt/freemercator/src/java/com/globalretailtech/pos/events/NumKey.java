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

import com.globalretailtech.util.*;
import com.globalretailtech.pos.ej.*;
import com.globalretailtech.pos.gui.*;
import com.globalretailtech.pos.events.PosEvent;
import org.apache.log4j.Logger;

/**
 * Handles number key input. Add to the input buffer. Also
 * checks for length of the input buffer if it matches a mask, do an item lookup (needs to
 * be parameterized in the property file.) (not implemented)
 *
 * @author  Quentin Olson
 */
public class NumKey extends PosEvent {

    static Logger logger = Logger.getLogger(NumKey.class);

    protected String promptText;
    protected String numText;

    /** Prompt text for dialogs */
    public String promptText() {
        return promptText;
    }

    /** Number text for dialogs */
    public String numText() {
        return numText;
    }

    /** Simple constructor for dynamic load */
    public NumKey() {
    }

    /**
     * If the input is >= 0, assume valid number is entered.
     * Else look at the current PosEvent and figure out what to do:
     * <ul>
     * <li>Logon, get the prompt text from that event</li>
     * <li>EjCheckTender, get the prompt text from that event</li>
     * </ul>
     * etc.
     */
    public void engage(int value) {

        if (value >= 0) {
            context().addToInput(value);
        }

        PosEvent currEvent = context().eventStack().event();

        if (currEvent instanceof Weight) {
            Weight weight = (Weight) currEvent;
            weight.setWeight(context().inputDouble());
        }

        // Format a number prompt with masking

        logger.debug("NumKey " + currEvent.getClass());

        if (currEvent instanceof PosNumberDialog) {

            PosNumberDialog numberDialog = (PosNumberDialog) currEvent;
            promptText = numberDialog.promptText();

            numText = context().inputLine();

            switch (numberDialog.type()) {

                case PosNumberDialog.CLEAR:
                    break;
                case PosNumberDialog.MASK:
                    StringBuffer tmp = new StringBuffer();
                    for (int i = 0; i < numText.length(); i++) {
                        tmp.append("*");
                    }
                    numText = tmp.toString();
                    break;
                case PosNumberDialog.DECIMAL:

                    double decimal = context().inputDouble() / 100.0;
                    numText = Double.toString(decimal);
                    break;
                case PosNumberDialog.CURRENCY:
                    numText = Format.toMoney(Double.toString(context().inputDouble()), Application.locale());
                    break;
                default:
                    break;
            }

            if (currEvent instanceof CustMain) {
                CustMain cust = (CustMain) currEvent;
                cust.setInputText(numText);
            }
        }

        // Convert it to be displayed as currency
        else if (currEvent instanceof EjLine) {

            EjLine line = (EjLine) currEvent;
            promptText = line.prompt();
            if (line.numberType() == EjLine.AMOUNT) {
                numText = Format.toMoney(Double.toString(context().inputDouble()), Application.locale());
            } else {
                numText = context().inputLine();
            }
        } else {
            promptText = "";
            numText = context().inputLine();
        }
        context().operPrompt().update(this);

    }

    /** Validate transistions state */
    public boolean validTransition(String event) {
        return true;
    }

    /**
     * Erase the last char on the input line
     */
    public void clear() {
        context().eraseLast();
        engage(-1);
    }

    /** Always return true. */
    public boolean checkProfile(int event) {
        return true;
    }

    private static String eventname = "NumKey";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }
}


