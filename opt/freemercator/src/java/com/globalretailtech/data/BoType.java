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
 * Contains the businss object type.
 *
 *
 * @author  Quentin Olson
 */
public class BoType extends DBRecord {

    private static String table;
    private static String[] columns;
    private static int[] col_types;

    static {

        table = "bo_type";

        columns = new String[3];

        columns[0] = "obj_id";
        columns[1] = "bu_id";
        columns[2] = "obj_desc";

        col_types = new int[4];

        col_types[0] = DBRecord.INT;
        col_types[1] = DBRecord.INT;
        col_types[2] = DBRecord.STRING;
    }

    private int objid;
    private int buid;
    private String objdesc;

    public BoType() {
    }

    public int objID() {
        return objid;
    }

    public int buID() {
        return buid;
    }

    public String objDesc() {
        return objdesc;
    }

    public void setObjID(int value) {
        objid = value;
    }

    public void setBuID(int value) {
        buid = value;
    }

    public void setObjDesc(String value) {
        objdesc = value;
    }

    public static String getByID(int n) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[0]);
        s.append(" = ");
        s.append(n);

        return new String(s.toString());
    }

    public static String getByIDandType(int id, int type) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[0]);
        s.append(" = ");
        s.append(id);
        s.append(" and ");
        s.append(columns[1]);
        s.append(" = ");
        s.append(type);

        return new String(s.toString());
    }

    public DBRecord copy() {
        BoType b = new BoType();
        return b;
    }

    public void populate(ResultSet rset) {

        try {
            setObjID(rset.getInt("obj_id"));
            setBuID(rset.getInt("bu_id"));
            setObjDesc(rset.getString("obj_desc"));
        } catch (SQLException e) {
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

    public void relations() {
    }

    public String toXML() {
        return super.toXML(table, columnObjects(), columns, col_types);
    }

    public Vector columnObjects() {

        Vector objs = new Vector();

        objs.addElement(new Integer(objID()));
        objs.addElement(new Integer(buID()));
        objs.addElement(new String(objDesc()));

        return objs;
    }
}


