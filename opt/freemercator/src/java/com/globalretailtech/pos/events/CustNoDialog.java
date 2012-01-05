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

import java.util.Hashtable;

import com.globalretailtech.pos.context.*;
import com.globalretailtech.pos.gui.CustMain;

/**
 * CustNoDialog requests customer no and set this customer to
 * the CustMain panel.
 *
 * @author  Igor Semenko
 */
public class CustNoDialog extends PosNumberDialog {
	String promptText;
	
    /** Simple constructor */
    public CustNoDialog() {
        initTransition();
    }

    /** Constructor sets the context*/
    public CustNoDialog(PosContext context) {
        setContext(context);
        initTransition();
    }

    /**
     * Switch on the report type and update displays.
     */
    public void engage(int value) {

		if ( value > 0){
			setPromptText(context().posParameters().getString("CustNoDialogPrompt"));
			if (context().eventStack().event() instanceof CustNoDialog) {
				context().operPrompt().update((CustNoDialog)context().eventStack().event());
				return;
			}	
			states().pushState(0);
			context().eventStack().pushEvent(this);
			context().operPrompt().update(this);
		}else{
			//find CustMain GUI element (PosKey) and
			//set entered number as cust no
			CustMain custMain = CustMain.getInstance(context());

			if ( custMain != null ){

				custMain.setInputText(context().inputLine(), false);
				
				// pop itself to avoid recursion
				context().eventStack().popEvent();			
				if (context().eventStack().event() instanceof CustMain) {
					return;
				} else {
					custMain.states().pushState(0);  // dummy state
					context().eventStack().pushEvent((PosEvent)custMain);
					context().eventStack().nextEvent();
				}
			}
			
			states().popState();
		
		}
    }

    /** Validate transistions state */
    private Hashtable transistions;

    private void initTransition() {
		transistions = new Hashtable();
		transistions.put(RegisterOpen.eventName(), new Boolean(true));
		transistions.put(FirstItem.eventName(), new Boolean(true));
    }

    public boolean validTransition(String event) {
		if (transistions.get(event) != null)
			return ((Boolean)transistions.get(event)).booleanValue();
		else
			return false;
    }

    /** Clear key implementation for this class */
    public void clear() {
    }

    public boolean checkProfile(int event) {
        return true;
    }

    private static String eventname = "CustNoDialog";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }

	/* 
	 * @see com.globalretailtech.pos.events.PosNumberDialog#promptText()
	 */
	public String promptText() {
		return promptText;
	}
	public void setPromptText(String value){
		promptText = value;
	}

	/* 
	 * @see com.globalretailtech.pos.events.PosNumberDialog#type()
	 */
	public int type() {
		return 0;
	}
}


