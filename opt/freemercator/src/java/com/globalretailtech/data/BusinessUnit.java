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
import java.util.Date;
import java.util.Vector;

import com.globalretailtech.util.Application;

/**
 * Business unit is the primary entity
 * in the business hierarchy that can represnt
 * abstract nodes, such as a district office or
 * region. Root nodes are also business units.
 *
 * @author  Quentin Olson
 */
public class BusinessUnit extends BusinessObject {

    public static final int TYPE = 1;

    private static String table;
    private static String[] columns;
    private static int[] col_types;

    static {

        table = "bu";

        columns = new String[3];

        columns[0] = "bu_id";
        columns[1] = "bu_desc";
        columns[2] = "create_date";

        col_types = new int[3];

        col_types[0] = DBRecord.INT;
        col_types[1] = DBRecord.STRING;
        col_types[2] = DBRecord.DATE;
    }

    private int buid;
    private String budesc;
    private Date createdate;

    public BusinessUnit() {
    }

    public int buID() {
        return buid;
    }

    public String buDesc() {
        return budesc;
    }

    public Date createDate() {
        return createdate;
    }

    public void setBuID(int value) {
        buid = value;
    }

    public void setBuDesc(String value) {
        budesc = value;
    }

    public void setCreateDate(Date value) {
        createdate = value;
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

    // parent relations

    public Vector parents() {

        String fetchSpec = BoMap.getByIdAndType(buID(), TYPE);
        Vector v = Application.dbConnection().fetch(new BoMap(), fetchSpec);
        Vector bus = null;

        for (int i = 0; i < v.size(); i++) {
            BoMap bo = (BoMap) v.elementAt(i);
            String fs2 = BusinessUnit.getByID(bo.parentBoID());
            bus = Application.dbConnection().fetch(new BusinessUnit(), fs2);
        }
        return bus;
    }


    public DBRecord copy() {
        BusinessUnit b = new BusinessUnit();
        return b;
    }

    public void populate(ResultSet rset) {

        try {
            setBuID(rset.getInt("bu_id"));
            setBuDesc(rset.getString("bu_desc"));
            setCreateDate(rset.getDate("create_date"));
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

    public int boID() {
        return buID();
    }

    public int boType() {
        return TYPE;
    }

    public String toString() {
        return toXML();
    }

    public void relations() {
    }  // no relations (yet)


    private BoMap bomap;

    public BoMap boMap() {
        return bomap;
    }

    public void setBoMap(BoMap value) {
        bomap = value;
    }

    public String toXML() {
        return super.toXML(table, columnObjects(), columns, col_types);
    }

    public Vector columnObjects() {

        Vector objs = new Vector();

        objs.addElement(new Integer(buID()));
        objs.addElement(new String(buDesc()));
        objs.addElement(createDate());

        return objs;
    }
}


