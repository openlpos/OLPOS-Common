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
 * Dialog event is the individual events in a POS dialog.
 * This includes the event class and the states which the
 * class can execute.
 *
 * @author  Quentin Olson
 */
public class DialogEvent extends DBRecord implements java.io.Serializable {

    public static final int TYPE = 5;

    private static String table;
    private static String[] columns;
    private static int[] col_types;

    static {

        table = "dialog_event";

        columns = new String[9];

        columns[0] = "dialog_event_id";
        columns[1] = "dialog_id";
        columns[2] = "dialog_seq";
        columns[3] = "help_id";
        columns[4] = "dialog_desc";
        columns[5] = "state";
        columns[6] = "event_enabled";
        columns[7] = "event_required";
        columns[8] = "vent_class";

        col_types = new int[9];

        col_types[0] = DBRecord.INT;
        col_types[1] = DBRecord.INT;
        col_types[2] = DBRecord.INT;
        col_types[3] = DBRecord.INT;
        col_types[4] = DBRecord.INT;
        col_types[5] = DBRecord.INT;
        col_types[6] = DBRecord.BOOLEAN;
        col_types[7] = DBRecord.BOOLEAN;
        col_types[8] = DBRecord.STRING;
    }

    private int dialogeventid;
    private int dialogid;
    private int dialogseq;
    private int helpid;
    private String dialogdesc;
    private int state;
    private boolean eventenabled;
    private boolean eventrequired;
    private String eventclass;

    public DialogEvent() {
    }

    public int dialogEventID() {
        return dialogeventid;
    }

    public int dialogID() {
        return dialogid;
    }

    public int dialogSeq() {
        return dialogseq;
    }

    public int helpID() {
        return helpid;
    }

    public String dialogDesc() {
        return dialogdesc;
    }

    public int state() {
        return state;
    }

    public boolean eventEnabled() {
        return eventenabled;
    }

    public boolean eventRequired() {
        return eventrequired;
    }

    public String eventClass() {
        return eventclass;
    }

    public void setDialogEventID(int value) {
        dialogeventid = value;
    }

    public void setDialogID(int value) {
        dialogid = value;
    }

    public void setDialogSeq(int value) {
        dialogseq = value;
    }

    public void setHelpID(int value) {
        helpid = value;
    }

    public void setDialogDesc(String value) {
        dialogdesc = value;
    }

    public void setState(int value) {
        state = value;
    }

    public void setEventEnabled(boolean value) {
        eventenabled = value;
    }

    public void setEventRequired(boolean value) {
        eventrequired = value;
    }

    public void setEventClass(String value) {
        eventclass = value;
    }



    // fetch specs

    public static String getByID(int id) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[0]);
        s.append(" = ");
        s.append(Integer.toString(id));
        s.append(" order by dialog_seq");

        return new String(s.toString());
    }

    public static String getByDialogID(int id) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[1]);
        s.append(" = ");
        s.append(Integer.toString(id));
        s.append(" order by dialog_seq");

        return new String(s.toString());
    }

    public Vector parents() {
        return null;
    }

    public DBRecord copy() {
        DialogEvent b = new DialogEvent();
        return b;
    }

    public void populate(ResultSet rset) {

        try {
            setDialogEventID(rset.getInt("dialog_event_id"));
            setDialogID(rset.getInt("dialog_id"));
            setDialogSeq(rset.getInt("dialog_seq"));
            setHelpID(rset.getInt("help_id"));
            setDialogDesc(rset.getString("dialog_desc"));
            setState(rset.getInt("state"));
            setEventEnabled(rset.getInt("event_enabled") > 0);
            setEventRequired(rset.getInt("event_required") > 0);
            setEventClass(rset.getString("event_class"));
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
        tmp.append(columns[5]).append(" = ").append(eventEnabled() ? 1 : 0);
        tmp.append(" where ").append(columns[0]).append(" = ").append(Integer.toString(dialogEventID()));
        try {
            Application.dbConnection().execute(tmp.toString());
            Application.dbConnection().commit();
        } catch (java.sql.SQLException e) {
            Application.dbConnection().setException(e);
            return false;
        }
        return true;
    }

    public String toString() {
        return toXML();
    }

    // relations

    public void relations() {
        return;
    }

    public String toXML() {
        return super.toXML(table, columnObjects(), columns, col_types);
    }

    public Vector columnObjects() {

        Vector objs = new Vector();

        objs.addElement(new Integer(dialogEventID()));
        objs.addElement(new Integer(dialogID()));
        objs.addElement(new Integer(dialogSeq()));
        objs.addElement(new Integer(helpID()));
        objs.addElement(dialogDesc() == null ? "" : dialogDesc());
        objs.addElement(new Integer(state()));
        objs.addElement(new Boolean(eventEnabled()));
        objs.addElement(new Boolean(eventRequired()));
        objs.addElement(eventClass() == null ? "" : eventClass());

        return objs;
    }
}


