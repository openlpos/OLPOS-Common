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

/**
 * Interface for math functions for implementing various
 * localizations such as rounding.
 *
 * Note: should appyTax () be added???
 *
 * @author  Quentin Olson
 */
public interface PosMath {

    /** Addition */
    public double add(double a, double b);

    /** Subtraction */
    public double sub(double a, double b);

    /** Multiplication */
    public double mult(double a, double b);

    /** Division */
    public double div(double a, double b);

    /** Round double up */
    public double roundUp(double a);

    /** Round double down */
    public double roundDown(double a);

    /** Negate, ex: n*-1 */
    public double negate(double a);

    /** Integer add */
    public int add(int a, int b);

    /** Integer subtract */
    public int sub(int a, int b);

    /** Integer multiply */
    public int mult(int a, int b);

    /** Integer divide */
    public int div(int a, int b);
}


