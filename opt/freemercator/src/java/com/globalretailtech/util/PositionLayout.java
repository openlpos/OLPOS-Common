//--
// A custom constraint-based layout manager for laying out components
// in the traditional positional way (a component at any X and Y
// coordinate with any width and height).
//--
package com.globalretailtech.util;

import java.awt.*;
import java.beans.*;
import java.util.*;

public class PositionLayout implements LayoutManager3 {
    private static final int PREFERRED = 0;
    private static final int MINIMUM = 1;

    private Hashtable consTable = new Hashtable();
    private PositionConstraints
            defaultConstraints = new PositionConstraints();
    public int width;
    public int height;


    //-1 for width or height means use the preferred size
    public PositionLayout() {
        width = -1;
        height = -1;
    }

    public PositionLayout(int w, int h) {
        width = w;
        height = h;
    }

    public void addLayoutComponent(String name, Component comp) {
        //Not used. The user should call setConstraints, and then just
        //use the generic add(comp) method, like with GridBagLayout.
    }

    public Object getConstraints(Component comp) {
        return lookupConstraints(comp).clone();
    }

    public void layoutContainer(Container parent) {
        Component comp;
        Component[] comps;
        PositionConstraints cons;
        Dimension d;
        Dimension pD;
        int x;
        int y;

        comps = parent.getComponents();
        Insets insets = parent.getInsets();
        pD = parent.getSize();
        for (int i = 0; i < comps.length; i++) {
            comp = comps[i];
            cons = lookupConstraints(comp);
            x = cons.x + cons.insets.left + insets.left;
            y = cons.y + cons.insets.top + insets.top;
            d = comp.getPreferredSize();
            if (cons.width != -1)
                d.width = cons.width;
            if (cons.height != -1)
                d.height = cons.height;
            if ((cons.fill == PositionConstraints.BOTH) ||
                    (cons.fill == PositionConstraints.HORIZONTAL)) {
                x = insets.left + cons.insets.left;
                d.width = pD.width - cons.insets.left - cons.insets.right -
                        insets.left - insets.right;
            }
            if ((cons.fill == PositionConstraints.BOTH) ||
                    (cons.fill == PositionConstraints.VERTICAL)) {
                y = insets.top + cons.insets.top;
                d.height = pD.height - cons.insets.top - cons.insets.bottom -
                        insets.top - insets.bottom;
            }
            switch (cons.anchor) {
                case PositionConstraints.NORTH:
                    x = (pD.width - d.width) / 2;
                    y = cons.insets.top + insets.top;
                    break;
                case PositionConstraints.NORTHEAST:
                    x = pD.width - d.width - cons.insets.right - insets.right;
                    y = cons.insets.top + insets.top;
                    break;
                case PositionConstraints.EAST:
                    x = pD.width - d.width - cons.insets.right - insets.right;
                    y = (pD.height - d.height) / 2;
                    break;
                case PositionConstraints.SOUTHEAST:
                    x = pD.width - d.width - cons.insets.right - insets.right;
                    y = pD.height - d.height - cons.insets.bottom -
                            insets.bottom;
                    break;
                case PositionConstraints.SOUTH:
                    x = (pD.width - d.width) / 2;
                    y = pD.height - d.height - cons.insets.bottom -
                            insets.bottom;
                    break;
                case PositionConstraints.SOUTHWEST:
                    x = cons.insets.left + insets.left;
                    y = pD.height - d.height - cons.insets.bottom -
                            insets.bottom;
                    break;
                case PositionConstraints.WEST:
                    x = cons.insets.left + insets.left;
                    y = (pD.height - d.height) / 2;
                    break;
                case PositionConstraints.NORTHWEST:
                    x = cons.insets.left + insets.left;
                    y = cons.insets.top + insets.top;
                    break;
                default:
                    break;
            }
            comp.setBounds(x, y, d.width, d.height);
        }
    }

    private Dimension layoutSize(Container parent, int type) {
        int newWidth;
        int newHeight;

        if ((width == -1) || (height == -1)) {
            Component comp;
            Component[] comps;
            PositionConstraints cons;
            Dimension d;
            int x;
            int y;

            newWidth = newHeight = 0;
            comps = parent.getComponents();
            for (int i = 0; i < comps.length; i++) {
                comp = comps[i];
                cons = lookupConstraints(comp);
                if (type == PREFERRED)
                    d = comp.getPreferredSize();
                else
                    d = comp.getMinimumSize();
                if (cons.width != -1)
                    d.width = cons.width;
                if (cons.height != -1)
                    d.height = cons.height;
                if (cons.anchor == PositionConstraints.NONE) {
                    x = cons.x;
                    y = cons.y;
                } else {
                    x = cons.insets.left;
                    y = cons.insets.top;
                }
                if ((cons.fill != PositionConstraints.BOTH) &&
                        (cons.fill != PositionConstraints.HORIZONTAL))
                    newWidth = Math.max(newWidth, x + d.width);
                else
                    newWidth = Math.max(newWidth, d.width + cons.insets.left +
                            cons.insets.right);
                if ((cons.fill != PositionConstraints.BOTH) &&
                        (cons.fill != PositionConstraints.VERTICAL))
                    newHeight = Math.max(newHeight, y + d.height);
                else
                    newHeight = Math.max(newHeight, d.height + cons.insets.top +
                            cons.insets.bottom);
            }
            if (width != -1)
                newWidth = width;
            if (height != -1)
                newHeight = height;
        } else {
            newWidth = width;
            newHeight = height;
        }
        Insets insets = parent.getInsets();
        return (new Dimension(newWidth + insets.left + insets.right,
                newHeight + insets.top + insets.bottom));
    }

    private PositionConstraints lookupConstraints(Component comp) {
        PositionConstraints p = (PositionConstraints) consTable.get(comp);
        if (p == null) {
            setConstraints(comp, defaultConstraints);
            p = defaultConstraints;
        }
        return p;
    }

    public Dimension minimumLayoutSize(Container parent) {
        return layoutSize(parent, MINIMUM);
    }

    public Dimension preferredLayoutSize(Container parent) {
        return layoutSize(parent, PREFERRED);
    }

    public void removeLayoutComponent(Component comp) {
        consTable.remove(comp);
    }

    public void setConstraints(Component comp, Object cons) {
        if ((cons == null) || (cons instanceof PositionConstraints)) {
            PositionConstraints pCons;
            if (cons == null)
                pCons = (PositionConstraints) defaultConstraints.clone();
            else
                pCons = (PositionConstraints)
                        ((PositionConstraints) cons).clone();
            consTable.put(comp, pCons);
            //The following is necessary for nested position layout
            //managers. When the constraints of the component are set
            //to be elastic or non-elastic, then check to see if the
            //component itself is a container with a position layout
            //manager and, if so, set the layout manager itself to be
            //elastic or non-elastic as necessary.
            if (Beans.isInstanceOf(comp, Container.class))
                if (((Container) Beans.getInstanceOf(comp,
                        Container.class)).getLayout()
                        instanceof PositionLayout) {
                    PositionLayout layout;
                    layout = (PositionLayout)
                            ((Container) Beans.getInstanceOf(comp,
                                    Container.class)).getLayout();
                    layout.width = pCons.width;
                    layout.height = pCons.height;
                }
        }
    }

    public Object clone() {
        PositionLayout p = new PositionLayout(width, height);
        return p;
    }
}
