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
import org.apache.log4j.Logger;

/**
 * Subscriber is a user in the business unit
 * hierachy. Not a POS user.
 *
 * @author  Quentin Olson
 */
public class Subscriber extends DBRecord {

    static Logger logger = Logger.getLogger(Subscriber.class);

    private static String table;
    private static String[] columns;
    private static int[] col_types;

    static {

        table = "sub";

        columns = new String[3];

        columns[0] = "subscriber_id";
        columns[1] = "subscriber_name";
        columns[2] = "subscriber_pass";

        col_types = new int[3];

        col_types[0] = DBRecord.INT;
        col_types[1] = DBRecord.STRING;
        col_types[2] = DBRecord.STRING;
    }

    private int subscriberid;
    private String subscribername;
    private String subscriberpass;

    public Subscriber() {
    }

    public int subscriberID() {
        return subscriberid;
    }

    public String subscriberName() {
        return subscribername;
    }

    public String subscriberPass() {
        return subscriberpass;
    }

    public void setSubscriberID(int value) {
        subscriberid = value;
    }

    public void setSubscriberName(String value) {
        subscribername = value;
    }

    public void setSubscriberPass(String value) {
        subscriberpass = value;
    }

    public static int getID(int n) {
        return 0;
    }

    public static String getUser(String n) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[1]);
        s.append(" = '");
        s.append(n);
        s.append("'");

        return new String(s.toString());
    }


    public DBRecord copy() {
        Subscriber b = new Subscriber();
        return b;
    }

    public void populate(ResultSet rset) {

        try {
            setSubscriberID(rset.getInt("subscriber_id"));
            setSubscriberName(rset.getString("subscriber_name"));
            setSubscriberPass(rset.getString("subscriber_pass"));
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

    // relations

    private Vector subscribermap;

    public Vector subscriberMap() {
        return subscribermap;
    }

    public void setSubscriberMap(Vector value) {
        subscribermap = value;
    }

    public void relations() {

        String fetchSpec = null;
        Vector v = null;
        fetchSpec = SubscriberMap.getByID(subscriberID());
        v = Application.dbConnection().fetch(new SubscriberMap(), fetchSpec);

        if (v.size() == 0) {
            logger.warn("Relation in Subscriber, found... " + v.size());
            return;
        }
        setSubscriberMap(v);
    }

    /** Abstract implementation of toXML () */
    public String toXML() {
        return super.toXML(table, columnObjects(), columns, col_types);
    }

    public Vector columnObjects() {

        Vector objs = new Vector();

        objs.addElement(new Integer(subscriberID()));
        objs.addElement(new String(subscriberName()));
        objs.addElement(new String(subscriberPass()));

        return objs;
    }
}


