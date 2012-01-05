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

import java.sql.ResultSet;
import java.util.Vector;

import org.apache.log4j.Logger;


import com.globalretailtech.util.Application;
import com.globalretailtech.data.Item;
import com.globalretailtech.pos.ej.EjItem;

/**
 * Looks up an item either by alternate CODE or BARCODE depending
 * on input line length. If it's less than (MAX_PLU_LEN) digits
 * use the CODE else use BARCODE to find item_id.
 *
 * @author  Igor Semenko
 */
public class LookupItemAlt extends PosEvent {

	Logger logger = Logger.getLogger (LookupItemAlt.class.getName());
    public static final int MAX_PLU_LEN = 6;

    /** Simple constructor for dynamic load */
    public LookupItemAlt() {
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
		if ( input.length() == 0){
			logger.warn ("input is empty");
			return;
		}
		int itemId = Integer.parseInt(input);
		
        if (context().inputLine().length() > MAX_PLU_LEN) {
			itemId = lookupByBarCode (context().inputLine().trim());
        }

		if ( itemId >= 0 ){
            String fetchSpec = Item.getByID(itemId);
            Vector tmp = Application.dbConnection().fetch(new Item(), fetchSpec);

            if (tmp.size() > 0) {  // Item found

                Item item = (Item) tmp.elementAt(0);  // Get the item
                
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

    /**
	 * @param string
	 * @return
	 */
	private int lookupByBarCode(String barcode) {
		try {
			ResultSet rs = Application.dbConnection().executeWithResult(
					"select item_id from lookupalt_barcode where barcode='"+barcode+"'");
			if (rs != null && rs.next())
				return rs.getInt(1);
		} catch (Exception e) {
			logger.error ("",e);
		}
		return 0;
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

    private static String eventname = "LookupItemAlt";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }
}



