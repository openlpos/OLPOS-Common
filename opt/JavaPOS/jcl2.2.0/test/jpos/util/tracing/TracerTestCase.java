package jpos.util.tracing;

/*
 */

import junit.framework.*;

/**
 * Tests the Tracer class
 * @author E. Michael Maximilien
 * @since 2.1.0
 */
public class TracerTestCase extends TestCase
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	public TracerTestCase( String name ) 
	{ 
		super( name ); 
	}
	
	protected void setUp()
	{
		tracer = Tracer.getInstance();
		testTracerOutput = this.new TestTracerOutput();
		tracer.setTracerOutput( testTracerOutput );
		
		namedTracer = new Tracer( NAMED_TRACER_NAME );
	}
	
	protected void tearDown()
	{
		tracer = null;
		testTracerOutput = null;
		
		namedTracer = null;
	}

	//---------------------------------------------------------------------
	// Public test<Xyz> methods
	//
	
	public void testGetInstance()
	{
		Tracer tracer1 = Tracer.getInstance();
		assertTrue( "Did not get same instance from calls to Tracer.getInstance()",
					tracer1 == Tracer.getInstance() );
	}
	
	public void testStringPrintln()
	{
		String testString = "testString";
		tracer.println( testString );
		
		assertTrue( "TracerOutput did not get testString",
					testTracerOutput.getPrintlnString().equals( testString ) );
	}

	public void testStringPrint()
	{
		String testString = "testString";
		tracer.print( testString );
		
		assertTrue( "TracerOutput did not get testString",
					testTracerOutput.getPrintString().equals( testString ) );
	}
	
	public void testObjectPrintln()
	{
		Object testObj = new Integer( 10 );
		tracer.println( testObj );
		
		assertTrue( "TracerOutput did not get testObj",
					testTracerOutput.getPrintlnString().equals( testObj.toString() ) );
	}
	
	public void testObjectPrint()
	{
		Object testObj = new Boolean( false );
		tracer.print( testObj );
		
		assertTrue( "TracerOutput did not get testObj",
					testTracerOutput.getPrintString().equals( testObj.toString() ) );
	}
	
	public void testFlush()
	{
		Object testObj = "testString";
		tracer.print( testObj );
		tracer.flush();
		
		assertTrue( "TracerOutput did not get flush call",
					testTracerOutput.isFlush() );
	}

    public void testExceptionPrint()
    {
        Exception e = new IllegalArgumentException();
        tracer.print(e);

        assertTrue( "Exception not equal to printed exception",
                    testTracerOutput.getException().equals(e));
    }

	public void testIsAppendName()
	{
		assertTrue( "Default Tracer should have no name and isAppendName() == false",
					tracer.isAppendName() == false );
					
		assertTrue( "Named Tracer should default to isAppendName() == true",
					namedTracer.isAppendName() );
	}
	
	public void testSetAppendName()
	{						
		assertTrue( "Named Tracer should default to isAppendName() == true",
					namedTracer.isAppendName() );
		
		namedTracer.setAppendName( false );
		
		assertTrue( "Named Tracer should default to isAppendName() == true",
					namedTracer.isAppendName() == false );
	}
	
	public void testCreateTracerWithName()
	{
		Tracer newNamedTracer = new Tracer( "newNamedTracer" );
		
		assertTrue( "newNamedTracer should default to isAppenName() == true",
					newNamedTracer.isAppendName() );
					
		assertTrue( "newNamedTracer.getName().equals( \"newNamedTracer\"",
					newNamedTracer.getName().equals( "newNamedTracer" ) );
	}

	//-----------------------------------------------------------------------
	// Instance variables
	//

	private Tracer namedTracer = null;	
	private Tracer tracer = null;
	private TestTracerOutput testTracerOutput = null;
	
	//-----------------------------------------------------------------------
	// Class constants
	//
	
	public static final String NAMED_TRACER_NAME = "NamedTracer";
	
	
	//-----------------------------------------------------------------------
	// Inner classes
	//
	
	protected class TestTracerOutput extends Object implements TracerOutput
	{	
		//---------------------------------------------------------------------
		// Ctor(s)
		//
		
		//---------------------------------------------------------------------
		// Public methods
		//
		
		public void close() {}
		
		public String getPrefix() { return suffix; }
		
		public void print( Exception e ) { exception = e; }

	    public void println( String s ) { printlnString = s; }

		public void print( String s ) { printString = s; }
    
    	public void flush() { flushed = true; }	

		boolean isFlush() { return flushed; }

		String getPrintString() { return printString; }
		
		String getPrintlnString() { return printlnString; }

		Exception getException() { return exception; }

		//---------------------------------------------------------------------
		// Instance variables
		//

		private String suffix = "";
		private boolean flushed = false;		
		private String printlnString = "";
		private String printString = "";
		private Exception exception = null;
	}
}