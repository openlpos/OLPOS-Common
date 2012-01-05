package com.globalretailtech.admin.settings;

import javax.swing.*;
import java.util.Vector;
import java.util.Hashtable;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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

public class Settings extends JPanel {

    protected Hashtable fields = new Hashtable();
    protected SettingsHandler callback;

    boolean allValuesEmpty = false;

	protected Vector ignoreFields = new Vector();
	protected Vector colorFields = new Vector();

    GridBagLayout gridbag;
    GridBagConstraints constraints;

    public Settings(SettingsHandler callback) {
        this.callback = callback;

        gridbag = new GridBagLayout();
        constraints = new GridBagConstraints();
        this.setLayout(gridbag);

        this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(), BorderFactory.createEmptyBorder(5,5,5,5)));

        constraints.ipadx = 5;
        constraints.ipady = 5;
        constraints.weightx = 1.0;
        constraints.anchor = GridBagConstraints.WEST;
    }

    public void addField(String name, Object value, String className, boolean editable){

        if (allValuesEmpty){
            value = new String();
        }
        JLabel label = new JLabel(name);
        JTextField field = new JTextField(value + "");

        field.setEditable(editable);
        field.setColumns(30);

        if (ignoreFields.contains(name.toLowerCase())){
            field.setEditable(false);
        } else {
            fields.put(name.toLowerCase(), new Field(field, name, className));
        }

		layoutField(label, field);

    }

    public Field getField(String name){
        return (Field)fields.get(name.toLowerCase());
    }

    public JComboBox addSelect(String name, String[] options, int selectedIndex) {
        JComboBox field = new JComboBox(options);
        JLabel label = new JLabel(name);

        field.setSelectedIndex(selectedIndex);

        if (ignoreFields.contains(name.toLowerCase())){
            field.setEditable(false);
        } else {
            fields.put(name.toLowerCase(), new Field(field, name, "java.lang.String"));
        }

		layoutField(label, field);

        return field;
    }

    protected void layoutField(JComponent label, JComponent field){
        label.setAlignmentX(JLabel.LEFT_ALIGNMENT);

        gridbag.setConstraints(label, constraints);
        this.add(label);

		String fieldName = ((JLabel)label).getText().toLowerCase();
		if (colorFields.contains(fieldName)){
			((JTextField)field).setColumns(26);
				JButton chooser = new JButton ("..");
				Field f = getField (fieldName);
				if ( f != null ){
					chooser.setBackground( new Color (new Integer (f.getStringValue()).intValue()));
				}
				chooser.addActionListener( new ColorChooserActionListener(chooser,fieldName));
				chooser.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		    
				gridbag.setConstraints(field, constraints);
				this.add(chooser);
		}		
		
        int gridwidth = constraints.gridwidth;
        constraints.gridwidth = GridBagConstraints.REMAINDER;

        gridbag.setConstraints(field, constraints);
        this.add(field);

        constraints.gridwidth = gridwidth;
    }

    public void addDefaultButtonSet(){
        JButton cancel = new JButton("Cancel");
        JButton apply = new JButton("Apply");
        JButton ok = new JButton("OK");

        Box buttonPanel = Box.createHorizontalBox();
//        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(Box.createGlue());

        int gridwidth = constraints.gridwidth;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        gridbag.setConstraints(buttonPanel, constraints);
        this.add(buttonPanel);
        constraints.gridwidth = gridwidth;

        buttonPanel.add(cancel);
//        buttonPanel.add(apply);
        buttonPanel.add(ok);

        cancel.addActionListener(new CancelListener());
        apply.addActionListener(new ApplyListener());
        ok.addActionListener(new OKListener());
    }

    public void ignoreField(String name){
        ignoreFields.add(name.toLowerCase());
    }

    public void allValuesEmpty(boolean b) {
        allValuesEmpty = b;
    }

	public void setColorField(String name){
		colorFields.add(name.toLowerCase());
	}



    class CancelListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            SettingsContainer.closeAll();
            callback.cancel();
        }
    }

    class ApplyListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            callback.commit(fields);
        }
    }

    class OKListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            callback.commit(fields);
            SettingsContainer.closeAll();
        }
    }
    
    class ColorChooserActionListener implements ActionListener {

		JButton button;
		String fieldName;
		    	
    	public ColorChooserActionListener(JButton button, String fieldName){
    		this.button = button;
    		this.fieldName = fieldName;
    	}
    	
		public void actionPerformed(ActionEvent e) {
			Color bgColor
				  = JColorChooser.showDialog(null /*Component*/,
											 "Choose Background Color",
											 getBackground());
				if (bgColor != null){
					button.setBackground(bgColor);
					Field f = getField (fieldName);
					if ( f != null ){
						((JTextField)f.getTextField()).setText(""+bgColor.getRGB());
					}
				}
		}
} 
}
