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

import com.globalretailtech.data.DBRecord;

import com.globalretailtech.pos.events.*;

/**
 * Abastract class for managing Electronic Journal records.
 *
 * The base class for all electronic journal lines.
 * Defines abstract methods for EjLine attributes such as
 * quantity, amount, etc.
 * @author  Quentin Olson
 */
public abstract class EjLine extends PosDialogEvent implements Comparable {

    // ej line types

    public static final int TRANS_HEADER = 0;
    public static final int ITEM = 1;
    public static final int PROMOTION = 2;
    public static final int TENDER = 3;
    public static final int TAX = 4;
    public static final int TOTAL = 5;
    public static final int ITEM_LINK = 6;
    public static final int BANK = 7;
    public static final int ACCOUNT = 8;
    public static final int ALT_CURRENCY_TENDER = 9;
    public static final int CHECK_TENDER = 10;
    public static final int CC_TENDER = 11;
    public static final int SPLIT = 12;

    public static final int NUMBER = 1;
    public static final int AMOUNT = 2;

    private int linetype;
    protected int numberType;

    /** The ej line type */
    public int lineType() {
        return linetype;
    }

    /** Print the number as an amount or number */
    public int numberType() {
        return numberType;
    }

	/** Set the line type. */
	public void setLineType(int value) {
		linetype = value;
	}

	/** Set the number type. */
	public void setNumberType(int value) {
		numberType = value;
	}

    /** Simple Constructor */
    public EjLine() {
    }

    /** Quantitiy for this line. */
    public abstract double quantity();

    /** Amount for this line. */
    public abstract double amount();

    /** Extended amount (quantity * amount). */
    public abstract double extAmount();

    /** Taxable amount. */
    public abstract double taxAmount();

    /** Chage for this transaction. */
    public abstract double change();

    /** Access the line number. */
    public abstract int lineNo();

	/** Update total records for this object. */
	public abstract void updateTotals();

	/** Rollback total records for this object. */
	public abstract void rollbackTotals();

    /** Return the associated transaction record, (database entity). */
    public abstract DBRecord dataRecord();

    /** Database save function. */
    public abstract boolean save();

    // useful display interfaces.

    /** Display prompt for this object */
    public abstract String prompt();

    /** Customer display information for the object */
    public abstract String cust();

    /** Description for this object */
    public abstract String desc();

    /**
     * Implementation of EjLine compareTo () used to keep order
     * in the ej.
     */
    public int compareTo(Object o) {

        EjLine line = (EjLine) o;
        if (lineNo() < line.lineNo())
            return -1;
        if (lineNo() == line.lineNo())
            return 0;
        if (lineNo() > line.lineNo())
            return 1;
        return 0;
    }
}


