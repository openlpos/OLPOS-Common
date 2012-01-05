package com.globalretailtech.util.xmlservices;

import java.util.Iterator;
import java.util.Vector;
import java.sql.ResultSetMetaData;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.apache.log4j.Logger;

import com.globalretailtech.util.Application;

// import javax.xml.parsers.*;
// import org.apache.xerces.dom.*;
// import org.w3c.dom.Attr;
// import org.w3c.dom.Document;
// import org.w3c.dom.NamedNodeMap;
// import org.w3c.dom.Node;
// import org.w3c.dom.NodeList;
// import org.xml.sax.InputSource;
// import org.w3c.dom.NamedNodeMap;
// import org.xml.sax.Locator;
// import org.xml.sax.helpers.*;
// import org.apache.xerces.dom.NodeImpl;
// import org.apache.xerces.framework.XMLAttrList;
// import org.apache.xerces.parsers.DOMParser;
// import org.apache.xerces.utils.QName;

public class SQLTable {
    static Logger logger = Logger.getLogger(SQLTable.class);

    private String name;
    private Vector columns;

    public SQLTable(Node nd) {
        NodeList ndlist = nd.getChildNodes();
        for (int i = 0; i < ndlist.getLength(); i++) {
            Node aNode = ndlist.item(i);
            if (aNode.getNodeName().equals("name")) {
                name = SQLHelpers.getNodeText(aNode);
            } else if (aNode.getNodeName().equals("columns")) {
                columns = extractColumns(aNode);
            }
        }
    }

    public SQLTable(String tableName){
        this.name = tableName;

        columns = extractColumns(tableName);

    }

    private Vector extractColumns(String tableName) {
        Vector ret = new Vector();
        try {
            ResultSetMetaData md = Application.dbConnection().executeWithResult("select * from " + tableName).getMetaData();

            for (int i = 1; i <= md.getColumnCount(); i++) {
                ret.add(new SQLColumn(i, md));
            }
        } catch (Exception e){
            logger.fatal("Exception trying to get table def from MetaData", e);
        }
//        NodeList ndlist = nd.getChildNodes();
//        for (int i = 0; i < ndlist.getLength(); i++) {
//            Node aNode = ndlist.item(i);
//            if (aNode.getNodeName().equals("column")) {
//                ret.add(new SQLColumn(aNode));
//            }
//        }
        return ret;
    }

    private Vector extractColumns(Node nd) {
        Vector ret = new Vector();
        NodeList ndlist = nd.getChildNodes();
        for (int i = 0; i < ndlist.getLength(); i++) {
            Node aNode = ndlist.item(i);
            if (aNode.getNodeName().equals("column")) {
                ret.add(new SQLColumn(aNode));
            }
        }
        return ret;
    }

    public String getName() {
        return name;
    }

    public int getColumnSize() {
        if (columns == null) {
            return 0;
        } else {
            return columns.size();
        }
    }

	public SQLColumn getColumn(int item) {
		if (getColumnSize() > 0) {
			return (SQLColumn) columns.elementAt(item);
		} else {
			return null;
		}
	}

	public SQLColumn getColumn(String colName) {
		int index = 0;
		
		for (Iterator i = columns.iterator(); i.hasNext();) {
			SQLColumn col = (SQLColumn) i.next();
			if ( col.getName()!= null && col.getName().equalsIgnoreCase(colName))
				return col;
		}
		return null;
	}
}
