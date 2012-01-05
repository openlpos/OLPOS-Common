package com.globalretailtech.util.xmlservices;


import org.w3c.dom.Node;

import com.globalretailtech.util.xmlservices.SQLHelpers;

public class SQLTableUpdate extends SQLTableInsert{
    private String pk;

    public SQLTableUpdate(Node nd) {
    	super (nd);
        pk = SQLHelpers.getAttributeByName(nd, "pk");
    }

    public String getPKName() {
        return pk;
    }

}
