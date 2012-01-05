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
 * Dialog holds the root information for a POS dialog,
 * that is a sequence of events that a POS user must execute
 * to complete a given transaction.
 *
 * @author  Quentin Olson
 */
public class Dialog extends DBRecord {

    public static final int TYPE = 5;

    private static String table;
    private static String[] columns;
    private static int[] col_types;

    static {

        table = "dialog";

        columns = new String[5];

        columns[0] = "dialog_id";
        columns[1] = "config_no";
        columns[2] = "help_id";
        columns[3] = "dialog_type";
        columns[4] = "dialog_name";

        col_types = new int[5];

        col_types[0] = DBRecord.INT;
        col_types[1] = DBRecord.INT;
        col_types[2] = DBRecord.INT;
        col_types[3] = DBRecord.INT;
        col_types[4] = DBRecord.STRING;
    }

    private int dialogid;
    private int configno;
    private int helpid;
    private int dialogtype;
    private String dialogname;

    public Dialog() {
    }

    public int dialogID() {
        return dialogid;
    }

    public int configNo() {
        return configno;
    }

    public int helpID() {
        return helpid;
    }

    public int dialogType() {
        return dialogtype;
    }

    public String dialogName() {
        return dialogname;
    }

    public void setDialogID(int value) {
        dialogid = value;
    }

    public void setConfigNo(int value) {
        configno = value;
    }

    public void setHelpID(int value) {
        helpid = value;
    }

    public void setDialogType(int value) {
        dialogtype = value;
    }

    public void setConfigName(String value) {
        dialogname = value;
    }



    // fetch specs

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
        return null;
    }

    public DBRecord copy() {
        Dialog b = new Dialog();
        return b;
    }

    public void populate(ResultSet rset) {

        try {
            setDialogID(rset.getInt("dialog_id"));
            setConfigNo(rset.getInt("config_no"));
            setHelpID(rset.getInt("help_id"));
            setDialogType(rset.getInt("dialog_type"));
            setConfigName(rset.getString("dialog_name"));
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
        return configNo();
    }

    public int boType() {
        return TYPE;
    }

    public String toString() {
        return toXML();
    }

    // relations

    private Vector dialogevents;

    public Vector dialogEvents() {
        return dialogevents;
    }

    public void setDialogEvents(Vector value) {
        dialogevents = value;
    }

    public void relations() {

        String fetchSpec = null;
        Vector v = null;

        // Get menus

        fetchSpec = DialogEvent.getByDialogID(dialogID());
        setDialogEvents(Application.dbConnection().fetch(new DialogEvent(), fetchSpec));

    }

    /** Abstract implementation of toXML () */
    public String toXML() {
        return super.toXML(table, columnObjects(), columns, col_types);
    }

    public Vector columnObjects() {

        Vector objs = new Vector();

        objs.addElement(new Integer(dialogID()));
        objs.addElement(new Integer(configNo()));
        objs.addElement(new Integer(helpID()));
        objs.addElement(new Integer(dialogType()));
        objs.addElement(dialogName() == null ? "" : dialogName());

        return objs;
    }
}


