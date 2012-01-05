/*
 * Copyright 2001 Linux Developers Group
 */

package net.sf.mercator.test.data;

import com.globalretailtech.data.DBRecord;

import junit.framework.*;

/**
 * Test class for com.globalretailtech.data.DBRecord
 */

public class DBRecordTest extends TestCase {
    public DBRecordTest(String name) {
        super(name);
    }

    public void testSQLStringGen() {
        assertEquals(DBRecord.translateSQLArgToString(new Integer(20)),
                "20");
        assertEquals(DBRecord.translateSQLArgToString("foobar"),
                "'foobar'");
    }

    public void testWhereArg() {
        DBRecord.WhereArg arg1 = new DBRecord.WhereArg("col1", null);
        DBRecord.WhereArg arg2 = new DBRecord.WhereArg("col2", new Integer(10));
        DBRecord.WhereArg arg3 = new DBRecord.WhereArg("col3", "test string");

        assertEquals(arg1.toString(),
                "col1 = 0");
        assertEquals(arg2.toString(),
                "col2 = 10");
        assertEquals(arg3.toString(),
                "col3 = 'test string'");
    }

    public void testAddStringGen() {
        assertEquals(DBRecord.genSelectCall(
                "foobar", null,
                DBRecord.genAndWhere(new DBRecord.WhereArg("col1", "val1"),
                        new DBRecord.WhereArg("col2", "val2"))),
                "select * from foobar where col1 = 'val1' and col2 = 'val2'");
        assertEquals(DBRecord.genSelectCall(
                "raboof", null,
                DBRecord.genOrWhere(new DBRecord.WhereArg("col1", "val1"),
                        new DBRecord.WhereArg("col2", "val2"))),
                "select * from raboof where (col1 = 'val1' or col2 = 'val2')");
        assertEquals(DBRecord.genSelectCall(
                "barfoo", null,
                DBRecord.genAndWhere(new DBRecord.WhereArg("col1", "val1"),
                        DBRecord.genOrWhere(new DBRecord.WhereArg("col2", "val2"),
                                new DBRecord.WhereArg("col3", "val3")))),
                "select * from barfoo where col1 = 'val1' and (col2 = 'val2' or col3 = 'val3')");
    }

    public void testSelectString() {
        assertEquals(DBRecord.genSelectCall("foobar", null,
                new DBRecord.WhereArg[]{}),
                "select * from foobar");
        assertEquals(DBRecord.genSelectCall("barfoo", null, new DBRecord.WhereArg[]{
            new DBRecord.WhereArg("col1", null)}),
                "select * from barfoo where col1 = 0");
        assertEquals(DBRecord.genSelectCall("oofrab", null, new DBRecord.WhereArg[]{
            new DBRecord.WhereArg("col2", "my value"),
            new DBRecord.WhereArg("col3", new Integer(30))}),
                "select * from oofrab where col2 = 'my value' and col3 = 30");
        assertEquals(DBRecord.genSelectCall("baroof",
                new String[]{"col1", "col2", "col3"},
                new DBRecord.WhereArg[]{}),
                "select col1, col2, col3 from baroof");

    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(DBRecordTest.class);
    }

}
