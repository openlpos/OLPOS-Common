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
 * Site Data Store defines a place a database
 * might exist on the network.
 *
 * @author  Quentin Olson
 */
public class SiteDataStore extends DBRecord implements java.io.Serializable {

    public static final int POS_PRIMARY = 1;
    public static final int POS_SECONDARY = 2;

    private static String table = "site_data_store";
    private static String[] columns = {"site_data_store_id",
                                       "site_id",
                                       "pos_id",
                                       "data_store_type",
                                       "ipaddr",
                                       "port"};

    private static int[] col_types = {DBRecord.INT,
                                      DBRecord.INT,
                                      DBRecord.INT,
                                      DBRecord.INT,
                                      DBRecord.STRING,
                                      DBRecord.INT};

    private int sitedatastoreid;
    private int siteid;
    private int posid;
    private int datastoretype;
    private String ipaddr;
    private int port;

    public SiteDataStore() {
    }

    public int siteDataStoreID() {
        return sitedatastoreid;
    }

    public int siteID() {
        return siteid;
    }

    public int posID() {
        return posid;
    }

    public int dataStoreType() {
        return datastoretype;
    }

    public String ipaddr() {
        return ipaddr;
    }

    public int port() {
        return port;
    }

    public void setSiteDataStoreID(int value) {
        sitedatastoreid = value;
    }

    public void setSiteID(int value) {
        siteid = value;
    }

    public void setPosID(int value) {
        posid = value;
    }

    public void setDataStoreType(int value) {
        datastoretype = value;
    }

    public void setIpaddr(String value) {
        ipaddr = value;
    }

    public void setPort(int value) {
        port = value;
    }

    public static String getPrimary(int posno) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[2]);
        s.append(" = ");
        s.append(Integer.toString(posno));
        s.append(" and ");
        s.append(columns[3]);
        s.append(" = ");
        s.append(Integer.toString(POS_PRIMARY));

        return new String(s.toString());
    }

    public static String getAllPrimaryBySite(int siteid) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[1]);
        s.append(" = ");
        s.append(Integer.toString(siteid));
        s.append(" and ");
        s.append(columns[3]);
        s.append(" = ");
        s.append(Integer.toString(POS_PRIMARY));

        return new String(s.toString());
    }

    public static String getAllPrimary() {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[3]);
        s.append(" = ");
        s.append(Integer.toString(POS_PRIMARY));

        return new String(s.toString());
    }

    public static String getSecondary(int posid) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where (");
        s.append(columns[1]);
        s.append(" = ");
        s.append(Integer.toString(posid));
        s.append(" or");
        s.append(columns[1]);
        s.append(" = ");
        s.append(Integer.toString(0));
        s.append(") and ");
        s.append(columns[3]);
        s.append(" = ");
        s.append(Integer.toString(POS_SECONDARY));

        return new String(s.toString());
    }


    public DBRecord copy() {

        SiteDataStore b = new SiteDataStore();

        b.setSiteDataStoreID(siteDataStoreID());
        b.setSiteID(siteID());
        b.setDataStoreType(dataStoreType());
        b.setIpaddr(ipaddr());
        b.setPort(port());

        return b;
    }

    public DBRecord copy(SiteDataStore s) {

        SiteDataStore b = new SiteDataStore();

        b.setSiteDataStoreID(s.siteDataStoreID());
        b.setSiteID(s.siteID());
        b.setDataStoreType(s.dataStoreType());
        b.setIpaddr(s.ipaddr());
        b.setPort(s.port());

        return b;
    }

    public void populate(ResultSet rset) {

        try {
            setSiteDataStoreID(rset.getInt("site_data_store_id"));
            setSiteID(rset.getInt("site_id"));
            setPosID(rset.getInt("pos_id"));
            setDataStoreType(rset.getInt("data_store_type"));
            setIpaddr(rset.getString("ipaddr"));
            setPort(rset.getInt("port"));
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

    public String desc() {

        String desc = "Unknown";

        if (posID() == 0) {
            desc = new String("Data Store Server");
        } else {

            switch (dataStoreType()) {
                case POS_PRIMARY:
                    desc = new String("Primary POS " + posID());
                    break;
                case POS_SECONDARY:
                    desc = new String("Secondary POS " + posID());
                    break;
            }
        }
        return desc;
    }


    // Relations

    public void relations() {
    }

    /** Abstract implementation of toXML () */
    public String toXML() {
        return super.toXML(table, columnObjects(), columns, col_types);
    }

    public Vector columnObjects() {

        Vector objs = new Vector();

        objs.addElement(new Integer(siteDataStoreID()));
        objs.addElement(new Integer(siteID()));
        objs.addElement(new Integer(posID()));
        objs.addElement(new Integer(dataStoreType()));
        objs.addElement(new String(ipaddr()));
        objs.addElement(new Integer(port()));

        return objs;
    }
}



