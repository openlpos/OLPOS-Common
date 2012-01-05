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

package com.globalretailtech.pos.ej;

import java.util.Hashtable;

import com.globalretailtech.data.Media;
import com.globalretailtech.data.TransTender;
import com.globalretailtech.data.Total;
import com.globalretailtech.pos.context.*;

/**
 * Credit tender operation
 *
 * Behaves exactly as cash tender (EjTender),
 * but sets different type of tender
 *
 * @author  Igor Semenko
 */
public class EjCreditTender extends EjTender {

    public EjCreditTender() {
    	super ();
    }

    public EjCreditTender(PosContext c) {
    	super (c);
    }

    public EjCreditTender(TransTender t) {
    	super (t);
    }

    public EjCreditTender(TransTender t, Hashtable literals) {
    	super (t, literals);
    }


	/**
	 * Methods to override for other types of simple tenders
	 *  
	 */

	public int getTenderType() {
		return TransTender.CREDIT;
	}

	public String getTenderDesc() {
		return context().posParameters().getString("CreditTender");
	}

	protected int getMediaType() {
		return Media.CREDIT;
	}


    private static String eventname = "EjCreditTender";


	/**
	 * TODO It is supposed that there will be no change, but no check is made
	 **/
	protected void updateTotals(int sign) {

		if (context().trainingMode())
			return;

//		double result = sign * context().posMath().sub(amount(), change() != amount() ? change() : 0.0);
		double result = sign * amount();

		if (result != 0.0) {
			Total.addToTotal(context().siteID(),
					context().posNo(),
					Total.CREDIT_BASE,
					result);
		}
		//	subtract change cash back from drawer
//		if (transTender().change() > 0) {  
//			Total.addToTotal(context().siteID(),
//					context().posNo(),
//					sign * Total.CASH_IN_DRAWER, (context().posMath().mult(transTender().change(), -1.0)));
//		}



//        double result = sign * context().posMath().sub(amount(), change() != amount() ? change() : 0.0);

  }

}
