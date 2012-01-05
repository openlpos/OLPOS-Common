package com.globalretailtech.util.xmlservices;

import java.util.Vector;
import java.util.Hashtable;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.globalretailtech.util.xmlservices.SQLHelpers;
import com.globalretailtech.util.xmlservices.SQLFuncArg;

public class SQLFunction {
    static final private String DEFAULT_TEXT_TAG = "%%DEFAULT%%";

    private String name;
    private String returnType;
    private String returnName;
    private boolean returnRequired;
    private Vector args;
    private Hashtable bodies;

    public SQLFunction(Node nd) {
        bodies = new Hashtable();

        NodeList ndlist = nd.getChildNodes();
        for (int i = 0; i < ndlist.getLength(); i++) {
            Node aNode = ndlist.item(i);
            if (aNode.getNodeName().equals("name")) {
                name = SQLHelpers.getNodeText(aNode);
            } else if (aNode.getNodeName().equals("arguments")) {
                args = extractArgs(aNode);
            } else if (aNode.getNodeName().equals("return")) {
                returnName = SQLHelpers.getNodeText(aNode);
                returnType = SQLHelpers.getAttributeByName(aNode, "type");
                returnRequired = true;
                String tmpreq =
                        SQLHelpers.getAttributeByName(aNode, "required");
                if (tmpreq != null && tmpreq.equals("false")) {
                    returnRequired = false;
                }
            } else if (aNode.getNodeName().equals("text")) {
                extractText(bodies, aNode);
            }
        }
    }

    private void extractText(Hashtable into, Node nd) {
        String contents = SQLHelpers.getNodeText(nd);
        String type = SQLHelpers.getAttributeByName(nd, "type");
        if (type == null) {
            into.put(DEFAULT_TEXT_TAG, contents);
        } else {
            into.put(type, contents);
        }
    }

    private Vector extractArgs(Node nd) {
        NodeList ndlist = nd.getChildNodes();
        Vector ret = new Vector();
        for (int i = 0; i < ndlist.getLength(); i++) {
            Node aNode = ndlist.item(i);
            if (aNode.getNodeName().equals("arg")) {
                ret.add(new SQLFuncArg(aNode));
            }
        }
        return ret;
    }

    public String getName() {
        return name;
    }

    public String getReturnType() {
        return returnType;
    }

    public String getReturnName() {
        return returnName;
    }

    public boolean isReturnRequired() {
        return returnRequired;
    }

    public String getBody() {
        return (String) bodies.get(DEFAULT_TEXT_TAG);
    }

    public String getBodyFor(String type) {
        return (String) bodies.get(type);
    }

    public int getArgSize() {
        if (args == null) {
            return 0;
        } else {
            return args.size();
        }
    }

    public SQLFuncArg getArg(int item) {
        if (getArgSize() > 0) {
            return (SQLFuncArg) args.elementAt(item);
        } else {
            return null;
        }
    }
}
