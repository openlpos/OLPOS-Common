package com.globalretailtech.util.xmlservices;

import java.util.Vector;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.globalretailtech.util.xmlservices.SQLHelpers;
import com.globalretailtech.util.xmlservices.SQLTableInsertItem;

public class SQLTableInsert {
    private String tablename;
    private Vector items;

    public SQLTableInsert(Node nd) {
        tablename = SQLHelpers.getAttributeByName(nd, "table");
        NodeList ndlist = nd.getChildNodes();
        items = new Vector();
        for (int i = 0; i < ndlist.getLength(); i++) {
            Node aNode = ndlist.item(i);
            if (aNode.getNodeName().equals("item")) {
                items.add(new SQLTableInsertItem(aNode));
            }
        }
    }

    public String getTableName() {
        return tablename;
    }

    public int getInsertSize() {
        if (items == null) {
            return 0;
        } else {
            return items.size();
        }
    }

    public SQLTableInsertItem getInsertItem(int item) {
        if (getInsertSize() > 0) {
            return (SQLTableInsertItem) items.elementAt(item);
        } else {
            return null;
        }
    }
}
