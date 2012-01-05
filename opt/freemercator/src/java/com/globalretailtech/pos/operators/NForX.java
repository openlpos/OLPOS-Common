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


import com.globalretailtech.util.Format;
import com.globalretailtech.pos.context.*;
import com.globalretailtech.pos.ej.EjItem;
import com.globalretailtech.data.Promotion;

/**
 * Compute discount for  n for the price of x, i.e. 3 for $5.
 * (Mix and match pricing).
 *
 * @author  Quentin Olson
 */
public class NForX implements PosItemModifier {

    private double totalAmount;
    private double totalDiscount;
    private int itemcount;
    private boolean applied;
    private PosContext context;
    private EjItem item;
    private Promotion promotion;

    private static int apply_count;

    /**
     * Default constructor used when the key is loaded.
     */
    public NForX() {
        applied = false;
    }

    /**
     * Set up the class.
     */
    public void init(PosContext c, EjItem i, Promotion p) {
        context = c;
        item = i;
        promotion = p;
    }

    /** Return the applied field. */
    public boolean applied() {
        return applied;
    }

    /**
     * Uses a static counter to see how many items with this
     * promotion are in the ej.
     */
    public void apply() {

        applied = true;

        totalDiscount = 0.0;
        double itemAmount = item.amount();

        // reverse the sign of the amount if it's less than zero, (discount sale)

        if (itemAmount < 0.0)
            itemAmount *= -1.0;

        totalAmount = context.posMath().mult(itemAmount, (double) item.quantity());
        int remainingItemCount = (int)item.quantity();

        double tmp = promotion.promotionDVal1() / (double) promotion.promotionVal1();
        double up = context.posMath().roundUp(tmp);
        double down = context.posMath().roundDown(tmp);

        // First compute the total discount price if the number of items
        // is greater than or equal to the discount qunatity

        if (remainingItemCount >= promotion.promotionVal1()) {

            int mult = (int)item.quantity() / promotion.promotionVal1();
            totalDiscount = context.posMath().mult(promotion.promotionDVal1(), (double) mult);
            remainingItemCount = (int)item.quantity() % promotion.promotionVal1();
        }

        // now compute the total discount for remaining items or
        // initial items

        for (int i = 0; i < remainingItemCount; i++) {

            if (up > down) {

                if (apply_count <= (promotion.promotionVal1() / 2)) {
                    totalDiscount = context.posMath().add(totalDiscount, down);
                } else {
                    totalDiscount = context.posMath().add(totalDiscount, up);
                }
            } else
                totalDiscount = context.posMath().add(totalDiscount, up);

            if (++apply_count == promotion.promotionVal1())
                apply_count = 0;
        }
    }


    /**
     * Description is the promotion text, example, 3 for $5.
     */
    public String desc() {

        if (totalDiscount >= promotion.promotionDVal1()) {
            return promotion.promotionVal1() +
                    " for " +
                    Format.toMoney(Double.toString(promotion.promotionDVal1()), context.locale());
        } else {
            return promotion.promotionVal1() +
                    " for " +
                    Format.toMoney(Double.toString(promotion.promotionDVal1()), context.locale()) +
                    " " +
                    Format.toMoney(Double.toString(totalDiscount), context.locale());
        }
    }

    /** Amount of this modifier */
    public double amount() {
        return context.posMath().negate(context.posMath().sub(totalAmount, totalDiscount));
    }

    /** Quantity of this modifier */
    public int quantity() {
        return 1;
    }

    /** Description of the modifier. */
    private static String name = "NForX";

    public String toString() {
        return name;
    }

    /** Decrement apply count or the next sale won't work */
    public void finalize() {
        if (apply_count > 0)
            apply_count--;
    }
}


