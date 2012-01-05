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
 * Input filter field associates filtered sub-fields
 * within the regular expression with keys. These keys
 * are used within the event classes to select filtered
 * data.
 *
 * @author  Quentin Olson
 * @see Filter
 */
public class InputFilterField extends DBRecord {

    private static String table = "input_filter_field";
    private static String[] columns = {"filter_id",
                                       "sequence_no",
                                       "name"};

    private static int[] col_types = {DBRecord.INT,
                                      DBRecord.INT,
                                      DBRecord.STRING};

    private int filterid;
    private int sequenceno;
    private String name;


    public InputFilterField() {
    }

    public int filterID() {
        return filterid;
    }

    public int sequenceNo() {
        return sequenceno;
    }

    public String name() {
        return name;
    }

    public void setFilterID(int value) {
        filterid = value;
    }

    public void setSequenceNo(int value) {
        sequenceno = value;
    }

    public void setName(String value) {
        name = value;
    }

    // selectors

    public static String getByFilterID(int id) {

        StringBuffer scratch = new StringBuffer();

        scratch.append("select * from ");
        scratch.append(table);
        scratch.append(" where ");
        scratch.append(columns[0]);
        scratch.append(" = ");
        scratch.append(Integer.toString(id));
        scratch.append(" order by sequence_no");

        return new String(scratch.toString());
    }


    public DBRecord copy() {
        InputFilterField b = new InputFilterField();
        return b;
    }

    public void populate(ResultSet rset) {

        try {

            setFilterID(rset.getInt("filter_id"));
            setSequenceNo(rset.getInt("sequence_no"));
            setName(rset.getString("name"));

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

        objs.addElement(new Integer(filterID()));
        objs.addElement(new Integer(sequenceNo()));
        objs.addElement(name() == null ? "" : name());
        return objs;
    }
}


