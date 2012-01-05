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

package com.globalretailtech.pos.operators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.globalretailtech.pos.context.PosContext;
import com.globalretailtech.pos.events.PosEvent;
import com.globalretailtech.pos.gui.CustMain;

/**
 * Looks up customer by number on card, if found,
 * updates transaction header, if customer has discount,
 * create and set sale-wide Promotion 
 *
 * @author  Igor Semenko
 */
public class CustDiscountCardFilter extends Filter{

	org.apache.log4j.Logger logger =
		org.apache.log4j.Logger.getLogger(CustDiscountCardFilter.class);
		
	public void apply(PosContext context, String s){
		String regex = getRegex();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(s);
		if (!matcher.find())
			return;
		
		//TODO make it thru input_filter_field
		if ( matcher.groupCount() < 1){
			logger.warn ("One regex group representing customer no is expected");
		}
		
		String custNoStr = matcher.group(1);
		logger.debug ("Customer #"+custNoStr);
		int custNo = 0;
		
		try {
			custNo = Integer.parseInt(custNoStr);
		} catch (Exception e) {
			logger.warn("Integer is expected");
		}
		
		//find CustMain GUI element (PosKey) and
		//set scanned data as cust no
		CustMain custMain = CustMain.getInstance(context);
		if ( custMain != null ){
			logger.debug("found CustMain element");
			custMain.setInputText(custNo+"", false);
			
			if (context.eventStack().event() instanceof CustMain) {
				return;
			} else {
				custMain.states().pushState(0);  // dummy state
				context.eventStack().pushEvent((PosEvent)custMain);
			}
			// process event on top
			context.eventStack().nextEvent();		
			context.clearInput();
		}
	}

}


