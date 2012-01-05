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

import com.globalretailtech.pos.context.*;
import com.globalretailtech.data.DBRecord;
import com.globalretailtech.data.Transaction;

/**
 * Holds the base data for a transaction.
 *
 * @author  Quentin Olson
 */
public class EjHeader extends EjLine {

    private Transaction transHeader;

    /** Transaction ID. */
    public int transID() {
        return transHeader().transID();
    }

    /** Transactin Number. */
    public int transNo() {
        return transHeader().transNo();
    }

    /** Employee number. */
    public int empNo() {
        return transHeader().empNo();
    }

    /** POS number. */
    public int posNo() {
        return transHeader().posNo();
    }

    /** Database transactin record. */
    public Transaction transHeader() {
        return transHeader;
    }

    /** Sets the transaction header. */
    public void setTransHeader(Transaction value) {
        transHeader = value;
    }

    /** Set the transaction number */
    public void setTransNo(int value) {
        transHeader().setTransNo(value);
    }

    /** Set the POS number. */
    public void setPosNo(int value) {
        transHeader().setPosNo(value);
    }

    /** Simple constructor just sets line type. */
    public EjHeader() {
        setLineType(EjLine.TRANS_HEADER);
    }

    /** Constructor just sets line type and context. */
    public EjHeader(PosContext c) {
        setLineType(EjLine.TRANS_HEADER);
        setContext(c);
    }

    /** Constructor sets transaction record */
    public EjHeader(com.globalretailtech.data.Transaction t) {
        setLineType(EjLine.TRANS_HEADER);
        transHeader = t;
    }

    /**
     * Does nothing.
     */
    public void engage(int value) {
    }

    // Abstract implementations, PosEvent

    /** Validate transistions state. */
    public boolean validTransition(String event) {
        return true;
    }

    /** Clear key implementation for this class */
    public void clear() {
    }

    private static String eventname = "EjHeader";

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

    /** Amount for this line, always 0 */
    public double amount() {
        return 0;
    }

    /** Extended amount for this line, always 0 */
    public double extAmount() {
        return 0;
    }

    /** Tax amount for this line, always 0 */
    public double taxAmount() {
        return 0;
    }

    /** Chage for this transaction. */
    public double change() {
        return 0.0;
    }

    /** Save the transaction record */
    public boolean save() {

        // 		if (!context ().trainingMode ()) {
        // 			transHeader ().save ();
        // 		}
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
        return Integer.toString(transHeader().transID());
    }

    /** The line nubmer */
    public int lineNo() {
        return 0;
    }

    /** The data record */
    public DBRecord dataRecord() {
        return transHeader;
    }

    /** No total updates for this record. */
    public void updateTotals() {
    }

	/** No total updates for this record. */
	public void rollbackTotals() {
	}
}


