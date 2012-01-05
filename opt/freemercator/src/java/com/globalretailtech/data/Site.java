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

import com.globalretailtech.data.Pos;
import com.globalretailtech.util.Application;


/**
 * The Site entity in the business unit hierarchy.
 * Pos are typically associated to a site, a site is
 * the first concrete node in the hierarchy.
 *
 * @author  Quentin Olson
 */
public class Site extends BusinessObject {

    public static final int TYPE = 2;

    private static String table = "site";
    private static String[] columns = {"site_id",
                                       "site_no",
                                       "name",
                                       "lat",
                                       "lon",
                                       "addr1",
                                       "addr2",
                                       "addr3",
                                       "addr4",
                                       "addr5",
                                       "phone",
                                       "ip",
                                       "port",
                                       "merchant_id"};

    private static int[] col_types = {DBRecord.INT,
                                      DBRecord.INT,
                                      DBRecord.STRING,
                                      DBRecord.STRING,
                                      DBRecord.STRING,
                                      DBRecord.STRING,
                                      DBRecord.STRING,
                                      DBRecord.STRING,
                                      DBRecord.STRING,
                                      DBRecord.STRING,
                                      DBRecord.STRING,
                                      DBRecord.STRING,
                                      DBRecord.INT,
                                      DBRecord.INT};

    private int siteid;
    private int siteno;
    private String name;
    private String lat;
    private String lon;
    private String addr1;
    private String addr2;
    private String addr3;
    private String addr4;
    private String addr5;
    private String phone;
    private String ipaddr;
    private int port;
    private int merchantid;

    public Site() {
    }

    public int siteID() {
        return siteid;
    }

    public int siteNo() {
        return siteno;
    }

    public String name() {
        return name;
    }

    public String lat() {
        return lat;
    }

    public String lon() {
        return lon;
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

    public String ipAddr() {
        return ipaddr;
    }

    public int port() {
        return port;
    }

    public int merchantID() {
        return merchantid;
    }

    public void setSiteID(int value) {
        siteid = value;
    }

    public void setSiteNo(int value) {
        siteno = value;
    }

    public void setName(String value) {
        name = value;
    }

    public void setLat(String value) {
        lat = value;
    }

    public void setLon(String value) {
        lon = value;
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

    public void setIpAddr(String value) {
        ipaddr = value;
    }

    public void setPort(int value) {
        port = value;
    }

    public void setMerchantID(int value) {
        merchantid = value;
    }

    private BoMap bomap;

    public BoMap boMap() {
        return bomap;
    }

    public void setBoMap(BoMap value) {
        bomap = value;
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

    public static String getBySiteNo(int id) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[1]);
        s.append(" = ");
        s.append(Integer.toString(id));

        return new String(s.toString());
    }

    public static String getAll() {

        StringBuffer s = new StringBuffer("select * from ");
        s.append(table);
        return new String(s.toString());
    }

    /**
     * Returns first row in site table which is considered to be default
     */
    public static Site getDefault(){
        String fetchSpec = getAll();
        Vector tmp = Application.dbConnection().fetch(new Site(), fetchSpec);
        if (tmp.size() > 0) 
            return (Site)tmp.elementAt (0);
        return null;
    }

    /**
     * Gets all sites associated with a given business unit. Note: this is
     * not a static class since it performs recursion.
     */
    public Vector getAllByParent(int parentid) {

        Vector v = new Vector();
        BoMap map;
        Pos p;

        String fetchSpec = BoMap.getChildrenById2(parentid);
        Vector tmp = Application.dbConnection().fetch(new BoMap(), fetchSpec);

        if (tmp.size() > 0) {

            for (int i = 0; i < tmp.size(); i++) {

                BoMap bo = (BoMap) tmp.elementAt(i);
                if (bo.objType() == Site.TYPE) {
                    v.addElement(bo.businessObject());
                } else if (bo.objType() == BusinessUnit.TYPE) {

                    Vector bu = getAllByParent(bo.boID());
                    if (bu.size() > 0) {
                        v.addAll(bu);
                    }
                }
            }
        }
        return v;
    }

    public DBRecord copy() {

        Site b = new Site();

        b.setSiteID(siteID());
        b.setSiteNo(siteNo());
        b.setName(name == null ? new String("") : name());
        b.setLat(lat == null ? new String("") : lat());
        b.setLon(lon == null ? new String("") : lon());
        b.setAddr1(addr1 == null ? new String("") : addr1());
        b.setAddr2(addr2 == null ? new String("") : addr2());
        b.setAddr3(addr3 == null ? new String("") : addr3());
        b.setAddr4(addr4 == null ? new String("") : addr4());
        b.setAddr5(addr5 == null ? new String("") : addr5());
        b.setPhone(phone == null ? new String("") : phone());
        b.setIpAddr(ipaddr == null ? new String("") : ipAddr());
        b.setPort(port());

        return b;
    }

    public DBRecord copy(Site s) {

        Site b = new Site();

        b.setSiteID(s.siteID());
        b.setSiteNo(s.siteNo());
        b.setName(s.name());
        b.setLat(s.lat());
        b.setLon(s.lon());
        b.setAddr1(s.addr1());
        b.setAddr2(s.addr2());
        b.setAddr3(s.addr3());
        b.setAddr4(s.addr4());
        b.setAddr5(s.addr5());
        b.setPhone(s.phone());
        b.setIpAddr(s.ipAddr());
        b.setPort(s.port());

        return b;
    }

    public Vector parents() {

        String fetchSpec = BoMap.getByIdAndType(siteID(), TYPE);
        Vector v = Application.dbConnection().fetch(new BoMap(), fetchSpec);
        setBoMap((BoMap) v.elementAt(0));
        Vector bus = new Vector();

        for (int i = 0; i < v.size(); i++) {
            BoMap bo = (BoMap) v.elementAt(i);
            String fs2 = BusinessUnit.getByID(bo.parentBoID());
            Vector tmp = Application.dbConnection().fetch(new BusinessUnit(), fs2);

            for (int j = 0; j < tmp.size(); j++) {
                bus.addElement(tmp.elementAt(j));
            }
        }
        return bus;
    }

    public void populate(ResultSet rset) {

        try {
            setSiteID(rset.getInt("site_id"));
            setSiteNo(rset.getInt("site_no"));
            setName(rset.getString("name"));
            setLat(rset.getString("lat"));
            setLon(rset.getString("lon"));
            setAddr1(rset.getString("addr1"));
            setAddr2(rset.getString("addr2"));
            setAddr3(rset.getString("addr3"));
            setAddr4(rset.getString("addr4"));
            setAddr5(rset.getString("addr5"));
            setPhone(rset.getString("phone"));
            setIpAddr(rset.getString("ip"));
            setPort(rset.getInt("port"));
            setMerchantID(rset.getInt("merchant_id"));
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
        return siteID();
    }

    public int boType() {
        return TYPE;
    }

    public String toString() {
        return toXML();
    }

    // Relations

    private Vector pos;

    public Vector pos() {
        return pos;
    }

    public void setPos(Vector value) {
        pos = value;
    }

    /**
     Get the POS related to this site
     */
    public void relations() {

        BoMap map;

        String fetchSpec = BoMap.getChildrenByIdAndType(siteID(), Site.TYPE);
        Vector v = Application.dbConnection().fetch(new BoMap(), fetchSpec);

        Vector p = new Vector();
        for (int i = 0; i < v.size(); i++) {
            BoMap bo = (BoMap) v.elementAt(i);
            if (bo.objType() == Pos.TYPE) {
                p.add(bo.businessObject());
            }
        }
        setPos(p);
    }

    /** Abstract implementation of toXML () */
    public String toXML() {
        return super.toXML(table, columnObjects(), columns, col_types);
    }

    public Vector columnObjects() {

        Vector objs = new Vector();

        objs.addElement(new Integer(siteID()));
        objs.addElement(new Integer(siteNo()));
        objs.addElement(new String(name()));
        objs.addElement(new String(lat()));
        objs.addElement(new String(lon()));
        objs.addElement(new String(addr1()));
        objs.addElement(new String(addr2()));
        objs.addElement(new String(addr3()));
        objs.addElement(new String(addr4()));
        objs.addElement(new String(addr5()));
        objs.addElement(new String(phone()));
        objs.addElement(new String(ipAddr()));
        objs.addElement(new Integer(port()));
        objs.addElement(new Integer(merchantID()));

        return objs;
    }
}


