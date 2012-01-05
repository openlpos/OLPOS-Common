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
import com.globalretailtech.pos.ej.*;

/**
 * Traverse the ej and switch to primary tax rate
 * for each EjTax record.
 *
 * @author  Quentin Olson
 */
public class UsePrimaryTax extends PosEvent {

    /** Simple constructor for dynamic load */
    public UsePrimaryTax() {
    }

    public UsePrimaryTax(PosContext context) {
        setContext(context);
    }

    /**
     * Switch each tax item in the current EJ to the primay tax rate
     */
    public void engage(int value) {

        for (int i = 0; i < context().currEj().size(); i++) {

            EjLine line = (EjLine) context().currEj().elementAt(i);
            if (line.lineType() == EjLine.TAX) {
                EjTax tax = (EjTax) line;
                tax.setActiveTaxRate(EjTax.PRIMARY_TAX);
            }
        }
    }

    /** Validate transitions state */
    public boolean validTransition(String event) {
        return true;
    }

    /** Clear key implementation for this class */
    public void clear() {
    }

    private static String eventname = "UsePrimaryTax";

    /** Return static name. */
    public String toString() {
        return eventname;
    }

    /** Return static name. */
    public static String eventName() {
        return eventname;
    }
}


