package com.globalretailtech.admin;

import com.globalretailtech.util.Application;
import com.globalretailtech.data.DBContext;
import com.globalretailtech.admin.settings.instance.ChangeDBSettings;
import org.apache.log4j.Logger;

import javax.swing.*;


/*
 * Copyright (C) 2003 Casey Haakenson
 * <casey@haakenson.com>
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

/**
 * This class is the main program for the configuration application.  It will provide the ability to edit all current
 * values in the DB and save the result out to XML so it can be reproduced as needed.
 */

public class ConfigMain {

    static Logger log = Logger.getLogger(ConfigMain.class);

    public static void main(String[] args) {
    	
    	initLog4j();

        Application.setDebug(true);
        Application.setLogEvents(true);
        Application.setDbConnection(new DBContext());

        if (!Application.dbConnection().init()) {
//            Log.fatal("Database initialization failure");
            JFrame frame = new JFrame("DB Connection Not Working");
            JOptionPane.showMessageDialog(frame, "Something is wrong with the DB Backend connection, please correct the settings.");
            frame.getContentPane().add(new ChangeDBSettings());
            frame.pack();
            frame.show();
            
        }

        ConfigFrame cf = new ConfigFrame();

        cf.show();
    }

	/**
	 * Configures Log4j system from System Property "log4j.configuration".
	 */
	protected static void initLog4j() {
		String log4jconf = System.getProperty("log4j.configuration");
		if ( log4jconf != null)
		org.apache.log4j.PropertyConfigurator.configure (log4jconf);
		else
			System.err.println("System property 'log4j.configuration' not found.");
	}
}
