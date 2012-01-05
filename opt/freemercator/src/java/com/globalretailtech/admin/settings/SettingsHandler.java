package com.globalretailtech.admin.settings;

import com.globalretailtech.util.Application;

import java.util.Hashtable;
import java.util.Enumeration;
import java.sql.SQLException;
import java.sql.ResultSet;

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

public abstract class SettingsHandler {
    static Logger logger = Logger.getLogger(SettingsHandler.class);

    abstract public void commit(Hashtable fields);

    abstract public void cancel();

    public static void updateTable(Hashtable fields, String tableName, String unique_id_name, Object unique_value){
        StringBuffer sql = new StringBuffer();

        sql.append("UPDATE " + tableName + " set ");

        Enumeration e = fields.keys();

        while(e.hasMoreElements()){
            Field f = (Field)fields.get(e.nextElement());
            if (f.getClassName().equals("java.lang.String")){
                if (f.getStringValue().equals("null")){
                    sql.append(f.getName() + " = ''");
                } else {
                    sql.append(f.getName() + " = '" + f.getStringValue() + "'");
                }
            } else {
                sql.append(f.getName() + " = " + f.getStringValue());
            }
            if (e.hasMoreElements()){
                sql.append(",");
            }
        }

        if (unique_value instanceof String){
            sql.append(" WHERE " + unique_id_name + " = '" + unique_value + "'");
        } else {
            sql.append(" WHERE " + unique_id_name + " = " + unique_value);
        }
        try {
            Application.dbConnection().execute(sql.toString());
        } catch (SQLException e2){
            logger.fatal("Caught this trying to commit a user:" + e2, e2);
        }
    }

    public int addTable(Hashtable fields, String tableName, String unique_id_name){
        StringBuffer sql = new StringBuffer();
        int newID = -1;

        sql.append("INSERT into " + tableName + " (");

        Enumeration e = fields.keys();

        while(e.hasMoreElements()){
            sql.append(e.nextElement() + " ");

            if (e.hasMoreElements()){
                sql.append(",");
            }
        }

        e = fields.keys();

        sql.append(") values (");

        while(e.hasMoreElements()){
            Field f = (Field)fields.get(e.nextElement());
            if (f.getClassName().equals("java.lang.String")){
                if (f.getStringValue().equals("null")){
                    sql.append("''");

                } else {
                    sql.append(" '" + f.getStringValue() + "'");
                }
            } else {
                sql.append(f.getStringValue());
            }
            if (e.hasMoreElements()){
                sql.append(",");
            }
        }

        sql.append(")");

        try {
            Application.dbConnection().execute(sql.toString());

            ResultSet rs = Application.dbConnection().executeWithResult("select max (" + unique_id_name + ") from " +tableName);
            if (rs != null && rs.next()){
                newID = rs.getInt(1);
            }
        } catch (SQLException e2){
            logger.fatal("Caught this trying to commit a user:" + e2, e2);
        }
        return newID;
    }

}
