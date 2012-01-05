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

package com.globalretailtech.pos.hardware;

import java.util.Vector;

import jpos.FiscalPrinterConst;
import jpos.JposException;

import com.globalretailtech.util.*;
import com.globalretailtech.data.Item;
import com.globalretailtech.data.Pos;
import com.globalretailtech.data.TransItem;
import com.globalretailtech.data.TransItemLink;
import com.globalretailtech.data.TransPromotion;
import com.globalretailtech.data.TransTax;
import com.globalretailtech.data.TransTender;
import com.globalretailtech.data.Transaction;
import com.globalretailtech.pos.context.*;
import com.globalretailtech.pos.events.*;
import com.globalretailtech.pos.ej.*;
import org.apache.log4j.Logger;

/**
 * Fiscal Printer class.
 *
 *
 * @author  Igor Semenko
 * @see
 */
public class FiscalPrinter extends BaseDevice {

	static Logger logger = Logger.getLogger(FiscalPrinter.class);

	private jpos.FiscalPrinter control;
	private PosParameters posParameters;
	private PosContext context;
	
	public FiscalPrinter(jpos.FiscalPrinter c, String devicename, PosContext context) {
		super(c, devicename);
		control = c;
		setContext (context);
		// get exclusive access
		try {
			c.claim(1000);
			c.setDeviceEnabled(true);
		} catch (JposException e) {
			logger.error ("Can't claim FiscalPrinter");
		}
	}

	public jpos.POSPrinter getControl() {
		return (jpos.POSPrinter) control();
	}

	public void init(PosParameters params) {
		posParameters = params;
	}
	
	public void setContext (PosContext context){
		this.context = context;
	}
	public PosContext context(){
		return context;
	}

	public void xReport() throws JposException{
		if ( ! isOpen() ) return;

		control.printXReport();
	}
	
	public void zReport() throws JposException, PosException{
			
		if ( ! isOpen() ) return;

			// make sure there is no transaction-in-progress
			if ( context().currEj().size() > 1 ){ 
				context().currEj().dump();
				// error
				logger.error ("Ej is not empty");
				throw new PosException (context().posParameters().getString("SaleInProgress"));
			}
			
			control.printZReport();
			
			// fiscal printer performed z report,
			// update appropriate db records
	
			int z = getZReportNo();
			String fs = Pos.getByPosNo(context().posNo());
			Vector tmp = Application.dbConnection().fetch(new Pos(), fs);
	
			if (tmp.size() > 0) {  // found
				Pos pos = (Pos) tmp.elementAt(0);  
				// 'current' z report no will be next report no
				pos.setZ (z+1);
				pos.update();
			}
			
			// set trans type to ZReport
			
			context().setTransType(Transaction.ZREPORT);

			
			// complete z report transaction

			Transaction trans = (Transaction) context().currTrans().dataRecord();
			trans.setZ(z);
			trans.updateStateAndType(Transaction.COMPLETE, Transaction.ZREPORT);
			trans.updateState(Transaction.COMPLETE); // update z report no

			
			// create a new transaction, EjHeader and put it in the ej
			
			StartTransaction start = new StartTransaction(context());
			context().setCurrTrans(new EjHeader(start.getTrans(Transaction.SALES)));
			context().currEj().ejAdd(context().currTrans());

	}

	/** 
	 * Open receipt if it is not opened
	 **/
	public void openReceipt() throws JposException {

		if ( ! isOpen() ) return;

		if (control.getPrinterState()
			!= FiscalPrinterConst.FPTR_PS_FISCAL_RECEIPT)
			control.beginFiscalReceipt(false);
	}
	/**
	 * Returns current/last receipt no
	 **/
	public int getReceiptNo() throws JposException {

		if ( ! isOpen() ) return 0;

		String[] data = new String[1];
		control.getData(FiscalPrinterConst.FPTR_GD_RECEIPT_NUMBER, null, data);
		return Integer.parseInt(data[0]);
	}

	/**
	 * Returns current/last z report no
	 **/
	public int getZReportNo() throws JposException {

		if ( ! isOpen() ) return 0;

		String[] data = new String[1];
		control.getData(FiscalPrinterConst.FPTR_GD_Z_REPORT, null, data);
		return Integer.parseInt(data[0]);
	}

	public void printReceipt(Ej ej) throws JposException {
		
		if ( ! isOpen() ) return;
						
			for (int ejIndex = 0; ejIndex < ej.size(); ejIndex++) {

				EjLine line = (EjLine) ej.elementAt(ejIndex);

				switch (line.lineType()) {


					case EjLine.BANK:
						if (line instanceof EjBank)
							printBankTransaction((EjBank)line);
						break;	



					case EjLine.ITEM :

						TransItem transItem = (TransItem) line.dataRecord();
						if (transItem.state() != TransItem.VOID) {

							if (control.getPrinterState()
								!= FiscalPrinterConst.FPTR_PS_FISCAL_RECEIPT)
								control.beginFiscalReceipt(false);

							printItem(transItem);
						}
						break;



					case EjLine.ITEM_LINK :
						TransItemLink itemLink =
							(TransItemLink) line.dataRecord();
						
						if (itemLink.amount() != 0) 
							printItem(itemLink);
							
						break;



					case EjLine.PROMOTION :
						TransPromotion itemPromo =
							(TransPromotion) line.dataRecord();
			
						// don't print void promotion
						if (itemPromo.promotionAmount() != 0)
							printItemAdjustment(itemPromo);
							
						break;



					case EjLine.TAX :
						TransTax itemTax = (TransTax) line.dataRecord();
						//TODO what to do with tax?
						break;



					case EjLine.TENDER :
						EjTender itemTender = (EjTender) line;
						printTender(itemTender);
					default :
						}
			}

			if (logger.isDebugEnabled())
				dump(ej);

			if (control.getPrinterState()
				== FiscalPrinterConst.FPTR_PS_FISCAL_RECEIPT)
				control.endFiscalReceipt(false);
	}
	/**
	 * @param itemPromo
	 */
	private void printItemAdjustment(TransPromotion itemPromo)
		throws JposException {
		
		control.printRecItemAdjustment(
			FiscalPrinterConst.FPTR_AT_AMOUNT_DISCOUNT,
			itemPromo.promotionDesc(),
			(long) (itemPromo.promotionAmount() * itemPromo.promotionQuantity()),
			0);
	}

	/**
	 * @param itemTender
	 */
	private void printTender(EjTender itemTender) throws JposException {

		long amount = (long) itemTender.amount();

		int type = itemTender.getTenderType();
		
		switch (type) {

			case TransTender.CASH :
				control.printRecCash(amount);
				break;

			case TransTender.CREDIT :
			case TransTender.CREDIT_CARD :
				// prefix is important to be able to separate
				// credit from cheque tender in fiscal printer.
				// Hack, but how to implement it via JavaPos driver?
				control.printRecNotPaid("C"+itemTender.getTenderDesc(),amount);
				break;

			default :
				control.printRecNotPaid(itemTender.getTenderDesc(),amount);
				break;
		}
	}

	/**
	 * @param itemLink
	 */
	private void printItem(TransItemLink itemLink) {
		//		try {
		//			control.printRecItem(item.desc(), 21050, 10000, 1, 2105, "רע");
		//			control.printRecItem(itemLink.itemLinkDesc(), 21050, 10000, 1, 2105, "רע");
		//		} catch (JposException e) {
		//			logger.error("", e);
		//		}
	}

	/**
	 */
	private void printItem(TransItem transItem) throws JposException {
		Item item = lookupItemBySKU(transItem.sku());
		int dec = control.getQuantityDecimalPlaces();
		double tranQuantity = transItem.quantity();
		tranQuantity = (double) ((int) (tranQuantity * 1000)) / 1000;
		double quantity = tranQuantity;
		if (quantity == 1)
			quantity = 0; // for better printing
		else {
			for (int i = 0; i < dec; i++)
				quantity *= 10;
		}
		int unitPrice = (int) (transItem.amount() + transItem.extAmount());
		int amount = (int) (unitPrice * tranQuantity);
		//			control.printRecItem(item.desc(), 21050, 10000, 1, 2105, "רע");
		control.printRecItem(
			transItem.itemDesc(),
			amount,
			(int) quantity,
			item.taxGroup(),
			unitPrice,
			"רע");
	}

	/**
	 */
	private Item lookupItemBySKU(String sku) {
		String fetchSpec = Item.getBySKU(sku);
		java.util.Vector tmp =
			Application.dbConnection().fetch(new Item(), fetchSpec);

		Item item = null;
		if (tmp.size() > 0) { // Item found
			item = (Item) tmp.elementAt(0); // Get the item
		} else {
			logger.warn("Can't find item by SKU " + sku + " !");
		}
		return item;
	}

	private void dump(Ej ej) {
		for (int i = 0; i < ej.size(); i++) {
			EjLine line = (EjLine) ej.elementAt(i);
			StringBuffer buf = new StringBuffer();
			buf.append(i + " ");
			switch (line.lineType()) {

				case EjLine.ITEM :
					buf.append("ITEM     ");
					break;
				case EjLine.PROMOTION :
					buf.append("PROMOTION");
					break;
				case EjLine.TENDER :
					buf.append("TENDER   ");
					break;
				case EjLine.TAX :
					buf.append("TAX      ");
					break;
				case EjLine.TOTAL :
					buf.append("TOTAL    ");
					break;
				case EjLine.ITEM_LINK :
					buf.append("ITEM_LINK");
					break;
			}
			buf.append(" " + line.amount());
			logger.debug(buf.toString());
		}
	}

	public String getCheckHealthText() {

		if ( ! isOpen() ) return "Not Opened";

		try {
			return control.getCheckHealthText();
		} catch (JposException e) {
			logger.error("", e);
		}
		return "";
	}

	/**
	 * Pring "PaidIn" "PaidOut" transactions.
	 * This is not common for FiscalPrinter so hardcoded for my device
	 */
	public void printBankTransaction(EjBank bankTran) throws JposException {

		if ( ! isOpen() ) return;

		String operation = (bankTran.amount() > 0) ? "I" : "O";
		String amount = lpad("" + ((int) bankTran.amount()), 10, "0");
		String[] cmd = { "CAIO", operation + amount };

		if (control.getPhysicalDeviceName() != null &&
		control.getPhysicalDeviceName().equals("Maria301"))
				control.directIO(0, null, cmd);

	}

	protected String lpad(String str, int len, String padstr) {
		String _s = str;
		while (_s.length() < len)
			_s = padstr + _s;
		return _s;
	}

	/**
	 * Calls control's printPeriodicTotalsReport to prepare report from date to date
	 * @param string2
	 */
	public void printPeriodicTotalsReportD(String date1, String date2)
		throws JposException {

		if ( ! isOpen() ) return;

		control.printPeriodicTotalsReport(date1, date2);
	}
	/**
	 * Calls control's printReport to prepare report from start z no to end z no
	 * @param string2
	 */
	public void printPeriodicTotalsReportN(String start, String end)
		throws JposException {

		if ( ! isOpen() ) return;

		control.printReport(0, start, end);
	}

	/**
	 * Printer - specific, override for your device 
	 */
	public void feed() {

		if ( ! isOpen() ) return;
			
		String[] cmd = { "FEED", "1" };

		try {
			if (control.getPhysicalDeviceName() != null &&
			control.getPhysicalDeviceName().equals("Maria301"))
					control.directIO(0, null, cmd);
		} catch (JposException e) {
			logger.error("", e);
		}

	}
	/**
	 * Printer - specific, override for your device 
	 */
	public void discReport() throws JposException{

		if ( ! isOpen() ) return;

		if (control.getPhysicalDeviceName() != null &&
			control.getPhysicalDeviceName().equals("Maria301")){
			String[] cmd = { "DIZV", "" };
			control.directIO(0, null, cmd);
		}

	}


	/**
	 * Adjusts time on FiscalPrinter
	 * @param date in JavaPos format: ddMMyyyyHHmm
	 */
	public void setDate(String string) throws JposException {

		if ( ! isOpen() ) return;

		control.setDate(string);
	}
}
