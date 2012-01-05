package com.globalretailtech.admin.settings;

import com.globalretailtech.data.DBContext;


import java.sql.ResultSet;

import java.sql.SQLException;

import java.sql.ResultSetMetaData;

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

public class XMLSettings extends Settings {

    protected ResultSet rs;

    public XMLSettings(SettingsHandler callback) {
        super(callback);
    }


    public void setResultSet(ResultSet rs) throws SQLException {
        this.rs = rs;
        init();
    }

    public void handleEmptyTable(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();

        for (int i = 1; i <= columns; i++) {
            addField(md.getColumnName(i), null, DBContext.getClassNameForType(md.getColumnType(i)), true);
        }

        addDefaultButtonSet();
    }


    protected void init() throws SQLException {

        //convert the rs to settings fields.

        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();

        for (int i = 1; i <= columns; i++) {
            addField(md.getColumnName(i), rs.getObject(i), DBContext.getClassNameForType(md.getColumnType(i)), true);
        }

        addDefaultButtonSet();
    }
}

