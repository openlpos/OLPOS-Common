package com.globalretailtech.util.xmlservices;

import java.text.MessageFormat;
import java.io.FileReader;

import com.globalretailtech.util.xmlservices.SQLConverter;
import com.globalretailtech.util.xmlservices.SQLTable;
import com.globalretailtech.util.xmlservices.SQLFunction;
import com.globalretailtech.util.xmlservices.SQLFuncArg;
import com.globalretailtech.util.xmlservices.SQLColumn;


public class PostgresSQLConverter
        extends CommonImplSQLConverter {
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

    public PostgresSQLConverter(SQLConverter conv) {
        super(conv);
    }

    public PostgresSQLConverter(SQLConverter[] conv) {
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
                ret.append("serial");
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
