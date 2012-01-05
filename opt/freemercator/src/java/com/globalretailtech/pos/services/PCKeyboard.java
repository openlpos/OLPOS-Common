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

package com.globalretailtech.pos.services;

import jpos.*;
import jpos.config.JposEntry;
import jpos.events.DataEvent;

import java.util.*;
import java.awt.event.*;

import org.apache.log4j.Logger;

/**
 * Service implementation of a PC keyboard. 
 * This class must be listener of all KeyPressed events in order to work
 * correctly
 *
 *                                              
 *   key definition examples for jpos.xml entry 
 *   key.VK_F1             - F1                 
 *   key.VK_F1+SHIFT       - SHIFT+F1           
 *   key.VK_F1-SHIFT       - SHIFT+F1           
 *   key.VK_F1+SHIFT+CTRL  - SHIFT+CTRL+F1      
 *                                         
 *   ALT, CTRL, SHIFT are recognized       
 *
 * @author  Igor Semenko
 * @see PosTabs.java
 */
public class PCKeyboard extends BaseServiceAdapter 
	implements jpos.loader.JposServiceInstance, jpos.services.POSKeyboardService14, KeyListener {

	Logger logger = Logger.getLogger (PCKeyboard.class.getName());
	
    private boolean autoDisable;
    private boolean dataEventEnabled;
    private int dataCount;
    private int eventTypes;
    private int posKeyData;
	Map mappings;

    public PCKeyboard () {
    	
        super(PCKeyboard.class.getName());

		try {
			setDeviceServiceVersion(1004000);
		} catch (JposException e) {
		}
    }
    
    public void clearKeys(){
		mappings = new HashMap();
    }
    
    public void addKey (Key key){
    	if ( key != null )
			mappings.put ( new Integer(key.getHashCode()), key);
    }

	/**
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent e) {
		if ( mappings == null ) return;
		
		Key pressedKey = new Key (e);
		Integer keyHashCode = new Integer ( pressedKey.getHashCode() );
		if ( mappings.containsKey( keyHashCode )){

			Key key = (Key) mappings.get( keyHashCode);
			
			if ( key.matchModifiers (e.getModifiersEx())){

				DataEvent event = new DataEvent(this, 0);
				try{
					setDataCount(1);
					setPOSKeyData(key.getMercatorCode());
				} catch (jpos.JposException jpe) {
					logger.error("",jpe);
				}
	
				fireEvent(event);

				return;
			}

		}
		logger.debug ("key ["+e.getKeyCode()+"]["+e.getModifiersEx()+"] NOT defined");
	}

	public void keyReleased(KeyEvent e){
	}

	public void keyTyped(KeyEvent e){}

    // 1.2 Capabilities
    public boolean getCapKeyUp() throws JposException {
        return true;
    }

    // Properties
    public boolean getAutoDisable() throws JposException {
        return autoDisable;
    }

    public void setAutoDisable(boolean a) throws JposException {
        autoDisable = a;
    }

    public int getDataCount() throws JposException {
        return dataCount;
    }

    public void setDataCount(int d) throws JposException {
        dataCount = d;
    }

    public boolean getDataEventEnabled() throws JposException {
        return dataEventEnabled;
    }

    public void setDataEventEnabled(boolean d) throws JposException {
        dataEventEnabled = d;
    }

    public int getEventTypes() throws JposException {
        return eventTypes;
    }

    public void setEventTypes(int e) throws JposException {
        eventTypes = e;
    }

    public int getPOSKeyData() throws JposException {
        return posKeyData;
    }

    public void setPOSKeyData(int d) throws JposException {
        posKeyData = d;
    }

    public int getPOSKeyEventType() throws JposException {
        return eventTypes;
    }

    // Methods
    public void clearInput() throws JposException {
    }

    // 1.3 Capabilities


    public int getCapPowerReporting() throws JposException {
        return 0;
    }

    // Properties
    public int getPowerNotify() throws JposException {
        return 0;
    }

    public void setPowerNotify(int powerNotify) throws JposException {
    }

    public int getPowerState() throws JposException {
        return 0;
    }
	/**
	 * @see jpos.loader.JposServiceInstance#deleteInstance()
	 */
	public void deleteInstance() throws JposException {
	}
	/**
	 * @param entry
	 */
	public void init(JposEntry entry) {
		
		clearKeys();
		
		for (Enumeration keys = entry.getPropertyNames(); keys.hasMoreElements();) {
		
			String keyDef = (String) keys.nextElement();
		
			if ( keyDef.startsWith("key.")){
		
				String val = (String)entry.getPropertyValue(keyDef);
		
				keyDef = keyDef.substring(4);
		
				try{
		
					addKey(parseKey (keyDef, val));
		
				}catch (Exception exc){logger.warn ("Can't parse pair "+keyDef+"="+val);}
			}
		}	
	}
	
	/**
	 */
	private Key parseKey(String keyDef, String val) {
		
		if (keyDef == null || keyDef.length()== 0)
			return null;
		
		String keyCodeDef = keyDef;
		String modifiersDef = null;	
		Key key = new Key ();

		// both '-' and '+' are recognized
		if (keyDef.indexOf('+') != -1 || keyDef.indexOf('-') != -1){
			int i = keyDef.indexOf('+');
			if (i==-1) i = keyDef.indexOf('-');
			keyCodeDef = keyDef.substring (0,i).trim();
			modifiersDef = keyDef.substring(i);
			key.setModifiers (key, modifiersDef); 			
		}
		
		key.setMercatorCode(Integer.parseInt(val));
		 
		try {
			if (keyCodeDef.startsWith("0x")) {
				key.setCode(Integer.parseInt(keyCodeDef.substring(2), 16));
			} else if (keyCodeDef.startsWith("VK_")) {
				java.lang.reflect.Field f =
					java.awt.event.KeyEvent.class.getField(keyCodeDef);
				key.setCode(f.getInt(f));
			} else {
				key.setCode(Integer.parseInt(keyCodeDef));
			}
		} catch (Exception e) {
			logger.warn ("Can't parse ["+keyDef+"]");
			return null;
		}
		
		logger.debug (keyDef+"->"+val);
		
		return key;	
	}
	/**************************************************/
	/**                     Key                       */	
	/**************************************************/
	class Key {
		int code;
		int mercatorCode;
		boolean alt;
		boolean ctrl;
		boolean shift;
		
		
		public Key (){
			super();
		}

		/** creates Key with known key modifiers */
		public Key (KeyEvent event){
			super();		
			if ( (event.getModifiersEx() & KeyEvent.ALT_DOWN_MASK) > 0 )
				setAlt (true);
			if ( (event.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) > 0)
				setCtrl (true);
			if ( (event.getModifiersEx() & KeyEvent.SHIFT_DOWN_MASK) > 0)
				setShift (true);
			setCode (event.getKeyCode());	
		}
		
		/**
		 * Returns whether modifiers matched
		 */
		public boolean matchModifiers(int i) {
			if ( alt && ((i & KeyEvent.ALT_DOWN_MASK) == 0))
				return false;
			if ( ctrl && ((i & KeyEvent.CTRL_DOWN_MASK) == 0))
				return false;
			if ( shift && ((i & KeyEvent.SHIFT_DOWN_MASK) == 0))
				return false;
			return true;
		}
		/**
		 */
		private void setModifiers(Key key, String modifiersDef) {
			if ( modifiersDef.indexOf ("ALT") != -1 ||
				 modifiersDef.indexOf ("alt") != -1)
				setAlt (true);
			if ( modifiersDef.indexOf ("CTRL") != -1 ||
				modifiersDef.indexOf ("ctrl") != -1)
				setCtrl (true);
			if ( modifiersDef.indexOf ("SHIFT") != -1 ||
				modifiersDef.indexOf ("shift") != -1)
				setShift (true);
		}
		
		/** Creates hash code as function of KeyCode, Alt, Ctrl, Shift modifiers*/
		public int getHashCode() {
			int _code = getCode();
			if (alt) _code += 1000;
			if (ctrl) _code += 2000;
			if (shift) _code += 4000;
			return _code;
		}
	
		
		public int getCode() {
			return code;
		}
		
		public void setCode(int i) {
			code = i;
		}

		public int getMercatorCode() {
			return mercatorCode;
		}
		public void setMercatorCode(int i) {
			mercatorCode = i;
		}

		/**
		 * Alt modifier
		 */
		public void setAlt(boolean b) {
			alt = b;
		}

		/**
		 * Ctrl modifier
		 */
		public void setCtrl(boolean b) {
			ctrl = b;
		}

		/**
		 * Shift modifier
		 */
		public void setShift(boolean b) {
			shift = b;
		}

	}
}




