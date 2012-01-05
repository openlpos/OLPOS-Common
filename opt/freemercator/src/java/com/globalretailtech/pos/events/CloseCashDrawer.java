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
 * Interface with the user for cash drawer operations.
 *
 * @author  Quentin Olson
 */
public class CloseCashDrawer extends PosEvent {

    private String promptText;

    /** The register open prompt from the context. */
    public String promptText() {
        return context().posParameters().getString("CloseDrawer");
    }

    /** Simple constructor */
    public CloseCashDrawer() {
    }

    /** Constructor sets the context*/
    public CloseCashDrawer(PosContext context) {
        setContext(context);
    }

    public CloseCashDrawer(PosContext context, String prompt) {
        setContext(context);
        promptText = prompt;
    }

    /**
     * Just update the dialogs and displays. For
     * displays like reciepts this clears.
     */
    public void engage(int value) {

        // if there is a balance due, don't open the cash drawer

        if (context().currEj().ejBalance() >= 0.0) {
            context().eventStack().nextEvent();
            return;
        }
        context().operPrompt().update(this);
    }

    /** Validate transistions state */
    public boolean validTransition(String event) {
        return true;
    }

    /** Clear key implementation for this class */
    public void clear() {
    }

    private static String eventname = "CloseCashDrawer";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }
}


