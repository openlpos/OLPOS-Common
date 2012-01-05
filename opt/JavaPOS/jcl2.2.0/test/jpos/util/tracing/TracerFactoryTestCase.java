package jpos.util.tracing;

/*
 */

import junit.framework.*;

/**
 * Tests the TracerFactory class
 * @author E. Michael Maximilien
 * @since 2.1.0
 */
public class TracerFactoryTestCase extends TestCase
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	public TracerFactoryTestCase( String name ) { super( name ); }

	//-------------------------------------------------------------------------
	// Protected methods
	//
	
	protected void setUp()
	{
		tracerFactory = TracerFactory.getInstance();
		
		globalTracer = tracerFactory.createGlobalTracer();
		tracer1 = tracerFactory.createTracer( NAMEDTRACER1_NAME );
		tracer2 = tracerFactory.createTracer( NAMEDTRACER2_NAME );
	}
	
	protected void tearDown()
	{
		tracerFactory = null;
		
		globalTracer = null;
		tracer1 = null;
		tracer2 = null;
	}

	//---------------------------------------------------------------------
	// Public test<Xyz> methods
	//
	
	public void testGetInstance()
	{
		assertTrue( "TracerFactory.getInstance() return different instances",
					tracerFactory == TracerFactory.getInstance() );
	}
	
	public void testCreateGlobalTracer()
	{
		assertTrue( "Got different GlobalTracer objects",
					globalTracer == TracerFactory.getInstance().createGlobalTracer() );
					
		assertTrue( "Got a GlobalTracer with incorrect state",
					TracerFactory.getInstance().createGlobalTracer( true ).isOn() == true );
	}

	public void testCreateTracer1()
	{
		assertTrue( "Got different Tracer1 objects",
					tracer1 == TracerFactory.getInstance().
					createTracer( NAMEDTRACER1_NAME ) );

		assertTrue( "Got different Tracer2 objects",
					tracer2 == TracerFactory.getInstance().
					createTracer( NAMEDTRACER2_NAME ) );
					
		assertTrue( "Got a Tracer with incorrect state",
					TracerFactory.getInstance().
					createTracer( NAMEDTRACER1_NAME, true ).isOn() == true );

		assertTrue( "Got a Tracer with incorrect state",
					TracerFactory.getInstance().
					createTracer( NAMEDTRACER1_NAME, false ).isOn() == false );
	}

	public void testCreateTracer2()
	{
		//<todo>verify Tracer created ON/OFF accoring to jutil.properties file</todo>
	}
	
	public void testSetPrintStream()
	{
		//<todo>verify that file gets created with data and messages correct</todo>
	}	

	//-----------------------------------------------------------------------
	// Instance variables
	//

	private TracerFactory tracerFactory = null;	
	
	private Tracer globalTracer = null;
	private Tracer tracer1 = null;
	private Tracer tracer2 = null;
	
	//-----------------------------------------------------------------------
	// Class constants
	//
	
	public static final String NAMEDTRACER1_NAME = "NamedTracer1";
	public static final String NAMEDTRACER2_NAME = "NamedTracer2";
}