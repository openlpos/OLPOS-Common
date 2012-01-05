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

package com.globalretailtech.pos.devices;

import java.util.*;
import javax.swing.*;

import com.globalretailtech.util.*;
import com.globalretailtech.pos.ej.*;
import com.globalretailtech.data.Item;
import com.globalretailtech.data.TransItem;
import com.globalretailtech.data.TransTender;
import com.globalretailtech.data.PosTotal;
import com.globalretailtech.data.Total;
import com.globalretailtech.pos.context.*;
import com.globalretailtech.pos.events.*;
import com.globalretailtech.pos.events.ext.LockedItemsReport;
import com.globalretailtech.pos.gui.*;
import com.globalretailtech.pos.hardware.*;
import org.apache.log4j.Logger;

/**
 * An invoice display. Contains a header field
 * for transaction information, #, pos #, employee
 * and date. Implements JTable to display transaction
 * detail.
 *
 *
 * @author  Quentin Olson
 * @see
 */
public class Receipt implements PosGui {

    static Logger logger = Logger.getLogger(Receipt.class);

    private int nlines;
    private PosTicket operDisplay;
    private PosTicket slipPrinter;
//    private PosTicket posPrinter;
    private PosTicket journalPrinter;

    private String operDisplayClass;
    private String slipPrinterClass;
    private String receiptPrinterClass;
    private String journalPrinterClass;

    private PosParameters posParameters;

    public void setOperDisplay(PosTicket value) {
        operDisplay = value;
    }

    public void setSlipPrinter(PosTicket value) {
        slipPrinter = value;
    }

    public void setJournalPrinter(PosTicket value) {
        journalPrinter = value;
    }

    /**
     * Initialize with the current literals and paramaters.
     */
    public Receipt() {

        super();

        ShareProperties p = new ShareProperties(this.getClass().getName());

        if (p.Found()) {
            slipPrinterClass = p.getProperty("SlipPrinterClass", "");
            receiptPrinterClass = p.getProperty("ReceiptPrinterClass", "");
            journalPrinterClass = p.getProperty("JournalPrinterClass", "");
        } else {
        }

        journalPrinter = new JournalPrinter(new jpos.POSPrinter(), "JournalPrinter");
//        posPrinter = new PosPrinter(new jpos.POSPrinter(), "POSPrinter");

    }

    /**
     * Abstract implementations
     */

    /** Return the graphical component */
    public JComponent getGui() {
        OperDisplay disp = (OperDisplay) operDisplay;
        return disp.getGui();
    }

    /** Initialize the displays */
    public void init(PosContext context) {
    }

    /** How to clear this thing */
    public void clear() {
        if (operDisplay != null)
            operDisplay.clear();
    }

    public void home() {
    }

    public void open() {
    }

    public void close() {

        logger.info("Close Receipt");
        operDisplay = null;
        slipPrinterClass = null;
        receiptPrinterClass = null;
        journalPrinterClass = null;

    }

    private void println(PosTicket device) {
        nlines++;
        device.println();
    }

    /**
     *
     */
	public void update(PosEvent event) {
		logger.warn("Unhandled event in Receipt " + event.getClass());
		// 		Exception e = new Exception ();
		// 		e.printStackTrace ();
	}

	/**
	 *
	 */
	public void update(TrainingMode event) {
		if (operDisplay != null) {
			operDisplay.clear();
			if (event.trainingMode()){
				operDisplay.setTrxNo("0");
			}else{
				operDisplay.setTrxNo(Integer.toString(event.context().currTrans().transNo()));
			}
			operDisplay.setPosNo(Integer.toString(event.context().posNo()));
			operDisplay.setOperator(Integer.toString(event.context().user()));
			operDisplay.setDate(Format.getShortTimeDate(new Date()));
			// 		 OperDisplay.setDate (Format.getDate (event.context ().currTrans ().transHeader ().startTime ()));
		}
	}
	
    /**
     * Clear the display and set the info fields.
     */
    public void update(RegisterOpen event) {

        if (operDisplay != null) {
            operDisplay.clear();
            if ( event.context().trainingMode() )
				operDisplay.setTrxNo("0");
    		else{
				operDisplay.setTrxNo(Integer.toString(event.context().currTrans().transNo()));
    		}
            operDisplay.setPosNo(Integer.toString(event.context().posNo()));
            operDisplay.setOperator(Integer.toString(event.context().user()));
            operDisplay.setDate(Format.getShortTimeDate(new Date()));
            // 		 OperDisplay.setDate (Format.getDate (event.context ().currTrans ().transHeader ().startTime ()));
        }

    }

    /**
     * Update display for all ej lines in one method.
     * They are done like this for reprints.
     */
    public void update(EjLine line) {

        switch (line.lineType()) {

            case EjLine.TRANS_HEADER:
                break;

            case EjLine.ITEM:
                update((EjItem) line);
                break;

            case EjLine.PROMOTION:
                update((EjPromotion) line);
                break;

            case EjLine.TAX:
                break;

            case EjLine.TENDER:
                update((EjTender) line);
                break;

            case EjLine.BANK:
                update((EjBank) line);
                break;

            case EjLine.ACCOUNT:
                break;
            case EjLine.ALT_CURRENCY_TENDER:
                update((EjAltCurrTender) line);
                break;

            case EjLine.CHECK_TENDER:
                update((EjCheckTender) line);
                break;

            case EjLine.CC_TENDER:
                update((EjCCTender) line);
                break;

            default:
                logger.warn("Unhandled ej type in Receipt " + line.toString());
                break;
        }
    }

    /**

     *
     */
    public void printHeader(PosContext context) {

        if (operDisplay != null) {
            operDisplay.clear();
            if (context.trainingMode())
				operDisplay.setTrxNo(Integer.toString(0));
            else
            	operDisplay.setTrxNo(Format.zeroFill(context.transNo(), 12, new String("############")));
            	
            operDisplay.setPosNo(Integer.toString(context.posNo()));

            if (context.employee() == null) {
                operDisplay.setOperator("");
            } else {
                operDisplay.setOperator(Integer.toString(context.employee().logonNo()));
            }
            operDisplay.printHeader();
        }
    }

    /**

     *
     */
    public void printTrailer(PosContext context) {
    }

    /**
     *
     */
    public void update(EjItem line) {

        nlines = 0;
        String desc = line.desc().trim();
        double amount = line.extAmount();
        TransItem transItem = (TransItem) line.dataRecord();

        if (transItem.state() == TransItem.VOID) {
            desc = "-" + desc;
            amount = line.context().posMath().negate(amount);
        } else if (transItem.state() == TransItem.PRICE_OVERRIDE) {
            desc = "*" + desc;
        }

        if (line.context().trainingMode())
            desc = "<T>" + desc;

        if (operDisplay != null) {

            operDisplay.setQty(Format.print(Double.toString(line.quantity()), " ", operDisplay.getQtyWidth(), Format.RIGHT));
            operDisplay.setDesc(desc);
            operDisplay.setAmount(Format.print(Format.toMoney(Double.toString(amount), Application.locale()), " ", operDisplay.getAmountWidth(), Format.RIGHT));
            println(operDisplay);
        }

        if (journalPrinter != null) {

            journalPrinter.setQty(Double.toString(line.quantity()));
            journalPrinter.setDesc(desc);
            journalPrinter.setAmount(Format.toMoney(Double.toString(amount), Application.locale()));
            journalPrinter.println();
        }

    }

    /**
     *
     */
    public void update(EjPromotion line) {

        nlines = 0;
		double amount = line.extAmount();
        String desc = line.desc().trim();
		
		if (line.context().trainingMode())
			desc = "<T>" + desc;
		
		if (amount > 0)
			desc = "-" + desc;

        if (operDisplay != null) {
            operDisplay.setQty("");
            operDisplay.setDesc(desc.trim());
            operDisplay.setAmount(Format.print(Format.toMoney(Double.toString(amount), Application.locale()), " ", operDisplay.getAmountWidth(), Format.RIGHT));
            println(operDisplay);
        }
    }

    /**
     *
     */
    public void update(EjBank line) {

        nlines = 0;
        String desc = line.desc().trim();
        if (line.context().trainingMode())
            desc = "<T>" + desc;
        double amount = line.amount();

        if (operDisplay != null) {
            operDisplay.setQty("");
            operDisplay.setDesc(desc.trim());
            operDisplay.setAmount(Format.print(Format.toMoney(Double.toString(amount), Application.locale()), " ", operDisplay.getAmountWidth(), Format.RIGHT));
            println(operDisplay);
        }
    }

    /**
     *
     */
    public void update(EjTax line) {

        nlines = 0;
        String desc = line.context().posParameters().getString("CashTender").trim();
        if (line.context().trainingMode())
            desc = "<T>" + desc;
        double amount = line.amount();

        if (operDisplay != null) {
            operDisplay.setQty("");
            operDisplay.setDesc(desc.trim());
            operDisplay.setAmount(Format.print(Format.toMoney(Double.toString(amount), Application.locale()), " ", operDisplay.getAmountWidth(), Format.RIGHT));
            println(operDisplay);
        }
    }


    /**
     *
     */
    public void update(EjTender line) {

        nlines = 0;
        // Make sure the record is complete

        TransTender transTender = (TransTender) line.dataRecord();

        String desc;
        double change = transTender.change();
        double amount = transTender.tenderAmount();
        double subTotal = line.context().currEj().ejSubTotal();
        double tax = line.context().currEj().ejTaxTotal();
        double saleTotal = line.context().currEj().ejSaleTotal();

        String tendertext = null;

        if (operDisplay != null) {



            desc = line.context().posParameters().getString("SubTotal").trim();
            if (line.context().trainingMode())
                desc = "<T>" + desc;
            operDisplay.setDesc(desc);
            operDisplay.setAmount(Format.print(Format.toMoney(Double.toString(subTotal), Application.locale()), " ", operDisplay.getAmountWidth(), Format.RIGHT));
            println(operDisplay);

            if (tax != 0.0) {
                desc = line.context().posParameters().getString("TotalTax").trim();
                if (line.context().trainingMode())
                    desc = "<T>" + desc;
                operDisplay.setDesc(desc);
                operDisplay.setAmount(Format.print(Format.toMoney(Double.toString(tax), Application.locale()), " ", operDisplay.getAmountWidth(), Format.RIGHT));
                println(operDisplay);
            }


            desc = line.context().posParameters().getString("Total").trim();
            if (line.context().trainingMode())
                desc = "<T>" + desc;
            operDisplay.setDesc(desc);
            operDisplay.setAmount(Format.print(Format.toMoney(Double.toString(saleTotal), Application.locale()), " ", operDisplay.getAmountWidth(), Format.RIGHT));
            println(operDisplay);

            desc = transTender.tenderDesc().trim();
            if (line.context().trainingMode())
                desc = "<T>" + desc;
            operDisplay.setDesc(desc);
            operDisplay.setAmount(Format.print(Format.toMoney(Double.toString(amount), Application.locale()), " ", operDisplay.getAmountWidth(), Format.RIGHT));
            println(operDisplay);

            if (change != 0.0) {
                desc = transTender.changeDesc().trim();
                if (line.context().trainingMode())
                    desc = "<T>" + desc;
                operDisplay.setDesc(desc);
                operDisplay.setAmount(Format.print(Format.toMoney(Double.toString(change), Application.locale()), " ", operDisplay.getAmountWidth(), Format.RIGHT));
                println(operDisplay);
            }

            line.context().disableKeys();
        }
    }

    public void clear(EjLine line) {

        if (operDisplay != null) {
            for (int i = 0; i < nlines; i++)
                operDisplay.clearln();
        }
    }

    /**
     *
     */
    public void update(EjCheckTender line) {

        nlines = 0;
        // Make sure the record is complete


        if (line.state() != EjCheckTender.TENDER_FINAL)
            return;

        TransTender transTender = (TransTender) line.dataRecord();

        double change = transTender.change();
        double amount = transTender.tenderAmount();
        double tax = line.context().currEj().ejTaxTotal();
        String desc;

        if (operDisplay != null) {
            desc = line.context().posParameters().getString("TotalTax").trim();
            if (line.context().trainingMode())
                desc = "<T>" + desc;
            operDisplay.setDesc(desc);
            operDisplay.setAmount(Format.print(Format.toMoney(Double.toString(tax), Application.locale()), " ", operDisplay.getAmountWidth(), Format.RIGHT));
            println(operDisplay);

            String tendertext = null;
            desc = transTender.tenderDesc().trim();
            if (line.context().trainingMode())
                desc = "<T>" + desc;
            operDisplay.setDesc(desc);
            operDisplay.setAmount(Format.print(Format.toMoney(Double.toString(amount), Application.locale()), " ", operDisplay.getAmountWidth(), Format.RIGHT));
            println(operDisplay);

            if (change != 0.0) {
                desc = transTender.changeDesc().trim();
                if (line.context().trainingMode())
                    desc = "<T>" + desc;
                operDisplay.setDesc(desc);
                operDisplay.setAmount(Format.print(Format.toMoney(Double.toString(change), Application.locale()), " ", operDisplay.getAmountWidth(), Format.RIGHT));
                println(operDisplay);
            }
        }
    }

    /**
     *
     */
    public void update(EjCCTender line) {


        nlines = 0;
        // Make sure the record is complete

        if (line.state() != EjCCTender.TENDER_FINAL)
            return;

        TransTender transTender = (TransTender) line.dataRecord();

        double change = transTender.change();
        double amount = transTender.tenderAmount();
        double tax = line.context().currEj().ejTaxTotal();

        String desc;

        if (operDisplay != null) {

            operDisplay.setAmount(Format.print(Format.toMoney(Double.toString(tax), Application.locale()), " ", operDisplay.getAmountWidth(), Format.RIGHT));
            println(operDisplay);

            String tendertext = null;

            desc = transTender.tenderDesc().trim();
            if (line.context().trainingMode())
                desc = "<T>" + desc;
            operDisplay.setDesc(desc);
            operDisplay.setAmount(Format.print(Format.toMoney(Double.toString(amount), Application.locale()), " ", operDisplay.getAmountWidth(), Format.RIGHT));
            println(operDisplay);

            if (change != 0.0) {
                desc = transTender.changeDesc().trim();
                if (line.context().trainingMode())
                    desc = "<T>" + desc;
                operDisplay.setDesc(transTender.changeDesc().trim());
                operDisplay.setAmount(Format.print(Format.toMoney(Double.toString(change), Application.locale()), " ", operDisplay.getAmountWidth(), Format.RIGHT));
                println(operDisplay);
            }
        }
    }

    /**
     *
     */
    public void update(EjAltCurrTender line) {

        nlines = 0;
        // Make sure the record is complete

        TransTender transTender = (TransTender) line.dataRecord();

        // get the locale

        java.util.Locale locale = new java.util.Locale(line.context().altCurrency().currencyCode().language(), line.context().altCurrency().currencyCode().variant());

        // output the amount tendered in local currency

        String desc = line.desc().trim();

        if (line.state() == EjAltCurrTender.ENTER_ALT_CURR_AMOUNT) {

            double total = line.context().currEj().ejTotal();
            double tax = line.context().currEj().ejTaxTotal();

            if (operDisplay != null) {

                desc = line.context().posParameters().getString("TotalTax").trim();
                operDisplay.setDesc(desc);
                if (line.context().trainingMode())
                    desc = "<T>" + desc;
                operDisplay.setAmount(Format.print(Format.toMoney(Double.toString(tax), Application.locale()), " ", operDisplay.getAmountWidth(), Format.RIGHT));
                println(operDisplay);

                desc = line.context().posParameters().getString("Total").trim();
                if (line.context().trainingMode())
                    desc = "<T>" + desc;
                operDisplay.setDesc(desc);
                operDisplay.setAmount(Format.print(Format.toMoney(Double.toString(total), Application.locale()), " ", operDisplay.getAmountWidth(), Format.RIGHT));
                println(operDisplay);

                desc = line.convertText().trim();
                operDisplay.setDesc(desc);
                if (line.context().trainingMode())
                    desc = "<T>" + desc;
                operDisplay.setAmount(Format.print(Format.toMoney(Double.toString(line.convertAmount()), locale), " ", operDisplay.getAmountWidth(), Format.RIGHT));
                println(operDisplay);
            }
        } else {

            if (operDisplay != null) {
                operDisplay.setDesc(transTender.tenderDesc().trim());
                operDisplay.setAmount(Format.print(Format.toMoney(Double.toString(line.splitAmount()), locale), " ", operDisplay.getAmountWidth(), Format.RIGHT));
                println(operDisplay);

                operDisplay.setDesc(line.context().posParameters().getString("TenderTotal").trim());
                operDisplay.setAmount(Format.print(Format.toMoney(Double.toString(transTender.tenderAmount()), Application.locale()), " ", operDisplay.getAmountWidth(), Format.RIGHT));
                println(operDisplay);

                operDisplay.setDesc(transTender.changeDesc().trim());
                operDisplay.setAmount(Format.print(Format.toMoney(Double.toString(transTender.change()), Application.locale()), " ", operDisplay.getAmountWidth(), Format.RIGHT));
                println(operDisplay);
            }
        }
    }


    /**
     * Use the context from the terminal report event to lookup the
     * total records.
     */
    public void update(TerminalReport report) {

        nlines = 0;
        String fetchSpec = PosTotal.getBySiteAndPos(report.context().siteID(),
                report.context().posNo());

        Vector results = Application.dbConnection().fetch(new PosTotal(), fetchSpec);

        if (results.size() > 0) {

            PosTotal posTotal = (PosTotal) results.elementAt(0);

            for (int i = 0; i < posTotal.totals().size(); i++) {

                Total total = (Total) posTotal.totals().elementAt(i);
                String countLit = "CountLit amount";
                String amountLit = "Amount";

                switch (total.totalType()) {

                    case Total.CASH:
                    case Total.CASH_IN_DRAWER:

                        countLit = report.context().posParameters().getString("CashInDrCount");
                        amountLit = report.context().posParameters().getString("CashInDrAmount");
                        break;

                    case Total.CHECK:
                    case Total.CHECK_IN_DRAWER:

                        countLit = report.context().posParameters().getString("CheckCount");
                        amountLit = report.context().posParameters().getString("CheckAmount");
                        break;

                    case Total.TAXABLE:

                        countLit = report.context().posParameters().getString("TaxCount");
                        amountLit = report.context().posParameters().getString("TaxAmount");
                        break;

                    case Total.NON_TAXABLE:

                        countLit = report.context().posParameters().getString("NonTaxCount");
                        amountLit = report.context().posParameters().getString("NonTaxAmount");
                        break;

                    case Total.VOID:

                        countLit = report.context().posParameters().getString("VoidCount");
                        amountLit = report.context().posParameters().getString("VoidAmount");
                        break;

                    case Total.RETURN:

                        countLit = report.context().posParameters().getString("ReturnCount");
                        amountLit = report.context().posParameters().getString("ReturnAmount");
                        break;

                    case Total.COUPON:

                        countLit = report.context().posParameters().getString("CouponCount");
                        amountLit = report.context().posParameters().getString("CouponAmount");
                        break;

                    case Total.RCVD_ON_ACCT:
                    	break;
                    	
                    case Total.PAID_IN:
						
						countLit = report.context().posParameters().getString("PaidInCount");
						amountLit = report.context().posParameters().getString("PaidInAmount");
						break;
						
                    case Total.PAID_OUT:
						
						countLit = report.context().posParameters().getString("PaidOutCount");
						amountLit = report.context().posParameters().getString("PaidOutAmount");
                        break;
                        
                    case Total.LOAN:

                        countLit = report.context().posParameters().getString("LoanCount");
                        amountLit = report.context().posParameters().getString("LoanAmount");
                        break;

                    case Total.PICK_UP:

                        countLit = report.context().posParameters().getString("PickupCount");
                        amountLit = report.context().posParameters().getString("PickupAmount");
                        break;

					case Total.CREDIT_CARD_BASE:

						countLit = report.context().posParameters().getString("CreditCardCount");
						amountLit = report.context().posParameters().getString("CreditCardAmount");
						break;

					case Total.CREDIT_BASE:

						countLit = report.context().posParameters().getString("CreditCount");
						amountLit = report.context().posParameters().getString("CreditAmount");
						break;

					case Total.GIFT_BASE:

						countLit = report.context().posParameters().getString("GiftCount");
						amountLit = report.context().posParameters().getString("GiftAmount");
						break;

                    case Total.DEPARTMENT_BASE:
                    case Total.ALT_CURRENCY_BASE:
                        break;
                    default:
                        break;
                }

                if (operDisplay != null) {
                	String prefix = "";
                	if ( report.context().trainingMode() )
                		prefix = "<T>";
                    operDisplay.setDesc(prefix+countLit.trim());
                    operDisplay.setAmount(Integer.toString(total.totalCount()));
                    println(operDisplay);

                    operDisplay.setDesc(prefix+amountLit.trim());
                    operDisplay.setAmount(Format.print(Format.toMoney(Double.toString(total.totalAmount()),
                            report.context().locale()),
                            " ",
                            operDisplay.getAmountWidth(),
                            Format.RIGHT));
                    println(operDisplay);
                }
            }
        }
    }
	/**
	 * Use the context from the report event to lookup the
	 * locked items.
	 */
	public void update(LockedItemsReport report) {

		// get list of locked items and print them
		String fs = Item.getLocked();

		Vector v = Application.dbConnection().fetch (new Item(), fs);
		if ( v != null && v.size() > 0){

			if (operDisplay != null) {

				for (int i = 0; i < v.size(); i++) {

					Item item = (Item)v.elementAt(i);

					operDisplay.setQty( (i+1)+"");
					operDisplay.setDesc(item.itemID()+" "+item.desc());
					operDisplay.setAmount(Format.print(Format.toMoney(Double.toString(item.amount()),
							report.context().locale()),
							" ",
							operDisplay.getAmountWidth(),
							Format.RIGHT));

					println(operDisplay);

				}

			}
		}
	}	

    /**
     *
     */
    public void printTotal(PosContext context, double amount, int count) {
    }

    public void printTotalHeader(PosContext context) {
    }

    public void printTotalTrailer(PosContext context) {
    }

}


