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


import com.globalretailtech.pos.context.*;
import com.globalretailtech.pos.ej.EjItem;
import com.globalretailtech.util.Format;
import com.globalretailtech.data.Promotion;

/**
 * A simple per cent markdown on an item. Designed to be
 * associated with a key and take a simple percentage off
 * of the previous item.2
 *
 * @author  Quentin Olson
 */
public class MarkdownByPercent implements PosItemModifier {

    private double amount;
    private int quantity;
    private boolean applied;
    private PosContext context;
    private EjItem item;
    private Promotion promotion;

    private static int apply_count;

    /**
     * Default constructor used when the key is loaded.
     * Since this is implicitly applied when the key it is
     * tied to is pressed, always return true.
     */
    public MarkdownByPercent() {
        applied = true;
    }

    /**
     * Set up the class.
     */
    public void init(PosContext c, EjItem i, Promotion p) {
        context = c;
        item = i;
        promotion = p;
        amount = 0.0;
        quantity = 0;
    }

    /**
     * Again, always return true.
     */
    public boolean applied() {
        return true;
    }

    /**
     * Use the data in the promotion record to calculate
     * a percent discount.
     */
	public void apply() {

		double percent = (double) (promotion.promotionVal1() / 100.0);
		amount =
			context.posMath().roundDown(
				context.posMath().mult(
					-1.0,
					context.posMath().mult(
						item.amount(), percent)));
	}

    /** Amount of this modifier */
    public double amount() {
        return amount;
    }

    /** Quantity of this modifier */
    public int quantity() {
        return quantity;
    }

    /** Description of the modifier. */
    public String desc() {
    	String tmpl = context.posParameters().getString("MarkdownByPercentDesc");
    	if ( tmpl == null )
    		tmpl = "Markdown: {0}% off";
		return Format.substitute (tmpl, new Integer(promotion.promotionVal1()));
    }

    /** Description of the modifier. */
    public String toString() {
        return desc();
    }
}


