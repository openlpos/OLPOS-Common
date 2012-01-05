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

package com.globalretailtech.pos.gui;

import java.util.Vector;
import java.util.Hashtable;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;


import com.globalretailtech.data.*;
import com.globalretailtech.pos.context.PosContext;
import com.globalretailtech.pos.events.*;
import com.globalretailtech.pos.devices.*;

import com.globalretailtech.util.PositionLayout;
import com.globalretailtech.util.PositionConstraints;
import com.globalretailtech.util.Application;
import com.globalretailtech.admin.settings.SettingsHandler;
import com.globalretailtech.admin.settings.XMLSettings;
import com.globalretailtech.admin.settings.Field;
import org.apache.log4j.Logger;

/**
 * Container for buttons.
 *
 * @author  Quentin Olson
 * @see
 */
public class MenuPanel extends JPanel {

    static Logger logger = Logger.getLogger(MenuPanel.class);

    private SubMenu menu;
    private Vector elements;

    public SubMenu menuData() {
        return menu;
    }

    public MenuPanel(PosContext context,
                     SubMenu m,
                     JPanel container,
                     int width,
                     int height) {

        super();

        menu = m;
        elements = new Vector();
        JComponent component = null;

        PositionLayout posLayout = new PositionLayout();
        setLayout(posLayout);
        PositionConstraints pCons;

        setPreferredSize(new Dimension(width, height));

        // Get the number of rows and columns from the buttons

        int nrows = 0;
        int ncols = 0;

        for (int n = 0; n < menu.buttons().size(); n++) {
            com.globalretailtech.data.PosKey k = (com.globalretailtech.data.PosKey) menu.buttons().elementAt(n);
            if ((k.yLoc() + 1) > nrows)
                nrows = k.yLoc() + 1;
            if ((k.xLoc() + 1) > ncols)
                ncols = k.xLoc() + 1;
        }

        // compute button size based on screen width/height and spacer

        int spacer = 4;

        int kwidth = getComponentSize(width, ncols, spacer);
        int kheight = getComponentSize(height, nrows, spacer);

        for (int n = 0; n < menu.buttons().size(); n++) {

            com.globalretailtech.data.PosKey k = (com.globalretailtech.data.PosKey) menu.buttons().elementAt(n);

            if (k.deviceType() == PosKey.POS_GUI) {

                PosGui gui = null;

                pCons = new PositionConstraints();
                pCons.x = 0;
                pCons.y = 0;
                pCons.width = width;
                pCons.height = height;

                try {
					gui = (PosGui) Class.forName(k.keyClass()).newInstance();
                } catch (Exception e) {
                    logger.warn("Can't load class in MenuPanel, " + k.keyClass(), e);
                }

                if (gui != null) {

                    gui.init(context);

                    posLayout.setConstraints(gui.getGui(), pCons);
                    add(gui.getGui());

                    elements.add(gui);

                    if (gui instanceof PosPrompt) {
                        context.operPrompt().setPrompt((PosPrompt) gui);
                    } else if (gui instanceof PosTicket) {
                        context.receipt().setOperDisplay((PosTicket) gui);
                    }
                } else {
                    logger.warn("Failed to create gui" + gui.toString());
                }
            } else {

                pCons = new PositionConstraints();
                pCons.x = getElementPosition(kwidth, k.xLoc(), spacer);
                pCons.y = getElementPosition(kheight, k.yLoc(), spacer);
                pCons.width = getElementSize(kwidth, k.keyWidth(), spacer);
                pCons.height = getElementSize(kheight, k.keyHeight(), spacer);

                switch (k.keyType()) {

                    case PosEvent.NAVIGATE:
                    case PosEvent.HOME:
                    case PosEvent.NEXT:
                    case PosEvent.PREV:

                        MenuButton mb = new MenuButton(context, k, container);
                        mb.addActionListener(new MenuAction(context));
                        component = mb;
                        break;

                    default:

                        mb = new MenuButton(context, k, container);
                        mb.addActionListener(new PosAction(context));
                        component = mb;
                        break;
                }

                posLayout.setConstraints(component, pCons);
                add(component);
                elements.add(component);
            }
        }
        context.disableKeys();  // disable at startup...

    }

    class MenuAction implements ActionListener {

        private PosContext context;

        public MenuAction(PosContext value) {
            context = value;
        }

        public void actionPerformed(ActionEvent e) {


            if (context.lockThread() != null) {
                context.lockThread().reset();
            }

            MenuButton button = (MenuButton) e.getSource();
            PosEvent.eventLogger.info(Integer.toString(button.keyEvent().config().keyID()));
            if (checkEdit(context, button, e)){

                CardLayout cardlayout = (CardLayout) button.container().getLayout();

                switch (button.keyEvent().config().keyType()) {
                    case com.globalretailtech.pos.events.PosEvent.PREV:
                        cardlayout.previous(button.container());
                        break;

                    case com.globalretailtech.pos.events.PosEvent.HOME:
                        cardlayout.first(button.container());
                        break;

                    default:

                        if (button.validTransition(context)) {
							cardlayout.show(button.container(), button.keyEvent().config().keyVal()+"");
//							cardlayout.show(button.container(), button.keyEvent().config().keyText());
                        }
                        break;
                }
            }
        }
    }

    /**
     * Finalize all components
     */
    public void close() {
    }

    class PosAction implements ActionListener {

        private PosContext context;

        public PosAction(PosContext value) {
            context = value;
        }

        public void actionPerformed(ActionEvent e) {

            if (context.lockThread() != null) {
                context.lockThread().reset();
            }

            MenuButton button = (MenuButton) e.getSource();
            PosEvent.eventLogger.info(Integer.toString(button.keyEvent().config().keyID()));
            if (checkEdit(context, button, e)){
                try {
                    button.process(context);
                } catch (PosException je) {
                }
            }
        }
    }

    protected boolean checkEdit(PosContext context, MenuButton button, ActionEvent e){
        if (Application.isEditMode() && (e.getModifiers() & ActionEvent.CTRL_MASK) == ActionEvent.CTRL_MASK ){
//            String[] options = new String[]{"Edit Button", "Execute Button"};
//            int result = JOptionPane.showOptionDialog(button, "Edit Options", "Edit Options", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
//
//            if (result == 0){
                //show a dialog that has the settings for the particular button.
                JDialog dialog = new JDialog(PosFrame.getFrames()[0], "Edit Button");
                XMLSettings settings = new XMLSettings(new ButtonSettingsHandler(context, button, dialog));
                settings.ignoreField("KEY_ID");
				settings.setColorField("BG_COLOR");
				settings.setColorField("FG_COLOR");
                ResultSet rs = Application.dbConnection().executeWithResult("SELECT * from pos_key where key_id = " + button.getKeyconfig().keyID());
                try {
                    rs.next();
                    settings.setResultSet(rs);
                    dialog.getContentPane().add(settings);
                    dialog.pack();
                    dialog.show();
                } catch (SQLException se){
                    logger.fatal("Couldn't edit button:" + se, se);
                }
                return false;
//            } else {
//                return true;
//            }
        } else {
            return true;
        }
    }

    class ButtonSettingsHandler extends SettingsHandler {
        PosContext context;
        MenuButton button;
        JDialog dialog;

        public ButtonSettingsHandler(PosContext context, MenuButton button, JDialog dialog) {
            this.context = context;
            this.button = button;
            this.dialog = dialog;
        }

        public void commit(Hashtable fields) {

            String keyText = ((Field)fields.get("key_text")).getStringValue();
            if (keyText != null && keyText.equals("null"))
            	keyText = "";
			button.setText(keyText);
			
			String keyBgStr = ((Field)fields.get("bg_color")).getStringValue();
			button.setBackground( new Color(new Integer(keyBgStr).intValue()) );
			
			String keyFgStr = ((Field)fields.get("fg_color")).getStringValue();
			button.setForeground( new Color(new Integer(keyFgStr).intValue()) );
			
			button.updateUI();
			
            SettingsHandler.updateTable(fields, "pos_key", "key_id", button.getKeyconfig().keyID()+"");
            
            dialog.hide();
        }

        public void cancel(){
            dialog.hide();
        }
    }

    public void dispose() {
    }

    private int getComponentSize(int size, int nelements, int space) {

        int fillsize = size - ((nelements + 1) * space);
        if (nelements == 0) {
            return size;
        } else {
            return (fillsize / nelements);
        }
    }

    private int getElementSize(int size, int span, int space) {
        return ((size * span) + (space * (span - 1)));
    }

    private int getElementPosition(int size, int pos, int space) {
        return (size + space) * pos + space;
    }
}


// Local Variables:
// tab-width: 3
// End:

