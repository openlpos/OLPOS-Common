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

package com.globalretailtech.data;

import java.sql.*;
import java.util.Vector;

import com.globalretailtech.util.Application;

/**
 * Help holds information and help text for arbitray
 * objects. It is used for assigning GUI display names
 * in user maintenance interfaces, and help text for tool
 * tips.
 *
 * @author  Quentin Olson
 */
public class Help extends DBRecord {

    private static String table;
    private static String[] columns;
    private static int[] col_types;

    static {

        table = "help";

        columns = new String[3];

        columns[0] = "help_id";
        columns[1] = "display_name";
        columns[2] = "isplay_text";

        col_types = new int[3];

        col_types[0] = DBRecord.INT;
        col_types[1] = DBRecord.STRING;
        col_types[2] = DBRecord.STRING;
    }

    private int helpid;
    private String displayname;
    private String displaytext;

    public Help() {
    }

    public int helpid() {
        return helpid;
    }

    public String displayName() {
        return displayname;
    }

    public String displayText() {
        return displaytext;
    }

    public void setHelpID(int value) {
        helpid = value;
    }

    public void setDisplayName(String value) {
        displayname = value;
    }

    public void setDisplayText(String value) {
        displaytext = value;
    }



    // fetch specs

    public static String getByID(int id) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[0]);
        s.append(" = ");
        s.append(Integer.toString(id));

        return new String(s.toString());
    }

    public Vector parents() {
        return null;
    }

    public DBRecord copy() {
        Help b = new Help();
        return b;
    }

    public void populate(ResultSet rset) {

        try {
            setHelpID(rset.getInt("help_id"));
            setDisplayName(rset.getString("display_name"));
            setDisplayText(rset.getString("display_text"));
        } catch (java.sql.SQLException e) {
            Application.dbConnection().setException(e);
        }
    }

    public boolean save() {
        return true;
    }

    public boolean update() {
        return true;
    }

    public String toString() {
        return toXML();
    }

    // relations

    public void relations() {
    }

    /** Abstract implementation of toXML () */
    public String toXML() {
        return super.toXML(table, columnObjects(), columns, col_types);
    }

    public Vector columnObjects() {

        Vector objs = new Vector();

        objs.addElement(new Integer(helpid()));
        objs.addElement(new String(displayName()));
        objs.addElement(new String(displayText()));

        return objs;
    }
}


