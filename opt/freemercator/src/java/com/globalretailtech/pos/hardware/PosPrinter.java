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

import jpos.JposException;
import jpos.POSPrinterConst;

import com.globalretailtech.data.Site;
import com.globalretailtech.data.TransItem;
import com.globalretailtech.data.TransItemLink;
import com.globalretailtech.data.TransPromotion;
import com.globalretailtech.data.TransTax;
import com.globalretailtech.pos.context.*;
import com.globalretailtech.pos.ej.Ej;
import com.globalretailtech.pos.ej.EjLine;
import com.globalretailtech.pos.ej.EjTender;
import com.globalretailtech.util.Format;

import org.apache.log4j.Logger;

/**
 * PosPrinter hardware class.
 *
 *
 * @author  Quentin Olson
 * @author  Igor Semenko
 * @see jpos.PosPrinter
 */
public class PosPrinter extends Printer {

	static Logger logger = Logger.getLogger(PosPrinter.class);

	private jpos.POSPrinter control;
	private PosParameters posParameters;

	private PosContext context;

	public PosPrinter(
		jpos.POSPrinter c,
		String devicename,
		PosContext context) {
		super(c, devicename);
		setContext(context);
		control = c;

		// some additional properties
		if (isOpen()) {
			logger.debug("init PosPrinter");
			try {
				//TODO parametrize quality
				control.setRecLetterQuality(true);
			} catch (JposException e) {
				logger.error("Can't init PosPrinter", e);
			}
		}
	}

	public void setContext(PosContext context) {
		this.context = context;
	}

	public PosContext context() {
		return context;
	}

	public jpos.POSPrinter getControl() {
		return (jpos.POSPrinter) control();
	}

	public int getColumns() {

		try {
			logger.debug("device claimed:" + control.getClaimed());
			logger.debug("device enabled:" + control.getDeviceEnabled());
			return control.getRecLineChars(); //.getRecLineWidth()
		} catch (jpos.JposException e) {
			logger.warn(e.toString(), e);
		}
		return 0;
	}

	public void println() {

		try {
			control.printNormal(0, "\n");
		} catch (jpos.JposException e) {
			logger.warn(e.toString(), e);
		}

	}

	public void println(String value) {

		try {
			control.printNormal(
				POSPrinterConst.PTR_S_RECEIPT,
				value + "\n");
		} catch (jpos.JposException e) {
			logger.warn(e.toString(), e);
		}
	}

	/** 
	 * Prints Ej as a receipt 
	 **/
	public void printReceipt(Ej ej) throws JposException {

		if (!isOpen())
			return;

		printHeader();

		for (int ejIndex = 0; ejIndex < ej.size(); ejIndex++) {

			EjLine line = (EjLine) ej.elementAt(ejIndex);

			switch (line.lineType()) {

				case EjLine.BANK :
					//						if (line instanceof EjBank)
					//							printBankTransaction((EjBank)line);
					break;

				case EjLine.ITEM :

					TransItem transItem = (TransItem) line.dataRecord();
					if (transItem.state() != TransItem.VOID) {

						printItem(transItem);
					}
					break;

				case EjLine.ITEM_LINK :
					TransItemLink itemLink = (TransItemLink) line.dataRecord();
					printItem(itemLink);
					break;

				case EjLine.PROMOTION :
					TransPromotion itemPromo =
						(TransPromotion) line.dataRecord();
					//TODO how to check whether promotion is voided?							
					//						if (itemPromo..state() != TransItem.VOID) {
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

		printTotal(ej);

		printTrailer();
	}
	public void printHeader() throws JposException {

		Site site = context().site();

		control.printNormal(
			POSPrinterConst.PTR_S_RECEIPT,
			"\u001b|cA" +site.name() + "\n");
		control.printNormal(
			POSPrinterConst.PTR_S_RECEIPT,
			"\u001b|cA" +site.addr1() + "\n");
		control.printNormal(
			POSPrinterConst.PTR_S_RECEIPT,
			"\u001b|cA" +site.phone() + "\n");
		control.printNormal(
			POSPrinterConst.PTR_S_RECEIPT, 
			"\n");
	}

	public void printTrailer() throws JposException {

		control.printNormal(
			POSPrinterConst.PTR_S_RECEIPT, 
			"\n");
		//TODO remove hardcoded "Thank you"	
		control.printNormal(
			POSPrinterConst.PTR_S_RECEIPT,
			"\u001b|cA" +"Thank You" + "\n");
	}

	protected void printTotal(Ej ej) throws JposException {
		double total = ej.ejSubTotal();
		control.printNormal(POSPrinterConst.PTR_S_RECEIPT, "\n");
		//TODO add internalization of "TOTAL"
		control.printNormal(
			POSPrinterConst.PTR_S_RECEIPT,
			"\u001b|bC\u001b|2C" + "TOTAL" + "\n");
		control.printNormal(
			POSPrinterConst.PTR_S_RECEIPT,
			"\u001b|rA" + Double.toString(total/100) + "\n");
	}

	/**
	 * @param itemPromo
	 */
	private void printItemAdjustment(TransPromotion itemPromo)
		throws JposException {
		//		logger.debug("printTender called");
		//		control.printRecItemAdjustment(
		//			FiscalPrinterConst.FPTR_AT_AMOUNT_DISCOUNT,
		//			itemPromo.promotionDesc(),
		//			(long) itemPromo.promotionAmount(),
		//			0);
	}

	/**
	 * @param itemTender
	 */
	private void printTender(EjTender itemTender) throws JposException {
		//		control.printRecCash((long) itemTender.amount());
		int width = control.getRecLineChars();
		control.printNormal(
			POSPrinterConst.PTR_S_RECEIPT,
			Format.print("Cash: ", Double.toString(itemTender.amount()/100), " ", width)
				+ "\n");
		if (itemTender.change() > 0)
			control.printNormal(
				POSPrinterConst.PTR_S_RECEIPT,
				Format.print("Change: ", Double.toString(itemTender.change()/100), " ", width)
					+ "\n");
	}

	/**
	 * @param itemLink
	 */
	private void printItem(TransItemLink itemLink) throws JposException {
		int width = control.getRecLineChars();
		control.printNormal(
			POSPrinterConst.PTR_S_RECEIPT,
			Format.print(
				"*" + itemLink.itemLinkDesc(),
				Double.toString(itemLink.amount()/100),
				" ",
				width)
				+ "\n");
	}

	/**
	 * Prints item on RECEIPT station
	 */
	private void printItem(TransItem transItem) throws JposException {
		int width = control.getRecLineChars();
		control.printNormal(
			POSPrinterConst.PTR_S_RECEIPT,
			Format.print(
				transItem.quantity() + " " + transItem.itemDesc(),
				Double.toString(transItem.amount()/100),
				" ",
				width)
				+ "\n");
	}

}
