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

import java.util.Vector;


import com.globalretailtech.util.Application;
import com.globalretailtech.data.InputFilter;

/**
 * Filter class
 *
 *
 * @author  Quentin Olson
 */
public class Filters extends Vector {

	org.apache.log4j.Logger logger =
		org.apache.log4j.Logger.getLogger(Filters.class);
		
    public Filters(int filtertype) {

        super();

        // get all input filters

        String fetchSpec = InputFilter.getAllByType(filtertype);
        Vector tmp = Application.dbConnection().fetch(new InputFilter(), fetchSpec);

        if (tmp.size() > 0) {

            for (int i = 0; i < tmp.size(); i++) {
            	InputFilter inputFilter = (InputFilter) tmp.elementAt(i);
				if (inputFilter.filterClass() != null) {
					try {
						Filter f = (Filter) Class.forName(inputFilter.filterClass()).newInstance();
						f.setDisplayName( inputFilter.displayName());
						f.setFilterName(inputFilter.filterName());
						f.setRegex(inputFilter.regex());
						f.setSubtype(inputFilter.filterSubType());
						f.setType(inputFilter.filterType());
						if (inputFilter.fields() != null)
							f.setFields (inputFilter.fields());
						add(f);
					} catch (Exception e) {
						logger.warn("Can't load class for filter: " + inputFilter.filterClass(), e);
						return;
					}
				}

            }
        }
    }

    public Filter getFilter(int index) {

        return (Filter) super.elementAt(index);

    }

}


