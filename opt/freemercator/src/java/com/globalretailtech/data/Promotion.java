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
 * Promotion defines (hopefully) a very arbitray promotion
 * mechanism. Various parameters and the actual promotion logic
 * is defined. This is linked to an Item through promotion map.
 *
 * @author  Quentin Olson
 * @see PromotionMap
 * @see Item
 */
public class Promotion extends DBRecord implements java.io.Serializable {

    private static String table;
    private static String[] columns;
    private static int[] col_types;

    static {

        table = "promotion";

        columns = new String[18];

        columns[0] = "promotion_id";
        columns[1] = "promotion_no";
        columns[2] = "promotion_type";
        columns[3] = "promotion_val1";
        columns[4] = "promotion_val2";
        columns[5] = "promotion_val3";
        columns[6] = "promotion_val4";
        columns[7] = "promotion_val5";
        columns[8] = "promotion_dval1";
        columns[9] = "promotion_dval2";
        columns[10] = "promotion_dval3";
        columns[11] = "promotion_dval4";
        columns[12] = "promotion_dval5";
        columns[13] = "print_before_item";
        columns[14] = "valid_times";
        columns[15] = "valid_days";
        columns[16] = "promotion_string";
        columns[17] = "promotion_class";

        col_types = new int[18];

        col_types[0] = DBRecord.INT;
        col_types[1] = DBRecord.INT;
        col_types[2] = DBRecord.INT;
        col_types[3] = DBRecord.INT;
        col_types[4] = DBRecord.INT;
        col_types[5] = DBRecord.INT;
        col_types[6] = DBRecord.INT;
        col_types[7] = DBRecord.INT;
        col_types[8] = DBRecord.DOUBLE;
        col_types[9] = DBRecord.DOUBLE;
        col_types[10] = DBRecord.DOUBLE;
        col_types[11] = DBRecord.DOUBLE;
        col_types[12] = DBRecord.DOUBLE;
        col_types[13] = DBRecord.BOOLEAN;
        col_types[14] = DBRecord.STRING;
        col_types[15] = DBRecord.STRING;
        col_types[16] = DBRecord.STRING;
        col_types[17] = DBRecord.STRING;
    }

    private int itempromotionid;
    private int promotionno;
    private int promotiontype;
    private int promotionval1;
    private int promotionval2;
    private int promotionval3;
    private int promotionval4;
    private int promotionval5;
    private double promotiondval1;
    private double promotiondval2;
    private double promotiondval3;
    private double promotiondval4;
    private double promotiondval5;
    private boolean printbeforeitem;
    private String validtimes;
    private String validdays;
    private String promotionstring;
    private String promotionclass;

    private static StringBuffer scratch;

    public Promotion() {
        if (scratch == null) {
            scratch = new StringBuffer();
        }
    }

    public int promotionID() {
        return itempromotionid;
    }

    public int promotionNo() {
        return promotionno;
    }

    public int promotionType() {
        return promotiontype;
    }

    public int promotionVal1() {
        return promotionval1;
    }

    public int promotionVal2() {
        return promotionval2;
    }

    public int promotionVal3() {
        return promotionval3;
    }

    public int promotionVal4() {
        return promotionval4;
    }

    public int promotionVal5() {
        return promotionval5;
    }

    public double promotionDVal1() {
        return promotiondval1;
    }

    public double promotionDVal2() {
        return promotiondval2;
    }

    public double promotionDVal3() {
        return promotiondval3;
    }

    public double promotionDVal4() {
        return promotiondval4;
    }

    public double promotionDVal5() {
        return promotiondval5;
    }

    public boolean printBeforeItem() {
        return printbeforeitem;
    }

    public String validTimes() {
        return validtimes;
    }

    public String validDays() {
        return validdays;
    }

    public String promotionString() {
        return promotionstring;
    }

    public String promotionClass() {
        return promotionclass;
    }

    public void setPromotionID(int value) {
        itempromotionid = value;
    }

    public void setPromotionNo(int value) {
        promotionno = value;
    }

    public void setPromotionType(int value) {
        promotiontype = value;
    }

    public void setPromotionVal1(int value) {
        promotionval1 = value;
    }

    public void setPromotionVal2(int value) {
        promotionval2 = value;
    }

    public void setPromotionVal3(int value) {
        promotionval3 = value;
    }

    public void setPromotionVal4(int value) {
        promotionval4 = value;
    }

    public void setPromotionVal5(int value) {
        promotionval5 = value;
    }

    public void setPromotionDVal1(double value) {
        promotiondval1 = value;
    }

    public void setPromotionDVal2(double value) {
        promotiondval2 = value;
    }

    public void setPromotionDVal3(double value) {
        promotiondval3 = value;
    }

    public void setPromotionDVal4(double value) {
        promotiondval4 = value;
    }

    public void setPromotionDVal5(double value) {
        promotiondval5 = value;
    }

    public void setPrintBeforeItem(boolean value) {
        printbeforeitem = value;
    }

    public void setValidTimes(String value) {
        validtimes = value;
    }

    public void setValidDays(String value) {
        validdays = value;
    }

    public void setPromotionString(String value) {
        promotionstring = value;
    }

    public void setPromotionClass(String value) {
        promotionclass = value;
    }

    public static String getByID(int id) {

        if (scratch == null) {
            scratch = new StringBuffer();
        }
        if (scratch.length() > 0)
            scratch.delete(0, scratch.length());
        scratch.append("select * from ");
        scratch.append(table);
        scratch.append(" where ");
        scratch.append(columns[0]);
        scratch.append(" = ");
        scratch.append(Integer.toString(id));

        return new String(scratch.toString());
    }

    public static String getByNo(int no) {

        if (scratch == null) {
            scratch = new StringBuffer();
        }
        if (scratch.length() > 0)
            scratch.delete(0, scratch.length());
        scratch.append("select * from ");
        scratch.append(table);
        scratch.append(" where ");
        scratch.append(columns[1]);
        scratch.append(" = ");
        scratch.append(Integer.toString(no));

        return new String(scratch.toString());
    }

    public DBRecord copy() {
        Promotion b = new Promotion();
        return b;
    }

    public void populate(ResultSet rset) {

        try {

            setPromotionID(rset.getInt("promotion_id"));
            setPromotionNo(rset.getInt("promotion_no"));
            setPromotionType(rset.getInt("promotion_type"));
            setPromotionVal1(rset.getInt("promotion_val1"));
            setPromotionVal2(rset.getInt("promotion_val2"));
            setPromotionVal3(rset.getInt("promotion_val3"));
            setPromotionVal4(rset.getInt("promotion_val4"));
            setPromotionVal5(rset.getInt("promotion_val5"));
            setPromotionDVal1(rset.getDouble("promotion_dval1"));
            setPromotionDVal2(rset.getDouble("promotion_dval2"));
            setPromotionDVal3(rset.getDouble("promotion_dval3"));
            setPromotionDVal4(rset.getDouble("promotion_dval4"));
            setPromotionDVal5(rset.getDouble("promotion_dval5"));
            setPrintBeforeItem(rset.getInt("print_before_item") > 0);
            setValidDays(rset.getString("valid_days"));
            setValidTimes(rset.getString("valid_times"));
            setPromotionString(rset.getString("promotion_string"));
            setPromotionClass(rset.getString("promotion_class"));

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

        objs.addElement(new Integer(promotionID()));
        objs.addElement(new Integer(promotionNo()));
        objs.addElement(new Integer(promotionType()));
        objs.addElement(new Integer(promotionVal1()));
        objs.addElement(new Integer(promotionVal2()));
        objs.addElement(new Integer(promotionVal3()));
        objs.addElement(new Integer(promotionVal4()));
        objs.addElement(new Integer(promotionVal5()));
        objs.addElement(new Double(promotionDVal1()));
        objs.addElement(new Double(promotionDVal2()));
        objs.addElement(new Double(promotionDVal3()));
        objs.addElement(new Double(promotionDVal4()));
        objs.addElement(new Double(promotionDVal5()));
        objs.addElement(new Boolean(printBeforeItem()));
        objs.addElement(validTimes() == null ? "" : validTimes());
        objs.addElement(validDays() == null ? "" : validDays());
        objs.addElement(promotionString() == null ? "" : promotionString());
        objs.addElement(promotionClass() == null ? "" : promotionClass());

        return objs;
    }
}


