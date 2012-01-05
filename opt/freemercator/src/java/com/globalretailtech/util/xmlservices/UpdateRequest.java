/*
 * Created on 17.04.2003
 *
 */
package com.globalretailtech.util.xmlservices;

import java.io.Reader;

/**
 * @author isemenko
 *
 */
public interface UpdateRequest {
	
	public boolean isValid ();
	
	public int getRequestID ();
	
	public Reader getDataBlockReader();
	
	public Reader getAppBlockReader();

	public void process();
	
	

}
