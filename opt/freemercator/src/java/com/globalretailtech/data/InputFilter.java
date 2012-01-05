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
 * Input filter stores a regular expression associated with a
 * input type (example: visa card, upc code, etc.). The check
 * digit class is associated at this level also (dynamically loaded).
 *
 * @author  Quentin Olson
 * @see InputFilterField
 * @see Filter
 */
public class InputFilter extends DBRecord {

    public static final int MSR = 1;
    public static final int EAN_UPC = 2;

    public static final int MASTER_CARD = 1;
    public static final int VISA = 2;
    public static final int DISCOVER = 3;
    public static final int AMEX = 4;

    public static final int EAN = 1;
    public static final int UPC = 2;

    private static String table;
    private static String[] columns;
    private static int[] col_types;

    static {

        table = "input_filter";

        columns = new String[8];

        columns[0] = "filter_id";
        columns[1] = "filter_type";
        columns[2] = "filter_sub_type";
        columns[3] = "filter_name";
        columns[4] = "display_name";
        columns[5] = "regex";
        columns[6] = "check_digit_class";
        columns[7] = "filter_class";

        col_types = new int[8];

        col_types[0] = DBRecord.INT;
        col_types[1] = DBRecord.INT;
        col_types[2] = DBRecord.INT;
        col_types[3] = DBRecord.STRING;
        col_types[4] = DBRecord.STRING;
        col_types[5] = DBRecord.STRING;
        col_types[6] = DBRecord.STRING;
        col_types[7] = DBRecord.STRING;
    }

    private int filterid;
    private int filtertype;
    private int filtersubtype;
    private String filtername;
    private String displayname;
    private String regex;
    private String checkdigitclass;
    private String filterclass;

    private static StringBuffer scratch;

    public InputFilter() {
    }

    public int filterID() {
        return filterid;
    }

    public int filterType() {
        return filtertype;
    }

    public int filterSubType() {
        return filtertype;
    }

    public String filterName() {
        return filtername;
    }

    public String displayName() {
        return displayname;
    }

    public String regex() {
        return regex;
    }

    public String checkDigitClass() {
        return checkdigitclass;
    }

    public String filterClass() {
        return filterclass;
    }

    public void setFilterID(int value) {
        filterid = value;
    }

    public void setFilterType(int value) {
        filtertype = value;
    }

    public void setFilterSubType(int value) {
        filtertype = value;
    }

    public void setFilterName(String value) {
        filtername = value;
    }

    public void setDisplayName(String value) {
        displayname = value;
    }

    public void setRegex(String value) {
        regex = value;
    }

    public void setCheckDigitClass(String value) {
        checkdigitclass = value;
    }

    public void setFilterClass(String value) {
        filterclass = value;
    }

    // selectors

    public static String getByID(int id) {

        scratch = new StringBuffer();

        scratch.append("select * from ");
        scratch.append(table);
        scratch.append(" where ");
        scratch.append(columns[0]);
        scratch.append(" = ");
        scratch.append(Integer.toString(id));

        return new String(scratch.toString());
    }

    public static String getAllByType(int type) {

        scratch = new StringBuffer();

        scratch.append("select * from ");
        scratch.append(table);
        scratch.append(" where ");
        scratch.append(columns[1]);
        scratch.append(" = ");
        scratch.append(Integer.toString(type));

        return new String(scratch.toString());
    }

	public static String getByName(String name) {

		scratch = new StringBuffer();

		scratch.append("select * from ");
		scratch.append(table);
		scratch.append(" where ");
		scratch.append(columns[3]);
		scratch.append(" = '");
		scratch.append(name);
		scratch.append("'");

		return new String(scratch.toString());
	}

    public DBRecord copy() {
        InputFilter b = new InputFilter();
        return b;
    }

    public void populate(ResultSet rset) {

        try {

            setFilterID(rset.getInt("filter_id"));
            setFilterType(rset.getInt("filter_type"));
            setFilterSubType(rset.getInt("filter_sub_type"));
            setFilterName(rset.getString("filter_name"));
            setDisplayName(rset.getString("display_name"));
            setRegex(rset.getString("regex"));
            setCheckDigitClass(rset.getString("check_digit_class"));
            setFilterClass(rset.getString("filter_class"));

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

    private Vector fields;

    public Vector fields() {
        return fields;
    }

    public void setFields(Vector value) {
        fields = value;
    }

    public void relations() {

        String fetchSpec = null;
        Vector v = null;
        fetchSpec = InputFilterField.getByFilterID(filterID());
        setFields(Application.dbConnection().fetch(new InputFilterField(), fetchSpec));
    }

    /** Abstract implementation of toXML () */
    public String toXML() {
        return super.toXML(table, columnObjects(), columns, col_types);
    }

    public Vector columnObjects() {

        Vector objs = new Vector();

        objs.addElement(new Integer(filterID()));
        objs.addElement(new Integer(filterType()));
        objs.addElement(new Integer(filterSubType()));
        objs.addElement(filterName() == null ? "" : filterName());
        objs.addElement(displayName() == null ? "" : displayName());
        objs.addElement(regex() == null ? "" : regex());
        objs.addElement(checkDigitClass() == null ? "" : checkDigitClass());
        objs.addElement(filterClass() == null ? "" : filterClass());

        return objs;
    }
}


