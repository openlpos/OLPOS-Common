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

import com.globalretailtech.data.*;
import com.globalretailtech.pos.operators.*;
import com.globalretailtech.pos.context.*;
import org.apache.log4j.Logger;

/**
 * EjPromotion
 *
 * @author      Quentin Olson
 */
public class EjPromotion extends EjLine {

    static Logger logger = Logger.getLogger(EjPromotion.class);

    private PosItemModifier modifier;
    private TransPromotion transpromotion;
    private Promotion promotion;


    public PosItemModifier posItemModifier() {
        return modifier;
    }

    public TransPromotion transPromotion() {
        return transpromotion;
    }

    public Promotion promotion() {
        return promotion;
    }

    public void setPosItemModifier(PosItemModifier value) {
        modifier = value;
    }

    public void setTransPromotion(TransPromotion value) {
        transpromotion = value;
    }

    public void setPromotion(Promotion value) {
        promotion = value;
    }

    /**
     * Default constructor */
    public EjPromotion() {
        setLineType(EjLine.PROMOTION);
    }

    /**
     * Constructor used when an item mod is linked to an item
     * when the item is looked up and added to the ej.
     */
    public EjPromotion(PosContext c, Promotion promotion) {

        setLineType(EjLine.PROMOTION);
        setPosItemModifier(null);
        setPromotion(promotion);
        setContext(c);
    }

    /**
     * Constructor used for reprint.
     */
    public EjPromotion(PosContext c, TransPromotion t) {
        setLineType(EjLine.PROMOTION);
        setTransPromotion(t);
        setContext(c);
    }

    public EjPromotion(TransPromotion t) {
        setLineType(EjLine.PROMOTION);
        setTransPromotion(t);
    }
    
    public void engage(int value) {
		applyToItem (context().lastItem());
    }

    /**
	 * 
	 */
	public void applyToItem(EjItem item) {

		// get the class and load it.
		try {
			setPosItemModifier((PosItemModifier) Class.forName(promotion().promotionClass()).newInstance());
		} catch (java.lang.Exception e) {
			logger.warn("Can't load class for EjPromotion : " + promotion().promotionClass(), e);
			return;
		}

		// If the modifier loaded, inialize and apply it.

		if (posItemModifier() != null) {

			posItemModifier().init(context(), item, promotion());

			// apply the item posItemModifier ().

			posItemModifier().apply();

			setTransPromotion(new TransPromotion());
			transPromotion().setTransID(context().transID());
			transPromotion().setSeqNo(context().currEj().currLineNo());

			if (posItemModifier().applied()) {

				transPromotion().setPromotionAmount(
					context().posMath().mult(posItemModifier().amount(),context().sign()));
				transPromotion().setPromotionQuantity(item.quantity());
				transPromotion().setReasonCode(0);
				transPromotion().setPromotionData(null);
				transPromotion().setPromotionDesc(posItemModifier().desc());
			} else {
				transPromotion().setPromotionAmount(0);
				transPromotion().setPromotionQuantity(0);
				transPromotion().setReasonCode(0);
				transPromotion().setPromotionData("");
				transPromotion().setPromotionDesc(posItemModifier().desc());
			}
		}
		context().currEj().ejAdd(item, this);
		context().receipt().update(this);
	}

	public void apply() {
        if (posItemModifier() != null) {
            posItemModifier().apply();
        }
    }

    public boolean applied() {
        if (posItemModifier() != null) {
            return posItemModifier().applied();
        }
        if (transPromotion() != null) {
            return true;
        }
        return false;
    }

    // Abstract implementations, PosEvent

    /** Validate transistions state. */
    public boolean validTransition(String event) {
        return true;
    }

    /** Clear key implementation for this class */
    public void clear() {
    }

    private static String eventname = "EjPromotion";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }

    // Abstract implementations, EjLine

    /** Quantitiy for this line. */
    public double quantity() {
        return transPromotion().promotionQuantity();
    }

    /** Quantitiy for this line. */
    public double amount() {
        if (transPromotion() != null) {
            return transPromotion().promotionAmount();
        } else
            return 0;
    }

	/** Extended amount (quantity * amount). */
    public double extAmount() {
		return context().posMath().mult(transPromotion().promotionQuantity(), 
										transPromotion().promotionAmount());
    }

    /** Tax amount for this line, always 0 */
    public double taxAmount() {
        return 0.0;
    }
    /** Save the transaction record */
    /** Chage for this transaction. */
    public double change() {
        return 0.0;
    }

    public boolean save() {
        if ((transPromotion() != null) &&
                (!context().trainingMode())) {
            transPromotion().save();
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
        return transPromotion().promotionDesc();
    }

    /** The line nubmer */
    public int lineNo() {
        return transPromotion().seqNo();
    }

    /** The data record */
    public DBRecord dataRecord() {
        return transPromotion();
    }

    /** No total updates for this record. */
    public void updateTotals() {
    }
	/** No total updates for this record. */
	public void rollbackTotals() {
	}
}


