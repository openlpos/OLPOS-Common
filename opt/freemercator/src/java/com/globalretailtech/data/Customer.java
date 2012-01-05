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
 * Customer maintains basci customer information.
 *
 * @author  Quentin Olson
 */
public class Customer extends DBRecord {

    private static String table;
    private static String[] columns;
    private static int[] col_types;

    static {

        table = "customer";

        columns = new String[15];

        columns[0] = "customer_id";
        columns[1] = "customer_no";
        columns[2] = "customer_name";
        columns[3] = "addr1";
        columns[4] = "addr2";
        columns[5] = "addr3";
        columns[6] = "addr4";
        columns[7] = "addr5";
        columns[8] = "phone";
		columns[9] = "fax";
		columns[10] = "email";
		columns[11] = "extra1";
		columns[12] = "extra2";
		columns[13] = "discount";
		columns[14] = "birthday";

        col_types = new int[15];

        col_types[0] = DBRecord.INT;
        col_types[1] = DBRecord.STRING;
        col_types[2] = DBRecord.STRING;
        col_types[3] = DBRecord.STRING;
        col_types[4] = DBRecord.STRING;
        col_types[5] = DBRecord.STRING;
        col_types[6] = DBRecord.STRING;
        col_types[7] = DBRecord.STRING;
        col_types[8] = DBRecord.STRING;
        col_types[9] = DBRecord.STRING;
		col_types[10] = DBRecord.STRING;
		col_types[11] = DBRecord.STRING;
		col_types[12] = DBRecord.STRING;
		col_types[13] = DBRecord.INT;
		col_types[14] = DBRecord.DATE;
    }

    private int customerid;
    private String customerno;
    private String customername;
    private String addr1;
    private String addr2;
    private String addr3;
    private String addr4;
    private String addr5;
    private String phone;
    private String fax;
	private String email;
	private String extra1;
	private String extra2;
	private int discount;
	private Date birthday;

    public Customer() {
    }

    public int customerID() {
        return customerid;
    }

    public String customerNo() {
        return customerno;
    }

    public String customerName() {
        return customername;
    }

    public String addr1() {
        return addr1;
    }

    public String addr2() {
        return addr2;
    }

    public String addr3() {
        return addr3;
    }

    public String addr4() {
        return addr4;
    }

    public String addr5() {
        return addr5;
    }

    public String phone() {
        return phone;
    }

    public String fax() {
        return fax;
    }

	public String email() {
		return email;
	}

	public String extra1() {
		return extra1;
	}

	public String extra2() {
		return extra2;
	}

	public int discount() {
		return discount;
	}

	public Date birthday() {
		return birthday;
	}


    public void setCustomerID(int value) {
        customerid = value;
    }

    public void setCustomerNo(String value) {
        customerno = value;
    }

    public void setCustomerName(String value) {
        customername = value;
    }

    public void setAddr1(String value) {
        addr1 = value;
    }

    public void setAddr2(String value) {
        addr2 = value;
    }

    public void setAddr3(String value) {
        addr3 = value;
    }

    public void setAddr4(String value) {
        addr4 = value;
    }

    public void setAddr5(String value) {
        addr5 = value;
    }

    public void setPhone(String value) {
        phone = value;
    }

    public void setFax(String value) {
        fax = value;
    }

	public void setEMail(String value) {
		email = value;
	}

	public void setExtra1(String value) {
		extra1 = value;
	}

	public void setExtra2(String value) {
		extra2 = value;
	}

	public void setDiscount(int value) {
		discount = value;
	}

	public void setBirthday(Date value) {
		birthday = value;
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

    public static String getByCustomerNo(String no) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[1]);
        s.append(" = '");
        s.append(no);
        s.append("'");

        return new String(s.toString());
    }

    public static String getByPhoneNo(String no) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[8]);
        s.append(" = '");
        s.append(no);
        s.append("'");

        return new String(s.toString());
    }

    public DBRecord copy() {
        Customer b = new Customer();
        return b;
    }

    public void populate(ResultSet rset) {

        try {

            setCustomerID(rset.getInt("customer_id"));
            setCustomerNo(rset.getString("customer_no"));
            setCustomerName(rset.getString("customer_name"));
            setAddr1(rset.getString("addr1"));
            setAddr2(rset.getString("addr2"));
            setAddr3(rset.getString("addr3"));
            setAddr4(rset.getString("addr4"));
            setAddr5(rset.getString("addr5"));
            setPhone(rset.getString("phone"));
            setFax(rset.getString("fax"));
			setEMail(rset.getString("email"));
			setExtra1(rset.getString("extra1"));
			setExtra2(rset.getString("extra2"));
			setDiscount(rset.getInt("discount"));
			setBirthday(rset.getDate("birthday"));
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

    public String toString() {
        return toXML();
    }

    //
    // Relations
    //

    public void relations() {
    }

    public Vector columnObjects() {

        Vector objs = new Vector();

        objs.addElement(new Integer(customerID()));
        objs.addElement(new String(customerNo()));
        objs.addElement(new String(customerName()));
        objs.addElement(new String(addr1()));
        objs.addElement(new String(addr2()));
        objs.addElement(new String(addr3()));
        objs.addElement(new String(addr4()));
        objs.addElement(new String(addr5()));
        objs.addElement(new String(phone()));
        objs.addElement(new String(fax()));
		objs.addElement(new String(email()));
		objs.addElement(new String(extra1()));
		objs.addElement(new String(extra2()));
		objs.addElement(new Integer(discount()));
		if ( birthday() != null)
			objs.addElement(new Date(birthday().getTime()));


        return objs;
    }
}


