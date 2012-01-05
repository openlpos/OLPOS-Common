package net.sf.mercator.test.util.xmlservices;

import java.io.StringReader;

import java.io.IOException;

import net.sf.mercator.test.MercatorBaseTest;

import org.xml.sax.SAXException;

import com.globalretailtech.util.xmlservices.SQLConverter;
import com.globalretailtech.util.xmlservices.SQLDataGenerator;
import com.globalretailtech.util.xmlservices.SQLMissingTableDef;
import com.globalretailtech.util.xmlservices.SQLMismatchedTableItem;
import com.globalretailtech.util.Application;
import com.globalretailtech.data.DBContext;

import junit.framework.*;

public class SQLDataGeneratorTest
        extends MercatorBaseTest {


    public void testSimpleInsert()
            throws IOException, SAXException, SQLMissingTableDef,
            SQLMismatchedTableItem {
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
                "<!-- An insert -->\n" +
                " <tableinsert table=\"Fobar\">\n" +
                "  <item>\n" +
                "   <col name=\"barof\">10</col>\n" +
                "  </item>\n" +
                " </tableinsert>\n" +
                "</mercatordata>\n";

        SQLConverter conv = new SQLConverter(new StringReader(testText));

        assertNotNull(conv);

        SQLDataGenerator dg = new SQLDataGenerator(conv);

        assertNotNull(dg);

        assertEquals("insert into Fobar (\n  barof\n)\nvalues (\n  10\n);\n",
                dg.generateSQLInserts());
    }

    public void testInsertStringTypes()
            throws IOException, SAXException, SQLMissingTableDef,
            SQLMismatchedTableItem {
        String testText1 =
                "<?xml version=\"1.0\"?>\n" +
                "<!-- This is a comment -->\n" +
                "<mercatordata\n>" +
                " <!-- Some comment about the table -->\n" +
                " <table>\n" +
                "  <name>Fobar</name>\n" +
                "  <columns>\n" +
                "    <column type=\"varchar(30)\">col1</column>\n" +
                "    <column type=\"varchar(30)\">col2</column>\n" +
                "    <column type=\"varchar(30)\">col3</column>\n" +
                "  </columns>\n" +
                " </table>\n" +
                "</mercatordata>\n";
        String testText2 =
                "<mercatordata>\n" +
                "<!-- An insert -->\n" +
                " <tableinsert table=\"Fobar\">\n" +
                "  <item>\n" +
                "   <col name=\"col1\">A String</col>\n" +
                "   <col name=\"col2\"></col>\n" +
                "   <col null=\"true\" name=\"col3\"/>\n" +
                "  </item>\n" +
                " </tableinsert>\n" +
                "</mercatordata>\n";

        SQLConverter conv1 = new SQLConverter(new StringReader(testText1));
        SQLConverter conv2 = new SQLConverter(new StringReader(testText2));

        assertNotNull(conv1);
        assertNotNull(conv2);

        SQLDataGenerator dg = new SQLDataGenerator(new SQLConverter[]{
            conv2, conv1});

        assertNotNull(dg);

        assertEquals("insert into Fobar (\n  col1,\n  col2,\n  col3\n)\nvalues (\n  'A String',\n" +
                "  '',\n  null\n);\n",
                dg.generateSQLInserts());
    }

    public void testInsertNullTimestamp()
            throws IOException, SAXException, SQLMissingTableDef,
            SQLMismatchedTableItem {
        String testText1 =
                "<?xml version=\"1.0\"?>\n" +
                "<!-- This is a comment -->\n" +
                "<mercatordata\n>" +
                " <!-- Some comment about the table -->\n" +
                " <table>\n" +
                "  <name>Fobar</name>\n" +
                "  <columns>\n" +
                "    <column type=\"varchar(30)\">col1</column>\n" +
                "    <column type=\"varchar(30)\">col2</column>\n" +
                "    <column type=\"timestamp\">col3</column>\n" +
                "  </columns>\n" +
                " </table>\n" +
                "</mercatordata>\n";
        String testText2 =
                "<mercatordata>\n" +
                "<!-- An insert -->\n" +
                " <tableinsert table=\"Fobar\">\n" +
                "  <item>\n" +
                "   <col name=\"col1\">A String</col>\n" +
                "   <col name=\"col2\"></col>\n" +
                "   <col null=\"true\" name=\"col3\"/>\n" +
                "  </item>\n" +
                " </tableinsert>\n" +
                "</mercatordata>\n";

        SQLConverter conv1 = new SQLConverter(new StringReader(testText1));
        SQLConverter conv2 = new SQLConverter(new StringReader(testText2));

        assertNotNull(conv1);
        assertNotNull(conv2);

        SQLDataGenerator dg = new SQLDataGenerator(new SQLConverter[]{
            conv1, conv2});

        assertNotNull(dg);

        assertEquals("insert into Fobar (\n  col1,\n  col2,\n  col3\n)\nvalues (\n  'A String',\n" +
                "  '',\n  null\n);\n",
                dg.generateSQLInserts());
    }

    public void testInsertTwoItemsMultipleStrings()
            throws IOException, SAXException, SQLMissingTableDef,
            SQLMismatchedTableItem {
        String testText1 =
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
                "</mercatordata>\n";
        String testText2 =
                "<mercatordata>\n" +
                "<!-- An insert -->\n" +
                " <tableinsert table=\"Fobar\">\n" +
                "  <item>\n" +
                "   <col name=\"barof\">10</col>\n" +
                "  </item>\n" +
                " </tableinsert>\n" +
                "</mercatordata>\n";
        String testText3 =
                "<?xml version=\"1.0\"?>\n" +
                "<!-- This is a comment -->\n" +
                "<mercatordata\n>" +
                " <!-- Some comment about the table -->\n" +
                " <table>\n" +
                "  <name>bleh</name>\n" +
                "  <columns>\n" +
                "    <column type=\"integer\">barof</column>\n" +
                "    <column type=\"varchar(30)\">forab</column>\n" +
                "  </columns>\n" +
                " </table>\n" +
                "</mercatordata>\n";
        String testText4 =
                "<mercatordata>\n" +
                "<!-- An insert -->\n" +
                " <tableinsert table=\"bleh\">\n" +
                "  <item>\n" +
                "   <col name=\"barof\">10</col>\n" +
                "   <col name=\"forab\">Some Value</col>\n" +
                "  </item>\n" +
                " </tableinsert>\n" +
                "</mercatordata>\n";

        SQLConverter conv1 = new SQLConverter(new StringReader(testText1));
        SQLConverter conv2 = new SQLConverter(new StringReader(testText2));
        SQLConverter conv3 = new SQLConverter(new StringReader(testText3));
        SQLConverter conv4 = new SQLConverter(new StringReader(testText4));

        assertNotNull(conv1);
        assertNotNull(conv2);
        assertNotNull(conv3);
        assertNotNull(conv4);

        SQLDataGenerator dg = new SQLDataGenerator(new SQLConverter[]{
            conv1, conv2, conv3, conv4});

        assertNotNull(dg);

        assertEquals("insert into Fobar (\n  barof\n)\nvalues (\n  10\n);\n" +
                "insert into bleh (\n  barof,\n  forab\n)\nvalues (\n  10,\n  'Some Value'\n);\n",
                dg.generateSQLInserts());
    }

    public void testInsertTwoItemsMultipleStringsUnique()
            throws IOException, SAXException, SQLMissingTableDef,
            SQLMismatchedTableItem {
        String testText1 =
                "<?xml version=\"1.0\"?>\n" +
                "<!-- This is a comment -->\n" +
                "<mercatordata\n>" +
                " <!-- Some comment about the table -->\n" +
                " <table>\n" +
                "  <name>Fobar</name>\n" +
                "  <columns>\n" +
                "    <column type=\"integer\" unique=\"true\">col1</column>\n" +
                "    <column type=\"varchar(25)\">col2</column>\n" +
                "    <column type=\"integer\">col3</column>\n" +
                "    <column type=\"varchar(30)\">col4</column>\n" +
                "  </columns>\n" +
                " </table>\n" +
                "</mercatordata>\n";
        String testText2 =
                "<mercatordata>\n" +
                "<!-- An insert -->\n" +
                " <tableinsert table=\"Fobar\">\n" +
                "  <item>\n" +
                "   <col name=\"col1\">1</col>\n" +
                "   <col name=\"col2\">1</col>\n" +
                "   <col name=\"col3\">300</col>\n" +
                "   <col name=\"col4\">A Value</col>\n" +
                "  </item>\n" +
                "  <item>\n" +
                "   <col name=\"col1\">2</col>\n" +
                "   <col name=\"col2\">Another Value</col>\n" +
                "   <col name=\"col3\">400</col>\n" +
                "   <col name=\"col4\"></col>\n" +
                "  </item>\n" +
                " </tableinsert>\n" +
                "</mercatordata>\n";

        SQLConverter conv1 = new SQLConverter(new StringReader(testText1));
        SQLConverter conv2 = new SQLConverter(new StringReader(testText2));

        assertNotNull(conv1);
        assertNotNull(conv2);

        SQLDataGenerator dg = new SQLDataGenerator(new SQLConverter[]{
            conv1, conv2});

        assertNotNull(dg);

        assertEquals("insert into Fobar (\n  col2,\n  col3,\n  col4\n)\n" +
                "values (\n  '1',\n  300,\n  'A Value'\n);\n" +
                "insert into Fobar (\n  col2,\n  col3,\n  col4\n)\n" +
                "values (\n  'Another Value',\n  400,\n  ''\n);\n",
                dg.generateSQLInserts());
    }

    public void testInsertTwoItemsMultipleStringsUniqueTimestampBad()
            throws IOException, SAXException, SQLMissingTableDef,
            SQLMismatchedTableItem {
        String testText1 =
                "<?xml version=\"1.0\"?>\n" +
                "<!-- This is a comment -->\n" +
                "<mercatordata\n>" +
                " <!-- Some comment about the table -->\n" +
                " <table>\n" +
                "  <name>Fobar</name>\n" +
                "  <columns>\n" +
                "    <column type=\"integer\" unique=\"true\">col1</column>\n" +
                "    <column type=\"varchar(25)\">col2</column>\n" +
                "    <column type=\"integer\">col3</column>\n" +
                "    <column type=\"timestamp\">col4</column>\n" +
                "  </columns>\n" +
                " </table>\n" +
                "</mercatordata>\n";
        String testText2 =
                "<mercatordata>\n" +
                "<!-- An insert -->\n" +
                " <tableinsert table=\"Fobar\">\n" +
                "  <item>\n" +
                "   <col name=\"col1\">1</col>\n" +
                "   <col name=\"col2\">1</col>\n" +
                "   <col name=\"col3\">300</col>\n" +
                "   <col name=\"col4\">0</col>\n" +
                "  </item>\n" +
                "  <item>\n" +
                "   <col name=\"col1\">2</col>\n" +
                "   <col name=\"col2\">Another Value</col>\n" +
                "   <col name=\"col3\">400</col>\n" +
                "   <col name=\"col4\"></col>\n" +
                "  </item>\n" +
                " </tableinsert>\n" +
                "</mercatordata>\n";

        SQLConverter conv1 = new SQLConverter(new StringReader(testText1));
        SQLConverter conv2 = new SQLConverter(new StringReader(testText2));

        assertNotNull(conv1);
        assertNotNull(conv2);

        SQLDataGenerator dg = new SQLDataGenerator(new SQLConverter[]{
            conv1, conv2});

        assertNotNull(dg);

        assertEquals("insert into Fobar (\n  col2,\n  col3\n)\n" +
                "values (\n  '1',\n  300\n);\n" +
                "insert into Fobar (\n  col2,\n  col3\n)\n" +
                "values (\n  'Another Value',\n  400\n);\n",
                dg.generateSQLInserts());
    }

    public void testInsertTwoItemsMultipleStringsUniqueTimestampGood()
            throws IOException, SAXException, SQLMissingTableDef,
            SQLMismatchedTableItem {
        String testText1 =
                "<?xml version=\"1.0\"?>\n" +
                "<!-- This is a comment -->\n" +
                "<mercatordata\n>" +
                " <!-- Some comment about the table -->\n" +
                " <table>\n" +
                "  <name>Fobar</name>\n" +
                "  <columns>\n" +
                "    <column type=\"integer\" unique=\"true\">col1</column>\n" +
                "    <column type=\"varchar(25)\">col2</column>\n" +
                "    <column type=\"integer\">col3</column>\n" +
                "    <column type=\"timestamp\">col4</column>\n" +
                "  </columns>\n" +
                " </table>\n" +
                "</mercatordata>\n";
        String testText2 =
                "<mercatordata>\n" +
                "<!-- An insert -->\n" +
                " <tableinsert table=\"Fobar\">\n" +
                "  <item>\n" +
                "   <col name=\"col1\">1</col>\n" +
                "   <col name=\"col2\">1</col>\n" +
                "   <col name=\"col3\">300</col>\n" +
                "   <col name=\"col4\">0</col>\n" +
                "  </item>\n" +
                "  <item>\n" +
                "   <col name=\"col1\">2</col>\n" +
                "   <col name=\"col2\">Another Value</col>\n" +
                "   <col name=\"col3\">400</col>\n" +
                "   <col name=\"col4\">2001-10-04 12:45</col>\n" +
                "  </item>\n" +
                " </tableinsert>\n" +
                "</mercatordata>\n";

        SQLConverter conv1 = new SQLConverter(new StringReader(testText1));
        SQLConverter conv2 = new SQLConverter(new StringReader(testText2));

        assertNotNull(conv1);
        assertNotNull(conv2);

        SQLDataGenerator dg = new SQLDataGenerator(new SQLConverter[]{
            conv1, conv2});

        assertNotNull(dg);

        assertEquals("insert into Fobar (\n  col2,\n  col3\n)\n" +
                "values (\n  '1',\n  300\n);\n" +
                "insert into Fobar (\n  col2,\n  col3,\n  col4\n)\n" +
                "values (\n  'Another Value',\n  400,\n  '2001-10-04 12:45'\n);\n",
                dg.generateSQLInserts());
    }

	public void testInsertBadColumnMismatch()
			throws IOException, SAXException, SQLMissingTableDef, SQLMismatchedTableItem {
			String testText3 =
					"<?xml version=\"1.0\"?>\n" +
					"<!-- This is a comment -->\n" +
					"<mercatordata\n>" +
					" <!-- Some comment about the table -->\n" +
					" <table>\n" +
					"  <name>bleh</name>\n" +
					"  <columns>\n" +
					"    <column type=\"integer\">barof</column>\n" +
					"    <column type=\"varchar(30)\">forab</column>\n" +
					"  </columns>\n" +
					" </table>\n" +
					"</mercatordata>\n";
			String testText4 =
					"<mercatordata>\n" +
					"<!-- An insert -->\n" +
					" <tableinsert table=\"bleh\">\n" +
					"  <item>\n" +
					"   <col name=\"baroooooof\">10</col>\n" +
					"  </item>\n" +
					" </tableinsert>\n" +
					"</mercatordata>\n";
	
			SQLConverter conv3 = new SQLConverter(new StringReader(testText3));
			SQLConverter conv4 = new SQLConverter(new StringReader(testText4));
	
			assertNotNull(conv3);
			assertNotNull(conv4);
	
			SQLDataGenerator dg = new SQLDataGenerator(new SQLConverter[]{
				conv3, conv4});
	
			assertNotNull(dg);
	
			assertEquals("insert into bleh (\n)\n" +
					"values (\n);\n",
					dg.generateSQLInserts());
		
//
//		  // block below is not valid because latest
//		  // version of SQLGenerator supports missing columns
//		  // it will never throw SQLMismatchedTableItem exception
//
//			boolean caughtRightError = false;
//			try {
//				String test = dg.generateSQLInserts();
//			} catch (SQLMismatchedTableItem e) {
//				caughtRightError = true;
//				assertTrue(true);
//			} finally {
//				if (!caughtRightError) {
//					assertTrue(false);
//				}
//			}
	}
	public void testInsertWithMissedColumn()
			throws IOException, SAXException, SQLMissingTableDef, SQLMismatchedTableItem {
			String testText3 =
					"<?xml version=\"1.0\"?>\n" +
					"<!-- This is a comment -->\n" +
					"<mercatordata\n>" +
					" <!-- Some comment about the table -->\n" +
					" <table>\n" +
					"  <name>bleh</name>\n" +
					"  <columns>\n" +
					"    <column type=\"integer\">barof</column>\n" +
					"    <column type=\"varchar(30)\">forab</column>\n" +
					"  </columns>\n" +
					" </table>\n" +
					"</mercatordata>\n";
			String testText4 =
					"<mercatordata>\n" +
					"<!-- An insert -->\n" +
					" <tableinsert table=\"bleh\">\n" +
					"  <item>\n" +
					"   <col name=\"barof\">10</col>\n" +
					"  </item>\n" +
					" </tableinsert>\n" +
					"</mercatordata>\n";
	
			SQLConverter conv3 = new SQLConverter(new StringReader(testText3));
			SQLConverter conv4 = new SQLConverter(new StringReader(testText4));
	
			assertNotNull(conv3);
			assertNotNull(conv4);
	
			SQLDataGenerator dg = new SQLDataGenerator(new SQLConverter[]{
				conv3, conv4});
	
			assertNotNull(dg);
	
			assertEquals("insert into bleh (\n  barof\n)\n" +
					"values (\n  10\n);\n",
					dg.generateSQLInserts());
	}

    public SQLDataGeneratorTest(String name) {
        super(name);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        System.setProperty("SHARE", "share");

        Application.setDbConnection(new DBContext());

        if (!Application.dbConnection().init()) {
            fail("Database initialization failure");
        }

        return new TestSuite(SQLDataGeneratorTest.class);
    }

}
