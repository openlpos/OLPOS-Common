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

package com.globalretailtech.pos.operators;

import java.sql.ResultSet;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.globalretailtech.data.Item;
import com.globalretailtech.pos.context.PosContext;
import com.globalretailtech.pos.ej.EjItem;
import com.globalretailtech.pos.events.PosError;
import com.globalretailtech.util.Application;

/**
 * Uses lookupalt_barcode table to find item by barcode.
 * It differs from EANFilter by trying to find exact match
 * for scanned barcode in lookupalt_barcode table without
 * parsing it. Note that filter will be applied only to
 * the input that matches 'regex' field in input_filter table.
 * It is possible to insert several records with same class,
 * but different regex to match different barcodes such as
 * EAN13, EAN8...
 *
 * @author  Igor Semenko
 */
public class ItemBarCodeFilter extends Filter{

	org.apache.log4j.Logger logger =
		org.apache.log4j.Logger.getLogger(ItemBarCodeFilter.class);
		
	public void apply(PosContext context, String s){
		Pattern pattern = Pattern.compile(getRegex());
		Matcher matcher = pattern.matcher(s);

		logger.debug("apply() ["+s+"]"+((s != null)?s.length():0));

		if (!matcher.find())
			return;
		
		
		int itemId = 0;
		itemId = lookupByBarCode (s);
		logger.debug ("itemid after search: "+itemId);
		
		if ( itemId > 0){
			String fetchSpec = Item.getByID(itemId);
			Vector tmp =
				Application.dbConnection().fetch(new Item(), fetchSpec);

			if (tmp.size() > 0) { // Item found

				Item item = (Item) tmp.elementAt(0); // Get the item

				// item locked?
				if ( item.locked() ){
					PosError error = new PosError (context,PosError.ITEM_LOCKED);
					context.clearInput();
					context.eventStack().pushEvent(error);
					context.operPrompt().update(error);
				}else{	
					EjItem ejitem = new EjItem(item);
					ejitem.setContext(context);
					ejitem.pushState(1);//dummy state
					context.eventStack().pushEvent (ejitem);
					context.eventStack().nextEvent();
				}
			}
		}else {
			logger.warn("BarCode not found: "+s);
			PosError error = new PosError (context,PosError.ITEM_NOT_FOUND);
			error.setDescr(s);
			context.clearInput();
			context.eventStack().pushEvent(error);
			context.operPrompt().update(error);
		}
	}

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
}


