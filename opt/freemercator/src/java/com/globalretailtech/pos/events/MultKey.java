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
 * Multiplyer key, takes the value from the key and multiplies the input buffer by
 * it, then reverts to NumKey
 *
 * @author  Quentin Olson
 */
public class MultKey extends NumKey {

    /** Simple constructor for dynamic load */
    public MultKey() {
        super();
    }

    /**
     * Multiplier for context input, useful for a "00" key.
     * Multiplies the input by the value, set in the PosKey
     * table. Extended for Logon event to echo the promptText
     * from that PosEvent.
     */
    public void engage(int value) {

        context().multByInput(value);
        super.engage(-1);
    }

    /** Always return true. */
    public boolean checkProfile(int event) {
        return true;
    }

    private static String eventname = "MultKey";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }
}


