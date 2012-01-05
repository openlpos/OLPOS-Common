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
import java.util.Vector;


import com.globalretailtech.util.Application;
import com.globalretailtech.pos.events.PosEvent;
import com.globalretailtech.data.Promotion;
import org.apache.log4j.Logger;

/**
 * Turn this into a discount sale. The promotion ID to use
 * for this is passed as the value for the engage () method
 * and retrieved from the database.
 *
 * @author  Quentin Olson
 */
public class Discount extends PosEvent {

    static Logger logger = Logger.getLogger(Discount.class);


    private String desc;

    /** Simple constructor. */
    public Discount() {
        initTransition();
    }

    /**
     * Use the provided discount ID, (tied to this
     * event/key in the PosKey table and set this as the
     * sale level promotion for the current sale. This will
     * be applied to every item as they are entered.
     */
    public void engage(int value) {

        String fetchSpec = null;
        Vector v = null;

        fetchSpec = Promotion.getByID(value);
        v = Application.dbConnection().fetch(new Promotion(), fetchSpec);

        if (v.size() > 0) {
            
            Promotion promo = (Promotion) v.elementAt(0);
			context().setSaleMod(promo);
			context().applySaleMod (); // apply to existing
			
        } else {
            logger.warn("Promotion record not found for Discount : " + value);
        }
    }

    /** Validate transistions state */
    private Hashtable transistions;

    private void initTransition() {
        transistions = new Hashtable();
        transistions.put(RegisterOpen.eventName(), new Boolean(true));
		transistions.put(PosError.eventName(), new Boolean(true));
		transistions.put(FirstItem.eventName(), new Boolean(true));
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

    private static String eventname = "Discount";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }
}


