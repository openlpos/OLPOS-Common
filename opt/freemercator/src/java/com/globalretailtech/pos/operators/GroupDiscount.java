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
import com.globalretailtech.data.Promotion;

/**
 * A simple group discount example, if three items are scanned with
 * this dicount, tack on a 10% amount off.
 *
 * @author  Quentin Olson
 */
public class GroupDiscount implements PosItemModifier {

    private double amount;
    private int quantity;
    private boolean applied;
    private PosContext context;
    private EjItem item;
    private Promotion promo;

    private static int apply_count;

    /**
     * Default constructor.
     */
    public GroupDiscount() {
        applied = false;
    }

    /**
     * Abstract implementations
     */

    /**
     * Set up the class.
     */
    public void init(PosContext c, EjItem i, Promotion p) {
        context = c;
        item = i;
        promo = p;
        amount = 0.0;
        quantity = 0;
    }

    /** Return the applied field. */
    public boolean applied() {
        return applied;
    }

    /**
     * This modifier counts to see if
     * any more of this item exist in the ej, if they do
     * and the number equals the number configured in the
     * database, set applied to true.
     */
    public void apply() {

        apply_count += item.quantity();

        if (apply_count == promo.promotionVal1()) {
            applied = true;
            double percent = promo.promotionDVal1() / 100.0;
            int quantity = 1;
            amount = context.posMath().mult(item.amount(), percent);
            amount = context.posMath().mult(context.posMath().mult(amount, -1.0), context.sign());
            apply_count = 0;
        }
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
        return promo.promotionString() + " " + promo.promotionDVal1() + "%";
    }

    private static String eventname = "GroupDiscount";

    public String toString() {
        return desc();
    }

    /**
     * finalize to decrement the apply_count.
     */
    public void finalize() {
        if (apply_count > 0)
            apply_count--;
    }
}


