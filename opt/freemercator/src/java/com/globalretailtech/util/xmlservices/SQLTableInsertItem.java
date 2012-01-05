package com.globalretailtech.util.xmlservices;

import java.util.Iterator;
import java.util.Vector;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class SQLTableInsertItem {
    private Vector columns;

    private class ItemColumn {
        public String columnName;
        public String columnValue;

        public ItemColumn(String cn, String cv) {
            columnName = cn;
            columnValue = cv;
        }
    }

    public SQLTableInsertItem(Node nd) {
        NodeList ndlist = nd.getChildNodes();
        columns = new Vector();
        for (int i = 0; i < ndlist.getLength(); i++) {
            Node aNode = ndlist.item(i);
            if (aNode.getNodeName().equals("col")) {
                String nullStr = SQLHelpers.getAttributeByName(aNode, "null");
                if (nullStr != null && nullStr.equals("true")) {
                    columns.add(
                            new ItemColumn(
                                    SQLHelpers.getAttributeByName(aNode, "name"),
                                    null));
                } else {
                    columns.add(
                            new ItemColumn(
                                    SQLHelpers.getAttributeByName(aNode, "name"),
                                    SQLHelpers.getNodeText(aNode)));
                }
            }
        }
    }

    public int getColumnSize() {
        if (columns == null) {
            return 0;
        } else {
            return columns.size();
        }
    }

	public String getColumnValue(String colName) {
		int index = 0;
		for (Iterator i = columns.iterator(); i.hasNext();) {
			ItemColumn col = (ItemColumn) i.next();
			if ( col.columnName != null && col.columnName.equalsIgnoreCase(colName))
				return getColumnValue (index);
			index++;	
		}
		return null;
	}
	
	public String getColumnValue(int item) {
        if (getColumnSize() > 0 && item <= (getColumnSize() - 1)) {
            return ((ItemColumn) columns.elementAt(item)).columnValue;
        } else {
            return null;
        }
    }

    public String getColumnName(int item) {
        if (getColumnSize() > 0 && item <= (getColumnSize() - 1)) {
            return ((ItemColumn) columns.elementAt(item)).columnName;
        } else {
            return null;
        }
    }

	public boolean containsColumn (String colName) {
		for (Iterator i = columns.iterator(); i.hasNext();) {
			ItemColumn col = (ItemColumn) i.next();
			if ( col.columnName != null && col.columnName.equalsIgnoreCase(colName))
				return true;
		}
		return false;
	}
}
