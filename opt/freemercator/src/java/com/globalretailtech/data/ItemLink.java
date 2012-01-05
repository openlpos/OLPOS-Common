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
 * Item link implements additional items linked to an actual
 * item (such as add milk for a coffe shop, or pants and a belt
 * for a clothier).
 *
 * @author  Quentin Olson
 */
public class ItemLink extends DBRecord {

    private static String table = "item_link";
    private static String[] columns = {"link_id",
                                       "sku",
                                       "sku_link"};

    private static int[] col_types = {DBRecord.INT,
                                      DBRecord.INT,
                                      DBRecord.STRING,
                                      DBRecord.STRING};

    private int linkid;
    private String sku;
    private String skulink;

    public ItemLink() {
    }

    public int linkID() {
        return linkid;
    }

    public String sku() {
        return sku;
    }

    public String skuLink() {
        return skulink;
    }

    public void setLinkID(int value) {
        linkid = value;
    }

    public void setSku(String value) {
        sku = value;
    }

    public void setSkuLink(String value) {
        skulink = value;
    }

    // relations

    public static String getByID(int id) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[0]);
        s.append(" = ");
        s.append(Integer.toString(id));

        return new String(s.toString());
    }

    public static String getBySKU(String sku) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[1]);
        s.append(" = '");
        s.append(sku);
        s.append("'");

        return new String(s.toString());
    }

    public static String getByLink(String skuLink) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[2]);
        s.append(" = '");
        s.append(skuLink);
        s.append("'");

        return new String(s.toString());
    }

    public DBRecord copy() {
        ItemLink b = new ItemLink();
        return b;
    }

    public void populate(ResultSet rset) {

        try {

            setLinkID(rset.getInt("link_id"));
            setSku(rset.getString("sku"));
            setSkuLink(rset.getString("sku_link"));
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

        String fetchSpec = null;
        Vector v = null;
        fetchSpec = Item.getBySKU(skuLink());

        // Using fetchNoRelations to avoid recursive item relationships

        v = Application.dbConnection().fetchNoRelations(new Item(), fetchSpec);

        if ((v != null) && (v.size() > 0)) {
            item = (Item) v.elementAt(0);
        }

    }

    /** Abstract implementation of toXML () */
    public String toXML() {
        return super.toXML(table, columnObjects(), columns, col_types);
    }

    public Vector columnObjects() {

        Vector objs = new Vector();

        objs.addElement(new Integer(linkID()));
        objs.addElement(sku() == null ? "" : sku());
        objs.addElement(skuLink() == null ? "" : skuLink());

        return objs;
    }
}


