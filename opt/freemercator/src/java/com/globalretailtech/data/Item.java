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
import java.util.Date;

import com.globalretailtech.util.Application;

import org.apache.log4j.Logger;

/**
 * Item holds basic item (SKU) information.
 *
 * @author  Quentin Olson
 */
public class Item extends DBRecord implements java.io.Serializable {

    static Logger logger = Logger.getLogger(Item.class);

    // pricing options

    public final static int STANDARD = 1;
    public final static int BULK = 2;
    public final static int SIZE = 3;
    public final static int MANUAL = 4;
    public final static int PRICE_OVERRIDE = 5;

    public final static int INITIAL = 0;
    public final static int PAID = 1;

    private static String table;
    private static String[] columns;
    private static int[] col_types;

    static {

        table = "item";

        columns = new String[14];

        columns[0] = "item_id";
        columns[1] = "config_no";
        columns[2] = "sku";
        columns[3] = "short_desc";
        columns[4] = "amount";
        columns[5] = "dept";
        columns[6] = "tax_group";
        columns[7] = "pricing_opt";
        columns[8] = "act_date";
        columns[9] = "deact_date";
        columns[10] = "tax_inclusive";
		columns[11] = "tax_exempt";
		columns[12] = "locked";
		columns[13] = "divider";

        col_types = new int[14];

        col_types[0] = DBRecord.INT;
        col_types[1] = DBRecord.INT;
        col_types[2] = DBRecord.STRING;
        col_types[3] = DBRecord.STRING;
        col_types[4] = DBRecord.DOUBLE;
        col_types[5] = DBRecord.INT;
        col_types[6] = DBRecord.INT;
        col_types[7] = DBRecord.INT;
        col_types[8] = DBRecord.DATE;
        col_types[9] = DBRecord.DATE;
        col_types[10] = DBRecord.BOOLEAN;
		col_types[11] = DBRecord.BOOLEAN;
		col_types[12] = DBRecord.BOOLEAN;
		col_types[13] = DBRecord.INT;
    }

    private int itemid;
    private int configno;
    private String sku;
    private String shortdesc;
    private double amount;
    private int dept;
    private int taxgroup;
    private int pricingopt;
    private Date actdate;
    private Date deactdate;
    private boolean taxinclusive;
	private boolean taxexempt;
	private boolean locked;
	private int divider;

    private static StringBuffer scratch;

    public Item() {
        if (scratch == null) {
            scratch = new StringBuffer();
        }
    }

    public int itemID() {
        return itemid;
    }

    public int configNo() {
        return configno;
    }

    public String sku() {
        return sku;
    }

    public String desc() {
        return shortdesc;
    }

    public double amount() {
        return amount;
    }

    public int dept() {
        return dept;
    }

    public int taxGroup() {
        return taxgroup;
    }

    public int pricingOpt() {
        return pricingopt;
    }

    public Date actDate() {
        return actdate;
    }

    public Date deactDate() {
        return deactdate;
    }

    public boolean taxInclusive() {
        return taxinclusive;
    }

	public boolean taxExempt() {
		return taxexempt;
	}

	public boolean locked() {
		return locked;
	}

	public int divider() {
		return divider;
	}

    public void setItemID(int value) {
        itemid = value;
    }

    public void setConfigNo(int value) {
        configno = value;
    }

    public void setSku(String value) {
        sku = value;
    }

    public void setDesc(String value) {
        shortdesc = value;
    }

    public void setAmount(double value) {
        amount = value;
    }

    public void setDept(int value) {
        dept = value;
    }

    public void setTaxGroup(int value) {
        taxgroup = value;
    }

    public void setPricingOpt(int value) {
        pricingopt = value;
    }

    public void setActDate(Date value) {
        actdate = value;
    }

    public void setDeactDate(Date value) {
        deactdate = value;
    }

    public void setTaxInclusive(boolean value) {
        taxinclusive = value;
    }

	public void setTaxExempt(boolean value) {
		taxexempt = value;
	}

	public void setLocked(boolean value) {
		locked = value;
	}

	public void setDivider(int value) {
		divider = value;
	}

    // selectors

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

	public static String getBySKU(String id) {

		if (scratch == null) {
			scratch = new StringBuffer();
		}
		if (scratch.length() > 0)
			scratch.delete(0, scratch.length());

		scratch.append("select * from ");
		scratch.append(table);
		scratch.append(" where ");
		scratch.append(columns[2]);
		scratch.append(" = '");
		scratch.append(id);
		scratch.append("'");

		return new String(scratch.toString());
	}

	public static String getLocked() {

		if (scratch == null) {
			scratch = new StringBuffer();
		}
		if (scratch.length() > 0)
			scratch.delete(0, scratch.length());

		scratch.append("select * from ");
		scratch.append(table);
		scratch.append(" where ");
		scratch.append(columns[12]); //locked
		scratch.append(" > 0");

		return new String(scratch.toString());
	}

    public static String getAll() {

        if (scratch == null) {
            scratch = new StringBuffer();
        }

        if (scratch.length() > 0)
            scratch.delete(0, scratch.length());

        scratch.append("select * from ");
        scratch.append(table);

        return new String(scratch.toString());
    }

    public DBRecord copy() {
        Item b = new Item();
        return b;
    }

    public void populate(ResultSet rset) {

        try {

            setItemID(rset.getInt("item_id"));
            setConfigNo(rset.getInt("config_no"));
            setSku(rset.getString("sku"));
            setDesc(rset.getString("short_desc"));
            setAmount(rset.getDouble("amount"));
            setDept(rset.getInt("dept"));
            setTaxGroup(rset.getInt("tax_group"));
            setPricingOpt(rset.getInt("pricing_opt"));
            setActDate(rset.getDate("act_date"));
            setDeactDate(rset.getDate("deact_date"));
            setTaxInclusive(rset.getInt("tax_inclusive") > 0);
			setTaxExempt(rset.getInt("tax_exempt") > 0);
			setLocked(rset.getInt("locked") > 0);
			setDivider(rset.getInt("divider"));
        } catch (java.sql.SQLException e) {
            Application.dbConnection().setException(e);
        }
    }

    public boolean save() {
        return true;
    }

    public boolean update() {

        StringBuffer tmp = new StringBuffer();

        tmp.append("update ").append(table).append(" set ");
        tmp.append(columns[3]).append(" = '").append(desc()).append ("'");
		tmp.append(", ").append(columns[4]).append(" = ").append(amount);
		tmp.append(", ").append(columns[10]).append(" = ").append(taxInclusive() ? 1 : 0);
		tmp.append(", ").append(columns[12]).append(" = ").append(locked() ? 1 : 0);
        tmp.append(" where ").append(columns[2]).append(" = '").append(sku()).append("'");
        try {
            Application.dbConnection().execute(tmp.toString());
            Application.dbConnection().commit();
        } catch (java.sql.SQLException e) {
            Application.dbConnection().setException(e);
            return false;
        }
        return true;
    }

    public String toString() {
        return toXML();
    }


    /**
     * update
     */
    public void update(int updateIndex [], Vector updateValue) {

        StringBuffer select = new StringBuffer();
        select.append(columns[0]).append(" = ").append(Integer.toString(itemID()));

        String updateString = getUpdateString(updateIndex, updateValue, table, columns, col_types, select.toString());

        try {

            Application.dbConnection().execute(updateString);
            Application.dbConnection().commit();
        } catch (SQLException e) {
            logger.warn("Update failed " + updateString, e);
        }
    }

    //
    // Relations
    //

    private Vector promotions;
    private Vector itemlinks;

    public Vector promotions() {
        return promotions;
    }

    public Vector itemLinks() {
        return itemlinks;
    }

    public void relations() {

        // get the associated promotions

        String fetchSpec = null;
        Vector v = null;
        fetchSpec = PromotionMap.getByMap(sku());
        v = Application.dbConnection().fetch(new PromotionMap(), fetchSpec);

        if ((v != null) && (v.size() > 0)) {

            for (int i = 0; i < v.size(); i++) {
                PromotionMap map = (PromotionMap) v.elementAt(i);
                fetchSpec = Promotion.getByID(map.promotionID());
                promotions = Application.dbConnection().fetch(new Promotion(), fetchSpec);
            }
        }

        // get the associated items, (item links)

        fetchSpec = ItemLink.getBySKU(sku());
        itemlinks = Application.dbConnection().fetch(new ItemLink(), fetchSpec);
    }

    /** Abstract implementation of toXML () */
    public String toXML() {
        return super.toXML(table, columnObjects(), columns, col_types);
    }

    public Vector columnObjects() {

        Vector objs = new Vector();

        objs.addElement(new Integer(itemID()));
        objs.addElement(new Integer(configNo()));
        objs.addElement(sku() == null ? "" : sku());
        objs.addElement(desc() == null ? "" : desc());
        objs.addElement(new Double(amount()));
        objs.addElement(new Integer(dept()));
        objs.addElement(new Integer(taxGroup()));
        objs.addElement(new Integer(pricingOpt()));
        objs.addElement(actDate());
        objs.addElement(deactDate());
        objs.addElement(new Boolean(taxInclusive()));
        objs.addElement(new Boolean(taxExempt()));

        return objs;
    }
}


