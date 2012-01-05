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

package jpos.services;

import jpos.*;

/**
 *
 * @author  Quentin Olson
 * @see 
 */
public class OperDisplayException extends JposException
{

    public OperDisplayException(int errorCode)
    {
        super(errorCode);
    }

    public OperDisplayException(int errorCode, int errorCodeExtended)
    {
        super(errorCode, errorCodeExtended);
    }

    public OperDisplayException(int errorCode, String description)
    {
        super(errorCode, description);
    }

    public OperDisplayException(int errorCode, int errorCodeExtended,
                                String description)
    {
        super(errorCode, errorCodeExtended, description);
    }

    public OperDisplayException(int errorCode, String description,
                                Exception origException)
    {
        super(errorCode, description, origException);
    }

    public OperDisplayException(int errorCode, int errorCodeExtended,
                                String description, Exception origException)
    {
        super(errorCode, errorCodeExtended, description, origException);
    }

}


