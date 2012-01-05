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
 * Item quantity input.
 *
 * @author  Quentin Olson
 */
public class Quantity extends PosEvent {

    private String prompttext;

    /** Display text for dialogs */
    public String promptText() {
        return prompttext;
    }

    public void setPromptText(String value) {
        prompttext = value;
    }

    /** Simple constructor */
    public Quantity() {
    }

    /**
     * Get the quantity from the input line and set the
     * quantity in the context. The context quantity is later applied to
     * an item. This is assumed to be followed by an item. If not
     * no big deal.
     */
    public void engage(int value) {

        context().setQuantity((context().inputDouble()));
        context().clearInput();
        setPromptText(context().posParameters().getString("EnterItem"));
        context().operPrompt().update(this);
    }

    /** Validate transistions state */
    public boolean validTransition(String event) {
        return true;
    }

    /** Clear key implementation for this class */
    public void clear() {
    }

    public boolean checkProfile(int event) {
        return true;
    }

    private static String eventname = "Quantity";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }
}


