/*
 * Created on 26.06.2003
 *
 */
package net.sf.mercator.test.regex;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.globalretailtech.data.InputFilter;
import com.globalretailtech.util.Application;

import net.sf.mercator.test.MercatorBaseTest;

/**
 * @author Igor Semenko (igorsemenko@yahoo.com)
 *
 */
public class ScannerRegexTest extends MercatorBaseTest {

	/**
	 * Constructor for ScannerRegexTest.
	 * @param arg0
	 */
	public ScannerRegexTest(String arg0) {
		super(arg0);
	}

	public void testDbConnection(){
		assertNotNull (Application.dbConnection());
	}
	
	public void testEmployeeKey (){
		Pattern p = Pattern.compile( "^01[\\d]{5}0");
		Matcher m = p.matcher("01000010");
		assertTrue (m.find());
		m.reset ("11000010");
		assertTrue ( ! m.find());
		m.reset ("01000011");
		assertTrue ( ! m.find());
		m.reset ("01000");
		assertTrue ( ! m.find());
		m.reset ("8901083200122");
		assertTrue ( ! m.find());
		m.reset ("0100001100");
		assertTrue ( ! m.find());
	}

	public void testCustomerDiscountCard (){
		Pattern p = Pattern.compile( "^0[2-9]([\\d]{6})");
		Matcher m = p.matcher("02000001");
		assertTrue (m.find());
		assertEquals ("000001",m.group(1));
		m.reset ("11000010");
		assertTrue ( ! m.find());
		m.reset ("01000011");
		assertTrue ( ! m.find());
		m.reset ("01000");
		assertTrue ( ! m.find());
		m.reset ("8901083200122");
		assertTrue ( ! m.find());
		m.reset ("0100001100");
		assertTrue ( ! m.find());
	}
	
	public void testEAN13 (){
		InputFilter f = getFilter ("ItemBarCode13");
		assertNotNull (f);
		Pattern p = Pattern.compile(f.regex());
		Matcher m = p.matcher("8901083200122");
		assertTrue (m.find());
		m.reset ("0100001100");
		assertTrue ( ! m.find());
	}

	public void testEAN12 (){
		InputFilter f = getFilter ("ItemBarCode12");
		assertNotNull (f);
		Pattern p = Pattern.compile(f.regex());
		Matcher m = p.matcher("890108320012");
		assertTrue (m.find());
		m.reset ("0100001100");
		assertTrue ( ! m.find());
		m.reset ("8901083200123"); //EAN13
		assertTrue ( ! m.find());
	}

	public void testEAN8 (){
		// don't mix with internal codes
		InputFilter f = getFilter ("ItemBarCode8");
		assertNotNull (f);
		Pattern p = Pattern.compile(f.regex());
		Matcher m = p.matcher("12345678");
		assertTrue (m.find());
		m.reset ("02345678");
		assertTrue ( ! m.find());
		m.reset ("8901083200123"); //EAN13
		assertTrue ( ! m.find());
	}

	public void testEAN7 (){
		InputFilter f = getFilter ("ItemBarCode7");
		assertNotNull (f);
		Pattern p = Pattern.compile(f.regex());
		Matcher m = p.matcher("1234567");
		assertTrue (m.find());
		m.reset ("02345678");
		assertTrue ( ! m.find());
		m.reset ("8901083200123"); //EAN13
		assertTrue ( ! m.find());
	}
	
	
	public static InputFilter getFilter (String name){
		String fetchSpec = InputFilter.getByName(name);
		try {
			Vector v =
				Application.dbConnection().fetch(new InputFilter(), fetchSpec);
			if (v.size() == 1) {
				InputFilter f = (InputFilter) v.elementAt(0);
				return f;
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(ScannerRegexTest.class);
	}

	
}
