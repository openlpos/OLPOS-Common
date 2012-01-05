/*
 * Copyright (C) 2001 Linux Developers Group
 * <http://www.linuxdevel.com>
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

import com.globalretailtech.util.sql.*;

/**
 *
 * @author  James LewisMoss
 */
public abstract class CommonDBContext {
    protected String translateSQLArgToString(Object val) {
        if (val == null) {
            return "null";
        } else if (val instanceof String) {
            return "'" + val + "'";
        } else {
            return val.toString();
        }
    }

    protected String genStringSeparatedString(Object[] args, String sep) {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < args.length; i++) {
            s.append(translateSQLArgToString(args[i]));
            if (i != args.length - 1) {
                s.append(sep);
            }
        }
        return s.toString();
    }

    public String generateCall(SQLCall call)
            throws UnknownSQLCall {
        if (call instanceof SQLProcedureCall) {
            return generateFunctionCall((SQLProcedureCall) call);
        } else {
            throw new UnknownSQLCall(call.getClass().getName());
        }
    }

    public String generateFunctionCall(SQLProcedureCall call) {
        return generateFunctionCall(call.name, call.args);
    }

    abstract public String generateFunctionCall(String funcName,
                                                Object[] args);

}

