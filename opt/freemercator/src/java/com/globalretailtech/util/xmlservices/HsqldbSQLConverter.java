package com.globalretailtech.util.xmlservices;

import java.text.MessageFormat;
import java.io.FileReader;

/*
 * Copyright (C) 2003 Casey Haakenson
 * <casey@haakenson.com>
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

public class HsqldbSQLConverter extends CommonImplSQLConverter {
    public static final String POSTGRES_TAG = "postgresql";

    private static MessageFormat funcFormatter;

    static {
        funcFormatter = new MessageFormat(
                "create function {0} (\n" +
                "{1}\n" +
                ")\n" +
                "  returns {2}\n" +
                "  as ''\n" +
                "{3}'' LANGUAGE ''plpgsql'';\n");
    }

    public HsqldbSQLConverter(SQLConverter conv) {
        super(conv);
    }

    public HsqldbSQLConverter(SQLConverter[] conv) {
        super(conv);
    }

    protected String formatTable(SQLTable tb) {
        return genTableString(tb.getName(), convertColumnsToString(tb));
    }

    protected String formatFunction(SQLFunction func) {
        return funcFormatter.format(new Object[]{
            func.getName(), convertFuncArgsToString(func),
            func.getReturnType(), getSpecificBody(func, POSTGRES_TAG)
        });
    }

    private String convertFuncArgsToString(SQLFunction func) {
        StringBuffer ret = new StringBuffer();
        for (int i = 0; i < func.getArgSize(); i++) {
            SQLFuncArg arg = func.getArg(i);
            ret.append("  /* ");
            ret.append(arg.getName());
            ret.append(" */ ");
            ret.append(arg.getType());
            if (i != (func.getArgSize() - 1)) {
                ret.append(",\n");
            }
        }
        return ret.toString();
    }

    private String convertColumnsToString(SQLTable tb) {
        StringBuffer ret = new StringBuffer();
        for (int i = 0; i < tb.getColumnSize(); i++) {
            SQLColumn col = tb.getColumn(i);
            ret.append("  ");
            ret.append(col.getName());
            ret.append(" ");
            if (col.isUnique()) {
                if (!col.getType().startsWith("integer")) {
                    System.err.println("Unique colmn not integer type: " +
                            col.getName() + "(Type: " +
                            col.getType() + ")");
                }
                ret.append("identity");
            } else {
                ret.append(col.getType());
            }
            if (i != (tb.getColumnSize() - 1)) {
                ret.append(",\n");
            } else {
                ret.append("\n");
            }
        }
        return ret.toString();
    }

    static public void main(String[] args)
            throws java.io.FileNotFoundException, java.io.IOException,
            org.xml.sax.SAXException {
        SQLConverter[] convs = new SQLConverter[args.length];
        for (int i = 0; i < args.length; i++) {
            convs[i] = new SQLConverter(new FileReader(args[i]));
        }
        PostgresSQLConverter gen = new PostgresSQLConverter(convs);
        System.out.println(gen.generateCreationSQL());
    }
}
