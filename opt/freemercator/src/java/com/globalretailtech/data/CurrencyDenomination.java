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
import java.util.Locale;

import com.globalretailtech.util.Application;

/**
 * Currency denomination describes the various denominations
 * that exist for a given currency. (Use for balancing, cash management).
 *
 * @author  Quentin Olson
 */
public class CurrencyDenomination extends DBRecord {

    private static String table;
    private static String[] columns;
    private static int[] col_types;

    static {

        table = "currency_denomination";

        columns = new String[4];

        columns[0] = "currency_denom_id";
        columns[1] = "currency_id";
        columns[2] = "denom_amount";
        columns[3] = "denom_desc";

        col_types = new int[4];

        col_types[0] = DBRecord.INT;
        col_types[1] = DBRecord.INT;
        col_types[2] = DBRecord.DOUBLE;
        col_types[3] = DBRecord.STRING;
    }

    private int currencydenomid;
    private int currencyid;
    private double denomamount;
    private String denomdesc;

    public CurrencyDenomination() {
    }

    public int currencyDenomID() {
        return currencydenomid;
    }

    public int currencyID() {
        return currencyid;
    }

    public double denomAmount() {
        return denomamount;
    }

    public String denomDesc() {
        return denomdesc;
    }


    public void setCurrencyDenomID(int value) {
        currencydenomid = value;
    }

    public void setCurrencyID(int value) {
        currencyid = value;
    }

    public void setDenomAmount(double value) {
        denomamount = value;
    }

    public void setDenomDesc(String value) {
        denomdesc = value;
    }

    public static String getByCurrencyID(int id) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[1]);
        s.append(" = ");
        s.append(Integer.toString(id));

        return new String(s.toString());
    }

    public DBRecord copy() {
        CurrencyDenomination b = new CurrencyDenomination();
        return b;
    }

    public void populate(ResultSet rset) {

        try {

            setCurrencyDenomID(rset.getInt("currency_denom_id"));
            setCurrencyID(rset.getInt("currency_id"));
            setDenomAmount(rset.getDouble("denom_amount"));
            setDenomDesc(rset.getString("denom_desc"));
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

    private Locale locale;

    public Locale locale() {
        if (denomDesc().equals("USA"))
            return null;
        return null;
    }

    public void relations() {
    }

    public Vector columnObjects() {

        Vector objs = new Vector();

        objs.addElement(new Integer(currencyDenomID()));
        objs.addElement(new Integer(currencyID()));
        objs.addElement(new Double(denomAmount()));
        objs.addElement(new String(denomDesc()));

        return objs;
    }
}


