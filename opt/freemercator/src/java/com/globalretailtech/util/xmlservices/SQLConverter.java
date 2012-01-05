package com.globalretailtech.util.xmlservices;

import java.io.Reader;
import java.io.IOException;
import java.util.Vector;

import org.xml.sax.SAXException;
import org.xml.sax.InputSource;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.NodeList;

public class SQLConverter
        extends DOMParser {
    Vector tables;
    Vector funcs;
	Vector tis;
	Vector tupd;
	Vector tdel;

    public SQLConverter(Reader input)
            throws IOException, SAXException {
        try {
            this.init();
            this.setValidation(false);
            this.parse(new InputSource(input));
        } catch (IOException e) {
            System.err.println(e);
            throw (e);
        } catch (SAXException e) {
            System.err.println(e);
            throw (e);
        }

        extractTables();
        extractFunctions();
		extractTableInserts();
		extractTableUpdates();
		extractTableDeletes();
    }

    private void extractTables() {
        //do the normal tables
        NodeList tablelist = this.getDocument().getDocumentElement().getElementsByTagName("table");
        if (tablelist.getLength() > 0) {
            tables = new Vector();
            for (int i = 0; i < tablelist.getLength(); i++) {
                tables.add(new SQLTable(tablelist.item(i)));
            }
        }

		//do the data-based table defs, "tableinsert"
		tablelist = this.getDocument().getDocumentElement().getElementsByTagName("tableinsert");
		extractTables(tablelist);
		//do the data-based table defs, "tableupdate"
		tablelist = this.getDocument().getDocumentElement().getElementsByTagName("tableupdate");
		extractTables(tablelist);
		//do the data-based table defs, "tabledelete"
		tablelist = this.getDocument().getDocumentElement().getElementsByTagName("tabledelete");
		extractTables(tablelist);
    }

	private void extractTables(NodeList tablelist) {
		if (tablelist.getLength() > 0) {
			if (tables == null)
				tables = new Vector();
			for (int i = 0; i < tablelist.getLength(); i++) {
				tables.add(new SQLTable(SQLHelpers.getAttributeByName(tablelist.item(i), "table")));
			}
		}
	}

    private void extractFunctions() {
        NodeList funclist = this.getDocument()
                .getDocumentElement().getElementsByTagName("procedure");
        if (funclist.getLength() > 0) {
            funcs = new Vector();
            for (int i = 0; i < funclist.getLength(); i++) {
                funcs.add(new SQLFunction(funclist.item(i)));
            }
        }
    }

	private void extractTableInserts() {
		NodeList tilist = this.getDocument()
				.getDocumentElement().getElementsByTagName("tableinsert");
		if (tilist.getLength() > 0) {
			tis = new Vector();
			for (int i = 0; i < tilist.getLength(); i++) {
				tis.add(new SQLTableInsert(tilist.item(i)));
			}
		}
	}

	private void extractTableUpdates() {
		NodeList tilist = this.getDocument()
				.getDocumentElement().getElementsByTagName("tableupdate");
		if (tilist.getLength() > 0) {
			tupd = new Vector();
			for (int i = 0; i < tilist.getLength(); i++) {
				tupd.add(new SQLTableUpdate(tilist.item(i)));
			}
		}
	}
	private void extractTableDeletes() {
		NodeList tdlist = this.getDocument()
				.getDocumentElement().getElementsByTagName("tabledelete");
		if (tdlist.getLength() > 0) {
			tdel = new Vector();
			for (int i = 0; i < tdlist.getLength(); i++) {
				tdel.add(new SQLTableUpdate(tdlist.item(i)));
			}
		}
	}

    public Vector getTables() {
        return tables;
    }

    public Vector getFunctions() {
        return funcs;
    }

	public Vector getTableInserts() {
		return tis;
	}

	public Vector getTableUpdates() {
		return tupd;
	}

	public Vector getTableDeletes() {
		return tdel;
	}

}
