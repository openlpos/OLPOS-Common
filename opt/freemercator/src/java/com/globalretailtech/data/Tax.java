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
 * A single tax definition.
 *
 * @author  Quentin Olson
 */
public class Tax extends DBRecord {

    private static String table;
    private static String[] columns;
    private static int[] col_types;

    static {

        table = "tax";

        columns = new String[6];

        columns[0] = "tax_id";
        columns[1] = "tax_group_id";
        columns[2] = "tax_type";
        columns[3] = "short_desc";
        columns[4] = "rate";
        columns[5] = "alt_rate";

        col_types = new int[6];

        col_types[0] = DBRecord.INT;
        col_types[1] = DBRecord.INT;
        col_types[2] = DBRecord.INT;
        col_types[3] = DBRecord.STRING;
        col_types[4] = DBRecord.DOUBLE;
        col_types[5] = DBRecord.DOUBLE;
    }

    private int taxid;
    private int taxgroupid;
    private int taxtype;
    private String shortdesc;
    private double rate;
    private double altrate;

    public Tax() {
    }

    public int taxID() {
        return taxid;
    }

    public int taxGroupID() {
        return taxgroupid;
    }

    public int taxType() {
        return taxtype;
    }

    public String taxDesc() {
        return shortdesc;
    }

    public double taxRate() {
        return rate;
    }

    public double altTaxRate() {
        return altrate;
    }

    public void setTaxID(int value) {
        taxid = value;
    }

    public void setTaxGroupID(int value) {
        taxgroupid = value;
    }

    public void setTaxType(int value) {
        taxtype = value;
    }

    public void setDesc(String value) {
        shortdesc = value;
    }

    public void setTaxRate(double value) {
        rate = value;
    }

    public void setAltTaxRate(double value) {
        altrate = value;
    }

    // fetch specifiers

    public static String getByID(int id) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[0]);
        s.append(" = ");
        s.append(Integer.toString(id));

        return new String(s.toString());
    }

    public static String getByGroupID(int id) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[1]);
        s.append(" = ");
        s.append(Integer.toString(id));

        return new String(s.toString());
    }

    public Vector parents() {

        return null;
    }

    public DBRecord copy() {
        Tax b = new Tax();
        return b;
    }

    public void populate(ResultSet rset) {

        try {
            setTaxID(rset.getInt("tax_id"));
            setTaxGroupID(rset.getInt("tax_group_id"));
            setTaxType(rset.getInt("tax_type"));
            setDesc(rset.getString("short_desc"));
            setTaxRate(rset.getDouble("rate"));
            setAltTaxRate(rset.getDouble("alt_rate"));
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

        objs.addElement(new Integer(taxID()));
        objs.addElement(new Integer(taxGroupID()));
        objs.addElement(new Integer(taxType()));
        objs.addElement(new String(taxDesc() == null ? "" : taxDesc()));
        objs.addElement(new Double(taxRate()));
        objs.addElement(new Double(altTaxRate()));

        return objs;
    }
}


