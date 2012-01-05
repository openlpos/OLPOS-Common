package com.globalretailtech.admin.settings.instance;

import com.globalretailtech.admin.settings.Settings;
import com.globalretailtech.admin.settings.SettingsContainer;
import com.globalretailtech.admin.settings.handler.ChangeDBHandler;
import com.globalretailtech.util.Application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Hashtable;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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

public class ChangeDBSettings extends Settings {

    static Logger logger = Logger.getLogger(ChangeDBSettings.class);

    protected static String[] validDrivers = new String[]{"org.postgresql.Driver", "interbase.interclient.Driver", "org.hsqldb.jdbcDriver"};

    int index = 0;

    public ChangeDBSettings() {
        super(new ChangeDBHandler());

        String currentDriver = Application.dbConnection().getDriver();

        for (int i = 0; i < validDrivers.length; i++) {
            String validDriver = validDrivers[i];
            if (validDriver.equals(currentDriver)){
                index = i;
            }
        }
        init();
    }

    public ChangeDBSettings(int index) {
        super(new ChangeDBHandler());
        this.index = index;
        init();
    }

    protected void init(){
        final JComboBox driver = super.addSelect("Driver", validDrivers, index);

        driver.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SettingsContainer.show(new ChangeDBSettings(driver.getSelectedIndex()));
            }
        });

        try {
            BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("SHARE") + "/com.globalretailtech.data.DBContext"));

            String line = reader.readLine();

            Hashtable values = new Hashtable();
            boolean start = false;
            while (line != null){

                if (start){
                    String nameVal = line;
                    if (line.indexOf("#") == 0){
                        nameVal = line.substring(1, line.length());
                    }
                    if (nameVal.indexOf("=") > -1){
                        String name = nameVal.substring(0, nameVal.indexOf("="));
                        String value = nameVal.substring(nameVal.indexOf("=") + 1, nameVal.length());
                        addField(name, value, "java.lang.String", true);
                    } else {
                        logger.info("Line that's not a name/value pair:" + line);
                    }
                }
                if (line.indexOf("#END#" + validDrivers[index]) > -1){
                    start = false;
                }
                if (line.indexOf("#BEGIN#" + validDrivers[index]) > -1){
                    start = true;
                }
                line = reader.readLine();
            }

        } catch (Exception e) {
            logger.error("Error trying to read properties file for DBContext", e);
        }
        addDefaultButtonSet();
    }


}
