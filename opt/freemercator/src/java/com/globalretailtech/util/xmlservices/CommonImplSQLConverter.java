package com.globalretailtech.util.xmlservices;

import java.text.MessageFormat;
import java.util.Vector;
import java.util.Enumeration;

import com.globalretailtech.util.xmlservices.SQLConverter;
import com.globalretailtech.util.xmlservices.SQLTable;
import com.globalretailtech.util.xmlservices.SQLFunction;

abstract public class CommonImplSQLConverter {
    private static MessageFormat tableFormatter;

    static {
        tableFormatter = new MessageFormat("create table {0} (\n{1});\n");
    }

    static public String genTableString(String tableName, String tableArgs) {
        return tableFormatter.format(new Object[]{tableName, tableArgs});
    }

    private SQLConverter[] convs;

    public CommonImplSQLConverter(SQLConverter conv) {
        this(new SQLConverter[]{conv});
    }

    public CommonImplSQLConverter(SQLConverter[] convs) {
        this.convs = convs;
    }

    public String generateCreationSQL() {
        StringBuffer ret = new StringBuffer();

        ret.append(generatePreSQL(convs));

        for (int i = 0; i < convs.length; i++) {
            Vector tables = convs[i].getTables();
            if (tables != null) {
                for (Enumeration e = tables.elements();
                     e.hasMoreElements();) {
                    SQLTable tb = (SQLTable) e.nextElement();
                    ret.append(formatTable(tb));
                }
            }

            Vector funcs = convs[i].getFunctions();
            if (funcs != null) {
                for (Enumeration e = funcs.elements(); e.hasMoreElements();) {
                    SQLFunction func = (SQLFunction) e.nextElement();
                    ret.append(formatFunction(func));
                }
            }
        }
        return ret.toString();
    }

    protected String generatePreSQL(SQLConverter[] convs) {
        return "";
    }

    abstract protected String formatTable(SQLTable tb);

    abstract protected String formatFunction(SQLFunction func);

    protected String getSpecificBody(SQLFunction func, String tag) {
        String ret = func.getBodyFor(tag);
        if (ret == null) {
            System.err.println("Body for " + tag + " null");
            throw new NullPointerException();
        }
        return ret;
    }

}
