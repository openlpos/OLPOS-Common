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

/**
 * An item has been entered. Set this event as the first on the stack.
 * FirstItem is used by other classes to determine valid transitions, example:
 * you can not start a discount sale after an item has been entered.
 *
 * @author  Quentin Olson
 */
public class FirstItem extends PosEvent {

    /** The register open prompt from the context. */
    public String promptText() {
        return context().posParameters().getString("RegOpen");
    }

    /** Constructor with context. */
    public FirstItem(PosContext context) {
        setContext(context);
    }

    /**
     * Just update the dialogs and displays. For
     * displays like reciepts this clears.
     */
    public void engage(int value) {
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

    private static String eventname = "FirstItem";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }
}


