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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
import java.util.Locale;

import com.globalretailtech.data.DBContext;

/**
 * An POS Application super class. Manages a single
 * database connection, debug mode, replay mode,
 * the local (language, variant).
 *
 * @author  Quentin Olson
 */
public abstract class Application {
	org.apache.log4j.Logger logger =
		org.apache.log4j.Logger.getLogger(Application.class.getName());

    public static String VERSION = "@version@";

    private static int status;
    private static int loglevel;
    private static boolean debug = true;
    private static boolean logevents;
    private static boolean editmode = false;
    private static DBContext db;
    private static Vector remotedb;
    private static String localelanguage;
    private static String localevariant;
    private static Locale locale;

    /** Status of the applicatiaon */
    public static int status() {
        return status;
    }

    /** Application debug leve */
    public static boolean debug() {
        return debug;
    }

    /** Application logging level */
    public static int logLevel() {
        return loglevel;
    }

    /** Whether events are logged for replay */
    public static boolean logEvents() {
        return logevents;
    }

    /** The Application database connection */
    public static DBContext dbConnection() {
        return db;
    }

    /** Remote databases for this applicaion */
    public static Vector remoteDB() {
        return remotedb;
    }

    /** Language */
    public static String localeLanguage() {
        return localelanguage;
    }

    /** Language variant */
    public static String localeVariant() {
        return localevariant;
    }

    /** Locale for this applicatoin */
    public static Locale locale() {
        return locale;
    }

    /** Sets the status */
    public static void setStatus(int value) {
        status = value;
    }

    /** Sets debug mode */
    public static void setDebug(boolean value) {
        debug = value;
    }

    /** Sets Log events */
    public static void setLogEvents(boolean value) {
        logevents = value;
    }

    /** Sets Log level */
    public static void setLogLevel(int value) {
        loglevel = value;
    }

    /** Sets the database connection object */
    public static void setDbConnection(DBContext value) {
        db = value;
    }

    /** Sets the remote database list */
    public static void setRemoteDB(Vector value) {
        remotedb = value;
    }

    /** Sets the language */
    public static void setLocaleLanguage(String value) {
        localelanguage = value;
    }

    /** Sets the language variant */
    public static void setLocaleVariant(String value) {
        localevariant = value;
    }

    /** Sets the local */
    public static void setLocale(Locale value) {
        locale = value;
    }

    public static void setEditMode(boolean editmode) {
        Application.editmode = editmode;
    }

    /** Edit mode indicator. */
    public static boolean isEditMode() {
        return editmode;
    }  /** Edit mode flag. */

    /** How a poss Application starts */
    public abstract void start(String[] args);

    /** Constructor, reads propertis and sets a default local,
     * then attempts to get the local from the properties
     */
    public Application() {
        ShareProperties p = new ShareProperties(this.getClass().getName());

        setLocaleLanguage("US");
        setLocaleVariant("en");

        if (p.Found()) {
            setLocaleLanguage(p.getProperty("Language", "US"));
            setLocaleVariant(p.getProperty("Variant", "en"));
        }
        initLocale(localeLanguage(), localeVariant());
    }

    /** Initialize the local from language and language variant */
    public void initLocale(String language, String variant) {
        setLocale(new Locale(localeLanguage(), localeVariant()));
    }

	/** Returns main window title*/
	protected String getTitle() {
		return "FreeMercator v." + Application.VERSION;
	}
	/**
	 * Configures Log4j system from System Property "log4j.configuration".
	 */
	protected void initLog4j() {
		String log4jconf = System.getProperty("log4j.configuration");
		if ( log4jconf != null)
			org.apache.log4j.PropertyConfigurator.configureAndWatch(log4jconf);
		else
			System.err.println("System property 'log4j.configuration' not found.");
	}


	/**
	 * Starts daemons. Daemons are classes implementing Runnable and performing
	 * some task, daemon descriptions are stored in SHARE/daemons.properties
	 */
	protected void startDaemons() {
		String shareDir = System.getProperty("SHARE");
		File daemonsProperties = new File (shareDir, "daemons");
		if ( daemonsProperties.exists()){
			// start daemons
			String ln = null;
			try {
				BufferedReader in = new BufferedReader (new FileReader (daemonsProperties));
				while ( ( ln = in.readLine() ) != null ){
					if ( ! ln.trim().startsWith("#") && ln.trim().length() > 0){
						logger.debug ("Starting "+ln+"...");
						Thread daemonThread = new Thread ( 
							(Runnable)Class.forName(ln.trim()).newInstance());
						daemonThread.setName(ln);	
						daemonThread.start();	
					}
				}
			} catch (FileNotFoundException e) {
				logger.error("Can't find file", e);
			} catch (IOException e) {
				logger.error("Can't perfom io", e);
			} catch (Exception e) {
				logger.error("Can't instantiate class ["+ln+"]", e);
			}
		}
	}

	/** Reports environment properties to the log */
	protected void dumpEnvironment() {
		logger.debug ("java.home: "+System.getProperty("java.home"));
		logger.debug ("java.vendor: "+System.getProperty("java.vendor"));
		logger.debug ("java.version: "+System.getProperty("java.version"));
		logger.debug ("os.arch: "+System.getProperty("os.arch"));
		logger.debug ("os.name: "+System.getProperty("os.name"));
		logger.debug ("os.version: "+System.getProperty("os.version"));
		logger.debug ("java.class.path: "+System.getProperty("java.class.path"));
		logger.debug ("SHARE: "+System.getProperty("SHARE"));
		logger.debug ("version $Id: Application.java,v 1.12 2003/07/10 06:22:38 isemenko Exp $");
	}

}


