/*
* Copyright (C) 2003 Igor Semenko
* igorsemenko@yahoo.com
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
*
*/

package com.globalretailtech.pos.events;


/**
 * Backspace key, removes last entered char in the
 * context input text.
 *
 * @author  Igor Semenko
 */
public class BackspaceKey extends PosEvent {

    /** Simple constructor for dynamic load */
    public BackspaceKey() {
    }

    /**
     * Erases last entered char, updates displays.
     */
    public void engage(int value) {
    	if (context().inputLine() != null &&
			context().inputLine().length()>0){
				context().eraseLast();
				// force display to be updated
				NumKey nk = new NumKey();
				nk.setContext(context());
				nk.engage(-1);
		}
    }

	/* 
	 */
	public void clear() {
	}

	/* 
	 */
	public boolean validTransition(String event) {
		return true;
	}

}


