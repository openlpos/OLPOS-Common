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
 * Sib-menu olds an individual menu, (set of buttons).
 *
 * @author  Quentin Olson
 * @see MenuRoot
 * @see PosKey
 */
public class SubMenu extends DBRecord {

    public final static int OPER = 1;
    public final static int NAVIGATE = 2;
    public final static int ITEM_MENU = 3;
    public final static int FUNCTION_MENU = 4;
    public final static int KEYPAD = 5;
    public final static int OPER_PROMPT = 6;
    public final static int INVOICE = 7;
    public final static int CUSTOMER = 8;
    public final static int OPER_RECEIPT = 9;

    private static String table;
    private static String[] columns;
    private static int[] col_types;

    static {

        table = "sub_menu";

        columns = new String[5];

        columns[0] = "sub_menu_id";
        columns[1] = "menu_id";
        columns[2] = "sub_menu_name";

        col_types = new int[3];

        col_types[0] = DBRecord.INT;
        col_types[1] = DBRecord.INT;
        col_types[2] = DBRecord.STRING;
    }

    private int submenuid;
    private int menuid;
    private String submenuname;

    public SubMenu() {
    }

    public int subMenuID() {
        return submenuid;
    }

    public int menuID() {
        return menuid;
    }

    public String subMenuName() {
        return submenuname;
    }

    public void setSubMenuID(int value) {
        submenuid = value;
    }

    public void setMenuID(int value) {
        menuid = value;
    }

    public void setSubMenuName(String value) {
        submenuname = value;
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

    public static String getByIDandType(int id, int type) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[0]);
        s.append(" = ");
        s.append(Integer.toString(id));
        s.append(" and ");
        s.append(columns[2]);
        s.append(" = ");
        s.append(Integer.toString(type));

        return new String(s.toString());
    }

    public static String getByMenuID(int id) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[1]);
        s.append(" = ");
        s.append(Integer.toString(id));

        return new String(s.toString());
    }

    public Vector parents() {
        return null;
    }

    public DBRecord copy() {
        SubMenu b = new SubMenu();
        return b;
    }

    public void populate(ResultSet rset) {

        try {
            setSubMenuID(rset.getInt("sub_menu_id"));
            setMenuID(rset.getInt("menu_id"));
            setSubMenuName(rset.getString("sub_menu_name"));
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

    private Vector buttons;

    public Vector buttons() {
        return buttons;
    }

    public void setButtons(Vector value) {
        buttons = value;
    }

    public void relations() {

        String fetchSpec = null;
        Vector v = null;
        fetchSpec = PosKey.getBySubMenuID(subMenuID());
        v = Application.dbConnection().fetch(new PosKey(), fetchSpec);
        setButtons(v);
    }

    /** Abstract implementation of toXML () */
    public String toXML() {
        return super.toXML(table, columnObjects(), columns, col_types);
    }

    public Vector columnObjects() {

        Vector objs = new Vector();

        objs.addElement(new Integer(subMenuID()));
        objs.addElement(new Integer(menuID()));
        objs.addElement(new String(subMenuName() == null ? "" : subMenuName()));

        return objs;
    }
}


