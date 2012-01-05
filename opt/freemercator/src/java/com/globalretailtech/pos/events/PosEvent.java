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


import com.globalretailtech.pos.context.*;
import com.globalretailtech.data.*;
import org.apache.log4j.Logger;

/**
 * Super class of all POS events.
 *
 * @author  Quentin Olson
 */
public abstract class PosEvent {

    public static Logger eventLogger = Logger.getLogger(PosEvent.class);

    // Event types

    /** Number key event */
    public final static int NUMBER = 1;
    /** Item key event (not used) */
    public final static int ITEM = 2;
    /** Item modifier key event (not used) */
    public final static int ITEM_MODIFIER = 3;
    /** Navigate key for menus */
    public final static int NAVIGATE = 4;
    /** Function key event */
    public final static int FUNCTION = 5;
    /** Navigate previous button (moves between panels) */
    public final static int PREV = 6;
    /** Navigate next button  */
    public final static int NEXT = 7;
    /** Navigate to home panel */
    public final static int HOME = 8;
    /** Navigate to customer main panel (needs to be depricated). */
    public final static int CUST_MAIN = 9;
    /** Navigate to customer history panel (needs to be depricated). */
    public final static int CUST_HIST = 10;


    private PosContext context;

    /**
     * Simple constructor.
     */
    public PosEvent() {
    }

    /** Context for the event */
    public PosContext context() {
        return context;
    }

    /** Set/reset the context for this event. */
    public void setContext(PosContext value) {
        context = value;
    }

    /** Dialog flag may be reset by sub-class. */
    public boolean isDialog() {
        return false;
    }

    /**
     * Abstract methods
     */

    /** Event logic is implemented here */
    public abstract void engage(int value) throws PosException;

    /** Class specific clear (key) implementation */
    public abstract void clear();

    private static String eventname = "PosEvent";

    public String toString() {
        return eventname;
    }

    public static String eventName() {
        return eventname;
    }

    /** Sublcasses implement event validation. */
    public abstract boolean validTransition(String event);

    /**
     * Sublcasses may override employee profile check.
     * This is used for common use events that don't need
     * to be checked, example, number keys. Override this
     * in profile sensitive classes.
     */
    public boolean checkProfile() {
        return false;
    }

    /**
     * Validate that this is a valid event transition. Implemented as
     * validTransition in subclass.
     */
    public boolean isValidEvent() {
        return (validTransition(context().eventStack().event().toString()));
    }

    /**
     * Validate that this user has the correct permission.
     */
    public boolean employeeHasProfile() {


        /**
         * If no employee is logged on for this context return true.
         */
        if (context().employee() == null)
            return true;

        /**
         * See if this profile needs to be checked
         */
        if (!checkProfile())
            return true;

        /**
         * Get the list of profile events from the employee profile
         * and see if this employee is allowed to do this function.
         */
        Vector profiles = context().employee().posProfile().events();

        if (profiles.size() == 0)
            return true;  // super user

        for (int i = 0; i < profiles.size(); i++) {

            PosProfileEvent prof = (PosProfileEvent) profiles.elementAt(i);
            if (prof.posEvent().equals(this.toString()))
                return true;
        }

        context().eventStack().pushEvent(new PopEmployee(context));  // to remove the temp manager logon
        context().eventStack().pushEvent(this);                       // to execute the original event

        LogOn logonDialog = new LogOn(context);
        logonDialog.pushState(LogOn.TEMPORARY_LOGON);                  // creats a temp logon
        logonDialog.pushState(LogOn.ENTER_PASS);                       // gets the pass
        logonDialog.pushState(LogOn.ENTER_USER);                       // gets the user
        context().eventStack().pushEvent(logonDialog);                 // adds the logon dialog to the event stack

        // ouput error prompt to display

        context().eventStack().pushEvent(new PosError(context(), PosError.MGR_REQUIRED));
        context().operPrompt().update((PosError) context().eventStack().event());

        return false;
    }
}


