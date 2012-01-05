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
 * Currency code maps Java locale descriptors
 * (language and varient) to currency.
 *
 * @author  Quentin Olson
 */
public class CurrencyCode extends DBRecord {

    private static String table;
    private static String[] columns;
    private static int[] col_types;

    static {

        table = "currency_code";

        columns = new String[5];

        columns[0] = "currency_code_id";
        columns[1] = "currency_code";
        columns[2] = "language";
        columns[3] = "variant";
        columns[4] = "currency_desc";

        col_types = new int[5];

        col_types[0] = DBRecord.INT;
        col_types[1] = DBRecord.INT;
        col_types[2] = DBRecord.STRING;
        col_types[3] = DBRecord.STRING;
        col_types[4] = DBRecord.STRING;
    }

    private int currencycodeid;
    private int currencycode;
    private String language;
    private String variant;
    private String currencydesc;

    public CurrencyCode() {
    }

    public int currencyCodeID() {
        return currencycodeid;
    }

    public int currrencyCode() {
        return currencycode;
    }

    public String language() {
        return language;
    }

    public String variant() {
        return variant;
    }

    public String currencyDesc() {
        return currencydesc;
    }

    public void setCurrencyCodeID(int value) {
        currencycodeid = value;
    }

    public void setCurrencyCode(int value) {
        currencycode = value;
    }

    public void setLanguage(String value) {
        language = value;
    }

    public void setVariant(String value) {
        variant = value;
    }

    public void setCurrencyDesc(String value) {
        currencydesc = value;
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
        CurrencyCode b = new CurrencyCode();
        return b;
    }

    public void populate(ResultSet rset) {

        try {

            setCurrencyCodeID(rset.getInt("currency_code_id"));
            setCurrencyCode(rset.getInt("currency_code"));
            setLanguage(rset.getString("language"));
            setVariant(rset.getString("variant"));
            setCurrencyDesc(rset.getString("currency_desc"));
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

        objs.addElement(new Integer(currencyCodeID()));
        objs.addElement(new Integer(currrencyCode()));
        objs.addElement(new String(language()));
        objs.addElement(new String(variant()));
        objs.addElement(new String(currencyDesc()));

        return objs;
    }
}


