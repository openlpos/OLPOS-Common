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
public class TransCount extends DBRecord {

    private static String table;
    private static String[] columns;
    private static int[] col_types;

    static {

        table = "trans_count";

        columns = new String[4];

        columns[0] = "trans_id";
        columns[1] = "seq_no";
        columns[2] = "currency_denom_id";
        columns[3] = "denom_count";

        col_types = new int[4];

        col_types[0] = DBRecord.INT;
        col_types[1] = DBRecord.INT;
        col_types[2] = DBRecord.INT;
        col_types[3] = DBRecord.INT;
    }

    private int transid;
    private int seqno;
    private int currencydenomid;
    private int denomcount;

    public TransCount() {
    }

    public int transID() {
        return transid;
    }

    public int seqNo() {
        return seqno;
    }

    public int currencyDenomID() {
        return currencydenomid;
    }

    public int denomCount() {
        return denomcount;
    }

    public void setTransID(int value) {
        transid = value;
    }

    public void setSeqNo(int value) {
        seqno = value;
    }

    public void setCurrencyDenomID(int value) {
        currencydenomid = value;
    }

    public void setDenomCount(int value) {
        denomcount = value;
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

        TransCount b = new TransCount();
        return b;
    }

    public void populate(ResultSet rset) {

        try {
            setTransID(rset.getInt("trans_id"));
            setSeqNo(rset.getInt("seq_no"));
            setSeqNo(rset.getInt("currency_denom_id"));
            setDenomCount(rset.getInt("denom_count"));

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
        tmp.append(Integer.toString(currencyDenomID()));
        tmp.append(", ");
        tmp.append(Integer.toString(denomCount()));
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
        tmp.append(Integer.toString(currencyDenomID()));
        tmp.append(", ");
        tmp.append(Integer.toString(denomCount()));
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
        objs.addElement(new Integer(currencyDenomID()));
        objs.addElement(new Integer(denomCount()));

        return objs;
    }
}


