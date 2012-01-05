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
 * Hold transation item promotion, modifies price, etc...
 *
 * @author  Quentin Olson
 * @see Promotion
 */
public class TransPromotion extends DBRecord {

    private static String table;
    private static String[] columns;
    private static int[] col_types;

    static {

        table = "trans_promotion";

        columns = new String[9];

        columns[0] = "trans_id";
        columns[1] = "seq_no";
        columns[2] = "promotion_no";
        columns[3] = "promotion_type";
        columns[4] = "promotion_amount";
        columns[5] = "promotion_quantity";
        columns[6] = "reason_code";
        columns[7] = "promotion_data";
        columns[8] = "promotion_desc";

        col_types = new int[9];

        col_types[0] = DBRecord.INT;
        col_types[1] = DBRecord.INT;
        col_types[2] = DBRecord.INT;
        col_types[3] = DBRecord.INT;
        col_types[4] = DBRecord.DOUBLE;
        col_types[5] = DBRecord.INT;
        col_types[6] = DBRecord.INT;
        col_types[7] = DBRecord.STRING;
        col_types[8] = DBRecord.STRING;
    }

    private int transid;
    private int seqno;
    private int promotionno;
    private int promotiontype;
    private double promotionamount;
    private double promotionquantity;
    private int reasoncode;
    private String promotiondata;
    private String promotiondesc;

    public TransPromotion() {
    }

    public int transID() {
        return transid;
    }

    public int seqNo() {
        return seqno;
    }

    public int promotionNo() {
        return promotionno;
    }

    public int promotionType() {
        return promotiontype;
    }

    public double promotionAmount() {
        return promotionamount;
    }

    public double promotionQuantity() {
        return promotionquantity;
    }

    public int reasonCode() {
        return reasoncode;
    }

    public String promotionData() {
        return promotiondata;
    }

    public String promotionDesc() {
        return promotiondesc;
    }

    public void setTransID(int value) {
        transid = value;
    }

    public void setSeqNo(int value) {
        seqno = value;
    }

    public void setPromotionType(int value) {
        promotiontype = value;
    }

    public void setPromotionNo(int value) {
        promotionno = value;
    }

    public void setPromotionAmount(double value) {
        promotionamount = value;
    }

    public void setPromotionQuantity(double value) {
        promotionquantity = value;
    }

    public void setReasonCode(int value) {
        reasoncode = value;
    }

    public void setPromotionData(String value) {
        promotiondata = value;
    }

    public void setPromotionDesc(String value) {
        promotiondesc = value;
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
        TransPromotion b = new TransPromotion();
        return b;
    }

    public void populate(ResultSet rset) {

        try {

            setTransID(rset.getInt("trans_id"));
            setSeqNo(rset.getInt("seq_no"));
            setPromotionNo(rset.getInt("promotion_no"));
            setPromotionType(rset.getInt("promotion_type"));
            setPromotionAmount(rset.getDouble("promotion_amount"));
            setPromotionQuantity(rset.getDouble("promotion_quantity"));
            setReasonCode(rset.getInt("reason_code"));
            setPromotionData(rset.getString("promotion_data"));
            setPromotionDesc(rset.getString("promotion_desc"));
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
        tmp.append(Integer.toString(promotionNo()));
        tmp.append(", ");
        tmp.append(Integer.toString(promotionType()));
        tmp.append(", ");
        tmp.append(Double.toString(promotionAmount()));
        tmp.append(", ");
        tmp.append(Double.toString(promotionQuantity()));
        tmp.append(", ");
        tmp.append(Integer.toString(reasonCode()));
        tmp.append(", '");
        tmp.append(promotionData());
        tmp.append("', '");
        tmp.append(promotionDesc());
        tmp.append("');");

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
        tmp.append(Integer.toString(promotionNo()));
        tmp.append(", ");
        tmp.append(Integer.toString(promotionType()));
        tmp.append(", ");
        tmp.append(Double.toString(promotionAmount()));
        tmp.append(", ");
        tmp.append(Double.toString(promotionQuantity()));
        tmp.append(", '");
        tmp.append(Integer.toString(reasonCode()));
        tmp.append(", '");
        tmp.append(promotionData());
        tmp.append("', '");
        tmp.append(promotionDesc());
        tmp.append("');");

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
        objs.addElement(new Integer(promotionNo()));
        objs.addElement(new Integer(promotionType()));
        objs.addElement(new Double(promotionType()));
        objs.addElement(new Double(promotionQuantity()));
        objs.addElement(new Integer(reasonCode()));
        objs.addElement(new String(promotionData()));
        objs.addElement(new String(promotionDesc()));

        return objs;
    }
}


