package com.globalretailtech.util.xmlservices;

import org.w3c.dom.Node;

import com.globalretailtech.util.xmlservices.SQLHelpers;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class SQLColumn {
    private String name;
    private String type;
    private boolean unique;
    private String uniqueName;

    public SQLColumn(Node nd) {
        if (nd == null) {
            System.err.println("Null node to SQLColumn constructor");
        }
        name = SQLHelpers.getNodeText(nd);
        type = SQLHelpers.getAttributeByName(nd, "type");
        uniqueName = SQLHelpers.getAttributeByName(nd, "unique");
        if (uniqueName == null) {
            unique = false;
        } else {
            unique = true;
            if (uniqueName.equals("true")) {
                uniqueName = null;
            }
        }
    }

    public SQLColumn(int col, ResultSetMetaData md) throws SQLException {
        this.name = md.getColumnName(col);
        this.type = md.getColumnTypeName(col);
        unique = md.isAutoIncrement(col);//can't always trust this.

//        name = SQLHelpers.getNodeText(nd);
//        type = SQLHelpers.getAttributeByName(nd, "type");
//        uniqueName = SQLHelpers.getAttributeByName(nd, "unique");
//        if (uniqueName == null) {
//            unique = false;
//        } else {
//            unique = true;
//            if (uniqueName.equals("true")) {
//                uniqueName = null;
//            }
//        }
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isUnique() {
        return unique;
    }

    public String getUniqueName() {
        return uniqueName;
    }

}
