//--
// The constraints for each component in a position layout manager.
// If the width or height is set to -1 (the default), then the
// component will automatically size to its preferred size in that
// dimension.
//--
package com.globalretailtech.util;

import java.awt.*;

public class PositionConstraints implements Cloneable {
    public static final int NONE = 0;
    public static final int BOTH = 1;
    public static final int HORIZONTAL = 2;
    public static final int VERTICAL = 3;

    public static final int NORTH = 1;
    public static final int NORTHEAST = 2;
    public static final int EAST = 3;
    public static final int SOUTHEAST = 4;
    public static final int SOUTH = 5;
    public static final int SOUTHWEST = 6;
    public static final int WEST = 7;
    public static final int NORTHWEST = 8;

    public int x;
    public int y;
    public int width;
    public int height;
    public int anchor;
    public int fill;
    public Insets insets;


    //-1 for width or height means use the preferred size
    public PositionConstraints() {
        x = 0;
        y = 0;
        width = -1;
        height = -1;
        anchor = NONE;
        fill = NONE;
        insets = new Insets(0, 0, 0, 0);
    }

    public Object clone() {
        PositionConstraints p = new PositionConstraints();
        p.x = x;
        p.y = y;
        p.width = width;
        p.height = height;
        p.anchor = anchor;
        p.fill = fill;
        p.insets = (Insets) insets.clone();
        return p;
    }
}
