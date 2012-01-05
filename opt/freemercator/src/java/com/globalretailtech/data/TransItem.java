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
 * Contains a transaction item.
 *
 * @author  Quentin Olson
 * @see Item
 */
public class TransItem extends DBRecord {

    public static int SUSPEND = 1;
    public static int DELETE = 2;
    public static int VOID = 3;
    public static int PAID = 4;
    public static int PRICE_OVERRIDE = 5;

    private static String table;
    private static String[] columns;
    private static int[] col_types;

    static {

        table = "trans_item";

        columns = new String[14];

        columns[0] = "trans_id";
        columns[1] = "seq_no";
        columns[2] = "sku";
        columns[3] = "sku_link";
        columns[4] = "quantity";
        columns[5] = "amount";
        columns[6] = "ext_amount";
        columns[7] = "weight";
        columns[8] = "item_desc";
        columns[9] = "state";
        columns[10] = "reason_code";
        columns[11] = "tax_exempt";
        columns[12] = "tax_incl";
        columns[13] = "var_amount";

        col_types = new int[14];

        col_types[0] = DBRecord.INT;
        col_types[1] = DBRecord.INT;
        col_types[2] = DBRecord.STRING;
        col_types[3] = DBRecord.STRING;
        col_types[4] = DBRecord.INT;
        col_types[5] = DBRecord.DOUBLE;
        col_types[6] = DBRecord.DOUBLE;
        col_types[7] = DBRecord.DOUBLE;
        col_types[8] = DBRecord.STRING;
        col_types[9] = DBRecord.INT;
        col_types[10] = DBRecord.INT;
        col_types[11] = DBRecord.BOOLEAN;
        col_types[12] = DBRecord.BOOLEAN;
        col_types[13] = DBRecord.BOOLEAN;
    }

    private int transid;
    private int seqno;
    private String sku;
    private String skulink;
    private double quantity;
    private double amount;
    private double extamount;
    private double weight;
    private String itemdesc;
    private int state;
    private int reasoncode;
    private boolean taxexempt;
    private boolean taxincl;
    private boolean varamount;

    public TransItem() {
    }

    public int transID() {
        return transid;
    }

    public int seqNo() {
        return seqno;
    }

    public String sku() {
        return sku;
    }

    public String skuLink() {
        return skulink;
    }

    public double quantity() {
        return quantity;
    }

    public double amount() {
        return amount;
    }

    public double extAmount() {
        return extamount;
    }

    public double weight() {
        return weight;
    }

    public String itemDesc() {
        return itemdesc;
    }

    public int state() {
        return state;
    }

    public int reasonCode() {
        return reasoncode;
    }

    public boolean taxExempt() {
        return taxexempt;
    }

    public boolean taxIncluded() {
        return taxincl;
    }

    public boolean varAmount() {
        return varamount;
    }

    public void setTransID(int value) {
        transid = value;
    }

    public void setSeqNo(int value) {
        seqno = value;
    }

    public void setSku(String value) {
        sku = value;
    }

    public void setSkuLink(String value) {
        skulink = value;
    }

    public void setQuantity(double value) {
        quantity = value;
    }

    public void setAmount(double value) {
        amount = value;
    }

    public void setExtAmount(double value) {
        extamount = value;
    }

    public void setWeight(double value) {
        weight = value;
    }

    public void setItemDesc(String value) {
        itemdesc = value;
    }

    public void setState(int value) {
        state = value;
    }

    public void setReasonCode(int value) {
        reasoncode = value;
    }

    public void setTaxExempt(boolean value) {
        taxexempt = value;
    }

    public void setTaxIncluded(boolean value) {
        taxincl = value;
    }

    public void setVarAmount(boolean value) {
        varamount = value;
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
        TransItem b = new TransItem();
        return b;
    }

    public void populate(ResultSet rset) {

        try {

            setTransID(rset.getInt("trans_id"));
            setSeqNo(rset.getInt("seq_no"));
            setSku(rset.getString("sku"));
            setSkuLink(rset.getString("sku_link"));
            setQuantity(rset.getDouble("quantity"));
            setAmount(rset.getDouble("amount"));
            setExtAmount(rset.getDouble("ext_amount"));
            setWeight(rset.getDouble("weight"));
            setItemDesc(rset.getString("item_desc"));
            setState(rset.getInt("state"));
            setReasonCode(rset.getInt("reason_code"));
            setTaxExempt(rset.getInt("tax_exempt") > 0);
            setTaxIncluded(rset.getInt("tax_incl") > 0);
            setVarAmount(rset.getInt("var_amount") > 0);
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
        tmp.append(", '");
        if (skuLink() != null) {
            tmp.append(sku());
            tmp.append("', '");
            tmp.append(skuLink());
            tmp.append("', ");
        } else {
            tmp.append(sku());
            tmp.append("', null, ");
        }
        tmp.append(Double.toString(quantity()));
        tmp.append(", ");
        tmp.append(Double.toString(amount()));
        tmp.append(", ");
        tmp.append(Double.toString(extAmount()));
        tmp.append(", ");
        tmp.append(Double.toString(weight()));
        tmp.append(", '");
        tmp.append(itemDesc().trim());
        tmp.append("', ");
        tmp.append(Integer.toString(state()));
        tmp.append(", ");
        tmp.append(Integer.toString(reasonCode()));
        tmp.append(", ");
        tmp.append(Integer.toString(taxExempt() ? 1 : 0));
        tmp.append(", ");
        tmp.append(Integer.toString(taxIncluded() ? 1 : 0));
        tmp.append(", ");
        tmp.append(Integer.toString(varAmount() ? 1 : 0));
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
        tmp.append(", '");
        tmp.append(sku());
        tmp.append("', ");
        tmp.append(Double.toString(quantity()));
        tmp.append(", ");
        tmp.append(Double.toString(amount()));
        tmp.append(", '");
        tmp.append(Double.toString(extAmount()));
        tmp.append(", '");
        tmp.append(Double.toString(weight()));
        tmp.append(", '");
        tmp.append(itemDesc().trim());
        tmp.append("', ");
        tmp.append(Integer.toString(state()));
        tmp.append(", ");
        tmp.append(Integer.toString(taxExempt() ? 1 : 0));
        tmp.append(", ");
        tmp.append(Integer.toString(taxIncluded() ? 1 : 0));
        tmp.append(", ");
        tmp.append(Integer.toString(varAmount() ? 1 : 0));
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

    public String toString() {
        return toXML();
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
        objs.addElement(new String(sku()));
        objs.addElement(new String(skuLink()));
        objs.addElement(new Double(quantity()));
        objs.addElement(new Double(amount()));
        objs.addElement(new Double(extAmount()));
        objs.addElement(new Double(weight()));
        objs.addElement(new String(itemDesc()));
        objs.addElement(new Integer(state()));
        objs.addElement(new Integer(reasonCode()));
        objs.addElement(new Boolean(taxExempt()));
        objs.addElement(new Boolean(taxIncluded()));
        objs.addElement(new Boolean(varAmount()));

        return objs;
    }
}


