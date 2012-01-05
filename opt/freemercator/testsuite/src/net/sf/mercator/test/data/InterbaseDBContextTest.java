package net.sf.mercator.test.data;

import com.globalretailtech.data.InterbaseDBContext;
import com.globalretailtech.util.sql.*;

import junit.framework.*;


public class InterbaseDBContextTest
        extends TestCase {
    static InterbaseDBContext ctxt;

    static {
        ctxt = new InterbaseDBContext();
    }

    public void testSimpleFunction() {
        assertEquals(
                "execute procedure foobar 20, 'this is a test' ;",
                ctxt.generateFunctionCall("foobar",
                        new Object[]{new Integer(20),
                                     "this is a test"
                        }));
    }

    public void testMoreComplexFunction() {
        assertEquals(
                "execute procedure foobar2 'test2', 30, 4.5, false ;",
                ctxt.generateFunctionCall(
                        "foobar2", new Object[]{
                            "test2", new Integer(30), new Double(4.5), new Boolean(false)
                        }));
    }

    public void testSimpleFunctionUsingProcCall() {
        assertEquals(
                "execute procedure foobar 20, 'this is a test' ;",
                ctxt.generateFunctionCall(
                        new SQLProcedureCall("foobar",
                                new Object[]{new Integer(20),
                                             "this is a test"
                                })));
    }

    public void testMoreComplexFunctionUsingProcCall() {
        assertEquals(
                "execute procedure foobar2 'test2', 30, 4.5, false ;",
                ctxt.generateFunctionCall(
                        new SQLProcedureCall(
                                "foobar2", new Object[]{
                                    "test2", new Integer(30), new Double(4.5), new Boolean(false)
                                })));
    }

    public void testGenericCall()
            throws UnknownSQLCall {
        assertEquals(
                "execute procedure foobar2 'test2', 30, 4.5, false ;",
                ctxt.generateCall(
                        new SQLProcedureCall(
                                "foobar2", new Object[]{
                                    "test2", new Integer(30), new Double(4.5), new Boolean(false)
                                })));
    }

    public InterbaseDBContextTest(String name) {
        super(name);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(InterbaseDBContextTest.class);
    }
}
