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

import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.globalretailtech.data.InputFilterField;
import com.globalretailtech.pos.context.PosContext;
import com.globalretailtech.pos.ej.EjCCTender;
import com.globalretailtech.pos.ej.EjLine;

/**
 *
 * @author  Quentin Olson
 */
public class CCTenderFilter extends Filter {

	static Logger logger = Logger.getLogger(CCTenderFilter.class);


	/**
	 * Apply the filter to the input
	 */
	public void apply(PosContext context, String s) {

		Pattern pattern = Pattern.compile(getRegex());
		Matcher matcher = pattern.matcher(s);
		if (!matcher.find())
			return;

		Hashtable results = new Hashtable();
		for (int i = 1; i < matcher.groupCount(); i++) { //.getParenCount()
			InputFilterField field =
				(InputFilterField) getFields().elementAt(i - 1);
			String match = matcher.group(i); //r.getParen(i);
			if (results != null)
				results.put(field.name(), match);

			if (field.name().equals(ACCT_NO)) {
				// try the check digit if there is one

//				if (inputfilter.checkDigitClass() != null) {
//
//					int result = checkdigit.apply(match);
//					int len = match.length();
//					int lastdigit =
//						Integer.parseInt(match.substring(len - 1, len), 10);
//					if (result != lastdigit){
//						logger.warn("Checkdigit failed in scanner " + match);
//				context.eventStack().clearPending();
//				context.eventStack().clearProcessed();
//				context.eventStack().pushEvent(new PosError(context, PosError.INVALID_CC));
//				context.operPrompt().update((PosError) context.eventStack().event());
//				return;
//                  }
//				}
			}
		}

		EjLine line = (EjLine) context.currEj().lastElement();

		if (line.lineType() == EjLine.TENDER) {

			EjCCTender tender = (EjCCTender) line;
			tender.setContext(context);
			tender.setCcNumber((String) results.get(Filter.ACCT_NO));
			tender.setExprDate((String) results.get(Filter.EXPR_MONTH) + (String) results.get(Filter.EXPR_YEAR));
			context.eventStack().clearPending();
			context.eventStack().clearProcessed();
			tender.states().pushState(EjCCTender.TENDER_FINAL);
			tender.transTender().setTenderDesc((String) results.get(Filter.DISPLAY_NAME));
			tender.engage(0);
		} else {
			logger.warn("Invalid tender ej record" + line);
		}
	}

}
