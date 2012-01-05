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
import java.util.Hashtable;

import com.globalretailtech.util.Application;

/**
 * The base entity for the pos configuration. Manages
 * menus, keys and pos parameters.
 *
 * @author  Quentin Olson
 */
public class PosConfig extends BusinessObject {

    public static final int TYPE = 5;

    private static String table;
    private static String[] columns;
    private static int[] col_types;

    static {

        table = "pos_config";

        columns = new String[3];

        columns[0] = "config_id";
        columns[1] = "config_no";
        columns[2] = "name";

        col_types = new int[3];

        col_types[0] = DBRecord.INT;
        col_types[1] = DBRecord.INT;
        col_types[2] = DBRecord.STRING;
    }

    private int configid;
    private int configno;
    private String configname;

    public PosConfig() {
    }

    public int configID() {
        return configid;
    }

    public int configNo() {
        return configno;
    }

    public String configName() {
        return configname;
    }

    public void setConfigID(int value) {
        configid = value;
    }

    public void setConfigNo(int value) {
        configno = value;
    }

    public void setConfigName(String value) {
        configname = value;
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

    public static String getByNo(int no) {

        StringBuffer s = new StringBuffer("select * from ");

        s.append(table);
        s.append(" where ");
        s.append(columns[1]);
        s.append(" = ");
        s.append(Integer.toString(no));

        return new String(s.toString());
    }

    public static String getAll() {

        StringBuffer s = new StringBuffer("select * from ");
        s.append(table);
        return new String(s.toString());
    }

    public Vector parents() {
        return null;
    }

    public DBRecord copy() {
        PosConfig b = new PosConfig();
        return b;
    }

    public void populate(ResultSet rset) {

        try {
            setConfigID(rset.getInt("config_id"));
            setConfigNo(rset.getInt("config_no"));
            setConfigName(rset.getString("name"));
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
        return configID();
    }

    public int boType() {
        return TYPE;
    }

    public String toString() {
        return toXML();
    }

    // relations

    private Vector containers;

    public Vector containers() {
        return containers;
    }

    public void setContainers(Vector value) {
        containers = value;
    }

    private Vector taxgroups;

    public Vector taxGroups() {
        return taxgroups;
    }

    public void setTaxGroups(Vector value) {
        taxgroups = value;
    }

    private Vector literals;

    public Vector literals() {
        return literals;
    }

    public void setLiterals(Vector value) {
        literals = value;
    }

    private Vector posparam;

    public Vector posParams() {
        return posparam;
    }

    public void setPosParams(Vector value) {
        posparam = value;
    }

    private Hashtable dialogs;

    public Hashtable dialogs() {
        return dialogs;
    }

    public void setDialogs(Hashtable value) {
        dialogs = value;
    }

    private Vector media;

    public Vector media() {
        return media;
    }

    public void setMedia(Vector value) {
        media = value;
    }

    private Vector posprofile;

    public Vector posProfile() {
        return posprofile;
    }

    public void setPosProfile(Vector value) {
        posprofile = value;
    }

    public void relations() {

        String fetchSpec = null;
        Vector v = null;

        // Get menus

        fetchSpec = MenuRoot.getByConfigNo(configNo());
        setContainers(Application.dbConnection().fetch(new MenuRoot(), fetchSpec));

        // Get taxes, one or more

        fetchSpec = TaxGroup.getByConfigNo(configNo());
        setTaxGroups(Application.dbConnection().fetch(new TaxGroup(), fetchSpec));

        for (int i = 0; i < taxGroups().size(); i++) {
            Object o = taxGroups().elementAt(i);
        }

        // Get params

        fetchSpec = PosParameter.getByConfigNo(configNo());
        setPosParams(Application.dbConnection().fetch(new PosParameter(), fetchSpec));

        // Get dialogs, put them in a hashtable for better access.

        fetchSpec = Dialog.getByConfigNo(configNo());
        Vector tmp = Application.dbConnection().fetch(new Dialog(), fetchSpec);
        setDialogs(new Hashtable());

        for (int i = 0; i < tmp.size(); i++) {
            Dialog dialog = (Dialog) tmp.elementAt(i);
            dialogs().put(dialog.dialogName(), dialog);
        }
    }

    private BoMap bomap;

    public BoMap boMap() {
        return bomap;
    }

    public void setBoMap(BoMap value) {
        bomap = value;
    }


    /** Abstract implementation of toXML () */
    public String toXML() {
        return super.toXML(table, columnObjects(), columns, col_types);
    }

    public Vector columnObjects() {

        Vector objs = new Vector();

        objs.addElement(new Integer(configID()));
        objs.addElement(new Integer(configNo()));
        objs.addElement(new String(configName() == null ? "" : configName()));

        return objs;
    }
}


