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
 * Transaction tax records.
 *
 *
 * @author  Quentin Olson
 * @see Tax
 */
public class TransTax extends DBRecord {

    private static String table;
    private static String[] columns;
    private static int[] col_types;

    static {

        table = "trans_tax";

        columns = new String[7];

        columns[0] = "trans_id";
        columns[1] = "seq_no";
        columns[2] = "tax_id";
        columns[3] = "tax_desc";
        columns[4] = "rate";
        columns[5] = "taxable_amount";
        columns[6] = "tax_tamount";

        col_types = new int[7];

        col_types[0] = DBRecord.INT;
        col_types[1] = DBRecord.INT;
        col_types[2] = DBRecord.INT;
        col_types[3] = DBRecord.STRING;
        col_types[4] = DBRecord.DOUBLE;
        col_types[5] = DBRecord.DOUBLE;
        col_types[6] = DBRecord.DOUBLE;
    }

    private int transid;
    private int seqno;
    private int taxid;
    private String taxdesc;
    private double rate;
    private double taxable;
    private double taxamount;

    public TransTax() {
    }

    public int transID() {
        return transid;
    }

    public int seqNo() {
        return seqno;
    }

    public int taxID() {
        return taxid;
    }

    public String taxDesc() {
        return taxdesc;
    }

    public double taxRate() {
        return rate;
    }

    public double taxable() {
        return taxable;
    }

    public double taxAmount() {
        return taxamount;
    }

    public void setTransID(int value) {
        transid = value;
    }

    public void setSeqNo(int value) {
        seqno = value;
    }

    public void setTaxID(int value) {
        taxid = value;
    }

    public void setDesc(String value) {
        taxdesc = value;
    }

    public void setRate(double value) {
        rate = value;
    }

    public void setTaxable(double value) {
        taxable = value;
    }

    public void setTaxAmount(double value) {
        taxamount = value;
    }

    // relations

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
        TransTax b = new TransTax();
        return b;
    }

    public void populate(ResultSet rset) {

        try {
            setTransID(rset.getInt("trans_id"));
            setSeqNo(rset.getInt("seq_no"));
            setTaxID(rset.getInt("tax_id"));
            setDesc(rset.getString("tax_desc"));
            setRate(rset.getDouble("rate"));
            setTaxable(rset.getDouble("taxable_amount"));
            setTaxAmount(rset.getDouble("tax_amount"));
        } catch (java.sql.SQLException e) {
            Application.dbConnection().setException(e);
        }
    }

    public boolean save() {

        StringBuffer tmp = new StringBuffer();

        tmp.append("insert into ");
        tmp.append(table);
        tmp.append(" values (");
        tmp.append(Integer.toString(transID()));
        tmp.append(", ");
        tmp.append(Integer.toString(seqNo()));
        tmp.append(", ");
        tmp.append(Integer.toString(taxID()));
        tmp.append(", '");
        tmp.append(taxDesc());
        tmp.append("', ");
        tmp.append(Double.toString(taxRate()));
        tmp.append(", ");
        tmp.append(Double.toString(taxable()));
        tmp.append(", ");
        tmp.append(Double.toString(taxAmount()));
        tmp.append(")");

        try {
            Application.dbConnection().execute(tmp.toString());
        } catch (SQLException e) {
            Application.dbConnection().setException(e);
            return false;
        }
        return true;
    }

    public boolean save(DBContext db) {

        StringBuffer tmp = new StringBuffer();

        tmp.append("insert into ");
        tmp.append(table);
        tmp.append(" values (");
        tmp.append(Integer.toString(transID()));
        tmp.append(", ");
        tmp.append(Integer.toString(seqNo()));
        tmp.append(", ");
        tmp.append(Integer.toString(taxID()));
        tmp.append(", '");
        tmp.append(taxDesc());
        tmp.append("', ");
        tmp.append(Double.toString(taxRate()));
        tmp.append(", ");
        tmp.append(Double.toString(taxable()));
        tmp.append(", ");
        tmp.append(Double.toString(taxAmount()));
        tmp.append(")");

        try {
            db.execute(tmp.toString());
        } catch (SQLException e) {
            db.setException(e);
            return false;
        }
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

        objs.addElement(new Integer(transID()));
        objs.addElement(new Integer(seqNo()));
        objs.addElement(new Integer(taxID()));
        objs.addElement(new String(taxDesc()));
        objs.addElement(new Double(taxRate()));
        objs.addElement(new Double(taxable()));
        objs.addElement(new Double(taxAmount()));

        return objs;
    }
}


