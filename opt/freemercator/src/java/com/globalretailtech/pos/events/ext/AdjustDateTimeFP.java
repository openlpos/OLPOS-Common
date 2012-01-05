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
package com.globalretailtech.pos.events.ext;

import java.text.SimpleDateFormat;
import java.util.Date;

import jpos.JposException;


import com.globalretailtech.pos.context.*;
import com.globalretailtech.pos.events.EnterPricePlu;

/**
 * Loads dialog for adjusting datetime on Fiscal Printer
 *
 * @author  Igor Semenko
 */
public class AdjustDateTimeFP extends EnterPricePlu {

	public static final int GET_FIRST_PARAM = 1;
	
	private String param1;
	
    /** Simple constructor */
    public AdjustDateTimeFP() {
    }

    /** Constructor sets the context*/
    public AdjustDateTimeFP(PosContext context) {
        setContext(context);
    }
    
	public String param1(){
		return param1;
	}
	public void setParam1(String value){
		param1=value;
	}
	
	public boolean param1IsValid (){
		return true;
	}

    /**
     * Load the start-end dates/numbers dialog
     */
    public void engage(int value) {

		// cancel?
//		if (mode != UNDEFINED && value == 0 && context().inputLine().length()==0){
//			mode = UNDEFINED;
//			states().clearPending();
//			states().clearProcessed();
//			states().popState();
//			context().operPrompt().update(context().posParameters().getString("CancelText"));
//			return;
//		}

		if (states().pendingSize() == 0) {

			states().pushState(GET_FIRST_PARAM);
			setPromptText(context().posParameters().getString("AdjustTime"));
			context().eventStack().pushEvent(this);
			context().operPrompt().update(this);
			return;
		}

		switch (state()) {

			case GET_FIRST_PARAM:  // get first param and push request for second

			SimpleDateFormat parser = new SimpleDateFormat ("HHmmss");
			SimpleDateFormat formatter = new SimpleDateFormat ("ddMMyyyyHHmm"); // jpos
				setParam1(context().inputLine());
				if (param1IsValid()) {
					popState();
					context().clearInput();
					states().popState();
					context().operPrompt().update(this);
					try {
						Date dt = parser.parse(param1());
						context().fiscalPrinter().setDate(formatter.format(dt));
					} catch (JposException je) {
						// TODO: handle jpos exception
					} catch (Exception e) {
						// TODO: handle exception
					}
					return;
				} else {
					return;
				}

		}

    }

    /**
	 */
	private String toDateStr(int i) {
		String s = Integer.toString(i);
		if ( s.length()== 5)
			return " "+s;
		return s;
	}

	/** Validate transistions state */
    public boolean validTransition(String event) {
        return true;
    }

    /** Clear key implementation for this class */
    public void clear() {
    }

    private static String eventname = "PeriodicTotalsReport";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }
}


