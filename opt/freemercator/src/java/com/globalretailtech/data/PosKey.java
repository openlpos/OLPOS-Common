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
 * A key definition, can be a for a physical keyboard,
 * or a graphic menu.
 *
 * @author  Quentin Olson
 * @see SubMenu
 */
public class PosKey extends DBRecord {

    public static final int POS_KEY = 0;
    public static final int POS_GUI = 1;

    private static String table;
    private static String[] columns;
    private static int[] col_types;

    static {

        table = "pos_key";

        columns = new String[16];

        columns[0] = "key_id";
        columns[1] = "sub_menu_id";
        columns[2] = "key_text";
        columns[3] = "key_type";
        columns[4] = "key_val";
        columns[5] = "key_code";
        columns[6] = "device_type";
        columns[7] = "x_loc";
        columns[8] = "y_loc";
        columns[9] = "key_height";
        columns[10] = "key_width";
        columns[11] = "fg_color";
        columns[12] = "bg_color";
        columns[13] = "attr";
        columns[14] = "logout_disable";
        columns[15] = "key_class";

        col_types = new int[16];

        col_types[0] = DBRecord.INT;
        col_types[1] = DBRecord.INT;
        col_types[2] = DBRecord.STRING;
        col_types[3] = DBRecord.INT;
        col_types[4] = DBRecord.INT;
        col_types[5] = DBRecord.INT;
        col_types[6] = DBRecord.INT;
        col_types[7] = DBRecord.INT;
        col_types[8] = DBRecord.INT;
        col_types[9] = DBRecord.INT;
        col_types[10] = DBRecord.INT;
        col_types[11] = DBRecord.INT;
        col_types[12] = DBRecord.INT;
        col_types[13] = DBRecord.INT;
        col_types[14] = DBRecord.BOOLEAN;
        col_types[15] = DBRecord.STRING;
    }

    private int keyid;
    private int submenuid;
    private String keytext;
    private int keytype;
    private int keyval;
    private int keycode;
    private int devicetype;
    private int xloc;
    private int yloc;
    private int keyheight;
    private int keywidth;
    private int fgcolor;
    private int bgcolor;
    private int attr;
    private boolean logoutdisable;
    private String keyclass;

    public PosKey() {
        setKeyID(0);
        setSubMenuID(0);
        setKeyText("???");
        setKeyType(0);
        setKeyVal(0);
        setKeyCode(0);
        setDeviceType(0);
        setXLoc(0);
        setYLoc(0);
        setKeyHeight(1);
        setKeyWidth(1);
        setFgColor(0);
        setBgColor(0);
        setAttr(0);
        setLogoutDisable(true);
        setKeyClass("com.globalretailtech.pos.operators.Null");

    }

    public int keyID() {
        return keyid;
    }

    public int subMenuID() {
        return submenuid;
    }

    public String keyText() {
        return keytext;
    }

    public int keyType() {
        return keytype;
    }

    public int keyVal() {
        return keyval;
    }

    public int keyCode() {
        return keycode;
    }

    public int deviceType() {
        return devicetype;
    }

    public int xLoc() {
        return xloc;
    }

    public int yLoc() {
        return yloc;
    }

    public int keyHeight() {
        return keyheight;
    }

    public int keyWidth() {
        return keywidth;
    }

    public int fgColor() {
        return fgcolor;
    }

    public int bgColor() {
        return bgcolor;
    }

    public int attr() {
        return attr;
    }

    public boolean logoutDisable() {
        return logoutdisable;
    }

    public String keyClass() {
        return keyclass;
    }

    public void setKeyID(int value) {
        keyid = value;
    }

    public void setSubMenuID(int value) {
        submenuid = value;
    }

    public void setKeyText(String value) {
        keytext = value;
    }

    public void setKeyType(int value) {
        keytype = value;
    }

    public void setKeyVal(int value) {
        keyval = value;
    }

    public void setKeyCode(int value) {
        keycode = value;
    }

    public void setDeviceType(int value) {
        devicetype = value;
    }

    public void setXLoc(int value) {
        xloc = value;
    }

    public void setYLoc(int value) {
        yloc = value;
    }

    public void setKeyHeight(int value) {
        keyheight = value;
    }

    public void setKeyWidth(int value) {
        keywidth = value;
    }

    public void setFgColor(int value) {
        fgcolor = value;
    }

    public void setBgColor(int value) {
        bgcolor = value;
    }

    public void setAttr(int value) {
        attr = value;
    }

    public void setLogoutDisable(boolean value) {
        logoutdisable = value;
    }

    public void setKeyClass(String value) {
        keyclass = value;
    }


    public static String getByID(int id) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[0]);
        s.append(" = ");
        s.append(Integer.toString(id));
        s.append(" order by ");
        s.append(columns[0]);

        return new String(s.toString());
    }

    public static String getBySubMenuID(int submenuid) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[1]);
        s.append(" = ");
        s.append(Integer.toString(submenuid));

        return new String(s.toString());
    }

    public Vector parents() {
        return null;
    }

    public DBRecord copy() {
        PosKey b = new PosKey();
        return b;
    }

    public void populate(ResultSet rset) {

        try {
            setKeyID(rset.getInt("key_id"));
            setSubMenuID(rset.getInt("sub_menu_id"));
            setKeyText(rset.getString("key_text"));
            setKeyType(rset.getInt("key_type"));
            setKeyVal(rset.getInt("key_val"));
            setKeyCode(rset.getInt("key_code"));
            setDeviceType(rset.getInt("device_type"));
            setXLoc(rset.getInt("x_loc"));
            setYLoc(rset.getInt("y_loc"));
            setKeyHeight(rset.getInt("key_height"));
            setKeyWidth(rset.getInt("key_width"));
            setFgColor(rset.getInt("fg_color"));
            setBgColor(rset.getInt("bg_color"));
            setAttr(rset.getInt("attr"));
            setLogoutDisable(rset.getInt("logout_disable") > 0);
            setKeyClass(rset.getString("key_class"));
        } catch (java.sql.SQLException e) {
            Application.dbConnection().setException(e);

        }
    }

    public boolean save() {
        return true;
    }

    public boolean update() {

        StringBuffer tmp = new StringBuffer();

        tmp.append("update ").append(table).append(" set ");
        tmp.append("sub_menu_id = ").append(Integer.toString(subMenuID()));
        tmp.append(", key_text = '").append(keyText());
        tmp.append("', key_type = ").append(Integer.toString(keyType()));
        tmp.append(", key_val = ").append(Integer.toString(keyVal()));
        tmp.append(", key_code = ").append(Integer.toString(keyCode()));
        tmp.append(", device_type = ").append(Integer.toString(deviceType()));
        tmp.append(", x_loc = ").append(Integer.toString(xLoc()));
        tmp.append(", y_loc = ").append(Integer.toString(yLoc()));
        tmp.append(", key_height = ").append(Integer.toString(keyHeight()));
        tmp.append(", key_width = ").append(Integer.toString(keyWidth()));
        tmp.append(", fg_color = ").append(Integer.toString(fgColor()));
        tmp.append(", bg_color = ").append(Integer.toString(bgColor()));
        tmp.append(", attr = ").append(Integer.toString(attr()));
        tmp.append(", logout_disable = ").append(Integer.toString(logoutDisable() ? 1 : 0));
        tmp.append(", key_class = '").append(keyClass());

        tmp.append("'  where ").append("key_id = ").append(Integer.toString((keyID())));

        try {
            Application.dbConnection().execute(tmp.toString());
            Application.dbConnection().commit();
        } catch (SQLException e) {
            Application.dbConnection().setException(e);
            return false;
        }
        return true;
    }

    public String toString() {
        return toXML();
    }

    // relations

    private Vector menus;

    public Vector Menus() {
        return menus;
    }

    public void setMenus(Vector value) {
        menus = value;
    }

    public void relations() {
    }

    // other

    /** Abstract implementation of toXML () */
    public String toXML() {
        return super.toXML(table, columnObjects(), columns, col_types);
    }

    public Vector columnObjects() {

        Vector objs = new Vector();

        objs.addElement(new Integer(keyID()));
        objs.addElement(new Integer(subMenuID()));
        objs.addElement(new String(keyText()));
        objs.addElement(new Integer(keyType()));
        objs.addElement(new Integer(keyVal()));
        objs.addElement(new Integer(keyCode()));
        objs.addElement(new Integer(deviceType()));
        objs.addElement(new Integer(xLoc()));
        objs.addElement(new Integer(yLoc()));
        objs.addElement(new Integer(keyHeight()));
        objs.addElement(new Integer(keyWidth()));
        objs.addElement(new Integer(fgColor()));
        objs.addElement(new Integer(bgColor()));
        objs.addElement(new Integer(attr()));
        objs.addElement(new Boolean(logoutDisable()));
        objs.addElement(new String(keyClass()));

        return objs;
    }
}


