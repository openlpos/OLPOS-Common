/*
 * Copyright (C) 2001 Global Retail Technology, LLC
 * <http://www.globalretailtech.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package com.globalretailtech.pos.services.factories;

import java.util.Enumeration;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.globalretailtech.pos.services.JournalPrinter;

import jpos.JposException;
import jpos.config.JposEntry;
import jpos.loader.JposServiceInstance;
import jpos.loader.JposServiceInstanceFactory;

/**
 * 
 * @author Igor Semenko
 *
 */
public class JournalPrinterServiceInstanceFactory
	implements JposServiceInstanceFactory {
		
	Logger logger = Logger.getLogger (JournalPrinterServiceInstanceFactory.class.getName());
		
	public JournalPrinterServiceInstanceFactory (){}
	
	/**
	 * 
	 * @see jpos.loader.JposServiceInstanceFactory#createInstance(java.lang.String, jpos.config.JposEntry)
	 */
	public JposServiceInstance createInstance(String logicalName,	JposEntry entry)
			throws JposException {

		Properties properties = getVendorProperties(entry);
				
		return new JournalPrinter (logicalName, properties);
	}

	protected Properties getVendorProperties(JposEntry entry) {
		Properties properties = new Properties();
		if ( entry != null){
			for (Enumeration keys = entry.getPropertyNames(); keys.hasMoreElements();) {
				String key = (String) keys.nextElement();
				if ( key.startsWith("vendor.")){
					String val = (String)entry.getPropertyValue(key);
					key = key.substring(7);
					properties.setProperty (key,val);
				}
			}
		}
		return properties;
	}

}
