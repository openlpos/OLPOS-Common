package com.globalretailtech.util.xmlservices;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SQLHelpers {
    static public String getNodeText(Node nd) {
        StringBuffer ret = new StringBuffer();
        if (nd == null) {
            return null;
        }

        NodeList ndlist = nd.getChildNodes();

        if (ndlist == null) {
            return null;
        }

        for (int i = 0; i < ndlist.getLength(); i++) {
            Node aNode = ndlist.item(i);
            if (aNode.getNodeType() == Node.TEXT_NODE) {
                ret.append(aNode.getNodeValue());
            }
        }
        return ret.toString();
    }

    static public String getAttributeByName(Node nd, String name) {
        NamedNodeMap ndmap = nd.getAttributes();
        Node typenode = ndmap.getNamedItem(name);
        return getNodeText(typenode);
    }
}
