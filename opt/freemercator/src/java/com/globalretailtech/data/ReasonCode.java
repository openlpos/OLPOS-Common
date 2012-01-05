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
 * Reason codes.
 *
 * @author  Quentin Olson
 */
public class ReasonCode extends DBRecord {

    private static String table;
    private static String[] columns;
    private static int[] col_types;

    static {

        table = "reason_code";

        columns = new String[4];

        columns[0] = "reason_code_id";
        columns[1] = "category_id";
        columns[2] = "reason_code";
        columns[3] = "code_desc";

        col_types = new int[4];

        col_types[0] = DBRecord.INT;
        col_types[1] = DBRecord.INT;
        col_types[2] = DBRecord.INT;
        col_types[3] = DBRecord.STRING;
    }

    private int reasoncodeid;
    private int categoryid;
    private int reasoncode;
    private String codedesc;

    public ReasonCode() {
    }

    public int reasonCodeID() {
        return reasoncodeid;
    }

    public int categoryID() {
        return categoryid;
    }

    public int reasonCode() {
        return reasoncode;
    }

    public String codeDesc() {
        return codedesc;
    }

    public void setReasonCodeID(int value) {
        reasoncodeid = value;
    }

    public void setCategoryID(int value) {
        categoryid = value;
    }

    public void setReasonCode(int value) {
        reasoncode = value;
    }

    public void setCodeDesc(String value) {
        codedesc = value;
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
        ReasonCode b = new ReasonCode();
        return b;
    }

    public void populate(ResultSet rset) {

        try {

            setReasonCodeID(rset.getInt("reason_code_id"));
            setCategoryID(rset.getInt("category_id"));
            setReasonCode(rset.getInt("reason_code"));
            setCodeDesc(rset.getString("code_desc"));
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

        objs.addElement(new Integer(reasonCodeID()));
        objs.addElement(new Integer(categoryID()));
        objs.addElement(new Integer(reasonCode()));
        objs.addElement(new String(codeDesc()));

        return objs;
    }
}


