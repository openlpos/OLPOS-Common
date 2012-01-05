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
import com.globalretailtech.pos.events.PosEvent;

/**
 * Initial state for most transactions, also used by FirstItem to
 * get the prompt text.
 *
 * @author  Quentin Olson
 * @see FirstItem
 */
public class Pause extends PosEvent {

    private String promptText;

    /** The register open prompt from the context. */
    public String promptText() {
        return promptText;
    }

    /** Simple constructor */
    public Pause() {
    }

    /** Constructor sets the context*/
    public Pause(PosContext context) {
        setContext(context);
        promptText = context().posParameters().getString("Pause");
    }

	public Pause(PosContext context, String prompt) {
		setContext(context);
		promptText = prompt;
	}
int passes;
	public Pause(PosContext context, int passes) {
		setContext(context);
		this.passes = passes;
	}

    /**
     * Just update the dialogs and displays. For
     * displays like reciepts this clears.
     */
    public void engage(int value) {
    	if (passes == 0)
        	context().eventStack().nextEvent();
        else
        	passes--;	
    }

    /** Validate transistions state */
    public boolean validTransition(String event) {
        return true;
    }

    /** Clear key implementation for this class */
    public void clear() {
    }

    private static String eventname = "Pause";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }
}


