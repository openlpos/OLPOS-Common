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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jpos.JposException;


import com.globalretailtech.pos.context.*;

/**
 * Loads dialog for start/end dates/numbers and ask FiscalPrinter
 * to print the report
 *
 * @author  Igor Semenko
 */
public class PeriodicTotalsReport extends EnterPricePlu {

	public static final int GET_FIRST_PARAM = 1;
	public static final int GET_SECOND_PARAM = 2;
	
	public static int UNDEFINED = 0;
	public static int DATE_DATE = 1;
	public static int START_END = 2;
	 
	private int mode; //DATE_DATE/START_END
	
	private int param1;
	private int param2;
	
    /** Simple constructor */
    public PeriodicTotalsReport() {
    }

    /** Constructor sets the context*/
    public PeriodicTotalsReport(PosContext context) {
        setContext(context);
    }
    
	public int param1(){
		return param1;
	}
	public int param2(){
		return param2;
	}
	public void setParam1(int value){
		param1=value;
	}
	public void setParam2(int value){
		param2=value;
	}
	
	public boolean param1IsValid (){
		return true;
	}
	public boolean param2IsValid (){
		return true;
	}
    /**
     * Load the start-end dates/numbers dialog
     */
    public void engage(int value) {

		if ((mode == UNDEFINED) && (value == DATE_DATE)) {
			mode = DATE_DATE;
		}else if ((mode == UNDEFINED) && (value == START_END)) {
			mode = START_END;
		}
		
		// cancel?
		if (mode != UNDEFINED && value == 0 && context().inputLine().length()==0){
			mode = UNDEFINED;
			states().clearPending();
			states().clearProcessed();
			states().popState();
			context().operPrompt().update(context().posParameters().getString("CancelText"));
			return;
		}

		if (states().pendingSize() == 0) {

			states().pushState(GET_FIRST_PARAM);
			if ( mode == DATE_DATE)
				setPromptText(context().posParameters().getString("PeriodicTotalsGetStartDate"));
			else	
				setPromptText(context().posParameters().getString("PeriodicTotalsGetStartNo"));
			context().eventStack().pushEvent(this);
			context().operPrompt().update(this);
			return;
		}

		switch (state()) {

			case GET_FIRST_PARAM:  // get first param and push request for second

				setParam1(context().input());
				if (param1IsValid()) {
					popState();
					context().clearInput();
					states().popState();
					states().pushState(GET_SECOND_PARAM);
					if ( mode == DATE_DATE)
						setPromptText(context().posParameters().getString("PeriodicTotalsGetEndDate"));
					else	
						setPromptText(context().posParameters().getString("PeriodicTotalsGetEndNo"));
					context().operPrompt().update(this);
					return;
				} else {
					return;
				}

			case GET_SECOND_PARAM:  // get second param, check and perform report

				setParam2(context().input());
				if (param2IsValid()) {
					popState();
					context().clearInput();
					if (mode == DATE_DATE){
						try {
							SimpleDateFormat formatter = new SimpleDateFormat ("ddMMyyyyHHmm"); // jpos format
							SimpleDateFormat parser = new SimpleDateFormat (context().posParameters().getString("PeriodicTotalsDateFormat")); // user input format
							Date start = parser.parse(""+toDateStr(param1()));
							Date end = parser.parse(""+toDateStr(param2()));
							context().fiscalPrinter().printPeriodicTotalsReportD (formatter.format(start),formatter.format(end));
							mode = UNDEFINED;
							// pause...
							context().eventStack().pushEvent(new Pause(context()));
							context().operPrompt().update((Pause) context().eventStack().event());
						} catch (JposException e1) {
							// TODO What to do with JPosException?
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							logger.error("",e);
						}
					}else{
						try {
							context().fiscalPrinter().printPeriodicTotalsReportN (""+param1,""+param2);
							mode = UNDEFINED;
							// pause...
							context().eventStack().pushEvent(new Pause(context()));
							context().operPrompt().update((Pause) context().eventStack().event());
						} catch (JposException e1) {
							// TODO What to do with JPosException?
						}
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


