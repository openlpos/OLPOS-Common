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
import com.globalretailtech.pos.context.*;
import org.apache.log4j.Logger;

/**
 * CashDrawer class.
 *
 *
 * @author  Igor Semenko
 * @see
 */
public class CashDrawer extends BaseDevice {

    static Logger logger = Logger.getLogger(CashDrawer.class);

    private jpos.CashDrawer control;

    public jpos.BaseControl control() {
        return control;
    }

    public CashDrawer(jpos.CashDrawer c, String devicename) {
        super(c, devicename);
        control = c;
		try {
			c.claim(1000);
			c.setDeviceEnabled(true);
		} catch (JposException e) {
			logger.error ("Can't claim CashDrawer");
		}
    }

    public jpos.CashDrawer getControl() {
        return (jpos.CashDrawer) control();
    }

    public void init(PosParameters params, int h, int w) {
    }

    public void openDrawer() {
        try {
        	if (control != null && isOpen())
				control.openDrawer();
		} catch (JposException e) {
			logger.error("Can't open drawer", e);
		}
    }

}


