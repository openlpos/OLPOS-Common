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

import java.util.Hashtable;
import java.util.Vector;

import jpos.JposException;


import com.globalretailtech.pos.context.*;
import com.globalretailtech.pos.devices.*;
import org.apache.log4j.Logger;

/**
 * POS printer display class.
 *
 *
 * @author  Quentin Olson
 * @see
 */
public class PosLineDisplay extends BaseDevice implements PosPrompt {

    static Logger logger = Logger.getLogger(PosLineDisplay.class);

    private StringBuffer scratch;
    private jpos.LineDisplay control;
    private Hashtable poslits;
    private Vector header;
    private ContextSet contextset;

    public jpos.BaseControl Control() {
        return control;
    }

    public PosLineDisplay(jpos.LineDisplay c, String devicename) {
        super(c, devicename);
        control = c;
        scratch = new StringBuffer();
		try {
			c.claim(1000);
			c.setDeviceEnabled(true);
		} catch (JposException e) {
			logger.error ("Can't claim CashDrawer");
		}
    }

    public jpos.LineDisplay getControl() {
        return (jpos.LineDisplay) Control();
    }

    public void init(PosParameters params, int h, int w) {
    }

    public void clear() {
        displayText("", 0);
    }

    public void setText(String text) {
        displayText(text, 0);
    }

    public void setText(String text, int row, int col) {
        displayText(text, row);
    }

    public int getWidth() {
        return getColumns();
    }


    private void displayText(String t, int row) {
        try {
//			control.displayTextAt(row, 0, t, 0);
			control.displayText(t, 0);
        } catch (jpos.JposException e) {
            logger.warn("jpos exception PosLineDisplay, displayText : " + e.toString(), e);
        }
    }

    private int getColumns() {
        int c = 0;
        try {
            c = control.getColumns();
        } catch (jpos.JposException e) {
            logger.warn("jpos exception OperDisplay, getColumns : " + e.toString(), e);
        }

        if (c <= 0) {
            c = 40;
        }
        return c;
    }


}


