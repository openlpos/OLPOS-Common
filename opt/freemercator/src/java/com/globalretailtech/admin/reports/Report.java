package com.globalretailtech.admin.reports;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
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

public abstract class Report extends JPanel {
    String title;
    JTable table;
    String[] filterTypes;
    JPanel titlePanel = new JPanel();

    public Report(String title) {
        this.title = title;
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BorderLayout());

        this.setBackground(Color.white);

        this.setBorder(BorderFactory.createMatteBorder(15, 15, 15, 15, Color.white));

        this.setLayout(new BorderLayout());

        JPanel labelPanel = new JPanel();
        FlowLayout labelLayout = new FlowLayout(FlowLayout.CENTER);
        labelPanel.setLayout(labelLayout);
        labelPanel.setBackground(Color.white);

        JLabel titleLabel = new JLabel("<html><h3>" + title + "</h3></html>");

        labelPanel.add(titleLabel);

        titlePanel.add(labelPanel);
        this.add(titlePanel, BorderLayout.NORTH);

    }

    public void setModel(String[] columns, Object[][] rows){
//        table.setModel());
        table = new JTable(new ReportModel(columns, rows));
        table.setBackground(Color.white);
        table.setShowGrid(false);
        table.setDragEnabled(false);
        table.setRowSelectionAllowed(false);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
//        table.setCellSelectionEnabled(false);

//        table.setModel(new ReportModel(columns, rowData));

        JScrollPane scroller = new JScrollPane(table);
        scroller.setBackground(Color.WHITE);
        scroller.setViewportBorder(null);
        scroller.setOpaque(true);
        this.add(scroller, BorderLayout.CENTER);
    }

    public void setTotals(String[] values){
        JTable table = new JTable(new ReportModel(values, new String[][]{values}));
        table.setBackground(Color.white);
        table.setShowGrid(false);
        table.setDragEnabled(false);
        table.setRowSelectionAllowed(false);
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);

        this.add(table, BorderLayout.SOUTH);
    }

    public void setFilterTypes(String[] filterTypes, int selected) {
        this.filterTypes = filterTypes;

        JPanel filterPanel = new JPanel();

        final JComboBox filter = new JComboBox(filterTypes);
        filter.setSelectedIndex(selected);
        filter.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                filterChanged(filter.getSelectedIndex());
            }
        });

        filterPanel.add(filter);
        filterPanel.setBackground(Color.white);
        titlePanel.add(filterPanel, BorderLayout.SOUTH);
    }

    public void filterChanged(int index){
        //no-op, override, if the subclass wants a callback on filterchange
    }

    class ReportModel extends AbstractTableModel {

        String[] columns;
        Object[][] data;

        public ReportModel(String[] columns, Object[][] data) {
            this.columns = columns;
            this.data = data;
            fireTableStructureChanged();
        }

        public String getColumnName(int column) {
            return columns[column];
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }

        public int getRowCount() {
            return data.length;
        }

        public int getColumnCount() {
            return columns.length;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            return data[rowIndex][columnIndex];
        }

    }
}
