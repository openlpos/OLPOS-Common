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

import com.globalretailtech.pos.context.*;
import com.globalretailtech.pos.devices.*;
import org.apache.log4j.Logger;

/**
 * Journal printer display class.
 *
 *
 * @author  Quentin Olson
 * @see
 */
public class JournalPrinter extends Printer implements PosTicket {

    static Logger logger = Logger.getLogger(JournalPrinter.class);

    private StringBuffer scratch;
    private jpos.POSPrinter control;
    private PosParameters posParameters;

    public JournalPrinter(jpos.POSPrinter c, String devicename) {
        super(c, devicename);
        control = c;
        scratch = new StringBuffer();
    }

    public jpos.POSPrinter getControl() {
        return (jpos.POSPrinter) control();
    }

    public void init(PosParameters params, int width, int height) {
    }

    public int getQtyWidth() {
        return 0;
    }

    public int getItemWidth() {
        return 0;
    }

    public int getItemDescWidth() {
        return 0;
    }

    public int getAmountWidth() {
        return 0;
    }

    public int getColumns() {

        try {
            return control.getJrnLineWidth();
        } catch (jpos.JposException e) {
            logger.warn(e.toString(), e);
        }
        return 0;
    }

    public void setQty(String value) {
        scratch.append(value).append(",");
    }

    public void setItem(String value) {
        scratch.append(value).append(",");
    }

    public void setDesc(String value) {
        scratch.append(value).append(",");
    }

    public void setAmount(String value) {
        scratch.append(value).append(",");
    }

    public void setTrxNo(String value) {
        scratch.append(value).append(",");
    }

    public void setPosNo(String value) {
        scratch.append(value).append(",");
    }

    public void setOperator(String value) {
        scratch.append(value).append(",");
    }

    public void setDate(String value) {
        scratch.append(value).append(",");
    }

    public void println() {

        try {
            control.printNormal(0, scratch.toString());
        } catch (jpos.JposException e) {
            logger.warn(e.toString());
        }
        if (scratch.length() > 0)
            scratch.delete(0, scratch.length());

    }

    public void println(String value) {

        scratch.append(value);
        try {
            control.printNormal(0, scratch.toString());
        } catch (jpos.JposException e) {
            logger.warn(e.toString(), e);
        }
        if (scratch.length() > 0)
            scratch.delete(0, scratch.length());
    }

    public void printHeader() {
    }

    public void printTrailer() {
    }

    public void clear() {
    }

    public void clearln() {
    }
}


