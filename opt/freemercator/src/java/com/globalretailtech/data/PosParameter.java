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
 * POS parameters are associated with a POS configuration
 * and hold arbitrary configuration parameters.
 *
 * @author  Quentin Olson
 */
public class PosParameter extends DBRecord {

    public static final int INT = 1;
    public static final int DOUBLE = 2;
    public static final int STRING = 3;
    public static final int BOOLEAN = 4;

    private static String table;
    private static String[] columns;
    private static int[] col_types;

    static {

        table = "pos_param";

        columns = new String[7];

        columns[0] = "param_id";
        columns[1] = "config_no";
        columns[2] = "param_type";
        columns[3] = "cache_flag";
        columns[4] = "help_id";
        columns[5] = "param_name";
        columns[6] = "param_value";

        col_types = new int[7];

        col_types[0] = DBRecord.INT;
        col_types[1] = DBRecord.INT;
        col_types[2] = DBRecord.INT;
        col_types[3] = DBRecord.BOOLEAN;
        col_types[4] = DBRecord.INT;
        col_types[5] = DBRecord.STRING;
        col_types[6] = DBRecord.STRING;
    }

    private int paramid;
    private int configno;
    private int paramtype;
    private boolean cacheflag;
    private int helpid;
    private String paramname;
    private String paramvalue;

    public PosParameter() {
    }

    public int paramid() {
        return paramid;
    }

    public int configNo() {
        return configno;
    }

    public int paramType() {
        return paramtype;
    }

    public boolean cacheFlag() {
        return cacheflag;
    }

    public int helpID() {
        return helpid;
    }

    public String paramName() {
        return paramname;
    }

    public String paramValue() {
        return paramvalue;
    }

    public void setParamid(int value) {
        paramid = value;
    }

    public void setConfigNo(int value) {
        configno = value;
    }

    public void setParamType(int value) {
        paramtype = value;
    }

    public void setCacheFlag(boolean value) {
        cacheflag = value;
    }

    public void setHelpID(int value) {
        helpid = value;
    }

    public void setParamName(String value) {
        paramname = value;
    }

    public void setParamValue(String value) {
        paramvalue = value;
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

    public static String getByConfigNo(int no) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[1]);
        s.append(" = ");
        s.append(Integer.toString(no));

        return new String(s.toString());
    }

    public DBRecord copy() {
        PosParameter b = new PosParameter();
        return b;
    }

    public void populate(ResultSet rset) {

        try {

            setParamid(rset.getInt("param_id"));
            setConfigNo(rset.getInt("config_no"));
            setParamType(rset.getInt("param_type"));
            setCacheFlag(rset.getInt("cache_flag") > 0);
            setHelpID(rset.getInt("help_id"));
            setParamName(rset.getString("param_name"));
            setParamValue(rset.getString("param_value"));
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

        objs.addElement(new Integer(paramid()));
        objs.addElement(new Integer(configNo()));
        objs.addElement(new Integer(paramType()));
        objs.addElement(new Boolean(cacheFlag()));
        objs.addElement(new Integer(helpID()));
        objs.addElement(paramName() == null ? "" : paramName());
        objs.addElement(paramValue() == null ? "" : paramValue());

        return objs;
    }

}


