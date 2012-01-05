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

import jpos.JposException;

import com.globalretailtech.pos.context.*;
import com.globalretailtech.pos.operators.*;
import org.apache.log4j.Logger;

/**
 * Scanner implementation. Attaches to the jpos service
 * passed in the constructor. When input arrivives it
 * is added (appended) to the input buffer associated
 * with the current context.
 *
 *
 * @author  Quentin Olson
 * @see
 */
public class Scanner extends BaseDevice {

    static Logger logger = Logger.getLogger(Scanner.class);

    private jpos.Scanner control;
    private ContextSet contextSet;
    private byte[] scanData;
    private int scanDataType;

    Vector filters;

    public Scanner(jpos.Scanner c, String devicename, ContextSet set) {

        super(c, devicename);
        control = c;
        contextSet = set;

        c.addDataListener(new com.globalretailtech.pos.services.events.DataListenerAdapter() {
            public void dataOccurred(jpos.events.DataEvent e) {

                try {
                    scanData = control.getScanData();
                    scanDataType = control.getScanDataType();
                    //logger.debug("data: "+control.getScanDataLabel());
                } catch (jpos.JposException jpe) {
					logger.error ("",jpe);
                }
                process();

            }
        }
        );
		try {
			c.claim(1000);
			c.setDeviceEnabled(true);
			c.setDataEventEnabled(true);
			c.setDecodeData(true);
		} catch (JposException e) {
		    logger.error ("Can't claim/enable Scanner");
		}
    }

    public void process() {

        PosContext context = contextSet.currentContext();

        String data = new String(scanData);
		logger.debug ("["+data+"]"+data.length());
        if (context != null) {

            for (int i = 0; i < context.scannerFilters().size(); i++) {

                // Get the filter and apply it

                Filter f = context.scannerFilters().getFilter(i);
				f.apply (context, data);
				
            }
        } else {
            logger.warn("Invalid context set in Scanner!!!");
        }
    }
	/**
	 * Returns scanned data
	 * @return
	 */
	public byte[] getScanData() {
		return scanData;
	}
	
	public void clearInput() throws JposException{
		if (control != null && isOpen())
			control.clearInput();
	}
	
	public void release () throws JposException{
		if (control != null && isOpen())
			control.release();
	}

	public void close () throws JposException{
		if (control != null && isOpen())
			control.close();
	}

}


