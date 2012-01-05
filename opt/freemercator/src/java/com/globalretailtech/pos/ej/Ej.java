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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

import jpos.FiscalPrinterConst;
import jpos.JposConst;
import jpos.JposException;


import com.globalretailtech.util.Application;
import com.globalretailtech.pos.context.PosContext;
import com.globalretailtech.pos.events.FinishTransaction;
import com.globalretailtech.pos.events.Pause;
import com.globalretailtech.pos.events.PosError;
import com.globalretailtech.pos.events.StartTransaction;
import com.globalretailtech.data.TransPromotion;
import com.globalretailtech.data.Transaction;
import com.globalretailtech.data.TransBank;
import com.globalretailtech.data.TransItem;
import com.globalretailtech.data.TransTax;
import com.globalretailtech.data.TransTender;
import org.apache.log4j.Logger;

/**
 * Ej - (electronic jounal) is the data structure
 * for maintaining the fundamental sale information.
 *
 * @author Quentin Olson
 */
public class Ej extends Vector {

    static Logger logger = Logger.getLogger(Ej.class);

    PosContext context;

    public void setContext(PosContext value) {
        context = value;
    }

    /**
     * Simple constructor calls super class
     * (Vector) constructor.
     */
    public Ej() {
        super();
    }

    /**
     * Simple constructor calls super class
     * (Vector) constructor and sets the context.
     */
    public Ej(PosContext c) {
        super();
        context = c;
    }

    /**
     * Just the current line number (Vector.size ()).
     */
    public int currLineNo() {
        return size();
    }

    /**
     * Saves this set of EJ records, by calling the
     * save () and updateTotals () virtual functions.
     * Starts a duplicate thread to support transaction
     * resliency (not fully implemented).
     */
    public void complete() {

        if (context.trainingMode())
            return;

		// make sure everything is within one transaction
		Connection conn = Application.dbConnection().getConn();
		EjHeader trans = (EjHeader) elementAt(0);
		try {
			conn.setAutoCommit(false);

			// save only if it is not 'printing' or 'complete'
			if ( trans.transHeader().state() != Transaction.PRINTING 
			  && trans.transHeader().state() != Transaction.COMPLETE){
			
				saveEj();

				// get the header, set the state to printing
				if (!trans.transHeader().updateState(Transaction.PRINTING)) {
					logger.warn("Transaction update failure" + trans.transHeader().toString());
				}
			}
			conn.commit();
		}catch (Exception e){
			logger.error ("can't save ej",e);

		}finally{
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e2) {
				logger.error("", e2);
			}	
		}
	

        // transaction saved, 
        // print it and set state to complete
		try {
			// fiscal printer
			if ( trans.transHeader().state() == Transaction.PRINTING ) 
				context.fiscalPrinter().printReceipt (context.currEj());

			// pos printer
			if ( trans.transHeader().state() == Transaction.PRINTING ) 
				context.posPrinter().printReceipt(context.currEj());
			
			// update status to 'complete', update receipt no and z no				
			completeTransaction(trans);
				
		} catch (JposException e) {
			
			// transaction can't be printed, 
			// there are recoverable and non-recoverable errors
			int code = e.getErrorCode();
			int extCode = e.getErrorCodeExtended();
			
			PosError posError = new PosError(context, e);
			
			if ( (code == JposConst.JPOS_E_EXTENDED && extCode == FiscalPrinterConst.JPOS_EFPTR_JRN_EMPTY) ||
				 (code == JposConst.JPOS_E_EXTENDED && extCode == FiscalPrinterConst.JPOS_EFPTR_REC_EMPTY) ||
				 (code == JposConst.JPOS_E_EXTENDED && extCode == FiscalPrinterConst.JPOS_EFPTR_WRONG_STATE)
				){
			
				// try to recover	
				context.eventStack().pushEvent( new FinishTransaction (context));			
			
			}else{
			
				// non recoverable
				context.eventStack().clearPending();
				context.eventStack().clearProcessed();
				trans.transHeader().updateState(Transaction.FAILED);
				rollbackEj();				
				context.eventStack().pushEvent(new StartTransaction(context));
			}
			
			context.eventStack().pushEvent(new Pause(context,1));
			context.eventStack().pushEvent(posError);
			context.operPrompt().update(posError);

			return;
		}

        // Send this transaction to the server and shadows
        new DuplicateThread().start();
    }

	/** 
	 * Upadates trans_no with receipe no, sets current z report no and
	 * sets transaction status to 'complete'
	 **/
	private void completeTransaction(EjHeader trans) throws JposException{
		
		int receiptNo = 0;
		int zNo = 0;
		
		receiptNo = context.fiscalPrinter().getReceiptNo(); 
		
		zNo = context.fiscalPrinter().getZReportNo();
		 
		if (receiptNo > 0) trans.setTransNo(receiptNo);
		
		if (zNo > 0) trans.transHeader().setZ(zNo);
		
		if (!trans.transHeader().updateState(com.globalretailtech.data.Transaction.COMPLETE)) {
			logger.warn("Transaction update failure" + trans.transHeader().toString());
		}
	}

	/**
	 * Saves current Ej to the db (issues 'insert' statements)
	 * Prints BankTransaction on FiscalPrinter
	 **/
	private void saveEj() {
		for (int i = 0; i < this.size(); i++) {
		
			EjLine line = (EjLine) this.elementAt(i);
		
			// put some ej line type customizations here
		
			switch (line.lineType()) {
		
				case EjLine.TRANS_HEADER:
					break;
				case EjLine.ITEM:
				case EjLine.PROMOTION:
				case EjLine.BANK:
				case EjLine.TENDER:
				case EjLine.CHECK_TENDER:
				case EjLine.CC_TENDER:
				case EjLine.ALT_CURRENCY_TENDER:
					line.updateTotals();
					break;
				case EjLine.TAX:
				case EjLine.TOTAL:
					break;
				default:
					break;
			}
		
			if (!line.save()) {
				logger.warn("Ej update failure, " + line.toString());
			}
		
		}
	}
	private void rollbackEj() {
		for (int i = 0; i < this.size(); i++) {
		
			EjLine line = (EjLine) this.elementAt(i);
		
			// put some ej line type customizations here
		
			switch (line.lineType()) {
		
				case EjLine.TRANS_HEADER:
					break;
				case EjLine.ITEM:
				case EjLine.PROMOTION:
				case EjLine.BANK:
				case EjLine.TENDER:
				case EjLine.CHECK_TENDER:
				case EjLine.CC_TENDER:
				case EjLine.ALT_CURRENCY_TENDER:
					line.rollbackTotals();
					break;
				case EjLine.TAX:
				case EjLine.TOTAL:
					break;
				default:
					break;
			}
		
		
		}
	}

    /**
     * Ej management routines
     */

    /**
	 * 
	 */
	private void printBankTransaction(EjBank bankTran) throws JposException {
		context.fiscalPrinter().printBankTransaction(bankTran);
	}

	/**
     * Clears this EJ
     */
    public void clear() {
        super.clear();
    }

	/**
	 * Add an object to the ej.
	 */
	public void ejAdd(Object o) {
		EjLine line = (EjLine) o;
		addElement(o);
	}

	/**
	 * Add an object to the ej after specified object.
	 */
	public void ejAdd(Object after, Object ins) {
		int i = this.indexOf(after);
		if ( i > 0 ){
			insertElementAt(ins,i+1);
		}else{
			ejAdd (ins);
		}
	}

    /**
     * Sub-totals the current ej.
     */
    public double ejSubTotal() {

        double total = 0;
        EjLine line;

        for (int i = 0; i < size(); i++) {

            line = (EjLine) elementAt(i);

            switch (line.lineType()) {

                case EjLine.ITEM:

                    TransItem transItem = (TransItem) line.dataRecord();
                    if (transItem.state() != TransItem.VOID) {
                        total = context.posMath().add(total, line.extAmount());
                    }
                    break;

                case EjLine.ITEM_LINK:
                    transItem = (TransItem) (context.lastItem().dataRecord());
                    if (transItem.state() != TransItem.VOID) {
                        total = context.posMath().add(total, line.extAmount());
                    }
                    break;

                case EjLine.PROMOTION:
                    if (((EjPromotion) line).applied()) {
                        total = context.posMath().add(total, line.extAmount());
                    }
                    break;
                default:
                    break;
            }
        }
        return Math.rint(total);
    }

    /**
     * Totals the current ej, not including the tender records.
     */
    public double ejTotal() {

        double total = 0;
        EjLine line;

        for (int i = 0; i < size(); i++) {


            Object o = elementAt(i);

            line = (EjLine) elementAt(i);

            switch (line.lineType()) {

                case EjLine.ITEM:
                    TransItem transItem = (TransItem) line.dataRecord();
                    if (transItem.state() != TransItem.VOID) {
                        total = context.posMath().add(total, line.extAmount());
                    }
                    break;

                case EjLine.ITEM_LINK:
                    transItem = (TransItem) (context.lastItem().dataRecord());
                    if (transItem.state() != TransItem.VOID) {
                        total = context.posMath().add(total, line.extAmount());
                    }
                    break;

                case EjLine.TAX:
                    total = context.posMath().add(total, line.extAmount());
                    break;

                case EjLine.PROMOTION:
                    if (((EjPromotion) line).applied()) {
                        total = context.posMath().add(total, line.extAmount());
                    }
                    break;

                default:
                    break;
            }
        }

        total = context.posMath().sub(total, ejTotalTender());
        return Math.rint(total);
    }

    /**
     * Totals the current ej, includes tender records.
     */
    public double ejSaleTotal() {

        double total = 0;
        EjLine line;

        for (int i = 0; i < size(); i++) {

            line = (EjLine) elementAt(i);

            switch (line.lineType()) {

                case EjLine.ITEM:
                    TransItem transItem = (TransItem) line.dataRecord();
                    if (transItem.state() != TransItem.VOID) {
                        total = context.posMath().add(total, line.extAmount());
                    }
                    break;

                case EjLine.ITEM_LINK:
                    transItem = (TransItem) (context.lastItem().dataRecord());
                    if (transItem.state() != TransItem.VOID) {
                        total = context.posMath().add(total, line.extAmount());
                    }
                    break;

                case EjLine.TAX:
                    total = context.posMath().add(total, line.extAmount());
                    break;

                case EjLine.PROMOTION:
                    if (((EjPromotion) line).applied()) {
                        total = context.posMath().add(total, line.extAmount());
                    }
                    break;

                default:
                    break;
            }
        }
        return Math.rint(total);
    }

    /**
     * Totals the taxable items.
     */
    public double ejTaxable() {

        double total = 0;
        EjLine line;

        for (int i = 0; i < size(); i++) {

            line = (EjLine) elementAt(i);

            switch (line.lineType()) {

                case EjLine.ITEM:
                    TransItem transItem = (TransItem) line.dataRecord();
                    if (transItem.state() != TransItem.VOID) {
                        total = context.posMath().add(total, line.extAmount());
                    }
                    break;

                case EjLine.ITEM_LINK:
                    transItem = (TransItem) (context.lastItem().dataRecord());
                    if (transItem.state() != TransItem.VOID) {
                        total = context.posMath().add(total, line.extAmount());
                    }
                    break;

                case EjLine.PROMOTION:
                    if (((EjPromotion) line).applied()) {
                        total = context.posMath().add(total, line.taxAmount());
                    }
                    break;

                default:
                    break;
            }
        }
        return Math.rint(total);
    }

    /**
     * Totals tax records
     */
    public double ejTaxTotal() {

        double total = 0;
        EjLine line;

        for (int i = 0; i < size(); i++) {

            line = (EjLine) elementAt(i);

            switch (line.lineType()) {

                case EjLine.TAX:
                    total = context.posMath().add(total, line.amount());
                    break;

                default:
                    break;
            }
        }
        return Math.rint(total);
    }

    /**
     * Balance of items - tender.
     */
    public double ejBalance() {

        double total = 0;
        EjLine line;

        for (int i = 0; i < size(); i++) {

            line = (EjLine) elementAt(i);

            switch (line.lineType()) {

                case EjLine.ITEM:
                    TransItem transItem = (TransItem) line.dataRecord();
                    if (transItem.state() != TransItem.VOID) {
                        total = context.posMath().add(total, line.extAmount());
                    }
                    break;
                case EjLine.ITEM_LINK:
                    transItem = (TransItem) (context.lastItem().dataRecord());
                    if (transItem.state() != TransItem.VOID) {
                        total = context.posMath().add(total, line.extAmount());
                    }
                    break;
                case EjLine.TAX:
                    total = context.posMath().add(total, line.extAmount());
                    break;
                case EjLine.PROMOTION:
                    if (((EjPromotion) line).applied()) {
                        total = context.posMath().add(total, line.extAmount());
                    }
                    break;
                case EjLine.TENDER:
                case EjLine.CHECK_TENDER:
                case EjLine.CC_TENDER:
                case EjLine.ALT_CURRENCY_TENDER:
                    total = context.posMath().sub(total, line.extAmount());
                    break;
                default:
                    break;
            }
        }
        return Math.rint(total);
    }

    /**
     * Totals the tender amounts.
     */
    public double ejTotalTender() {

        double total = 0;
        EjLine line;

        for (int i = 0; i < size(); i++) {
            Object o = elementAt(i);
            line = (EjLine) elementAt(i);
            switch (line.lineType()) {

                case EjLine.TENDER:
                case EjLine.CHECK_TENDER:
                case EjLine.CC_TENDER:
                case EjLine.ALT_CURRENCY_TENDER:
                    total = context.posMath().add(total, line.extAmount());
                    break;
                default:
                    break;
            }
        }
        return Math.rint(total);
    }

    /**
     * Count the number of items.
     */
    public int ejNumItems() {

        int items = 0;
        EjLine line;

        for (int i = 0; i < size(); i++) {

            line = (EjLine) elementAt(i);

            switch (line.lineType()) {

                case EjLine.ITEM:

                    items++;
                    break;


                default:
                    break;
            }
        }
        return items;
    }

    /**
     * Return the tender type, if more than one tender
     * record is found in this ej return split.
     */
    public int ejTenderType() {

        int tenderType = -1;
        EjLine line;

        for (int i = 0; i < size(); i++) {

            line = (EjLine) elementAt(i);

            switch (line.lineType()) {

                case EjLine.TENDER:
                case EjLine.BANK:
                case EjLine.ACCOUNT:
                case EjLine.ALT_CURRENCY_TENDER:
                case EjLine.CHECK_TENDER:
                case EjLine.CC_TENDER:

                    if (tenderType >= 0)
                        return EjLine.SPLIT;
                    tenderType = line.lineType();
                    break;
                default:
                    break;
            }
        }
        return tenderType;
    }

    /**
     * Returns the transaction ID from the current ej. Can be found
     * in the first record.
     */
    public int transID(PosContext context) {
        EjHeader t = (EjHeader) context.currEj().elementAt(0);
        return t.transID();
    }

    /**
     * Returns the transaction header record.
     */
    public Transaction transHeader() {
        EjHeader t = (EjHeader) elementAt(0);
        if (t == null) {
            logger.fatal("No trans header");
            System.exit(0);
        }
        return (Transaction) t.dataRecord();
    }

    /**
     * Returns the current (last element) in the ej.
     */
    public EjLine currLine() {
        return (EjLine) lastElement();
    }

    /**
     * Remove the current (last) line in the ej.
     */
    public void removeCurrLine() {
        if (size() > 0) {
            removeElementAt(size() - 1);
        }
        return;
    }

    /**
     * Call toXML data virtual function for all items in the ej.
     */
    public String toXML() {

        StringBuffer xml = new StringBuffer();

        xml.append("\n<transaction>\n");

        for (int i = 0; i < this.size(); i++) {
            EjLine line = (EjLine) this.elementAt(i);
            xml.append(line.dataRecord().toXML());
        }
        xml.append("</transaction>\n");

        return xml.toString();
    }

    /**
     * Method for retrieving a full ej from the db sorted
     * by line number.
     */
    public static Ej getEj(Transaction trans, PosContext context) {

        Ej transRecords = new Ej();
        transRecords.setContext(context);
        EjHeader header = new EjHeader(trans);
        header.setContext(context);
        transRecords.add(header);

        String fetchSpec = TransItem.getByID(trans.transID());
        Vector tmp = Application.dbConnection().fetch(new TransItem(), fetchSpec);
        for (int i = 0; i < tmp.size(); i++) {
            TransItem t = (TransItem) tmp.elementAt(i);
            EjItem item = new EjItem(t);
            item.setContext(context);
            transRecords.addElement(item);
        }

		fetchSpec = TransTax.getByID(trans.transID());
		tmp = Application.dbConnection().fetch(new TransTax(), fetchSpec);
		for (int i = 0; i < tmp.size(); i++) {
			TransTax t = (TransTax) tmp.elementAt(i);
			EjTax tax = new EjTax(t);
			tax.setContext(context);
			transRecords.addElement(tax);
		}

		fetchSpec = TransPromotion.getByID(trans.transID());
		tmp = Application.dbConnection().fetch(new TransPromotion(), fetchSpec);
		for (int i = 0; i < tmp.size(); i++) {
			TransPromotion p = (TransPromotion) tmp.elementAt(i);
			EjPromotion ejp = new EjPromotion(p);
			ejp.setContext(context);
			transRecords.addElement(ejp);
		}

        fetchSpec = TransTender.getByID(trans.transID());
        tmp = Application.dbConnection().fetch(new TransTender(), fetchSpec);
        EjLine tender = null;
        for (int i = 0; i < tmp.size(); i++) {
            TransTender t = (TransTender) tmp.elementAt(i);

            switch (t.tenderType()) {
                case TransTender.CASH:
                    tender = new EjTender(t);
                    break;
                case TransTender.CHECK:
                    tender = new EjCheckTender(t);
                    break;
                case TransTender.CREDIT_CARD:
                    tender = new EjCCTender(t);
                    break;
                case TransTender.ALT_CURRENCY:
                    tender = new EjAltCurrTender(t);
                    break;
				case TransTender.CREDIT:
					tender = new EjCreditTender(t);
					break;
				case TransTender.GIFT:
					tender = new EjGiftTender(t);
					break;
                default:
                    break;
            }
            tender.setContext(context);
            transRecords.addElement(tender);
        }

        fetchSpec = TransBank.getByID(trans.transID());
        tmp = Application.dbConnection().fetch(new TransBank(), fetchSpec);
        for (int i = 0; i < tmp.size(); i++) {
            TransBank t = (TransBank) tmp.elementAt(i);
            EjBank bank = new EjBank(t);
            bank.setContext(context);
            transRecords.addElement(bank);
        }
        java.util.Collections.sort(transRecords);
        return transRecords;
    }

    /**
     * useful method for printing debug info
     * about the ej.
     */
    public void dump() {

        for (int i = 0; i < size(); i++) {
            EjLine line = (EjLine) elementAt(i);
            logger.info("Ej LINE: " + line.getClass() + " amount " + line.amount());
        }
    }

    /**
     * Transaction duplicate thread
     */
    class DuplicateThread extends Thread {

        /** Simple Constructor */
        public DuplicateThread() {
        }

        /**
         * Copy the ej and send it somewhere else.
         */
        public void run() {

            Vector trans = new Vector();
            for (int i = 0; i < size(); i++) {
                set(i, null);
            }
            return;

            // 			Vector trans = new Vector ();
            // 			for (int i=0; i< size (); i++) {
            // 				EjLine line = (EjLine) elementAt (i);
            // 				trans.addElement (line.dataRecord ());
            // 			}

            // 			Vector sites;
            // 			String fetchSpec = TransItem.getByID (trans.transID ());
            // 			Vector sites = Application.dbConnection ().fetch (new TransItem (), fetchSpec);

            // 			UpdateAgent agent = new UpdateAgent ("Shadow Agent", sites, trans);
            // 			agent.execute ();

        }
    }
}


