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
 * Modifies the tax records for the last item entered to use the
 * primary tax rate.
 *
 *
 * @author  Quentin Olson
 */

import java.util.Hashtable;

import com.globalretailtech.pos.operators.*;
import com.globalretailtech.pos.ej.*;

public class ItemUseAltTax extends PosEvent {

    private PosItemModifier modifier;

    /** The item modifier (promotion) for this event */
    public PosItemModifier modifier() {
        return modifier;
    }

    /** Simple constructor for dynamic load */
    public ItemUseAltTax() {
        initTransition();
    }

    /**
     * Void the last item in the ej. Logic also
     * sets any related ej record amounts to 0,
     * but doesn't remove them.
     */
    public void engage(int value) {

        for (int ejIndex = context().lastItemIndex();
             ejIndex < context().currEj().size(); ejIndex++) {

            EjLine line = (EjLine) context().currEj().elementAt(ejIndex);

            if (line.lineType() == EjLine.TAX) {
                EjTax itemTax = (EjTax) line;
                itemTax.setActiveTaxRate(EjTax.ALTERNATE_TAX);
            }
        }

        context().eventStack().nextEvent();
    }

    /**
     * Check profile on this one.
     */
    public boolean checkProfile() {
        return true;
    }

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

    /** Clear implementation for clear, do nothing. */
    public void clear() {
    }

    /** Always return true. */
    public boolean checkProfile(int event) {
        return true;
    }

    private static String eventname = "ItemUseAltTax";

    /** Return static name. */
    public String toString() {
        return eventname;
    }

    /** Return static name. */
    public static String eventName() {
        return eventname;
    }
}


