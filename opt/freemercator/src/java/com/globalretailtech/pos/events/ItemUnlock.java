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

import com.globalretailtech.data.Item;
import com.globalretailtech.util.Application;

/**
 * Unlocks item by item_id, if entered item_id is 0,
 * unlocks all locked items. Check permissions should
 * be added.
 *
 * @author  Igor Semenko
 */
public class ItemUnlock extends PosNumberDialog {

    private String prompttext;

    /** Prompt text for user dialog. */
    public String promptText() {
        return prompttext;
    }

    public void setPromptText(String value) {
        prompttext = value;
    }

    /** Simple constructor */
    public ItemUnlock() {
    }

    /**
     * Get the weight (scale amount) from the
     * context input line.
     */
    public void engage(int value) {

	//TODO probably check employee permissions
	
        if (state() == 0) {

			context().clearInput();
            popState();
            pushState(1);
            setPromptText(context().posParameters().getString("EnterItemID"));
            context().operPrompt().update(this);
            context().eventStack().pushEvent(this);
        } else {

			try {
				//
				// if nothing or "0" is entered, unlock all,
				// otherwise unlock specified item_id 
				//
				
				int itemId = 0;
				if (context().inputLine().length() > 0)
					itemId = Integer.parseInt(context().inputLine());

				if ( itemId > 0){

					String fetchSpec = Item.getByID(itemId);
					Vector tmp =
						Application.dbConnection().fetch(new Item(), fetchSpec);

					if (tmp.size() > 0) { // Item found

						Item item = (Item) tmp.elementAt(0); // Get the item
						item.setLocked(false);
						item.update();
					}

				} else {

					// unlock all locked
					String fs = Item.getLocked();
					Vector v = Application.dbConnection().fetch (new Item(), fs);

					if ( v != null && v.size() > 0){

						for (int i = 0; i < v.size(); i++) {

							Item item = (Item)v.elementAt(i);
							item.setLocked(false);
							item.update();
						}
					}

				}
			} catch (Exception e) {
				logger.warn ("",e);
			} finally {
				popState();
				context().eventStack().popEvent();
				context().eventStack().nextEvent();	
			}

        }

    }

    private int type;

    /** Implemntation of type for PosNumberDialog */
    public int type() {
        return type;
    }

    /** Validate transistions state */
    public boolean validTransition(String event) {
        return true;
    }

    /** Clear key implementation for this class */
    public void clear() {
    }

    public boolean checkProfile(int event) {
        return true;
    }

    private static String eventname = "ItemUnlock";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }
}


