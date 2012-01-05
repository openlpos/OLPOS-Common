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

package com.globalretailtech.pos.events;

/**
 * Abstract class to help dialogs with
 * numbers (integer) input.
 *
 * @author  Quentin Olson
 */
public abstract class PosNumberDialog extends PosDialogEvent {

    public final static int CLEAR = 0;
    public final static int MASK = 1;
    public final static int DECIMAL = 2;
    public final static int CURRENCY = 3;

    public abstract String promptText();

    public abstract int type();
}


