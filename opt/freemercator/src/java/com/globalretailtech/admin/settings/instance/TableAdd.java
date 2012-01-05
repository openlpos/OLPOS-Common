package com.globalretailtech.admin.settings.instance;

import com.globalretailtech.admin.settings.XMLSettings;
import com.globalretailtech.admin.settings.Field;

import com.globalretailtech.admin.settings.handler.TableAddHandler;

import com.globalretailtech.admin.nav.instance.TableFolder;

import com.globalretailtech.util.Application;

import org.apache.log4j.Logger;


import javax.swing.*;
import java.sql.ResultSet;

import java.sql.SQLException;

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

public class TableAdd extends XMLSettings {

    static Logger logger = Logger.getLogger(TableAdd.class);

    TableFolder folder;

    public TableAdd(TableFolder folder) {

        super(new TableAddHandler(folder));
        this.folder = folder;
        ignoreField(folder.unique_id_name);
        allValuesEmpty(true);

        //don't care about records, just the result set

        ResultSet rs = Application.dbConnection().executeWithResult("SELECT * from " + folder.tableName);

        try {
            rs.next();
            setResultSet(rs);
        } catch (SQLException se) {
            if (se.getMessage().equalsIgnoreCase("no data is available")) {
                logger.warn("Empty table, attempting to add initial settings");
                try {
                    handleEmptyTable(rs);
                } catch (SQLException e) {
                    logger.warn("Exception: " + e + "when attempting to add settings to empty table");
                }
            } else {
                logger.warn("Caught this trying to create Add Settings:" + se, se);
            }
        }
    }

    public void duplicate(int id){
        try {
            ResultSet rs = Application.dbConnection().executeWithResult("select * from " + folder.tableName + " where " + folder.unique_id_name + " = " + id);
            rs.next();
            int columns = rs.getMetaData().getColumnCount();
            for (int i = 1; i <= columns; i++){
                Field f = getField(rs.getMetaData().getColumnName(i));
                if (rs.getObject(i) != null && f != null){
                    ((JTextField)f.getTextField()).setText(rs.getObject(i).toString());
                }
            }
        } catch (SQLException se){
            logger.warn("Could not duplicate object:" + se, se);
        }
    }
}

