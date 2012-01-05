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


import com.globalretailtech.util.ShareProperties;

/**
 * MOD10 check digit calculation
 *
 * @author  Quentin Olson
 */
public class Mod10 implements CheckDigit {

    /** Left to right calculation */
    public static final int LEFTTORIGHT = 0;
    /** Right to left calculation. */
    public static final int RIGHTTOLEFT = 1;

    private int myDirection;
    private int myWeight;

    /**
     * Constructor gets direction and weight from properties file.
     */
    public Mod10() {

        ShareProperties p = new ShareProperties(this.getClass().getName());

        if (p.Found()) {

            myDirection = Integer.valueOf(p.getProperty("Direction", "1")).intValue();
            myWeight = Integer.valueOf(p.getProperty("Weight", "2")).intValue();
        }
    }

    /**
     * Apply the check digit algorithm.
     */
    public int apply(String number) {

        int tens = 0;
        int ones = 0;
        int num;
        int result = 0;
        int direction = myDirection;
        int weight = myWeight;

        if (direction == LEFTTORIGHT) {

            for (int i = 0; i < number.length(); i++) {

                num = Integer.parseInt(number.substring(i, i + 1), 10);
                result = num * weight;
                tens += result / 10;
                ones += result % 10;

                if (weight == 1) {
                    weight = 2;
                } else {
                    weight = 1;
                }
            }
        } else {

            for (int i = number.length() - 1; i >= 0; i--) {

                num = Integer.parseInt(number.substring(i, i + 1), 10);
                result = num * weight;
                tens += result / 10;
                ones += result % 10;

                if (weight == 1) {
                    weight = 2;
                } else {
                    weight = 1;
                }
            }
        }

        result = 10 - ((tens + ones) % 10);
        if (result == 10)
            result = 0;
        return result;
    }

    private static String eventname = "Mod10";

    public String toString() {
        return eventname;
    }
}


