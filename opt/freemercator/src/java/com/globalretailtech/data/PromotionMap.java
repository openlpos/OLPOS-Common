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
 * Map a promotion to something, an item, a department
 * a manufacturer???
 *
 * @author  Quentin Olson
 */
public class PromotionMap extends DBRecord {

    public static int ITEM_MAP = 1;
    public static int DEPT_MAP = 2;
    public static int MANU_MAP = 3;


    private static String table = "promotion_map";
    private static String[] columns = {"promotion_id",
                                       "promotion_map",
                                       "map_type"};

    private static int[] col_types = {DBRecord.INT,
                                      DBRecord.STRING,
                                      DBRecord.INT};

    private int promotionid;
    private String promotionmap;
    private int maptype;

    private static StringBuffer scratch;

    public PromotionMap() {
        if (scratch == null) {
            scratch = new StringBuffer();
        }
    }

    public int promotionID() {
        return promotionid;
    }

    public String promotionMap() {
        return promotionmap;
    }

    public int mapType() {
        return maptype;
    }

    public void setPromotionID(int value) {
        promotionid = value;
    }

    public void setPromotionMap(String value) {
        promotionmap = value;
    }

    public void setMapType(int value) {
        maptype = value;
    }


    public static String getByPromotionID(int id) {

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

    public static String getByMap(String map) {

        if (scratch == null) {
            scratch = new StringBuffer();
        }
        if (scratch.length() > 0)
            scratch.delete(0, scratch.length());
        scratch.append("select * from ");
        scratch.append(table);
        scratch.append(" where ");
        scratch.append(columns[1]);
        scratch.append(" = ");
        scratch.append(map);

        return new String(scratch.toString());
    }

    public DBRecord copy() {
        PromotionMap b = new PromotionMap();
        return b;
    }

    public void populate(ResultSet rset) {

        try {

            setPromotionID(rset.getInt("promotion_id"));
            setPromotionMap(rset.getString("promotion_map"));
            setMapType(rset.getInt("map_type"));

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

        objs.addElement(new Integer(promotionID()));
        objs.addElement(promotionMap() == null ? "" : promotionMap());
        objs.addElement(new Integer(mapType()));

        return objs;
    }
}


