/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tab.layout;

import java.awt.Component;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import tab.Section;
import static ui.FormManaqer.*;

/**
 *
 * @author Daniel
 */
public class LayoutSection implements Section {

    private LayoutTableModel layoutTableModel;

    private JTable jTable;

    private Component baseComponent;
    
    public static final String[] layoutTypeList = new String[]{"forecastTable", "miniStory", "button", "import", "custom"};
    public static final int FORECASTTABLE = 0;
    public static final int MINISTORY = 1;
    public static final int BUTTON = 2;
    public static final int IMPORT = 3;
    public static final int CUSTOM = 4;
    
    public int whoAmI() {
        return LAYOUTSECTION;
    }

    public LayoutSection() {
        this.layoutTableModel = new LayoutTableModel();
        this.jTable = new JTable(getLayoutTableModel());
        this.baseComponent = new JScrollPane(jTable);
        
        
        //jTable.setDragEnabled(true);
        jTable.getColumnModel().getColumn(0).setCellRenderer(new StatusColumnCellRenderer());
        jTable.getTableHeader().setReorderingAllowed(true);
        jTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTableMousePressed(evt);
            }
        });
        TableColumn colToHide = jTable.getColumnModel().getColumn(4);
        jTable.removeColumn(colToHide);   
        
        jTable.validate();
    }

    public void jTableMousePressed(java.awt.event.MouseEvent evt) {
        if (jTable.getSelectedRow() >= 0){
            LayoutTableItem item = getLayoutTableModel().getItemAt(jTable.getSelectedRow());
            ui.FormManaqer.setEnabledLayoutForm(true);
            ui.FormManaqer.layoutFormUpdate(item);
        }
//        ForcastUi.enableForm();
//        ForcastUi.tableRowSelected(jTable, tableManager);
//        updateViewCSVFile();
    }

    public void selectRow(int i) {
        jTable.setRowSelectionInterval(i, i);
    }

    public Component getBaseComponent() {
        return baseComponent;
    }

    public LayoutTableModel getLayoutTableModel() {
        return layoutTableModel;
    }

    public JTable getJTable() {
        return jTable;
    }
}
