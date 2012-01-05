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
 * Plu is a many to one map for arbitray numerical
 * items to actual items.
 *
 * @author  Quentin Olson
 * @see Item
 */
public class Plu extends DBRecord {

    static Logger logger = Logger.getLogger(Plu.class);

    private static String table;
    private static String[] columns;
    private static int[] col_types;

    static {

        table = "plu";

        columns = new String[2];

        columns[0] = "plu";
        columns[1] = "sku";

        col_types = new int[2];

        col_types[0] = DBRecord.INT;
        col_types[1] = DBRecord.INT;
    }

    private int plu;
    private String sku;

    public Plu() {
    }

    public int plu() {
        return plu;
    }

    public String sku() {
        return sku;
    }

    public void setPlu(int value) {
        plu = value;
    }

    public void setSku(String value) {
        sku = value;
    }


    public static String getByPlu(int id) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[0]);
        s.append(" = ");
        s.append(id);

        return new String(s.toString());
    }

    public static String getBySku(String id) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[1]);
        s.append(" = '");
        s.append(id);
        s.append("'");

        return new String(s.toString());
    }


    public DBRecord copy() {
        Plu b = new Plu();
        return b;
    }

    public void populate(ResultSet rset) {

        try {

            setPlu(rset.getInt("plu"));
            setSku(rset.getString("sku"));
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

    //
    // Relations
    //

    private Item item;

    public Item item() {
        return item;
    }


    public void relations() {

        String fetchSpec = Item.getBySKU(sku());
        Vector v = Application.dbConnection().fetch(new Item(), fetchSpec);

        if (v.size() > 0) {
            item = (Item) v.elementAt(0);
        } else {
            logger.warn("Item not found for plu " + plu());
        }
    }

    /** Abstract implementation of toXML () */
    public String toXML() {
        return super.toXML(table, columnObjects(), columns, col_types);
    }

    public Vector columnObjects() {

        Vector objs = new Vector();

        objs.addElement(new Integer(plu()));
        objs.addElement(sku() == null ? "" : sku());

        return objs;
    }
}


