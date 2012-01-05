package net.sf.mercator.test.util.xmlservices;

import java.util.Vector;
import java.io.StringReader;

import java.io.IOException;

import com.globalretailtech.util.xmlservices.SQLConverter;
import com.globalretailtech.util.xmlservices.SQLTable;
import com.globalretailtech.util.xmlservices.SQLColumn;
import com.globalretailtech.util.xmlservices.SQLFunction;
import com.globalretailtech.util.xmlservices.SQLFuncArg;
import com.globalretailtech.util.xmlservices.SQLTableInsert;
import com.globalretailtech.util.xmlservices.SQLTableInsertItem;

import junit.framework.*;

public class SQLConverterTest
        extends TestCase {

    public void testCompleteDocWithComments()
            throws IOException, org.xml.sax.SAXException {
        String testText =
                "<?xml version=\"1.0\"?>\n" +
                "<!-- This is a comment -->\n" +
                "<mercatordata\n>" +
                " <!-- Some comment about the table -->\n" +
                " <table>\n" +
                "  <name>Fobar</name>\n" +
                "  <columns>\n" +
                "    <column type=\"integer\">barof</column>\n" +
                "  </columns>\n" +
                " </table>\n" +
                "\n" +
                " <!-- some comment about the procedure -->\n" +
                " <procedure>\n" +
                "  <name>proc1</name>\n" +
                "  <arguments>\n" +
                "   <arg type=\"integer\">arg1</arg>" +
                "  </arguments>\n" +
                "  <return type=\"integer\">ret1</return>\n" +
                "  <text>\n" +
                "Some Function Text\n" +
                "  </text>\n" +
                " </procedure>\n" +
                "<!-- Another Comment -->\n" +
                "</mercatordata>\n";

        SQLConverter conv = new SQLConverter(new StringReader(testText));

        assertNotNull(conv);
        assertNotNull(conv.getTables());
        assertNotNull(conv.getFunctions());

        Vector tables = conv.getTables();

        assertEquals(1, tables.size());

        SQLTable tb = (SQLTable) tables.elementAt(0);

        assertEquals("Fobar", tb.getName());
        assertEquals(1, tb.getColumnSize());

        SQLColumn col = tb.getColumn(0);
        assertEquals("barof", col.getName());
        assertEquals("integer", col.getType());
        assertEquals(false, col.isUnique());
        assertNull(col.getUniqueName());

        Vector funcs = conv.getFunctions();

        assertEquals(1, funcs.size());

        SQLFunction func = (SQLFunction) funcs.elementAt(0);
        assertEquals("proc1", func.getName());
        assertEquals("integer", func.getReturnType());
        assertEquals("ret1", func.getReturnName());
        assertTrue(func.isReturnRequired());
        assertEquals("\nSome Function Text\n  ", func.getBody());
        assertEquals(1, func.getArgSize());

        SQLFuncArg arg = func.getArg(0);
        assertEquals("integer", arg.getType());
        assertEquals("arg1", arg.getName());
    }

    public void testSimpleLoad()
            throws IOException, org.xml.sax.SAXException {
        String testText = "<mercatordata> </mercatordata>";

        SQLConverter conv = new SQLConverter(new StringReader(testText));

        assertNull(conv.getTables());
        assertNull(conv.getFunctions());
    }

    public void testTableInsertSimple()
            throws IOException, org.xml.sax.SAXException {
        String testText =
                "<mercatordata>\n" +
                "  <tableinsert table=\"datable\">\n" +
                "    <item>\n" +
                "      <col>1</col>\n" +
                "      <col>A String</col>\n" +
                "    </item>\n" +
                "  </tableinsert>\n" +
                "</mercatordata>\n";

        SQLConverter conv = new SQLConverter(new StringReader(testText));

        assertNotNull(conv);
        assertNotNull(conv.getTables());
        assertEquals(((SQLTable)conv.getTables().elementAt(0)).getName(),"datable");
        assertNull(conv.getFunctions());
        assertNotNull(conv.getTableInserts());

        Vector inserts = conv.getTableInserts();

        assertEquals(1, inserts.size());

        SQLTableInsert ins = (SQLTableInsert) inserts.elementAt(0);

        assertEquals("datable", ins.getTableName());
        assertEquals(1, ins.getInsertSize());

        SQLTableInsertItem item1 = (SQLTableInsertItem) ins.getInsertItem(0);

        assertEquals(2, item1.getColumnSize());
        assertEquals("1", item1.getColumnValue(0));
        assertNull(item1.getColumnName(0));
        assertEquals("A String", item1.getColumnValue(1));
        assertNull(item1.getColumnName(1));
    }

    public void testTableInsertWithColNames()
            throws IOException, org.xml.sax.SAXException {
        String testText =
                "<mercatordata>\n" +
                "  <tableinsert table=\"datable\">\n" +
                "    <item>\n" +
                "      <col name=\"col1\">1</col>\n" +
                "      <col>A String</col>\n" +
                "    </item>\n" +
                "  </tableinsert>\n" +
                "</mercatordata>\n";

        SQLConverter conv = new SQLConverter(new StringReader(testText));

        assertNotNull(conv);
		assertNotNull(conv.getTables());
		assertEquals(((SQLTable)conv.getTables().elementAt(0)).getName(),"datable");
        assertNull(conv.getFunctions());
        assertNotNull(conv.getTableInserts());

        Vector inserts = conv.getTableInserts();

        assertEquals(1, inserts.size());

        SQLTableInsert ins = (SQLTableInsert) inserts.elementAt(0);

        assertEquals("datable", ins.getTableName());
        assertEquals(1, ins.getInsertSize());

        SQLTableInsertItem item1 = (SQLTableInsertItem) ins.getInsertItem(0);

        assertEquals(2, item1.getColumnSize());
        assertEquals("1", item1.getColumnValue(0));
        assertEquals("col1", item1.getColumnName(0));
        assertEquals("A String", item1.getColumnValue(1));
        assertNull(item1.getColumnName(1));
    }

    public void testTableInsertWithTwoItems()
            throws IOException, org.xml.sax.SAXException {
        String testText =
                "<mercatordata>\n" +
                "  <tableinsert table=\"datable\">\n" +
                "    <item>\n" +
                "      <col name=\"col1\">1</col>\n" +
                "      <col>A String</col>\n" +
                "    </item>\n" +
                "    <item>\n" +
                "      <col name=\"a_column\">A Value</col>\n" +
                "    </item>\n" +
                "  </tableinsert>\n" +
                "</mercatordata>\n";

        SQLConverter conv = new SQLConverter(new StringReader(testText));

        assertNotNull(conv);
		assertNotNull(conv.getTables());
		assertEquals(((SQLTable)conv.getTables().elementAt(0)).getName(),"datable");
        assertNull(conv.getFunctions());
        assertNotNull(conv.getTableInserts());

        Vector inserts = conv.getTableInserts();

        assertEquals(1, inserts.size());

        SQLTableInsert ins = (SQLTableInsert) inserts.elementAt(0);

        assertEquals("datable", ins.getTableName());
        assertEquals(2, ins.getInsertSize());

        SQLTableInsertItem item1 = (SQLTableInsertItem) ins.getInsertItem(0);

        assertEquals(2, item1.getColumnSize());
        assertEquals("1", item1.getColumnValue(0));
        assertEquals("col1", item1.getColumnName(0));
        assertEquals("A String", item1.getColumnValue(1));
        assertNull(item1.getColumnName(1));

        SQLTableInsertItem item2 = (SQLTableInsertItem) ins.getInsertItem(1);

        assertEquals(1, item2.getColumnSize());
        assertEquals("A Value", item2.getColumnValue(0));
        assertEquals("a_column", item2.getColumnName(0));
    }

    public void testTableInsertWithTwoItemsNullString()
            throws IOException, org.xml.sax.SAXException {
        String testText =
                "<mercatordata>\n" +
                "  <tableinsert table=\"unique_datable\">\n" +
                "    <item>\n" +
                "      <col name=\"unique_col1\">Unique Value 1</col>\n" +
                "      <col>Unique Value 2</col>\n" +
                "    </item>\n" +
                "    <item>\n" +
                "      <col name=\"Unique_column2\">Unique Value 3</col>\n" +
                "      <col null=\"true\"/>\n" +
                "      <col></col>\n" +
                "    </item>\n" +
                "  </tableinsert>\n" +
                "</mercatordata>\n";

        SQLConverter conv = new SQLConverter(new StringReader(testText));

        assertNotNull(conv);
		assertNotNull(conv.getTables());
		assertEquals(((SQLTable)conv.getTables().elementAt(0)).getName(),"unique_datable");
        assertNull(conv.getFunctions());
        assertNotNull(conv.getTableInserts());

        Vector inserts = conv.getTableInserts();

        assertEquals(1, inserts.size());

        SQLTableInsert ins = (SQLTableInsert) inserts.elementAt(0);

        assertEquals("unique_datable", ins.getTableName());
        assertEquals(2, ins.getInsertSize());

        SQLTableInsertItem item1 = (SQLTableInsertItem) ins.getInsertItem(0);

        assertEquals(2, item1.getColumnSize());
        assertEquals("Unique Value 1", item1.getColumnValue(0));
        assertEquals("unique_col1", item1.getColumnName(0));
        assertEquals("Unique Value 2", item1.getColumnValue(1));
        assertNull(item1.getColumnName(1));

        SQLTableInsertItem item2 = (SQLTableInsertItem) ins.getInsertItem(1);

        assertEquals(3, item2.getColumnSize());
        assertEquals("Unique Value 3", item2.getColumnValue(0));
        assertEquals("Unique_column2", item2.getColumnName(0));
        assertNull(item2.getColumnValue(1));
        assertNull(item2.getColumnName(1));
        assertEquals("", item2.getColumnValue(2));
        assertNull(item2.getColumnName(2));
    }

    public void testSimpleTable()
            throws IOException, org.xml.sax.SAXException {
        String testText =
                "<mercatordata>\n" +
                " <table>\n" +
                "  <name>Fobar</name>\n" +
                "  <columns>\n" +
                "    <column type=\"integer\">barof</column>\n" +
                "  </columns>\n" +
                " </table>\n" +
                "</mercatordata>\n";

        SQLConverter conv = new SQLConverter(new StringReader(testText));

        assertNotNull(conv);
        assertNotNull(conv.getTables());
        assertNull(conv.getFunctions());

        Vector tables = conv.getTables();

        assertEquals(1, tables.size());

        SQLTable tb = (SQLTable) tables.elementAt(0);

        assertEquals("Fobar", tb.getName());
        assertEquals(1, tb.getColumnSize());

        SQLColumn col = tb.getColumn(0);
        assertEquals("barof", col.getName());
        assertEquals("integer", col.getType());
        assertEquals(false, col.isUnique());
        assertNull(col.getUniqueName());
    }

    public void testTwoColumnTable()
            throws IOException, org.xml.sax.SAXException {
        String testText =
                "<mercatordata>\n" +
                " <table>\n" +
                "  <name>Bleh</name>\n" +
                "  <columns>\n" +
                "    <column type=\"integer\">blah</column>\n" +
                "    <column type=\"varchar(32)\">bloh</column>\n" +
                "  </columns>\n" +
                " </table>\n" +
                "</mercatordata>\n";

        SQLConverter conv = new SQLConverter(new StringReader(testText));

        assertNotNull(conv);
        assertNotNull(conv.getTables());
        assertNull(conv.getFunctions());

        Vector tables = conv.getTables();

        assertEquals(1, tables.size());

        SQLTable tb = (SQLTable) tables.elementAt(0);

        assertEquals("Bleh", tb.getName());
        assertEquals(2, tb.getColumnSize());

        SQLColumn col1 = tb.getColumn(0);
        assertEquals("blah", col1.getName());
        assertEquals("integer", col1.getType());
        assertEquals(false, col1.isUnique());
        assertNull(col1.getUniqueName());

        SQLColumn col2 = tb.getColumn(1);
        assertEquals("bloh", col2.getName());
        assertEquals("varchar(32)", col2.getType());
        assertEquals(false, col2.isUnique());
        assertNull(col2.getUniqueName());
    }

    public void testUniqueColumn()
            throws IOException, org.xml.sax.SAXException {
        String testText =
                "<mercatordata>\n" +
                " <table>\n" +
                "  <name>Fobar</name>\n" +
                "  <columns>\n" +
                "    <column type=\"integer\" unique=\"true\">barof</column>\n" +
                "  </columns>\n" +
                " </table>\n" +
                "</mercatordata>\n";

        SQLConverter conv = new SQLConverter(new StringReader(testText));

        assertNotNull(conv);
        assertNotNull(conv.getTables());
        assertNull(conv.getFunctions());

        Vector tables = conv.getTables();

        assertEquals(1, tables.size());

        SQLTable tb = (SQLTable) tables.elementAt(0);

        assertEquals("Fobar", tb.getName());
        assertEquals(1, tb.getColumnSize());

        SQLColumn col = tb.getColumn(0);
        assertEquals("barof", col.getName());
        assertEquals("integer", col.getType());
        assertTrue(col.isUnique());
        assertNull(col.getUniqueName());
    }

    public void testUniqueColumnWithName()
            throws IOException, org.xml.sax.SAXException {
        String testText =
                "<mercatordata>\n" +
                " <table>\n" +
                "  <name>Fobar</name>\n" +
                "  <columns>\n" +
                "    <column type=\"integer\" unique=\"beebop\">barof</column>\n" +
                "  </columns>\n" +
                " </table>\n" +
                "</mercatordata>\n";

        SQLConverter conv = new SQLConverter(new StringReader(testText));

        assertNotNull(conv);
        assertNotNull(conv.getTables());
        assertNull(conv.getFunctions());

        Vector tables = conv.getTables();

        assertEquals(1, tables.size());

        SQLTable tb = (SQLTable) tables.elementAt(0);

        assertEquals("Fobar", tb.getName());
        assertEquals(1, tb.getColumnSize());

        SQLColumn col = tb.getColumn(0);
        assertEquals("barof", col.getName());
        assertEquals("integer", col.getType());
        assertTrue(col.isUnique());
        assertEquals("beebop", col.getUniqueName());
    }

    public void testSimpleFunction()
            throws IOException, org.xml.sax.SAXException {
        String testText =
                "<mercatordata>\n" +
                " <procedure>\n" +
                "  <name>proc1</name>\n" +
                "  <arguments>\n" +
                "   <arg type=\"integer\">arg1</arg>" +
                "  </arguments>\n" +
                "  <return type=\"integer\">ret1</return>\n" +
                "  <text>\n" +
                "Some Function Text\n" +
                "  </text>\n" +
                " </procedure>\n" +
                "</mercatordata>\n";

        SQLConverter conv = new SQLConverter(new StringReader(testText));

        assertNotNull(conv);
        assertNull(conv.getTables());
        assertNotNull(conv.getFunctions());

        Vector funcs = conv.getFunctions();

        assertEquals(1, funcs.size());

        SQLFunction func = (SQLFunction) funcs.elementAt(0);
        assertEquals("proc1", func.getName());
        assertEquals("integer", func.getReturnType());
        assertEquals("ret1", func.getReturnName());
        assertTrue(func.isReturnRequired());
        assertEquals("\nSome Function Text\n  ", func.getBody());
        assertEquals(1, func.getArgSize());

        SQLFuncArg arg = func.getArg(0);
        assertEquals("integer", arg.getType());
        assertEquals("arg1", arg.getName());
    }

    public void testMoreArgFunction()
            throws IOException, org.xml.sax.SAXException {
        String testText =
                "<mercatordata>\n" +
                " <procedure>\n" +
                "  <name>proc1</name>\n" +
                "  <arguments>\n" +
                "   <arg type=\"integer\">arg1</arg>" +
                "   <arg type=\"varchar(20)\">arg2</arg>" +
                "  </arguments>\n" +
                "  <return type=\"integer\">ret1</return>\n" +
                "  <text>\n" +
                "Some Function Text\n" +
                "  </text>\n" +
                " </procedure>\n" +
                "</mercatordata>\n";

        SQLConverter conv = new SQLConverter(new StringReader(testText));

        assertNotNull(conv);
        assertNull(conv.getTables());
        assertNotNull(conv.getFunctions());

        Vector funcs = conv.getFunctions();

        assertEquals(1, funcs.size());

        SQLFunction func = (SQLFunction) funcs.elementAt(0);
        assertEquals("proc1", func.getName());
        assertEquals("integer", func.getReturnType());
        assertEquals("ret1", func.getReturnName());
        assertTrue(func.isReturnRequired());
        assertEquals("\nSome Function Text\n  ", func.getBody());
        assertEquals(2, func.getArgSize());

        SQLFuncArg arg1 = func.getArg(0);
        assertEquals("integer", arg1.getType());
        assertEquals("arg1", arg1.getName());

        SQLFuncArg arg2 = func.getArg(1);
        assertEquals("varchar(20)", arg2.getType());
        assertEquals("arg2", arg2.getName());
    }

    public void testFunctionNoRequiredReturn()
            throws IOException, org.xml.sax.SAXException {
        String testText =
                "<mercatordata>\n" +
                " <procedure>\n" +
                "  <name>proc1</name>\n" +
                "  <arguments>\n" +
                "   <arg type=\"integer\">arg1</arg>" +
                "  </arguments>\n" +
                "  <return type=\"integer\" required=\"false\">ret1</return>\n" +
                "  <text>\n" +
                "Some Function Text\n" +
                "  </text>\n" +
                " </procedure>\n" +
                "</mercatordata>\n";

        SQLConverter conv = new SQLConverter(new StringReader(testText));

        assertNotNull(conv);
        assertNull(conv.getTables());
        assertNotNull(conv.getFunctions());

        Vector funcs = conv.getFunctions();

        assertEquals(1, funcs.size());

        SQLFunction func = (SQLFunction) funcs.elementAt(0);
        assertEquals("proc1", func.getName());
        assertEquals("integer", func.getReturnType());
        assertEquals("ret1", func.getReturnName());
        assertEquals(false, func.isReturnRequired());
        assertEquals("\nSome Function Text\n  ", func.getBody());
        assertEquals(1, func.getArgSize());

        SQLFuncArg arg1 = func.getArg(0);
        assertEquals("integer", arg1.getType());
        assertEquals("arg1", arg1.getName());
    }

    public void testFunctionComplexText()
            throws IOException, org.xml.sax.SAXException {
        String testText =
                "<mercatordata>\n" +
                " <procedure>\n" +
                "  <name>proc1</name>\n" +
                "  <arguments>\n" +
                "   <arg type=\"integer\">arg1</arg>" +
                "  </arguments>\n" +
                "  <return type=\"integer\">ret1</return>\n" +
                "  <text type=\"type1\">\n" +
                "Some Function Text\n" +
                "  </text>\n" +
                "  <text type=\"type2\">\n" +
                "Some Other Function Text\n" +
                "  </text>\n" +
                " </procedure>\n" +
                "</mercatordata>\n";

        SQLConverter conv = new SQLConverter(new StringReader(testText));

        assertNotNull(conv);
        assertNull(conv.getTables());
        assertNotNull(conv.getFunctions());

        Vector funcs = conv.getFunctions();

        assertEquals(1, funcs.size());

        SQLFunction func = (SQLFunction) funcs.elementAt(0);
        assertEquals("proc1", func.getName());
        assertEquals("integer", func.getReturnType());
        assertEquals("ret1", func.getReturnName());
        assertTrue(func.isReturnRequired());
        assertNull(func.getBody());
        assertEquals("\nSome Function Text\n  ", func.getBodyFor("type1"));
        assertEquals("\nSome Other Function Text\n  ",
                func.getBodyFor("type2"));
        assertEquals(1, func.getArgSize());

        SQLFuncArg arg1 = func.getArg(0);
        assertEquals("integer", arg1.getType());
        assertEquals("arg1", arg1.getName());
    }

    public SQLConverterTest(String name) {
        super(name);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(SQLConverterTest.class);
    }

}
