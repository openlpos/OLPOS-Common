//--
// A simple alternative to LayoutManager2 that provides for a
// consistent way of getting and setting constraints for the
// components in a container.
//--
package com.globalretailtech.util;

import java.awt.*;

public interface LayoutManager3 extends LayoutManager, Cloneable {
    public abstract Object getConstraints(Component comp);

    public abstract void setConstraints(Component comp, Object cons);

    public abstract Object clone();
}

