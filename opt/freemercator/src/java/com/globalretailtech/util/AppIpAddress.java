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

package com.globalretailtech.util;

import org.apache.log4j.Logger;

import java.net.InetAddress;
import java.net.Socket;

/**
 * Usefull class for holding Application address, IP and Port.
 *
 * @author  Quentin Olson
 * @see java.net.InetAddress
 */
public class AppIpAddress {

    static Logger logger = Logger.getLogger(AppIpAddress.class);

    private InetAddress addr;
    private String host;
    private int port;
    private boolean isvalid;

    /** Is this a valid address, i.e. can we derive
     * the host name.
     */
    public boolean isValid() {
        return isvalid;
    }


    /** Simple constructor */
    public AppIpAddress() {
    }

    /** Constructor with host name and ip port number,
     * attempts to look up the host name and create the
     * java.net.InetAddres object
     */
    public AppIpAddress(String h, int p) {

        host = h;
        port = p;
        isvalid = true;

        try {
            addr = InetAddress.getByName(host);
        } catch (java.net.UnknownHostException e) {
            isvalid = false;
            logger.warn("Unknown host exception " + host, e);
        }
    }

    /**
     * See if a connection can be made to
     * the host and port, no data is sent,
     * connection is closed.
     */
    public boolean ping() {

        Socket s;
        try {
            s = new Socket(addr, port);
        } catch (java.io.IOException e) {
            return false;
        }

        try {
            s.close();
        } catch (java.io.IOException e) {
        }

        return true;
    }

    /** Returns host:port */
    public String toString() {
        return host + ":" + port;
    }
}



