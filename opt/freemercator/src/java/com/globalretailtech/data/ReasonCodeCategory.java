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
 * Reason code categories.
 *
 * @author  Quentin Olson
 */
public class ReasonCodeCategory extends DBRecord {

    private static String table = "reason_code_category";
    private static String[] columns = {"category_id",
                                       "category_desc"};

    private static int[] col_types = {DBRecord.INT,
                                      DBRecord.STRING};

    private int categoryid;
    private String categorydesc;

    public ReasonCodeCategory() {
    }

    public int categoryID() {
        return categoryid;
    }

    public String categoryDesc() {
        return categorydesc;
    }

    public void setCategoryID(int value) {
        categoryid = value;
    }

    public void setCategoryDesc(String value) {
        categorydesc = value;
    }

    public static String getByID(int id) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[0]);
        s.append(" = ");
        s.append(Integer.toString(id));

        return new String(s.toString());
    }

    public DBRecord copy() {
        ReasonCodeCategory b = new ReasonCodeCategory();
        return b;
    }

    public void populate(ResultSet rset) {

        try {

            setCategoryID(rset.getInt("category_id"));
            setCategoryDesc(rset.getString("category_desc"));
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

    //
    // Relations
    //

    public void relations() {
    }

    /** Abstract implementation of toXML () */
    public String toXML() {
        return super.toXML(table, columnObjects(), columns, col_types);
    }

    public Vector columnObjects() {

        Vector objs = new Vector();

        objs.addElement(new Integer(categoryID()));
        objs.addElement(new String(categoryDesc()));

        return objs;
    }
}


