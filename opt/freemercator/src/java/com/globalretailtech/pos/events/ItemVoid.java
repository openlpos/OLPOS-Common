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
 * Voids the last item by changing the state in the
 * TransItem record of the final item in the Ej thn
 * updating receipts.
 *
 *
 * @author  Quentin Olson
 */

import java.util.Hashtable;

import com.globalretailtech.pos.operators.*;
import com.globalretailtech.pos.ej.*;
import com.globalretailtech.data.*;

public class ItemVoid extends PosEvent {

    private PosItemModifier modifier;
    org.apache.log4j.Logger logger =
		org.apache.log4j.Logger.getLogger(ItemVoid.class.getName());


    /** The item modifier (promotion) for this event */
    public PosItemModifier modifier() {
        return modifier;
    }

    /** Simple constructor for dynamic load */
    public ItemVoid() {
        initTransition();
    }

    /**
     * Void item in the ej. If value == 0 void last item,
     * if value > 0 void appropriate item. Item actually can
     * be item with related promotions or taxes or just promotion.
     * Logic also sets any related ej record amounts to 0,
     * but doesn't remove them.
     * Logic reflects what is printed on receipt:
     * "What you see is what you may void"
     * 
     * @param value item index to void, if 0 - last if 1 or greater - specified.
     *        first item index is 1 (not 0)
     */
    public void engage(int value) {

		logger.info ("itemVoid called with #"+value);

        boolean firstItem = true;
        boolean done = false;

		if ( logger.isDebugEnabled())		
			dump (context().currEj());
		
        int itemIndex = context().lastItemIndex();
        if ( value > 0) { // not last item, find it's index
        	int items = 0;
			for (int i = 0; i < context().currEj().size(); i++) {
				EjLine line = (EjLine) context().currEj().elementAt(i);
				if ( line.lineType() == EjLine.ITEM || line.lineType() == EjLine.PROMOTION){
					items ++;
					if ( items == value){
						itemIndex = i;
						break;
					}
				}
			}
        }
        
        if ( value > 0 && itemIndex == 0){
        	logger.info ("Item #"+value+" not found");
        	return;
        }
        
        // is it promotion?
		EjLine line = (EjLine) context().currEj().elementAt(itemIndex);
		if ( line.lineType() == EjLine.PROMOTION){
			voidPromotion(line);
			return;
		}
        
        // void item and assotiated item_link, promotion and taxs
		for (int ejIndex = itemIndex; (!done) && (ejIndex < context().currEj().size()); ejIndex++) {

            line = (EjLine) context().currEj().elementAt(ejIndex);

            switch (line.lineType()) {

                case EjLine.ITEM:

                    // Stop after current item.
                    if (!firstItem) {
                        done = true;
                        break;
                    }
                    firstItem = false;
	//				TransItem transItem = (TransItem) context().lastItem().dataRecord();
					TransItem transItem = (TransItem) line.dataRecord();
                    if (transItem.state() != TransItem.VOID) {  // alread voided?
                        transItem.setState(TransItem.VOID);
                        context().receipt().update((EjItem) line);
                    } else {
                        done = true;
                        break;
                    }
                    break;

                case EjLine.ITEM_LINK:
                    TransItemLink itemLink = (TransItemLink) line.dataRecord();
                    itemLink.setAmount(0.0);
                    break;

                case EjLine.PROMOTION:
					voidPromotion(line);
                    break;

                case EjLine.TAX:
                    TransTax itemTax = (TransTax) line.dataRecord();
                    itemTax.setTaxAmount(0.0);
                    break;
                default:
            }
        }

		if ( logger.isDebugEnabled())		
			dump (context().currEj());

        context().eventStack().nextEvent();
    }

	private void voidPromotion(EjLine line) {
		TransPromotion itemPromo = (TransPromotion) line.dataRecord();
		itemPromo.setPromotionAmount(-1*itemPromo.promotionAmount());
		context().receipt().update((EjPromotion) line);
		itemPromo.setPromotionAmount(0.0);
	}

    /**
	 * @param ej
	 */
	private void dump(Ej ej) {
		for (int i = 0; i < ej.size(); i++) {
			EjLine line = (EjLine) ej.elementAt(i);
			StringBuffer buf = new StringBuffer();
			buf.append(i+" ");
			switch (line.lineType()) {

				case EjLine.ITEM:
					buf.append("ITEM     ");
					break;
				case EjLine.PROMOTION:
					buf.append("PROMOTION");
					break;
				case EjLine.TENDER:
					buf.append("TENDER   ");
					break;
				case EjLine.TAX:
					buf.append("TAX      ");
					break;
				case EjLine.TOTAL:
					buf.append("TOTAL    ");
					break;
				case EjLine.ITEM_LINK:
					buf.append("ITEM_LINK");
					break;
			}	
			buf.append(" "+line.amount());
			logger.debug (buf.toString());
		}
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

    /** Clear impementation for clear, do nothing. */
    public void clear() {
    }

    /** Always return true. */
    public boolean checkProfile(int event) {
        return true;
    }

    private static String eventname = "ItemVoid";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }
}


