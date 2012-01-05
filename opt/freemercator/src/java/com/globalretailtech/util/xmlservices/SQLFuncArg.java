package com.globalretailtech.util.xmlservices;

import org.w3c.dom.Node;

import com.globalretailtech.util.xmlservices.SQLHelpers;

public class SQLFuncArg {
    private String type;
    private String name;

    public SQLFuncArg(Node nd) {
        name = SQLHelpers.getNodeText(nd);
        type = SQLHelpers.getAttributeByName(nd, "type");
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

}
