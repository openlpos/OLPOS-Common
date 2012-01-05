package com.globalretailtech.admin.nav;

import com.globalretailtech.admin.settings.Settings;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.*;
import java.util.Vector;

/*
 * Copyright (C) 2003 Casey Haakenson
 * <casey@haakenson.com>
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

public class NavNode extends DefaultMutableTreeNode {

    protected Vector children = new Vector();

    public NavNode(String name) {
        super(name);
    }

    public Settings createSettings(){
        return null;
    }

    public JPopupMenu getPopupMenu(){
        return null;
    }

    public void add(MutableTreeNode newChild) {
        children.add(newChild);
        super.add(newChild);
    }

    public void remove(int childIndex) {
        children.remove(getChildAt(childIndex));
        super.remove(childIndex);
    }

}
