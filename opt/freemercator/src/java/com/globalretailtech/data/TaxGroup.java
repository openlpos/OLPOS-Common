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
 * Holds a group of taxes to gether by POS configuration ID.
 *
 * @author  Quentin Olson
 */
public class TaxGroup extends DBRecord {

    private static String table = "tax_group";
    private static String[] columns = {"tax_group_id",
                                       "config_no"};

    private static int[] col_types = {DBRecord.INT,
                                      DBRecord.INT};

    private int taxgroupid;
    private int configno;

    public TaxGroup() {
    }

    public int taxGroupID() {
        return taxgroupid;
    }

    public int configNo() {
        return configno;
    }

    public void setTaxGroupID(int value) {
        taxgroupid = value;
    }

    public void setConfigNo(int value) {
        configno = value;
    }

    // fetch specifiers

    public static String getByID(int id) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[0]);
        s.append(" = ");
        s.append(Integer.toString(id));

        return new String(s.toString());
    }

    public static String getByConfigNo(int no) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[1]);
        s.append(" = ");
        s.append(Integer.toString(no));

        return new String(s.toString());
    }

    public Vector parents() {

        return null;
    }

    public DBRecord copy() {
        TaxGroup b = new TaxGroup();
        return b;
    }

    public void populate(ResultSet rset) {

        try {
            setTaxGroupID(rset.getInt("tax_group_id"));
            setConfigNo(rset.getInt("config_no"));
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


    // relations

    private Vector taxes;

    public Vector taxes() {
        return taxes;
    }

    public void setTaxes(Vector value) {
        taxes = value;
    }

    public void relations() {
        setTaxes(Application.dbConnection().fetch(new Tax(),
                Tax.getByGroupID(taxGroupID())));
    }

    /** Abstract implementation of toXML () */
    public String toXML() {
        return super.toXML(table, columnObjects(), columns, col_types);
    }

    public Vector columnObjects() {

        Vector objs = new Vector();

        objs.addElement(new Integer(taxGroupID()));
        objs.addElement(new Integer(configNo()));

        return objs;
    }
}


