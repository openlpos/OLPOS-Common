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

import org.apache.log4j.Logger;

import com.globalretailtech.pos.services.PCKeyboard;

import jpos.JposException;
import jpos.config.JposEntry;
import jpos.loader.JposServiceInstance;
import jpos.loader.JposServiceInstanceFactory;

/**
 * Singleton, only one instance is possible to create.
 * Single instance is necessary to make one listener of keyboard events.
 * 
 * @author Igor Semenko
 *
 */
public class PCKeyboardServiceInstanceFactory
	implements JposServiceInstanceFactory {
		
	Logger logger = Logger.getLogger (PCKeyboardServiceInstanceFactory.class.getName());
		
	private static JposServiceInstance instance;
	
	public PCKeyboardServiceInstanceFactory (){}
	
	/**
	 * Loads keys from properties in format:
	 * 
	 *  key.PC_KEY_DEF=MERCATOR_KEY_CODE
	 *  
	 *  PC_KEY_DEF is either:
	 *  Integer, ex 103
	 *  Hex string, ex 0x6A
	 *  VK_XXX const, ex VK_ENTER
	 * 
	 * @see jpos.loader.JposServiceInstanceFactory#createInstance(java.lang.String, jpos.config.JposEntry)
	 */
	public JposServiceInstance createInstance(String logicalName,	JposEntry entry)
			throws JposException {
		if ( instance == null ){
			instance = new PCKeyboard ();	
		}
		if ( entry != null){
			((PCKeyboard)instance).init(entry);
		}
		return instance;
	}

}
