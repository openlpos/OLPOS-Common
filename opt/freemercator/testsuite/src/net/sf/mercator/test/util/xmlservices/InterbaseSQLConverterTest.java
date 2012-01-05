package net.sf.mercator.test.util.xmlservices;

import java.io.StringReader;

import java.io.IOException;

import com.globalretailtech.util.xmlservices.SQLConverter;
import com.globalretailtech.util.xmlservices.InterbaseSQLConverter;

import junit.framework.*;

public class InterbaseSQLConverterTest
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

        InterbaseSQLConverter psconv = new InterbaseSQLConverter(conv);

        assertEquals(
                "commit;\n" +
                "delete from rdb$generators where rdb$generator_name = 'beebop';\n" +
                "commit;\n" +
                "commit;\n" +
                "create generator beebop;\n" +
                "create table Fobar (\n  barof integer\n);\n" +
                "set term !! ;\n" +
                "create trigger Fobar_insert for Fobar\n" +
                "before insert position 0\n" +
                "as begin\n" +
                "new.barof = gen_id(beebop, 1);\n" +
                "end !!\n" +
                "commit !!\n" +
                "set term ; !!\n"
                ,
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

        InterbaseSQLConverter psconv = new InterbaseSQLConverter(conv);

        assertEquals(
                "create table Bleh (\n  blah integer,\n  bloh varchar(32)\n);\n",
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

        InterbaseSQLConverter psconv = new InterbaseSQLConverter(
                new SQLConverter[]{conv1, conv2});

        assertEquals(
                "create table Bleh (\n  blah integer,\n  bloh varchar(32)\n);\n" +
                "create table FOOBAR (\n  Wheer integer,\n  reehW varchar(32)\n);\n",
                psconv.generateCreationSQL());
    }

    public void testTableConversionTwoSQLConvertersTwoUniques()
            throws IOException, org.xml.sax.SAXException {
        String testText1 =
                "<mercatordata>\n" +
                " <table>\n" +
                "  <name>Bleh</name>\n" +
                "  <columns>\n" +
                "    <column type=\"integer\" unique=\"true\">blah</column>\n" +
                "    <column type=\"varchar(32)\">bloh</column>\n" +
                "  </columns>\n" +
                " </table>\n" +
                "</mercatordata>\n";

        String testText2 =
                "<mercatordata>\n" +
                " <table>\n" +
                "  <name>FOOBAR</name>\n" +
                "  <columns>\n" +
                "    <column type=\"integer\" unique=\"true\">Wheer</column>\n" +
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

        InterbaseSQLConverter psconv = new InterbaseSQLConverter(
                new SQLConverter[]{conv1, conv2});

        assertEquals(
                "commit;\n" +
                "delete from rdb$generators where rdb$generator_name = 'gen_Bleh_blah';\n" +
                "commit;\n" +
                "commit;\n" +
                "create generator gen_Bleh_blah;\n" +
                "commit;\n" +
                "delete from rdb$generators where rdb$generator_name = 'gen_FOOBAR_Wheer';\n" +
                "commit;\n" +
                "commit;\n" +
                "create generator gen_FOOBAR_Wheer;\n" +
                "create table Bleh (\n  blah integer,\n  bloh varchar(32)\n);\n" +
                "set term !! ;\n" +
                "create trigger Bleh_insert for Bleh\n" +
                "before insert position 0\n" +
                "as begin\n" +
                "new.blah = gen_id(gen_Bleh_blah, 1);\n" +
                "end !!\n" +
                "commit !!\n" +
                "set term ; !!\n" +
                "create table FOOBAR (\n  Wheer integer,\n  reehW varchar(32)\n);\n" +
                "set term !! ;\n" +
                "create trigger FOOBAR_insert for FOOBAR\n" +
                "before insert position 0\n" +
                "as begin\n" +
                "new.Wheer = gen_id(gen_FOOBAR_Wheer, 1);\n" +
                "end !!\n" +
                "commit !!\n" +
                "set term ; !!\n",
                psconv.generateCreationSQL());
    }

    public void testTableConversionTwoSQLConvertersTwoUniquesOneName()
            throws IOException, org.xml.sax.SAXException {
        String testText1 =
                "<mercatordata>\n" +
                " <table>\n" +
                "  <name>Bleh</name>\n" +
                "  <columns>\n" +
                "    <column type=\"integer\" unique=\"gen1\">blah</column>\n" +
                "    <column type=\"varchar(32)\">bloh</column>\n" +
                "  </columns>\n" +
                " </table>\n" +
                "</mercatordata>\n";

        String testText2 =
                "<mercatordata>\n" +
                " <table>\n" +
                "  <name>FOOBAR</name>\n" +
                "  <columns>\n" +
                "    <column type=\"integer\" unique=\"gen1\">Wheer</column>\n" +
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

        InterbaseSQLConverter psconv = new InterbaseSQLConverter(
                new SQLConverter[]{conv1, conv2});

        assertEquals(
                "commit;\n" +
                "delete from rdb$generators where rdb$generator_name = 'gen1';\n" +
                "commit;\n" +
                "commit;\n" +
                "create generator gen1;\n" +
                "create table Bleh (\n  blah integer,\n  bloh varchar(32)\n);\n" +
                "set term !! ;\n" +
                "create trigger Bleh_insert for Bleh\n" +
                "before insert position 0\n" +
                "as begin\n" +
                "new.blah = gen_id(gen1, 1);\n" +
                "end !!\n" +
                "commit !!\n" +
                "set term ; !!\n" +
                "create table FOOBAR (\n  Wheer integer,\n  reehW varchar(32)\n);\n" +
                "set term !! ;\n" +
                "create trigger FOOBAR_insert for FOOBAR\n" +
                "before insert position 0\n" +
                "as begin\n" +
                "new.Wheer = gen_id(gen1, 1);\n" +
                "end !!\n" +
                "commit !!\n" +
                "set term ; !!\n",
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

        InterbaseSQLConverter psconv = new InterbaseSQLConverter(conv);

        assertEquals(
                "commit;\n" +
                "delete from rdb$generators where rdb$generator_name = 'beebop';\n" +
                "commit;\n" +
                "commit;\n" +
                "create generator beebop;\n" +
                "create table Bleh (\n  blah integer,\n  bloh varchar(32)\n);\n" +
                "create table Fobar (\n  barof integer\n);\n" +
                "set term !! ;\n" +
                "create trigger Fobar_insert for Fobar\n" +
                "before insert position 0\n" +
                "as begin\n" +
                "new.barof = gen_id(beebop, 1);\n" +
                "end !!\n" +
                "commit !!\n" +
                "set term ; !!\n"
                ,
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
                "  <text type=\"interbase\">\n" +
                "Some Function Text\n" +
                "  </text>\n" +
                " </procedure>\n" +
                "</mercatordata>\n";

        SQLConverter conv = new SQLConverter(new StringReader(testText));

        assertNotNull(conv);
        assertNotNull(conv.getFunctions());

        InterbaseSQLConverter psconv = new InterbaseSQLConverter(conv);

        assertEquals(
                "set term !! ;\n" +
                "create procedure proc1 (\n" +
                "  arg1 integer\n" +
                ")\n" +
                "  returns (ret1 integer)\n" +
                "  as\n" +
                "\n" +
                "Some Function Text\n" +
                "   !!\n" +
                "set term ; !!\n",
                psconv.generateCreationSQL());
    }

    public void testFunctionNotRequiredReturn()
            throws IOException, org.xml.sax.SAXException {
        String testText =
                "<mercatordata>\n" +
                " <procedure>\n" +
                "  <name>proc1</name>\n" +
                "  <arguments>\n" +
                "   <arg type=\"integer\">arg1</arg>" +
                "  </arguments>\n" +
                "  <return type=\"integer\" required=\"false\">ret1</return>\n" +
                "  <text type=\"interbase\">\n" +
                "Some Function Text\n" +
                "  </text>\n" +
                " </procedure>\n" +
                "</mercatordata>\n";

        SQLConverter conv = new SQLConverter(new StringReader(testText));

        assertNotNull(conv);
        assertNotNull(conv.getFunctions());

        InterbaseSQLConverter psconv = new InterbaseSQLConverter(conv);

        assertEquals(
                "set term !! ;\n" +
                "create procedure proc1 (\n" +
                "  arg1 integer\n" +
                ")\n" +
                "  as\n" +
                "\n" +
                "Some Function Text\n" +
                "   !!\n" +
                "set term ; !!\n",
                psconv.generateCreationSQL());
    }


    public InterbaseSQLConverterTest(String name) {
        super(name);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(InterbaseSQLConverterTest.class);
    }

}
