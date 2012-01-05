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
 * Department record.
 *
 * @author  Quentin Olson
 */
public class Department extends DBRecord implements java.io.Serializable {

    // pricing options

    private static String table;
    private static String[] columns;
    private static int[] col_types;

    static {

        table = "department";

        columns = new String[11];

        columns[0] = "department_id";
        columns[1] = "department_no";
        columns[2] = "config_no";
        columns[3] = "name";

        col_types = new int[11];

        col_types[0] = DBRecord.INT;
        col_types[1] = DBRecord.INT;
        col_types[1] = DBRecord.INT;
        col_types[2] = DBRecord.STRING;
    }

    private int departmentid;
    private int departmentno;
    private int configno;
    private String name;

    private static StringBuffer scratch;

    public Department() {
        if (scratch == null) {
            scratch = new StringBuffer();
        }
    }

    public int departmentID() {
        return departmentid;
    }

    public int departmentNo() {
        return departmentno;
    }

    public int configID() {
        return configno;
    }

    public String name() {
        return name;
    }


    public void setDepartmentID(int value) {
        departmentid = value;
    }

    public void setDepartmentNo(int value) {
        departmentno = value;
    }

    public void setConfigNo(int value) {
        configno = value;
    }

    public void setName(String value) {
        name = value;
    }

    // selectors

    public static String getByID(int id) {

        if (scratch == null) {
            scratch = new StringBuffer();
        }
        if (scratch.length() > 0)
            scratch.delete(0, scratch.length());
        scratch.append("select * from ");
        scratch.append(table);
        scratch.append(" where ");
        scratch.append(columns[0]);
        scratch.append(" = ");
        scratch.append(Integer.toString(id));

        return new String(scratch.toString());
    }

    public static String getByDeptNo(int no) {

        if (scratch == null) {
            scratch = new StringBuffer();
        }
        if (scratch.length() > 0)
            scratch.delete(0, scratch.length());

        scratch.append("select * from ");
        scratch.append(table);
        scratch.append(" where ");
        scratch.append(columns[1]);
        scratch.append(" = ");
        scratch.append(no);

        return new String(scratch.toString());
    }

    /**
     * Get all departments by configuration number ==
     * the provided number or number == 0
     */
    public static String getAllByConfigNo(int no) {

        if (scratch == null) {
            scratch = new StringBuffer();
        }
        if (scratch.length() > 0)
            scratch.delete(0, scratch.length());

        scratch.append("select * from ");
        scratch.append(table);
        scratch.append(" where ");
        scratch.append(columns[2]);
        scratch.append(" = ");
        scratch.append(no);
        scratch.append(" or ");
        scratch.append(columns[2]);
        scratch.append(" = 0");

        return new String(scratch.toString());
    }

    public DBRecord copy() {
        Department b = new Department();
        return b;
    }


    public void populate(ResultSet rset) {

        try {

            setDepartmentID(rset.getInt("department_id"));
            setDepartmentNo(rset.getInt("department_no"));
            setConfigNo(rset.getInt("config_no"));
            setName(rset.getString("name"));
        } catch (java.sql.SQLException e) {
            Application.dbConnection().setException(e);
        }
    }

    public boolean save() {
        return true;
    }

    public boolean update() {

        StringBuffer tmp = new StringBuffer();
        return true;
    }

    public String toString() {
        return toXML();
    }

    public String toXML() {
        return super.toXML(table, columnObjects(), columns, col_types);
    }

    //
    // Relations
    //

    public void relations() {
    }

    public Vector columnObjects() {

        Vector objs = new Vector();

        objs.addElement(new Integer(departmentID()));
        objs.addElement(new Integer(departmentNo()));
        objs.addElement(new Integer(configID()));
        objs.addElement(new String(name()));

        return objs;
    }
}


