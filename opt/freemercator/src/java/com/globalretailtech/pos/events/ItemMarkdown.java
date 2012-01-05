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
 * Does a markdown on the current or selected item.
 * The value passed to engage specifies which item modifier
 * to load from the db and apply.
 *
 *
 * @author  Quentin Olson
 */

import java.util.Vector;
import java.util.Hashtable;

import com.globalretailtech.util.Application;

import com.globalretailtech.data.Promotion;
import com.globalretailtech.pos.operators.*;
import com.globalretailtech.pos.ej.*;

public class ItemMarkdown extends PosEvent {

    private PosItemModifier modifier;

    /** The item modifier (promotion) for this event */
    public PosItemModifier modifier() {
        return modifier;
    }

    /** Simple constructor for dynamic load */
    public ItemMarkdown() {
        initTransition();
    }

    /**
     * Load the promotion, engage it and add it to
     * the EJ.
     */
    public void engage(int value) {

        String fetchSpec = Promotion.getByNo(value);
        Vector v = Application.dbConnection().fetch(new Promotion(), fetchSpec);

        if (v.size() == 1) {

            Promotion promotion = (Promotion) v.elementAt(0);
            EjPromotion eji = new EjPromotion(context(), promotion);

            eji.engage(0);
        }
        context().eventStack().nextEvent();
    }

    /**
     * Check profile on this one.
     */

    public boolean checkProfile() {
        return true;
    }

    /** Validate transistions state */
    private Hashtable transistions;

    private void initTransition() {
        transistions = new Hashtable();
        transistions.put(FirstItem.eventName(), new Boolean(true));
        transistions.put(PosError.eventName(), new Boolean(true));
    }

    public boolean validTransition(String event) {
        if (transistions.get(event) != null)
            return true;
        else
            return false;
    }

    /** Clear impementation for clear, do nothing. */
    public void clear() {
    }

    /** Always return true. */
    public boolean checkProfile(int event) {
        return true;
    }

    private static String eventname = "ItemMarkdown";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }
}


