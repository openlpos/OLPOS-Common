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
 * Tender detail record.
 *
 * @author  Quentin Olson
 */
public class TransTender extends DBRecord {

    public static final int CASH = 0;
    public static final int CREDIT_CARD = 1;
    public static final int DEBIT = 2;
    public static final int GIFT = 3;
    public static final int ALT_CURRENCY = 4;
    public static final int CHECK = 5;
	public static final int CREDIT = 6;
    

    private static String table;
    private static String[] columns;
    private static int[] col_types;

    static {

        table = "trans_tender";

        columns = new String[10];

        columns[0] = "trans_id";
        columns[1] = "seq_no";
        columns[2] = "tendertype";
        columns[3] = "amount";
        columns[4] = "change";
        columns[5] = "tender_desc";
        columns[6] = "change_desc";
        columns[7] = "locale_language";
        columns[8] = "locale_variant";
        columns[9] = "data_capture";

        col_types = new int[10];

        col_types[0] = DBRecord.INT;
        col_types[1] = DBRecord.INT;
        col_types[2] = DBRecord.INT;
        col_types[3] = DBRecord.DOUBLE;
        col_types[4] = DBRecord.DOUBLE;
        col_types[5] = DBRecord.STRING;
        col_types[6] = DBRecord.STRING;
        col_types[7] = DBRecord.STRING;
        col_types[8] = DBRecord.STRING;
        col_types[9] = DBRecord.STRING;
    }

    private int transid;
    private int seqno;
    private int tendertype;
    private double tenderamount;
    private double change;
    private String tenderdesc;
    private String changedesc;
    private String localelanguage;
    private String localevariant;
    private String datacapture;


    public TransTender() {
    }

    public int transID() {
        return transid;
    }

    public int seqNo() {
        return seqno;
    }

    public int tenderType() {
        return tendertype;
    }

    public double tenderAmount() {
        return tenderamount;
    }

    public double change() {
        return change;
    }

    public String tenderDesc() {
        return tenderdesc;
    }

    public String changeDesc() {
        return changedesc;
    }

    public String localeLanguage() {
        return localelanguage;
    }

    public String localeVariant() {
        return localevariant;
    }

    public String dataCapture() {
        return datacapture;
    }

    public void setTransID(int value) {
        transid = value;
    }

    public void setSeqNo(int value) {
        seqno = value;
    }

    public void setTenderType(int value) {
        tendertype = value;
    }

    public void setTenderAmount(double value) {
        tenderamount = value;
    }

    public void setChange(double value) {
        change = value;
    }

    public void setTenderDesc(String value) {
        tenderdesc = value;
    }

    public void setChangeDesc(String value) {
        changedesc = value;
    }

    public void setLocaleLanguage(String value) {
        localelanguage = value;
    }

    public void setLocaleVariant(String value) {
        localevariant = value;
    }

    public void setDataCapture(String value) {
        datacapture = value;
    }


    // parent relations

    public Vector parents() {
        return null;
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

        TransTender b = new TransTender();
        return b;
    }

    public void populate(ResultSet rset) {

        try {
            setTransID(rset.getInt("trans_id"));
            setSeqNo(rset.getInt("seq_no"));
            setTenderType(rset.getInt("tendertype"));
            setTenderAmount(rset.getDouble("amount"));
            setChange(rset.getDouble("change"));
            setTenderDesc(rset.getString("tender_desc"));
            setChangeDesc(rset.getString("change_desc"));
            setLocaleLanguage(rset.getString("locale_language"));
            setLocaleLanguage(rset.getString("locale_variant"));
            setDataCapture(rset.getString("data_capture"));
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
        tmp.append(Integer.toString(tenderType()));
        tmp.append(", ");
        tmp.append(Double.toString(tenderAmount()));
        tmp.append(", ");
        tmp.append(Double.toString(change()));
        tmp.append(", '");
        tmp.append(tenderDesc());
        tmp.append("', '");
        tmp.append(changeDesc());
        tmp.append("', '");
        tmp.append(localeLanguage());
        tmp.append("', '");
        tmp.append(localeVariant());
        tmp.append("', '");
        tmp.append(dataCapture());
        tmp.append("')");

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
        tmp.append(Integer.toString(tenderType()));
        tmp.append(", ");
        tmp.append(Double.toString(tenderAmount()));
        tmp.append(", ");
        tmp.append(Double.toString(change()));
        tmp.append(", '");
        tmp.append(tenderDesc());
        tmp.append("', '");
        tmp.append(changeDesc());
        tmp.append("', '");
        tmp.append(localeLanguage());
        tmp.append("', '");
        tmp.append(localeVariant());
        tmp.append("', '");
        tmp.append(dataCapture());
        tmp.append("')");

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

    public void relations() {
    }  // no relations (yet)


    /** Abstract implementation of toXML () */
    public String toXML() {
        return super.toXML(table, columnObjects(), columns, col_types);
    }

    public Vector columnObjects() {

        Vector objs = new Vector();

        objs.addElement(new Integer(transID()));
        objs.addElement(new Integer(seqNo()));
        objs.addElement(new Integer(tenderType()));
        objs.addElement(new Double(tenderAmount()));
        objs.addElement(new Double(change()));
        objs.addElement(new String(tenderDesc()));
        objs.addElement(new String(changeDesc()));
        objs.addElement(new String(localeLanguage()));
        objs.addElement(new String(localeVariant()));
        objs.addElement(new String(dataCapture()));

        return objs;
    }
}


