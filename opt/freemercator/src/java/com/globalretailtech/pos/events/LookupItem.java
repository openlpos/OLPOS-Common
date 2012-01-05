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


import com.globalretailtech.util.Application;
import com.globalretailtech.data.Item;
import com.globalretailtech.pos.ej.EjItem;
/**
 * Simulates an item lookup by creating an EjItem then engaging that class with
 * the value from the input. If the input line is less than 8 (MAX_PLU_LEN) digits
 * use the plu else lookup the sku directly.
 *
 * @author  Quentin Olson
 */
public class LookupItem extends PosEvent {

    public static final int MAX_PLU_LEN = 8;

    /** Simple constructor for dynamic load */
    public LookupItem() {
    }

    /**
     * If the input line is a certain length test to see if it
     * exists as an item in the PLU file. Note: a 1-to-1 PLU
     * to SKU map must be set up. Note: this should be connected
     * to a filter instead of the hard-coded MAX_PLU_LEN. Otherwise
     * Assume the PLU is coming from the input line, again Note: a 1-to-1 PLU
     * to SKU map must be set up.
     */
    public void engage(int value) {

		String input = context().inputLine().trim();
        if (input.length() < MAX_PLU_LEN) {
            EjItem ejitem = new EjItem();
            ejitem.setContext(context());
            ejitem.engage(context().input());
        } else {

			String fetchSpec = Item.getBySKU(input);
            Vector tmp = Application.dbConnection().fetch(new Item(), fetchSpec);

            if (tmp.size() > 0) {  // Item found

                Item item = (Item) tmp.elementAt(0);  // Get the item

//                item.toXML();

				// item locked?
				if ( item.locked() ){
					PosError error = new PosError (context(),PosError.ITEM_LOCKED);
					context().clearInput();
					context().eventStack().pushEvent(error);
					context().operPrompt().update(error);
				}else{
	                EjItem ejitem = new EjItem(item);
    	            ejitem.setContext(context());
					context().eventStack().pushEvent(ejitem);
					context().eventStack().nextEvent(0,true);
				}    
            }else{
				PosError error = new PosError (context(),PosError.ITEM_NOT_FOUND);
				error.setDescr(input);
				context().clearInput();
				context().eventStack().pushEvent(error);
				context().operPrompt().update(error);
			}
        }
    }

    /** Validate transistions state */
    public boolean validTransition(String event) {
        return true;
    }

    /** Clear key implementation for this class */
    public void clear() {
    }

    /** Always return true. */
    public boolean checkProfile(int event) {
        return true;
    }

    private static String eventname = "LookupItem";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }
}



