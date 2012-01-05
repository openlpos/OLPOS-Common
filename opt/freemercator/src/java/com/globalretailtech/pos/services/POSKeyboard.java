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

package com.globalretailtech.pos.services;

import jpos.*;

import java.awt.*;
import java.util.*;

import com.globalretailtech.util.ShareProperties;


/**
 * Service implementation of a pos keyboard. Creates a
 * virtual keyboard using JFC.
 *
 *
 * @author  Quentin Olson
 * @see
 */
public class POSKeyboard extends BaseServiceAdapter implements jpos.services.POSKeyboardService14, jpos.loader.JposServiceInstance {

    private boolean capKeyUp;
    private boolean autoDisable;
    private boolean dataEventEnabled;
    private int dataCount;
    private int eventTypes;
    private int posKeyData;
    private int powerState;
    private int powerNotify;

    public POSKeyboard() {

        super("POSKeyboard");

        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        devPanel().setLayout(gridbag);
        c.fill = GridBagConstraints.BOTH;

        ShareProperties keyboard = new ShareProperties(propList().getProperty("KeyboardFile", ""));

        // name, value, x, y, height, width

        for (Enumeration e = keyboard.propertyNames(); e.hasMoreElements();) {

            String tmp = keyboard.getProperty((String) e.nextElement());

            StringTokenizer st = new StringTokenizer(tmp, ",");

            String name = st.nextToken();
            int val = Integer.valueOf(st.nextToken()).intValue();
            posKeyData = val;

            int x = Integer.valueOf(st.nextToken()).intValue();
            int y = Integer.valueOf(st.nextToken()).intValue();

            int bh = Integer.valueOf(st.nextToken()).intValue();
            int bw = Integer.valueOf(st.nextToken()).intValue();

            c.gridx = x;
            c.gridy = y;
            c.gridheight = bh;
            c.gridwidth = bw;
            POSKeyboardKey b = new POSKeyboardKey(this, name, val);
            gridbag.setConstraints(b, c);
            devPanel().add(b);
        }
        setVisible(true);
    }

    // 1.2 Capabilities
    public boolean getCapKeyUp() throws JposException {
        return true;
    }

    // Properties
    public boolean getAutoDisable() throws JposException {
        return autoDisable;
    }

    public void setAutoDisable(boolean a) throws JposException {
        autoDisable = a;
    }

    public int getDataCount() throws JposException {
        return dataCount;
    }

    public void setDataCount(int d) throws JposException {
        dataCount = d;
    }

    public boolean getDataEventEnabled() throws JposException {
        return dataEventEnabled;
    }

    public void setDataEventEnabled(boolean d) throws JposException {
        dataEventEnabled = d;
    }

    public int getEventTypes() throws JposException {
        return eventTypes;
    }

    public void setEventTypes(int e) throws JposException {
        eventTypes = e;
    }

    public int getPOSKeyData() throws JposException {
        return posKeyData;
    }

    public void setPOSKeyData(int d) throws JposException {
        posKeyData = d;
    }

    public int getPOSKeyEventType() throws JposException {
        return eventTypes;
    }

    // Methods
    public void clearInput() throws JposException {
    }

    // 1.3 Capabilities


    public int getCapPowerReporting() throws JposException {
        return 0;
    }

    // Properties
    public int getPowerNotify() throws JposException {
        return 0;
    }

    public void setPowerNotify(int powerNotify) throws JposException {
    }

    public int getPowerState() throws JposException {
        return 0;
    }
	/**
	 * @see jpos.loader.JposServiceInstance#deleteInstance()
	 */
	public void deleteInstance() throws JposException {
	}
}




