/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tab.forecast;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import static tab.forecast.ForecastTableItem.*;

/**
 *
 * @author Daniel
 */
public class ForecastTableModel extends AbstractTableModel{
    private List<ForecastTableItem> forecastTableItemList;
    private String[] columnNames = { "position","title","link","positionType","packageName",
        "subpackageName","csvFilePath","timeFrame","forecastDate","targetDate"};
    
    public ForecastTableModel(List<ForecastTableItem> itemList){
        forecastTableItemList = new ArrayList<ForecastTableItem>(itemList);
    }
    public ForecastTableModel(){
        forecastTableItemList = new ArrayList();
    }
    @Override
    public String getColumnName(int columnIndex){
         return columnNames[columnIndex];
    }
    
    @Override
    public int getRowCount() {
        return getForecastTableItemList().size();
    }

    @Override
    public int getColumnCount() {
        return ForecastTableItem.colCount;
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        ForecastTableItem value = getForecastTableItemList().get(rowIndex);
        switch (columnIndex) {
            case POSITION:
                return value.getPosition();
            case TITLE:
                return value.getTitle();
            case LINK:
                return value.getLink();
            case POSITIONTYPE:
                return value.getPositionType();
            case PACKAGENAME:
                return value.getPackageName();
            case SUBPACKAGENAME:
                return value.getSubpackageName();
            case CSVFILEPATH:
                return value.getCsvFilePath();
            case TIMEFRAME:
                return value.getTimeFrame();
            case FORECASTDATE:
                return value.getForecastDate();
            case TARGETDATE:
                return value.getTargetDate();
            
            default:
                return null;
                
        }


    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case POSITION:
                return Integer.class;
            case TITLE:
                return String.class;
            case LINK:
                return String.class;
            case POSITIONTYPE:
                return String.class;
            case PACKAGENAME:
                return String.class;
            case SUBPACKAGENAME:
                return String.class;
            case CSVFILEPATH:
                return String.class;
            case TIMEFRAME:
                return String.class;
            case FORECASTDATE:
                return String.class;
            case TARGETDATE:
                return String.class;
            
            default:
                return null;
                
        } 
    }

    

    /**
     * This will return the user at the specified row...
     * @param row
     * @return 
     */
    public ForecastTableItem getItemAt(int row) {
        return getForecastTableItemList().get(row);
    }
    
    public ForecastTableItem addRow(ForecastTableItem item){
        int posNumber = getForecastTableItemList().size();
        item.setPosition(posNumber);
        
        getForecastTableItemList().add(item);
        
        this.fireTableDataChanged();
        return item;
    }

    /**
     * @return the forecastTableItemList
     */
    public List<ForecastTableItem> getForecastTableItemList() {
        return forecastTableItemList;
    }
    
    public void clearRows(){
        getForecastTableItemList().clear();
        this.fireTableDataChanged();
    }
}
