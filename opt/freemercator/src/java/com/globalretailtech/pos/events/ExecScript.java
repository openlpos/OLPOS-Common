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

package com.globalretailtech.pos.events;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;


import com.globalretailtech.util.ShareProperties;
import org.apache.log4j.Logger;

/**
 * ExecScript executes a script found in the script directory.
 * The property file for this class has a list of possible scripts,
 * these are read into a vector with the index of which script to
 * execute provided as the value of engage (). Note: uses SCRIPTS
 * passed to JVM for path to script directory.
 *
 * @author  Quentin Olson
 */
public class ExecScript extends PosEvent {

    static Logger logger = Logger.getLogger(ExecScript.class);

    /** Simple constructor for dynamic loading. */
    public ExecScript() {
    }

    /**
     * Get the script to execute from the properties file. The
     * value passed to engage indicates which script to start, so
     * the button value must coorespond to the order of the
     * script in the property file.
     */
    public void engage(int value) {

        Vector scripts = null;
        ShareProperties p = new ShareProperties(this.getClass().getName());

        if (p.Found()) {
            String tmp = p.getProperty("Scripts");

            if (tmp != null) {
                scripts = new Vector();
                java.util.StringTokenizer st = new java.util.StringTokenizer(tmp, ",");
                while (st.hasMoreTokens()) {
                    scripts.addElement(st.nextToken());
                }
            }
        } else {
            logger.warn("Property file for " + this.getClass().getName() + " not found");
            return;
        }

        if (scripts.size() > value) { // avoid index out of range

            Process process = null;
            String scriptname = System.getProperty("SCRIPTS", ".") + "/" + (String) scripts.elementAt(value);

            try {
            	logger.debug ("exec ["+scriptname+"]");
                process = java.lang.Runtime.getRuntime().exec(scriptname);
                InputStream stderr = process.getErrorStream();
                InputStreamReader isr = new InputStreamReader (stderr);
                BufferedReader br = new BufferedReader (isr);
                String line = null;
                while ( (line = br.readLine()) != null)
                	logger.error (line);
            } catch (java.io.IOException e) {
                logger.warn("Error occured exec'ing script " + scriptname, e);
                return;
            }

            try {
                int code = process.waitFor();
                logger.debug("process exit value for ["+scriptname+"] :"+code);
            } catch (java.lang.InterruptedException e) {
                logger.warn("Script " + scriptname + " interrupted " + e.toString(), e);
            }
        }
    }

    /** Always return true. */
    public boolean validTransition(String event) {
        return true;
    }

    /** Clear impementation for clear, do nothing. */
    public void clear() {
    }

    /** Always return true. */
    public boolean checkProfile(int event) {
        return true;
    }

    private static String eventname = "ExecScript";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }
}


