package com.globalretailtech.util.xmlservices;

import java.text.MessageFormat;
import java.util.Vector;
import java.util.Enumeration;
import java.io.FileReader;

import com.globalretailtech.util.xmlservices.SQLConverter;
import com.globalretailtech.util.xmlservices.SQLTable;
import com.globalretailtech.util.xmlservices.SQLFunction;
import com.globalretailtech.util.xmlservices.SQLFuncArg;
import com.globalretailtech.util.xmlservices.SQLColumn;


public class InterbaseSQLConverter
        extends CommonImplSQLConverter {
    public static final String INTERBASE_TAG = "interbase";

    private static MessageFormat genFormatter;
    private static MessageFormat triggerFormatter;
    private static MessageFormat funcFormatter;

    static {
        genFormatter = new MessageFormat(
                "commit;\n" +
                "delete from rdb$generators" +
                " where rdb$generator_name = ''{0}'';\n" +
                "commit;\n" +
                "commit;\n" +
                "create generator {0};\n");
        triggerFormatter = new MessageFormat(
                "set term !! ;\n" +
                "create trigger {1}_insert for {1}\n" +
                "before insert position 0\n" +
                "as begin\n" +
                "new.{2} = gen_id({0}, 1);\n" +
                "end !!\n" +
                "commit !!\n" +
                "set term ; !!\n");
        funcFormatter = new MessageFormat(
                "set term !! ;\n" +
                "create procedure {0} (\n" +
                "{1}\n" +
                ")\n" +
                "{2}" +
                "  as\n" +
                "{3} !!\n" +
                "set term ; !!\n");
    }

    public InterbaseSQLConverter(SQLConverter conv) {
        super(conv);
    }

    public InterbaseSQLConverter(SQLConverter[] conv) {
        super(conv);
    }

    protected String generatePreSQL(SQLConverter[] convs) {
        Vector togen = new Vector();

        for (int i = 0; i < convs.length; i++) {
            SQLConverter conv = convs[i];
            Vector tbls = conv.getTables();
            if (tbls != null) {
                for (Enumeration e = tbls.elements(); e.hasMoreElements();) {
                    UniqueData ud = grabUniqueData((SQLTable) e.nextElement());
                    if (ud != null && !togen.contains(ud.genname)) {
                        togen.add(ud.genname);
                    }
                }
            }
        }

        StringBuffer ret = new StringBuffer();
        for (Enumeration e = togen.elements(); e.hasMoreElements();) {
            ret.append(genFormatter.format(new Object[]{e.nextElement()}));
        }
        return ret.toString();
    }

    private class UniqueData {
        public String colname;
        public String genname;

        public UniqueData(String c, String g) {
            colname = c;
            genname = g;
        }
    }

    private String generateUniqueName(String tableName, String columnName) {
        return "gen_" + tableName + "_" + columnName;
    }

    private UniqueData grabUniqueData(SQLTable tb) {
        for (int i = 0; i < tb.getColumnSize(); i++) {
            SQLColumn col = tb.getColumn(i);
            if (col.isUnique()) {
                String genname;
                if (col.getUniqueName() == null) {
                    genname = generateUniqueName(tb.getName(), col.getName());
                } else {
                    genname = col.getUniqueName();
                }
                return new UniqueData(col.getName(), genname);
            }
        }
        return null;
    }

    protected String formatTable(SQLTable tb) {
        UniqueData ud = grabUniqueData(tb);
        StringBuffer ret = new StringBuffer();
        ret.append(genTableString(tb.getName(),
                convertColumnsToString(tb)));

        if (ud != null) {
            ret.append(triggerFormatter.format(new Object[]{
                ud.genname, tb.getName(), ud.colname}));
        }
        return ret.toString();
    }

    private String getReturnString(SQLFunction func) {
        if (func.isReturnRequired()) {
            return "  returns (" + func.getReturnName() + " " +
                    func.getReturnType() + ")\n";
        } else {
            return "";
        }
    }

    protected String formatFunction(SQLFunction func) {
        return funcFormatter.format(new Object[]{
            func.getName(), convertFuncArgsToString(func),
            getReturnString(func), getSpecificBody(func, INTERBASE_TAG)
        });
    }

    private String convertFuncArgsToString(SQLFunction func) {
        StringBuffer ret = new StringBuffer();
        for (int i = 0; i < func.getArgSize(); i++) {
            SQLFuncArg arg = func.getArg(i);
            ret.append("  ");
            ret.append(arg.getName());
            ret.append(" ");
            ret.append(arg.getType());
            if (i != (func.getArgSize() - 1)) {
                ret.append(",\n");
            }
        }
        return ret.toString();
    }

    protected String convertColumnsToString(SQLTable tb) {
        StringBuffer ret = new StringBuffer();
        for (int i = 0; i < tb.getColumnSize(); i++) {
            SQLColumn col = tb.getColumn(i);
            ret.append("  ");
            ret.append(col.getName());
            ret.append(" ");
            ret.append(col.getType());
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
        InterbaseSQLConverter gen = new InterbaseSQLConverter(convs);
        System.out.println(gen.generateCreationSQL());
    }

}
