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
 * Transaction bank functions, paid-in, out...
 *
 * @author  Quentin Olson
 */

public class TransBank extends DBRecord {

    private static String table;
    private static String[] columns;
    private static int[] col_types;

    static {

        table = "trans_bank";

        columns = new String[10];

        columns[0] = "trans_id";
        columns[1] = "seq_no";
        columns[2] = "pay_type";
        columns[3] = "deposit_no";
        columns[4] = "state";
        columns[5] = "drawer_no";
        columns[6] = "bank_desc";
        columns[7] = "locale_language";
        columns[8] = "locale_variant";
        columns[9] = "amount";

        col_types = new int[10];

        col_types[0] = DBRecord.INT;
        col_types[1] = DBRecord.INT;
        col_types[2] = DBRecord.INT;
        col_types[3] = DBRecord.INT;
        col_types[4] = DBRecord.INT;
        col_types[5] = DBRecord.INT;
        col_types[6] = DBRecord.STRING;
        col_types[7] = DBRecord.STRING;
        col_types[8] = DBRecord.STRING;
        col_types[9] = DBRecord.DOUBLE;
    }

    private int transid;
    private int seqno;
    private int paytype;
    private int depositno;
    private int state;
    private int drawerno;
    private String bankdesc;
    private String localelanguage;
    private String localevariant;
    private double amount;

    public TransBank() {
    }

    public int transID() {
        return transid;
    }

    public int seqNo() {
        return seqno;
    }

    public int payType() {
        return paytype;
    }

    public int depositNo() {
        return depositno;
    }

    public int state() {
        return state;
    }

    public int drawerNo() {
        return drawerno;
    }

    public String bankDesc() {
        return bankdesc;
    }

    public String localeLanguage() {
        return localelanguage;
    }

    public String localeVariant() {
        return localevariant;
    }

    public double amount() {
        return amount;
    }

    public void setTransID(int value) {
        transid = value;
    }

    public void setSeqNo(int value) {
        seqno = value;
    }

    public void setPayType(int value) {
        paytype = value;
    }

    public void setDepositNo(int value) {
        depositno = value;
    }

    public void setState(int value) {
        state = value;
    }

    public void setDrawerNo(int value) {
        drawerno = value;
    }

    public void setBankDesc(String value) {
        bankdesc = value;
    }

    public void setLocaleLanguage(String value) {
        localelanguage = value;
    }

    public void setLocaleVariant(String value) {
        localevariant = value;
    }

    public void setAmount(double value) {
        amount = value;
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
        TransBank b = new TransBank();
        return b;
    }

    public void populate(ResultSet rset) {

        try {

            setTransID(rset.getInt("trans_id"));
            setSeqNo(rset.getInt("seq_no"));
            setPayType(rset.getInt("pay_type"));
            setDepositNo(rset.getInt("deposit_no"));
            setState(rset.getInt("state"));
            setState(rset.getInt("drawer_no"));
            setBankDesc(rset.getString("bank_desc"));
            setLocaleLanguage(rset.getString("locale_language"));
            setLocaleLanguage(rset.getString("locale_variant"));
            setAmount(rset.getDouble("amount"));
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
        tmp.append(Integer.toString(payType()));
        tmp.append(", ");
        tmp.append(Integer.toString(depositNo()));
        tmp.append(", ");
        tmp.append(Integer.toString(state()));
        tmp.append(", ");
        tmp.append(Integer.toString(drawerNo()));
        tmp.append(", '");
        tmp.append(bankDesc());
        tmp.append("', '");
        tmp.append(localeLanguage());
        tmp.append("', '");
        tmp.append(localeVariant());
        tmp.append("', ");
        tmp.append(Double.toString(amount()));
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
        tmp.append(Integer.toString(payType()));
        tmp.append(", ");
        tmp.append(Integer.toString(depositNo()));
        tmp.append(", ");
        tmp.append(Integer.toString(state()));
        tmp.append(", ");
        tmp.append(Integer.toString(drawerNo()));
        tmp.append(", '");
        tmp.append(bankDesc());
        tmp.append("', '");
        tmp.append(localeLanguage());
        tmp.append("', '");
        tmp.append(localeVariant());
        tmp.append("',");
        tmp.append(Double.toString(amount()));
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
        objs.addElement(new Integer(payType()));
        objs.addElement(new Integer(depositNo()));
        objs.addElement(new Integer(state()));
        objs.addElement(new Integer(drawerNo()));
        objs.addElement(new String(bankDesc()));
        objs.addElement(new String(localeLanguage()));
        objs.addElement(new String(localeVariant()));
        objs.addElement(new Double(amount()));

        return objs;
    }
}


