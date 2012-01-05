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
 * Subscriber maps links a subscriber to a business unit.
 *
 * @author  Quentin Olson
 */
public class SubscriberMap extends DBRecord {

    private static String table;
    private static String[] columns;
    private static int[] col_types;

    static {

        table = "sub_map";

        columns = new String[4];

        columns[0] = "subscriber_id";
        columns[1] = "bu_id";
        columns[2] = "bu_type";
        columns[3] = "subscriber_priv";

        col_types = new int[4];

        col_types[0] = DBRecord.INT;
        col_types[1] = DBRecord.INT;
        col_types[2] = DBRecord.INT;
        col_types[3] = DBRecord.INT;
    }

    private int subscriberid;
    private int buid;
    private int butype;
    private int subscriberpriv;

    private BoType botype;

    public SubscriberMap() {
    }

    public int subscriberID() {
        return subscriberid;
    }

    public int buID() {
        return buid;
    }

    public int buType() {
        return butype;
    }

    public int subscriberPriv() {
        return subscriberpriv;
    }

    public BoType boType() {
        return botype;
    }

    public void setSubscriberID(int value) {
        subscriberid = value;
    }

    public void setBuID(int value) {
        buid = value;
    }

    public void setBuType(int value) {
        butype = value;
    }

    public void setSubscriberPriv(int value) {
        subscriberpriv = value;
    }

    public void setBoType(BoType value) {
        botype = value;
    }

    public static String getByID(int n) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[0]);
        s.append(" = ");
        s.append(n);

        return new String(s.toString());
    }

    public static String getByIDandType(int id, int type) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[0]);
        s.append(" = ");
        s.append(id);
        s.append(" and ");
        s.append(columns[1]);
        s.append(" = ");
        s.append(type);

        return new String(s.toString());
    }


    public DBRecord copy() {
        SubscriberMap b = new SubscriberMap();
        return b;
    }

    public void populate(ResultSet rset) {

        try {
            setSubscriberID(rset.getInt("subscriber_id"));
            setBuID(rset.getInt("bu_id"));
            setBuType(rset.getInt("bu_type"));
            setSubscriberPriv(rset.getInt("subscriber_priv"));
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


    public void relations() {

        String fetchSpec = null;
        Vector v = null;
        fetchSpec = BoType.getByIDandType(buType(), 1);
        v = Application.dbConnection().fetch(new BoType(), fetchSpec);
        if ((v != null) && (v.size() == 1)) {
            setBoType((BoType) v.elementAt(0));
        }
    }

    /** Abstract implementation of toXML () */
    public String toXML() {
        return super.toXML(table, columnObjects(), columns, col_types);
    }

    public Vector columnObjects() {

        Vector objs = new Vector();

        objs.addElement(new Integer(subscriberID()));
        objs.addElement(new Integer(buID()));
        objs.addElement(new Integer(buType()));
        objs.addElement(new Integer(subscriberPriv()));

        return objs;
    }
}



