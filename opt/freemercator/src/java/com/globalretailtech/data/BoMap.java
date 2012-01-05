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
 * The Business object mapping class. Maps a
 * parent business object to a child.
 *
 * @author  Quentin Olson
 */
public class BoMap extends DBRecord {
    private static String table;
    private static String[] columns;
    private static int[] col_types;

    static {

        table = "bo_map";

        columns = new String[4];

        columns[0] = "bo_id";
        columns[1] = "obj_type";
        columns[2] = "parent_bo_id";
        columns[3] = "pobj_type";

        col_types = new int[4];

        col_types[0] = DBRecord.INT;
        col_types[1] = DBRecord.INT;
        col_types[2] = DBRecord.INT;
        col_types[3] = DBRecord.INT;
    }

    private int boid;
    private int objtype;
    private int parentboid;
    private int parentobjtype;

    /** Simple constructor */
    public BoMap() {
    }

    /** This business object id */
    public int boID() {
        return boid;
    }

    /** This business object type */
    public int objType() {
        return objtype;
    }

    /** Parent business object id */
    public int parentBoID() {
        return parentboid;
    }

    /** Parent business object type */
    public int parentObjType() {
        return parentobjtype;
    }

    public void setBoID(int value) {
        boid = value;
    }

    public void setObjType(int value) {
        objtype = value;
    }

    public void setParentBoID(int value) {
        parentboid = value;
    }

    public void setParentObjType(int value) {
        parentobjtype = value;
    }

    // relations

    private BusinessObject bo_relation;

    /** Return the business object */
    public BusinessObject businessObject() {
        return bo_relation;
    }

    public void setBusinessObject(BusinessObject value) {
        bo_relation = value;
    }

    /**
     * Creates a sql statement to select a bo_map object by ID
     */
    public static String getByID(int id) {
        return genSelectCall(table, null, new WhereArg[]{
            new WhereArg(columns[0], new Integer(id))
        }
        );
    }

    /**
     * Creates a sql statement to select a bo_map object by ID  and type
     */
    public static String getByIdAndType(int id, int type) {
        return genSelectCall(table, null, new WhereArg[]{
            new WhereArg(columns[0], new Integer(id)),
            new WhereArg(columns[1], new Integer(type)),
        }
        );
    }

    /**
     * Creates a sql statement to select all children of this object
     */
    public static String getChildrenById(int id) {
        return genSelectCall(table, null, new WhereArg[]{
            new WhereArg(columns[2], new Integer(id))
        }
        );
    }

    /**
     *Creates a sql statement to select all children of this object
     * of type BusinessUnit or Site.
     */
    public static String getChildrenById2(int id) {
        return genSelectCall(table, null,
                genAndWhere(
                        new WhereArg(columns[2],
                                new Integer(id)),
                        genOrWhere(new WhereArg(
                                columns[3],
                                new Integer(BusinessUnit.TYPE)),
                                new WhereArg(
                                        columns[3],
                                        new Integer(Site.TYPE)))));
    }

    /**
     * Creates a sql statement to select all children of this object
     * of speciied type.
     */
    public static String getChildrenByIdAndType(int id, int type) {
        return genSelectCall(table, null, new WhereArg[]{
            new WhereArg(columns[2], new Integer(id)),
            new WhereArg(columns[3], new Integer(type))
        }
        );
    }

    /** Abstract implementation of copy () */
    public DBRecord copy() {
        BoMap b = new BoMap();
        return b;
    }

    /** Abstract implementation of populate () */
    public void populate(ResultSet rset) {

        try {
            setBoID(rset.getInt("bo_id"));
            setObjType(rset.getInt("obj_type"));
            setParentBoID(rset.getInt("parent_bo_id"));
            setParentObjType(rset.getInt("pobj_type"));
        } catch (java.sql.SQLException e) {
            Application.dbConnection().setException(e);
        }
    }

    /** Abstract implementation of save () */
    public boolean save() {

        StringBuffer tmp = new StringBuffer();

        tmp.append("insert into ");
        tmp.append(table);
        tmp.append(" values (");
        tmp.append(Integer.toString(boID()));
        tmp.append(", ");
        tmp.append(Integer.toString(objType()));
        tmp.append(", ");
        tmp.append(Integer.toString(parentBoID()));
        tmp.append(", ");
        tmp.append(Integer.toString(parentObjType()));
        tmp.append(")");

        try {
            Application.dbConnection().execute(tmp.toString());
            Application.dbConnection().commit();
        } catch (java.sql.SQLException e) {
            Application.dbConnection().setException(e);
            return false;
        }
        return true;
    }

    /** Abstract implementation of update () */
    public boolean update() {
        return true;
    }

    //
    // Relations
    //

    /** Abstract implementation of relations () */
    public void relations() {

        String fetchSpec = null;
        Vector v = null;


        switch (objType()) {

            case BusinessUnit.TYPE:
                fetchSpec = BusinessUnit.getByID(boID());
                v = Application.dbConnection().fetch(new BusinessUnit(), fetchSpec);
                break;

            case Site.TYPE:
                fetchSpec = Site.getByID(boID());
                v = Application.dbConnection().fetch(new Site(), fetchSpec);
                break;

            case Pos.TYPE:
                fetchSpec = Pos.getByID(boID());
                v = Application.dbConnection().fetch(new Pos(), fetchSpec);
                break;

            case Transaction.TYPE:
                fetchSpec = Transaction.getByID(boID());
                v = Application.dbConnection().fetch(new Transaction(), fetchSpec);
                break;

            case PosConfig.TYPE: // pos
                fetchSpec = PosConfig.getByID(boID());
                v = Application.dbConnection().fetch(new PosConfig(), fetchSpec);
                break;

            default:
                break;
        }

        if ((v != null) && (v.size() > 0)) {
            setBusinessObject((BusinessObject) v.elementAt(0));
        }
    }

    /** Abstract implementation of toXML () */
    public String toXML() {
        return super.toXML(table, columnObjects(), columns, col_types);
    }

    /** Abstract implementation of toString () */
    public String toString() {
        return toXML();
    }

    public Vector columnObjects() {

        Vector objs = new Vector();

        objs.addElement(new Integer(boID()));
        objs.addElement(new Integer(objType()));
        objs.addElement(new Integer(parentBoID()));
        objs.addElement(new Integer(parentObjType()));

        return objs;
    }
}
