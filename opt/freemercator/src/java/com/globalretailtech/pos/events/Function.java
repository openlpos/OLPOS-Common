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


import com.globalretailtech.pos.events.PosEvent;

/**
 * Implments various functions, total subtotal. Disallows the same function to be
 * invoked twice. More functions may be added.
 *
 * @author  Quentin Olson
 */
public class Function extends PosEvent {

    /** Do subtotal function */
    public static final int SUBTOTAL = 0;
    /** do total function */
    public static final int TOTAL = 1;

    private String prompttext;
    private double num;

    /** Prompt text for function. */
    public String promptText() {
        return prompttext;
    }

    public double num() {
        return num;
    }

    public void setPromptText(String value) {
        prompttext = value;
    }

    public void setNum(double value) {
        num = value;
    }

    public Function() {
    }

    /**
     * Depending on value passed do the operation
     */
    public void engage(int function) {

        switch (function) {

            case SUBTOTAL:

                setPromptText(context().posParameters().getString("SubTotal"));
                setNum(context().currEj().ejSubTotal());
                break;

            case TOTAL:
                setPromptText(context().posParameters().getString("Total"));
                setNum(context().currEj().ejTotal());
                break;

            default:
                break;

        }
        context().operPrompt().update(this);
    }

    /** Always return true. */
    public boolean validTransition(String event) {
        return true;
    }

    /** Clear impementation for clear, do nothing. */
    public void clear() {
    }

    /** Always return true. */
    public boolean checkProfile(int event) {
        return true;
    }

    private static String eventname = "Function";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }
}


