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

import java.util.Hashtable;

/**
 * Toggle training mode flag in the context.
 *
 * @author  Quentin Olson
 */
public class TrainingMode extends PosEvent {

	boolean trainingMode;
	String promptText;
	
    /** Simple constructor */
    public TrainingMode() {
        initTransition();
    }

    /**
     * Toggle the training mode only at register open.
     */
    public void engage(int value) {
        context().toggleTrainingMode();
        trainingMode = context().trainingMode();
        if (trainingMode)
			promptText = context().posParameters().getString("TrainingModeOn");
        else	
		    promptText = context().posParameters().getString("TrainingModeOff");
        context().clearInput();
        context().receipt().update(this);
        context().operPrompt().update(this);
//        context().eventStack().nextEvent();
    }
    
	public boolean trainingMode (){
		return trainingMode;
	}
	public String promptText(){
		return promptText;
	}

    /** Validate transistions state */
    private Hashtable transistions;

    private void initTransition() {
        transistions = new Hashtable();
        transistions.put(RegisterOpen.eventName(), new Boolean(true));
    }

    public boolean validTransition(String event) {
        if (transistions.get(event) != null)
            return true;
        else
            return false;
    }

    /** Check the profile on this event. */
    public boolean checkProfile() {
        return true;
    }

    /** Clear key implementation for this class */
    public void clear() {
    }

    private static String eventname = "TrainingMode";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }
}


