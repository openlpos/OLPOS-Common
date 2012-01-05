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
import com.globalretailtech.data.Tax;
import com.globalretailtech.data.TransTax;

/**
 *
 * @author  Quentin Olson
 */
public class EjTax extends EjLine {

    public static final int PRIMARY_TAX = 0;
    public static final int ALTERNATE_TAX = 1;

    private Tax taxRecord;
    private TransTax transTax;
    private EjItem item;
    private int taxamount;

    /** Simple constructor, set type. */
    public EjTax() {
        setLineType(TAX);
        useAltTax = false;
    }

    /** Constructor, set type and item. */
    public EjTax(Tax t, EjItem i) {
        taxRecord = t;
        setLineType(TAX);
        item = i;
        useAltTax = false;
    }

    /** Constructor, set type and transaction tax record. */
    public EjTax(TransTax t) {
        transTax = t;
        setLineType(TAX);
        useAltTax = false;
    }

    /**
     * Create a tax record and add it to the EJ. Note:
     * a tax record is created for each item, summaries
     * are implemented in the print objects.
     */
    public void engage(int value) {

        transTax = new TransTax();

        transTax.setTransID(context().transID());
        transTax.setSeqNo(context().currEj().currLineNo());
        transTax.setTaxID(taxRecord.taxID());
        transTax.setDesc(taxRecord.taxDesc());
        transTax.setRate(taxRecord.taxRate());
        transTax.setTaxable(item.extAmount());

        applyTax();

        context().currEj().ejAdd(this);
    }

    /**
     * Toggle between tax rates.
     */

    private boolean useAltTax;

    /**
     * Toggle between tax rates.
     */
    public void toggleAltTax() {

        useAltTax = !useAltTax;
        if (useAltTax) {
            transTax.setRate(taxRecord.altTaxRate());
        } else {
            transTax.setRate(taxRecord.taxRate());
        }
        applyTax();
    }

    /**
     * Switch to specific tax rate. Only primary and alternate are
     * supported now, but we leave the door open for others. Anything
     * other than primary results in alternate.
     */
    public void setActiveTaxRate(int rate) {

        if (rate == PRIMARY_TAX) {
            useAltTax = false;
            transTax.setRate(taxRecord.taxRate());
        } else {
            useAltTax = true;
            transTax.setRate(taxRecord.altTaxRate());
        }
        applyTax();
    }

    /**
     * Set the taxable amount and multiply the tax rate times
     * the taxable amount.
     */
    public void applyTax() {

        transTax.setTaxAmount(context().posMath().mult(item.extAmount(), transTax.taxRate()));
    }

    /**
     * The tax rate.
     */
    public double rate() {
        return transTax.taxRate();
    }

    // Abstract implementations, PosEvent

    /** Validate transistions state. */
    public boolean validTransition(String event) {
        return true;
    }

    /** Clear key implementation for this class */
    public void clear() {
    }

    private static String eventname = "EjTax";

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
        return 0;
    }

    /** Amount for this line, tax amount */
    public double amount() {
        return transTax.taxAmount();
    }

    /** Extended amount for this line = amount () */
    public double extAmount() {
        return amount();
    }

    /** Tax amount for this line, always 0, can't tax tax.*/
    public double taxAmount() {
        return amount();
    }

    /** Chage for this transaction. */
    public double change() {
        return 0.0;
    }

    /** Save the transaction record */
    public boolean save() {

        if (!context().trainingMode()) {
            transTax.save();
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
        return transTax.taxDesc();
    }

    /** The line nubmer */
    public int lineNo() {
        return transTax.seqNo();
    }

    /** The data record */
    public DBRecord dataRecord() {
        return transTax;
    }

	/** No total updates for this record. */
	public void updateTotals() {
	}
	/** No total updates for this record. */
	public void rollbackTotals() {
	}
}


