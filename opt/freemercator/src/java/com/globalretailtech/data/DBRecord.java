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

import java.sql.ResultSet;
import java.util.Vector;


/**
 * Abstract database record class for the
 * DBContext api.
 *
 * @author  Quentin Olson
 */
public abstract class DBRecord {

    public static final int INT = 0;
    public static final int STRING = 1;
    public static final int DATE = 2;
    public static final int DATA = 3;
    public static final int BOOLEAN = 4;
    public static final int BYTE = 5;
    public static final int DOUBLE = 6;

    /**
     * Called by fetch method in DBContext to move entity
     * data from the result set to the record
     */
    public abstract void populate(ResultSet rs);

    /**
     * Called by fetch method in DBContext to service
     * any entity relationships (child records).
     */
    public abstract void relations();

    /**
     * Copies the record, used to create a new record
     * during population.
     */
    public abstract DBRecord copy();

    /**
     * Entity save.
     */
    public abstract boolean save();

    /**
     * Entity update.
     */
    public abstract boolean update();

    /**
     * Converts the entity to an XML string.
     */
    public abstract String toXML();

    /**
     * Temporary base class implementations for
     * xml import/export. Some of these will
     * eventually become abstract.
     */

    public Vector columnObjects() {
        return null;
    }

    public String toXML(String table, Vector objects, String[] names, int[] types) {

        StringBuffer xml = new StringBuffer();

        xml.append("<").append(toEntityName(table)).append(">");

        if (objects != null) {
            for (int i = 0; i < objects.size(); i++) {
                xml.append("<").append(toEntityName(names[i])).append(typeAttribute(types[i])).append(">");

                if (objects.elementAt(i) == null) {
                    xml.append("");
                } else {
                    xml.append(objects.elementAt(i).toString().trim());
                }

                xml.append("</").append(toEntityName(names[i])).append(">");
            }
        }
        xml.append("</").append(toEntityName(table)).append(">");
        return xml.toString();
    }

    private String toEntityName(String entity) {

        StringBuffer s = new StringBuffer();
        boolean cap = true;
        char[] chars = entity.toCharArray();

        for (int i = 0; i < chars.length; i++) {

            if (chars[i] == '_') {
                cap = true;
            } else {
                if (cap) {
                    s.append(String.valueOf(chars[i]).toUpperCase());
                    cap = false;
                } else {
                    s.append(String.valueOf(chars[i]));
                }
            }
        }
        return s.toString();
    }

    private String typeAttribute(int type) {
        return (" Type=\"" + type + "\"");
    }

    /**
     * end of temporary stuff
     */

    /**
     *
     */
    public String getUpdateString(int updateIndex [],
                                  Vector updateValue,
                                  String table,
                                  String columns [],
                                  int col_types [],
                                  String select) {

        StringBuffer str = new StringBuffer();

        str.append("update ").append(table).append(" set ");

        for (int i = 0; i < updateIndex.length; i++) {

            str.append(columns[updateIndex[i]]).append(" = ");

            switch (col_types[updateIndex[i]]) {

                case STRING:

                    String s = (String) updateValue.elementAt(i);
                    str.append("'").append(s).append("'");
                    break;

                case INT:

                    Integer n = (Integer) updateValue.elementAt(i);
                    str.append(n.toString());
                    break;

                case DOUBLE:

                    Double d = (Double) updateValue.elementAt(i);
                    str.append(d.toString());
                    break;

                default:
                    break;
            }

            if (i < updateIndex.length - 1)
                str.append(", ");
        }
        str.append(" where ").append(select);
        return (str.toString());
    }

    static public String translateSQLArgToString(Object val) {
        if (val == null) {
            return "0";
        } else if (val instanceof String) {
            return "'" + val + "'";
        } else {
            return val.toString();
        }
    }

    public static class WhereArg {
        String column;
        Object value;

        public WhereArg(String col, Object val) {
            column = col;
            value = val;
        }

        public String toString() {
            return column + " = " + translateSQLArgToString(value);
        }
    }

    static private String genStringSeparatedString(Object[] args, String sep) {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < args.length; i++) {
            s.append(translateSQLArgToString(args[i]));
            if (i != args.length - 1) {
                s.append(sep);
            }
        }
        return s.toString();
    }

    static public String genSelectCall(String table_name, String[] cols_to_get,
                                       WhereArg[] where_args) {
        return genSelectCall(table_name, cols_to_get,
                genStringSeparatedString(where_args, " and "));
    }

    static public String genAndWhere(Object where1, Object where2) {
        if ((!(where1 instanceof String) &&
                !(where1 instanceof WhereArg)) ||
                (!(where2 instanceof String) &&
                !(where2 instanceof WhereArg))) {
            return null;
        }
        return where1.toString() + " and " + where2.toString();
    }

    static public String genOrWhere(Object where1, Object where2) {
        if ((!(where1 instanceof String) &&
                !(where1 instanceof WhereArg)) ||
                (!(where2 instanceof String) &&
                !(where2 instanceof WhereArg))) {
            return null;
        }
        return "(" + where1.toString() + " or " + where2.toString() + ")";
    }

    static private String genColumnList(String[] args) {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < args.length; i++) {
            s.append(args[i]);
            if (i != args.length - 1) {
                s.append(", ");
            }
        }
        return s.toString();
    }

    static public String genSelectCall(String table_name, String[] cols_to_get,
                                       String where_args) {
        StringBuffer s = new StringBuffer();
        s.append("select ");
        if (cols_to_get == null) {
            s.append("* from ");
        } else {
            s.append(genColumnList(cols_to_get));
            s.append(" from ");
        }
        s.append(table_name);
        if (where_args.length() != 0) {
            s.append(" where ");
            s.append(where_args);
        }
        return new String(s.toString());
    }
}

