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


/**
 * For volume pricing, not implemented.
 *
 * @author  Quentin Olson
 */
public class Size extends PosEvent {

    private String prompttext;
    private int size;

    /** Prompt text for user dialog. */
    public String promptText() {
        return prompttext;
    }

    public int size() {
        return size;
    }

    public void setPromptText(String value) {
        prompttext = value;
    }

    public void setSize(int value) {
        size = value;
    }

    /** Simple constructor */
    public Size() {
    }

    /**
     * Get the size (scale amount) from the
     * context input line.
     */
    public void engage(int value) {

        // remove any size on the stack

        if (context().eventStack().event() instanceof Size) {
            context().eventStack().popEvent();
        }

        setSize(value);
        context().clearInput();
        setPromptText(context().posParameters().getString("EnterItem"));
        context().operPrompt().update(this);
        context().eventStack().pushEvent(this);
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

    private static String eventname = "Size";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }
}


