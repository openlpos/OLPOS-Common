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

import java.io.*;
import java.util.Properties;

/**
 * Slightly simplified properties interfacce. Always
 * looks in a directory prefixed by the SHARE env variable.
 *
 *
 * @author  Quentin Olson
 */
public class ShareProperties extends Properties {

	org.apache.log4j.Logger logger =
		org.apache.log4j.Logger.getLogger(ShareProperties.class);
		
    private FileInputStream fs;
    private boolean found;

    public boolean Found() {
        return fs != null;
    }

    public ShareProperties(String fname) {

        super();

        String sharePath = System.getProperty("SHARE", ".");
        String propFile = sharePath + "/" + fname;
        try {
            fs = new FileInputStream(propFile);
        } catch (FileNotFoundException e) {
            fs = null;
            return;
        }

        Properties prop = new Properties();
        try {
            load(fs);
        } catch (IOException e) {
            logger.error("",e);
        }
    }

    public void save(String name) {
    }
}



