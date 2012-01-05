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

package com.globalretailtech.pos.ej;

import java.util.Vector;

import com.globalretailtech.util.*;
import com.globalretailtech.data.*;
import com.globalretailtech.pos.context.*;
import com.globalretailtech.pos.events.*;
import org.apache.log4j.Logger;

/**
 * EjItem is the electonic journal method for managing a
 * purchased item.
 *
 * @author      Quentin Olson
 */
public class EjItem extends EjLine {

    static Logger logger = Logger.getLogger(EjItem.class);

    private String prompttext;
    private TransItem transitem;
    /** Database object for the saved EJ line. */
    private Item itemrecord;
    /** Database object for the Item record. */
    private double quantity;
    /** External quantity needed for linked items */

    private String skuLink;

    /** Prompt text for displays. */
    public String promptText() {
        return prompttext;
    }

    /** Transaction record. */
    public TransItem transItem() {
        return transitem;
    }

    /** The item record. */
    public Item itemRecord() {
        return itemrecord;
    }

    /** Set the transaction record */
    public void setTransItem(TransItem value) {
        transitem = value;
    }

    /** Set the item record */
    public void setItemRecord(Item value) {
        itemrecord = value;
    }

    /** Set the quantity */
    public void setQuantity(double value) {
        quantity = value;
    }


    /**
     * Default constructor, set line type.
     */
    public EjItem() {
        setLineType(EjLine.ITEM);
        quantity = 0;
        skuLink = null;
    }

    /**
     * Constructor sets transaction record, used in reprint mode
     */
    public EjItem(TransItem ti) {
        setLineType(EjLine.ITEM);
        setTransItem(ti);
        quantity = 0;
        skuLink = null;
    }

    /**
     * Constructor used in for scanned items
     */
    public EjItem(Item i) {
        setLineType(EjLine.ITEM);
        setItemRecord(i);
        quantity = 0;
        skuLink = null;
    }

    /**
     * Constructor used in for scanned items with context
     */
    public EjItem(Item i, PosContext context, String link) {
        setLineType(EjLine.ITEM);
        setItemRecord(i);
        setContext(context);
        quantity = 0;
        skuLink = link;
    }

    /**
     * Lookup the item indicated by passed value. If it's found
     * generate a TransItem record and add to the ej, then call
     * update displays.
     */
    public void engage(int value) {


        // If this is a item tied to a key, see if there are any
        // modifiers on the stack before we do the lookup.

        if (value > 0) {

            // If there is a Size object on the stack, add the size
            // to the value to get the plu (drinks, etc...)

            if (context().eventStack().event() instanceof Size) {
                Size size = (Size) context().eventStack().event();

                value += size.size();
                context().eventStack().popEvent();
            }

            // set up in the constructor?

            if (itemRecord() == null) {

                String fetchSpec = Plu.getByPlu(value);
                Vector tmp = Application.dbConnection().fetch(new Plu(), fetchSpec);

                if (tmp.size() > 0) {  // Item found

                    Plu plu = (Plu) tmp.elementAt(0);  // Get the item
                    setItemRecord(plu.item());
                }
            }
        }

        // found the item??

        if (itemRecord() != null) {

            setTransItem(new TransItem()); // create the transaction record

            Weight weight = null;
            double amount = itemRecord().amount();

            // Apply pricing options...
            // bulk, ...

            switch (itemRecord().pricingOpt()) {

                case Item.BULK:

                    // See if there is a weight event on the stack.

                    if (context().eventStack().event() instanceof Weight) {

                        weight = (Weight) context().eventStack().popEvent();
                        amount = context().posMath().mult(weight.weight(), itemRecord().amount());
                        transItem().setWeight(weight.weight());
                    }
                    break;

                default:
                    transItem().setWeight(0.0);
                    break;
            }

            // Set the ej header (update db) to sale in progress since we are
            // past reg open.

            Transaction transHeader = (Transaction) context().currEj().transHeader();
            if (!transHeader.updateState(Transaction.IN_PROGRESS)) {
                logger.warn("Failed to update transaction state in EjItem.");
                return;
            }

            // setup the trans item record.

            transItem().setTransID(context().transID());
            transItem().setSeqNo(context().currEj().currLineNo());
            transItem().setSku(itemRecord().sku());
            transItem().setSkuLink(skuLink);

            // if this is a linked item indent the description

            if (skuLink != null) {
                transItem().setItemDesc("*" + itemRecord().desc());
            } else {
                transItem().setItemDesc(itemRecord().desc());
            }

            // pick up quantity from the context or
            // from the internal quantity, set from linked items

            if (quantity == 0) {
            	
            	double qty = context().quantity() == 0 ? 1: context().quantity();
            	// divider changes item quantity,
            	// say if "2" is assigned as item divider
            	// "1/2" will be applied, say if you enter
            	// "1" actual quantity will be "0.5"(1/2),
            	// "2" - 1
            	// exact formula: 
            	// qty = 1/divider * qty
            	if (itemRecord().divider() > 1){
            		qty = context().posMath().mult(1.0/itemRecord().divider(),qty);
            	}
                transItem().setQuantity(qty);
            } else {
                transItem().setQuantity(quantity);
            }
            transItem().setAmount(context().posMath().mult(amount, context().sign()));

            transItem().setState(Item.PAID);
            transItem().setReasonCode(0);
            transItem().setTaxExempt(itemRecord().taxExempt());
            transItem().setTaxIncluded(itemRecord().taxInclusive());
            transItem().setVarAmount(false);

            // add it to the ej and updatedisplays

            context().currEj().ejAdd(this);
            context().setLastItem(this);
            context().setLastItemIndex(context().currEj().size() - 1);

            // do the initial output

            context().operPrompt().update(this);
            context().receipt().update(this);

            // add any modifiers that we find.

            if (context().saleMod() != null) {  // first apply sale level promotion

                // the modifier is applied in the EjPromotion constructor

                EjPromotion promo = new EjPromotion(context(), context().saleMod());
                promo.engage(0);
            }

            if (itemRecord().promotions() != null) {  // now apply promos attached to the item

                for (int i = 0; i < itemRecord().promotions().size(); i++) {

                    Promotion im = (Promotion) itemRecord().promotions().elementAt(i);

                    // the modifier is applied in the EjPromotion constructor

                    EjPromotion promo = new EjPromotion(context(), im);
                    promo.engage(0);
                }
            }

            // add tax records for this item.

            if ((!itemRecord().taxInclusive()) && (!itemRecord().taxExempt())) {

                Vector taxes = null;
                int grpid = itemRecord().taxGroup();
                if (context().config().taxGroups().size() > grpid) {
                    TaxGroup tg = (TaxGroup)
                            context().config().taxGroups().elementAt(grpid);
                    taxes = tg.taxes();
                }
                if (taxes != null) {
                    for (int i = 0; i < taxes.size(); i++) {

                        Tax t = (Tax) taxes.elementAt(i);
                        EjTax ejtax = new EjTax(t, this);
                        ejtax.setContext(context());
                        ejtax.engage(0);
                    }
                }
            }

            // get any linked items

            if (itemRecord().itemLinks() != null) {

                for (int i = 0; i < itemRecord().itemLinks().size(); i++) {
                    Item item = ((ItemLink) itemRecord().itemLinks().elementAt(i)).item();
                    EjItem ejItem = new EjItem(item, context(), itemRecord().sku());
                    ejItem.setQuantity(transItem().quantity());
                    ejItem.engage(0);
                }
            }

            // Set first item.

            context().eventStack().setEvent(new FirstItem(context()));
            context().clearInput();
            context().setQuantity(0);

            // clear the item record for EjItems in the menu

            setItemRecord(null);
        } else { 
        	// item not found perform next event
        	context().eventStack().popEvent();
			context().eventStack().nextEvent();
        }
    }

    // Abstract implementations, PosEvent

    /** Validate transistions state. */
    public boolean validTransition(String event) {
        return true;
    }

    /** Clear key implementation for this class */
    public void clear() {
    }

    private static String eventname = "EjItem";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }

    /** Quantity saved in TransItem */
    public double quantity() {
        return transItem().quantity();
    }
    /** Description saved in TransItem */
    /** The amount display field. */
    public double amount() {
        return transItem().amount();
    }

    /** Extended amount (quantity * (amount+extAmount)). */
    public double extAmount() {
        return context().posMath().mult(transItem().quantity(), transItem().amount()+transItem().extAmount());
    }

    /** Taxable amount. */
    public double taxAmount() {
        if (transItem().taxIncluded()) {
            return 0.0;
        }
        return extAmount();
    }

    /** Chage for this transaction. */
    public double change() {
        return 0;
    }

    /** Implementation of EjLine save (). */
    public boolean save() {

        if (!context().trainingMode()) {
            transItem().save();
        }
        return true;
    }

    /** Display prompt. */
    public String prompt() {
        return desc();
    }

    /** Display customer. */
    public String cust() {
        return desc();
    }

    /** Display description. */
    public String desc() {
        if (transItem() != null) {
            return transItem().itemDesc();
        } else {
            return getClass().toString();
        }
    }

    /** The line nubmer */
    public int lineNo() {
        return transItem().seqNo();
    }

    /** The data record */
    public DBRecord dataRecord() {
        return transItem();
    }

	/** Update totals, does nothiing. */
	public void updateTotals() {
	}

	/** Rollback totals, does nothiing. */
	public void rollbackTotals() {
	}

    public void finalize() {
    }
}


