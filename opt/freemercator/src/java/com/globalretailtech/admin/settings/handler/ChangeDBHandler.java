package com.globalretailtech.admin.settings.handler;

import com.globalretailtech.admin.settings.SettingsHandler;
import com.globalretailtech.admin.settings.Field;
import com.globalretailtech.admin.ConfigFrame;
import com.globalretailtech.util.Application;
import com.globalretailtech.data.DBContext;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;

import org.apache.log4j.Logger;

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

public class ChangeDBHandler extends SettingsHandler{

    static Logger logger = Logger.getLogger(ChangeDBHandler.class);

    public void commit(Hashtable fields) {

        try {
            BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("SHARE") + "/com.globalretailtech.data.DBContext"));

            String line = reader.readLine();

            Vector originalFile = new Vector();

            while(line != null){
                originalFile.add(line);
                line = reader.readLine();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(System.getProperty("SHARE") + "/com.globalretailtech.data.DBContext"));

            boolean output = true;

            for (int i = 0; i < originalFile.size(); i++) {
                String s = (String) originalFile.elementAt(i);

                if (output){
                    if (s.indexOf("#") == 0){
                        writer.write(s + "\n");
                    } else {
                        writer.write("#" + s + "\n");
                    }
                }
                if (s.indexOf("#BEGIN#" + ((Field)fields.get("driver")).getStringValue()) > -1){
                    output = false;
                    //print out all fields
                    Enumeration e = fields.keys();
                    while (e.hasMoreElements()){
                        String key = (String)e.nextElement();
                        String name = ((Field)fields.get(key)).getName();
                        String value = ((Field)fields.get(key)).getStringValue();
                        if (value.equals("null")) value = "";
                        writer.write(name + "=" + value + "\n");
                    }
                }

                if (s.indexOf("#END#" + ((Field)fields.get("driver")).getStringValue()) > -1){
                    if (output == false){
                        if (s.indexOf("#") == 0){
                            writer.write(s + "\n");
                        } else {
                            writer.write("#" + s + "\n");
                        }
                    }
                    output = true;
                }
            }
            writer.close();
            Application.setDbConnection(new DBContext());

            if (!Application.dbConnection().init()){
                logger.error("Could not init new DB connection.  Try restarting Admin Utility.");
            }

            ConfigFrame.getConfigFrame().reloadTree();

        } catch (Exception e){
            logger.error("Caught this trying to write out DBContext file");
        }

    }

    public void cancel() {
    }

}
