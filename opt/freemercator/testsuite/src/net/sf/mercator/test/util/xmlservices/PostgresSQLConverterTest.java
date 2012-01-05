package net.sf.mercator.test.util.xmlservices;

import java.io.StringReader;

import java.io.IOException;
import com.globalretailtech.util.xmlservices.SQLConverter;
import com.globalretailtech.util.xmlservices.PostgresSQLConverter;

import junit.framework.*;

public class PostgresSQLConverterTest
        extends TestCase {
    public void testTableConversion()
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

        PostgresSQLConverter psconv = new PostgresSQLConverter(conv);

        assertEquals("create table Fobar (\n  barof serial\n);\n",
                psconv.generateCreationSQL());
    }

    public void testTableConversionTwoSQLConverters()
            throws IOException, org.xml.sax.SAXException {
        String testText1 =
                "<mercatordata>\n" +
                " <table>\n" +
                "  <name>Bleh</name>\n" +
                "  <columns>\n" +
                "    <column type=\"integer\">blah</column>\n" +
                "    <column type=\"varchar(32)\">bloh</column>\n" +
                "  </columns>\n" +
                " </table>\n" +
                "</mercatordata>\n";

        String testText2 =
                "<mercatordata>\n" +
                " <table>\n" +
                "  <name>FOOBAR</name>\n" +
                "  <columns>\n" +
                "    <column type=\"integer\">Wheer</column>\n" +
                "    <column type=\"varchar(32)\">reehW</column>\n" +
                "  </columns>\n" +
                " </table>\n" +
                "</mercatordata>\n";

        SQLConverter conv1 = new SQLConverter(new StringReader(testText1));
        SQLConverter conv2 = new SQLConverter(new StringReader(testText2));

        assertNotNull(conv1);
        assertNotNull(conv2);
        assertNotNull(conv1.getTables());
        assertNotNull(conv2.getTables());

        PostgresSQLConverter psconv = new PostgresSQLConverter(
                new SQLConverter[]{conv1, conv2});

        assertEquals(
                "create table Bleh (\n  blah integer,\n  bloh varchar(32)\n);\n" +
                "create table FOOBAR (\n  Wheer integer,\n  reehW varchar(32)\n);\n",
                psconv.generateCreationSQL());
    }

    public void testTableConversionTwoColumn()
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

        PostgresSQLConverter psconv = new PostgresSQLConverter(conv);

        assertEquals(
                "create table Bleh (\n  blah integer,\n  bloh varchar(32)\n);\n",
                psconv.generateCreationSQL());
    }

    public void testTableConversionTwoTable()
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

        PostgresSQLConverter psconv = new PostgresSQLConverter(conv);

        assertEquals(
                "create table Bleh (\n  blah integer,\n  bloh varchar(32)\n);\n" +
                "create table Fobar (\n  barof serial\n);\n",
                psconv.generateCreationSQL());
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
                "  <text type=\"postgresql\">\n" +
                "Some Function Text\n" +
                "  </text>\n" +
                " </procedure>\n" +
                "</mercatordata>\n";

        SQLConverter conv = new SQLConverter(new StringReader(testText));

        assertNotNull(conv);
        assertNotNull(conv.getFunctions());

        PostgresSQLConverter psconv = new PostgresSQLConverter(conv);

        assertEquals(
                "create function proc1 (\n" +
                "  /* arg1 */ integer\n" +
                ")\n" +
                "  returns integer\n" +
                "  as '\n" +
                "\n" +
                "Some Function Text\n" +
                "  ' LANGUAGE 'plpgsql';\n",
                psconv.generateCreationSQL());
    }

    public PostgresSQLConverterTest(String name) {
        super(name);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(PostgresSQLConverterTest.class);
    }

}
