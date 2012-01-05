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
 * MenuRoot is the container for a set of menus defined
 * by SubMenu.
 *
 * @author  Quentin Olson
 */
public class MenuRoot extends DBRecord {

    private static String table;
    private static String[] columns;
    private static int[] col_types;

    static {

        table = "menu_root";

        columns = new String[9];

        columns[0] = "menu_id";
        columns[1] = "config_no";
        columns[2] = "xpos";
        columns[3] = "ypos";
        columns[4] = "height";
        columns[5] = "dwidth";
        columns[6] = "pix_height";
        columns[7] = "pix_width";
        columns[8] = "name";

        col_types = new int[9];

        col_types[0] = DBRecord.INT;
        col_types[1] = DBRecord.INT;
        col_types[2] = DBRecord.INT;
        col_types[3] = DBRecord.INT;
        col_types[4] = DBRecord.INT;
        col_types[5] = DBRecord.INT;
        col_types[6] = DBRecord.INT;
        col_types[7] = DBRecord.INT;
        col_types[8] = DBRecord.STRING;
    }

    private int menuid;
    private int configno;
    private int xloc;
    private int yloc;
    private int height;
    private int width;
    private int pixheight;
    private int pixwidth;
    private String name;

    public MenuRoot() {
    }

    public int menuID() {
        return menuid;
    }

    public int configNo() {
        return configno;
    }

    public int menuXLoc() {
        return xloc;
    }

    public int menuYLoc() {
        return yloc;
    }

    public int menuHeight() {
        return height;
    }

    public int menuWidth() {
        return width;
    }

    public int pixWidth() {
        return pixwidth;
    }

    public int pixHeight() {
        return pixheight;
    }

    public String name() {
        return name;
    }

    public void setMenuID(int value) {
        menuid = value;
    }

    public void setConfigNo(int value) {
        configno = value;
    }

    public void setMenuXLoc(int value) {
        xloc = value;
    }

    public void setMenuYLoc(int value) {
        yloc = value;
    }

    public void setMenuHeight(int value) {
        height = value;
    }

    public void setMenuWidth(int value) {
        width = value;
    }

    public void setPixWidth(int value) {
        pixwidth = value;
    }

    public void setPixHeight(int value) {
        pixheight = value;
    }

    public void setName(String value) {
        name = value;
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

    public static String getByConfigNo(int no) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[1]);
        s.append(" = ");
        s.append(Integer.toString(no));

        return new String(s.toString());
    }

    public Vector parents() {

        String fetchSpec = PosConfig.getByID(configNo());
        Vector v = Application.dbConnection().fetch(new PosConfig(), fetchSpec);
        return v;
    }

    public DBRecord copy() {
        MenuRoot b = new MenuRoot();
        return b;
    }

    public void populate(ResultSet rset) {

        try {
            setMenuID(rset.getInt("menu_id"));
            setConfigNo(rset.getInt("config_no"));
            setMenuXLoc(rset.getInt("xpos"));
            setMenuYLoc(rset.getInt("ypos"));
            setMenuHeight(rset.getInt("height"));
            setMenuWidth(rset.getInt("width"));
            setPixWidth(rset.getInt("pix_width"));
            setPixHeight(rset.getInt("pix_height"));
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

    // relations

    private Vector submenus;

    public Vector subMenus() {
        return submenus;
    }

    public void setSubMenus(Vector value) {
        submenus = value;
    }

    public void relations() {

        String fetchSpec = null;
        Vector v = null;
        fetchSpec = SubMenu.getByMenuID(menuID());
        v = Application.dbConnection().fetch(new SubMenu(), fetchSpec);
        setSubMenus(v);
    }

    /** Abstract implementation of toXML () */
    public String toXML() {
        return super.toXML(table, columnObjects(), columns, col_types);
    }

    public Vector columnObjects() {

        Vector objs = new Vector();

        objs.addElement(new Integer(menuID()));
        objs.addElement(new Integer(configNo()));
        objs.addElement(new Integer(menuXLoc()));
        objs.addElement(new Integer(menuYLoc()));
        objs.addElement(new Integer(menuHeight()));
        objs.addElement(new Integer(menuWidth()));
        objs.addElement(new Integer(pixWidth()));
        objs.addElement(new Integer(pixHeight()));
        objs.addElement(new String(name() == null ? "" : name()));

        return objs;
    }
}


