package jpos.config.simple.xml;

///////////////////////////////////////////////////////////////////////////////
//
// This software is provided "AS IS".  The JavaPOS working group (including
// each of the Corporate members, contributors and individuals)  MAKES NO
// REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE,
// EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED 
// WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
// NON-INFRINGEMENT. The JavaPOS working group shall not be liable for
// any damages suffered as a result of using, modifying or distributing this
// software or its derivatives. Permission to use, copy, modify, and distribute
// the software and its documentation for any purpose is hereby granted. 
//
///////////////////////////////////////////////////////////////////////////////

import java.util.*;

import jpos.config.*;
import jpos.config.simple.*;
import jpos.util.tracing.Tracer;
import jpos.util.tracing.TracerFactory;


/**
 * A JUnit TestCase for the Loading/saving XML entries testing the 
 * Xerces2RegPopulator class
 * @author E. Michael Maximilien (maxim@us.ibm.com)
 */
public class Xerces2RegPopulatorTestCase extends AbstractRegPopulatorTestCase
{
	//-------------------------------------------------------------------------
	// Ctor(s)
	//

	public Xerces2RegPopulatorTestCase( String name ) { super( name ); }

	//-------------------------------------------------------------------------
	// Protected overridden methods
	//

	protected void setUp() 
    {
        pop = new Xerces2RegPopulator();
        createEntry0();

		addToClasspath( TEST_DATA_PATH );
    }

	protected void tearDown() 
    {
        pop = null;
        entry0 = null;
    }

	//-------------------------------------------------------------------------
	// Private methods
	//

	private JposEntry createEntry0()
	{
		entry0 =
		createJposEntry( "entry0", 
		                 "com.xyz.jpos.XyzJposServiceInstanceFactory",
		                 "com.xyz.jpos.LineDisplayService",
		                 "Xyz, Corp.", "http://www.javapos.com",
		               	 "LineDisplay", "1.5",
		               	 "Example virtual LineDisplay JavaPOS Service from " +
		               	 "virtual Xyz Corporation", 
		               	 "Virtual LineDisplay JavaPOS Service",
		               	 "http://www.javapos.com" );
		               	 
		entry0.addProperty( "deviceBus", "Unknown" );
        entry0.addProperty( "vendor.prop.name2", "vendor.prop.value2" );
        entry0.addProperty( "vendor.prop.name1", "vendor.prop.value1" );
        entry0.addProperty( "vendor.prop.name0", "vendor.prop.value0" );
        
        return entry0;
	}

    private JposEntry 
    createJposEntry( String logicalName, String factoryClass,
                     String serviceClass, String vendorName,
                     String vendorURL, String deviceCategory,
                     String jposVersion, String productName,
                     String productDescription, String productURL )
    {
        JposEntry jposEntry = new SimpleEntry();

        jposEntry.addProperty( JposEntry.LOGICAL_NAME_PROP_NAME, logicalName );
        jposEntry.addProperty( JposEntry.SI_FACTORY_CLASS_PROP_NAME, 
        					   factoryClass );
        jposEntry.addProperty( JposEntry.SERVICE_CLASS_PROP_NAME, serviceClass );
        jposEntry.addProperty( JposEntry.VENDOR_NAME_PROP_NAME, vendorName );
        jposEntry.addProperty( JposEntry.VENDOR_URL_PROP_NAME, vendorURL );
        jposEntry.addProperty( JposEntry.DEVICE_CATEGORY_PROP_NAME, 
        					   deviceCategory );
        jposEntry.addProperty( JposEntry.JPOS_VERSION_PROP_NAME, jposVersion );
        jposEntry.addProperty( JposEntry.PRODUCT_NAME_PROP_NAME, productName );
        jposEntry.addProperty( JposEntry.PRODUCT_DESCRIPTION_PROP_NAME, 
        					   productDescription );
        jposEntry.addProperty( JposEntry.PRODUCT_URL_PROP_NAME, productURL );

        return jposEntry;
    }

    private Enumeration searchEntriesForVendorName( Enumeration entries, 
    												 String vendorName )
    {
        Vector v = new Vector();

        while( entries.hasMoreElements() )
        {
            JposEntry jposEntry = (JposEntry)entries.nextElement();

            if( jposEntry.hasPropertyWithName( JposEntry.VENDOR_NAME_PROP_NAME ) )
                if( jposEntry.getPropertyValue( JposEntry.VENDOR_NAME_PROP_NAME ).
                    toString().equals( "JUnit Corp." ) )
                    v.addElement( jposEntry );
        }

        return v.elements();
    }

	//-------------------------------------------------------------------------
	// Public testXyz() methods
	//

	public void testGetClassName()
	{
		assertTrue( "Incorrect class name returned",
					pop.getClassName().
					equals( Xerces2RegPopulator.class.getName() ) );
	}
	
	public void testGetName()
	{
		assertTrue( "Incorrect class name returned",
					pop.getName().
					equals( Xerces2RegPopulator.
					        XERCES2_REG_POPULATOR_NAME_STRING ) );

	}
	
	public void testLoad1()
	{
		pop.load( JCL_JUNIT_XML_FILE_NAME );
	}

	public void testLoad2()
	{
		//<todo/>
	}

	public void testSave1()
	{
		//<todo/>
	}
	
	public void testSave2()
	{
		//<todo/>
	}
	
	//-------------------------------------------------------------------------
	// Instance variables
	//

	private JposEntry entry0 = null;

    private Xerces2RegPopulator pop = null;

    private Tracer tracer = TracerFactory.getInstance().
    						 createTracer( "Xerces2RegPopulatorTestCase", 
    						               true );

	//-------------------------------------------------------------------------
	// Class constants
	//

	public static final String JCL_JUNIT_XML_FILE_NAME = 
							   RELATIVE_TEST_DATA_PATH + 
							   "jcl-junit-schema.xml";
}