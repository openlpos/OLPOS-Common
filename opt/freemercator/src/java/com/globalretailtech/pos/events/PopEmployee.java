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
 * Pop the current employee of the employee stack. Used to get
 * rid of a temporary manager profile.
 *
 * @author  Quentin Olson
 */
public class PopEmployee extends PosEvent {


    /** Simple constructor for dynamic load */
    public PopEmployee() {
    }

    /** Constructor with context */
    public PopEmployee(PosContext c) {
        setContext(c);
    }

    /**
     * Call context pop employee.
     */
    public void engage(int value) {
        context().popEmployee();
        context().eventStack().nextEvent();
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

    private static String eventname = "PopEmployee";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }
}


