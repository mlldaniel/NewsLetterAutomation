/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tab.layout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import resources.PreferenceSettings;
import static tab.layout.LayoutTableItem.*;

/**
 *
 * @author Daniel
 */
public class LayoutTableModel extends AbstractTableModel {

    private List<LayoutTableItem> layoutTableItemList;
    private String[] columnNames = {"position", "type", "title", "parameters needed", "preview"};
    static int NOTGENERATED = 0;
    static int GENERATED = 1;
    public LayoutTableModel(List<LayoutTableItem> itemList) {
        layoutTableItemList = new ArrayList<LayoutTableItem>(itemList);
    }

    public LayoutTableModel() {
        layoutTableItemList = new ArrayList();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public int getRowCount() {
        return getLayoutTableItemList().size();
    }

    @Override
    public int getColumnCount() {
        return LayoutTableItem.colCount;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        LayoutTableItem value = getLayoutTableItemList().get(rowIndex);
        switch (columnIndex) {
            case POSITION:
                return value.getPosition();
            case TYPE:
                return value.getType();
            case TITLE:
                return value.getTitle();
            case PARAMETERNEEDED:
                return value.getParametersNeeded();
            case PREVIEW:
                return value.getPreview();
            default:
                return null;

        }

    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case POSITION:
                return Integer.class;
            case TYPE:
                return String.class;
            case TITLE:
                //return String.class;
                return Document.class;
            case PARAMETERNEEDED:
                return String.class;
            case PREVIEW:
                //return String.class;
                return Document.class;
            default:
                return null;

        }
    }

    /**
     * This will return the user at the specified row...
     *
     * @param row
     * @return
     */
    public LayoutTableItem getItemAt(int row) {
        return getLayoutTableItemList().get(row);
    }

    public LayoutTableItem addRow(LayoutTableItem item) {
        //Get the default Parameters
        String param = PreferenceSettings.getTypeParameterValue(item.getType());
        item.setParametersNeeded(param);

        int posNumber = getLayoutTableItemList().size();
        item.setPosition(posNumber);

        getLayoutTableItemList().add(item);

        this.fireTableDataChanged();
        return item;
    }

    public void deleteRow(int rowNumber) {
        getLayoutTableItemList().remove(rowNumber);
        this.fireTableDataChanged();
    }

    public int updateRow(int rowNumber, LayoutTableItem inputItem) {
        //change Status to false
        getLayoutTableItemList().get(rowNumber).setReady(false);
        
        if (rowNumber != inputItem.getPosition()) {// If Position Changed
            int fromRow = rowNumber;
            int toRow = inputItem.getPosition();

            if (toRow > -1 && toRow < getRowCount()) {
                layoutTableItemList.get(fromRow).setPosition(toRow);
                layoutTableItemList.get(toRow).setPosition(fromRow);

                Collections.swap(getLayoutTableItemList(), fromRow, toRow);
                this.fireTableDataChanged();
                return toRow;
            }
        } else { // other except Position Changed
            LayoutTableItem item = getLayoutTableItemList().get(rowNumber);
            item.setPosition(inputItem.getPosition());
            //item.setType(inputItem.getType());
            //item.setTitle(item.getTitle());
            item.setParametersNeeded(inputItem.getParametersNeeded());
            //item.setPreview(inputItem.getPreviewDoc());

            this.fireTableCellUpdated(rowNumber, POSITION);
            //this.fireTableCellUpdated(rowNumber, TYPE);
            //this.fireTableCellUpdated(rowNumber, TITLE);
            this.fireTableCellUpdated(rowNumber, PARAMETERNEEDED);
            //this.fireTableCellUpdated(rowNumber, PREVIEW);
        }
        return -1;
    }

    //Used in Change row location
//    public void changeRow(int fromRow, int toRow){
//        LayoutTableItem fromItem = layoutTableItemList.get(fromRow);
//        LayoutTableItem toItem = layoutTableItemList.get(toRow);
//        
//        LayoutTableItem temp = toItem;
//        
//        layoutTableItemList.set(toRow, fromItem);
//        layoutTableItemList.set(fromRow, temp);
//        
//        this.fireTableDataChanged();
//    }
    /**
     * @return the layoutTableItemList
     */
    public List<LayoutTableItem> getLayoutTableItemList() {
        return layoutTableItemList;
    }

    /**
     * @param layoutTableItemList the layoutTableItemList to set
     */
    public void setLayoutTableItemList(List<LayoutTableItem> layoutTableItemList) {
        this.layoutTableItemList = layoutTableItemList;
        this.fireTableDataChanged();
    }
    
    public int getStatus(int row){
        //Document doc = Jsoup.parse(layoutTableItemList.get(row).getPreview());
        
        if(layoutTableItemList.get(row).isReady())
            return GENERATED;
        else
            return NOTGENERATED;
    }
    
    

}
