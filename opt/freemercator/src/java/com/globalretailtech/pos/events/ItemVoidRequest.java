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
 * Requests voiding current item.
 * Actual deleting will be done by appropriate listener by
 * using ItemVoid event.
 *
 * @author  Igor Semenko
 */

import java.util.Hashtable;

import com.globalretailtech.pos.operators.*;

public class ItemVoidRequest extends PosEvent {

    private PosItemModifier modifier;

    /** The item modifier (promotion) for this event */
    public PosItemModifier modifier() {
        return modifier;
    }

    /** Simple constructor for dynamic load */
    public ItemVoidRequest() {
        initTransition();
    }

    /**
     * Empty
     */
    public void engage(int value) {
    }

    /**
     * Check profile on this one.
     */
    public boolean checkProfile() {
        return true;
    }

    private Hashtable transistions;

    private void initTransition() {
    }

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

    private static String eventname = "ItemVoidRequest";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }
}


