package com.globalretailtech.util.xmlservices;

import com.globalretailtech.data.DBContext;

import com.globalretailtech.util.Application;

import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.ResultSetMetaData;

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

public class XMLExport {

    static Logger logger = Logger.getLogger(XMLExport.class);

    public static void exportXML(File directory){

        //loop through all tables.  convert each table to a file and save the file to
        try {
            DBContext conn = Application.dbConnection();
            for (int i = 0; i < DBContext.tableList.length; i++) {
                String tableName = DBContext.tableList[i];
                File f = new File(directory.getPath(), tableName + ".xml");
                BufferedWriter writer = new BufferedWriter(new FileWriter(f));

                writer.write(getHeader());

                writer.write("<tableinsert table=\"" + tableName + "\">\n");

                ResultSet rs = conn.executeWithResult("SELECT * from " + tableName);

                while (rs != null && rs.next()) {
                    if (rs != null){
                        writer.write("  <item>\n");
                        ResultSetMetaData md = rs.getMetaData();
                        for (int col = 1; col <= md.getColumnCount(); col++){
                            writer.write("    <col name=\"" + md.getColumnName(col) + "\">" + rs.getObject(col) + "</col>\n");
                        }
                        writer.write("  </item>\n");
                    }
                }

                writer.write("\n</tableinsert>\n\n");

                writer.write(getFooter());
                writer.flush();
                writer.close();
            }
        } catch (IOException ioe){
            logger.fatal("Exception during XML export:" + ioe, ioe);
        } catch (SQLException se){
            logger.fatal("Exception during XML export:" + se, se);
        }

    }


    public static String getHeader(){
        StringBuffer header = new StringBuffer();
        header.append("<?xml version=\"1.0\"?>\n\n");

        header.append("<mercatordata>\n");
        return header.toString();
    }

    public static String getFooter(){
        return "</mercatordata>";
    }
}
