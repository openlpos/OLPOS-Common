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

import org.apache.log4j.Logger;


/**
 * Super class for client side jpos devices.
 *
 * @author  Quentin Olson
 * @see
 */
public abstract class BaseDevice {

    static Logger logger = Logger.getLogger(BaseDevice.class);

    /** Jpos control */
    protected jpos.BaseControl control;
    private boolean isopen;

    /** Accessor for the jpos control */
    public jpos.BaseControl control() {
        return control;
    }

    public boolean isOpen() {
        return isopen;
    }

    /**
     * Constructor takes a service control and a device
     * name and tries to open it.
     */
    public BaseDevice(jpos.BaseControl c, String devicename) {

        isopen = false;
        control = c;
        try {
            control.open(devicename);
            isopen = true;
        } catch (jpos.JposException e) {
            logger.warn("Control not openened : " + devicename);
        }
    }
}


