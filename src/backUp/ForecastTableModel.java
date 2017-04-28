/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backUp;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import static backUp.ForecastTableItem.*;

/**
 *
 * @author Daniel
 */
public class ForecastTableModel extends AbstractTableModel{
    private List<ForecastTableItem> forecastTableItemList;
    private String[] columnNames = { "position","title","link","positionType","packageName",
        "subpackageName","csvFilePath","timeFrame","forecastDate","targetDate","topReturn1",
        "topStockName1","topReturn2","topStockName2","topReturn3","topStockName3",
        "avgReturn1","avgReturn2","snp500Return"," marketPremium1"," marketPremium2",
        " accuracy"," totalNumber"};
    
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
            case TOPRETURN1:
                return value.getTopReturn1();
            case TOPSTOCKNAME1:
                return value.getTopStockName1();
            case TOPRETURN2:
                return value.getTopReturn2();
            case TOPSTOCKNAME2:
                return value.getTopStockName2();
            case TOPRETURN3:
                return value.getTopReturn3();
            case TOPSTOCKNAME3:
                return value.getTopStockName3();
            case AVGRETURN1:
                return value.getAvgReturn1();
            case AVGRETURN2:
                return value.getAvgReturn2();
            case SNP500RETURN:
                return value.getSnp500Return();
            case MARKETPREMIUM1:
                return value.getMarketPremium1();
            case MARKETPREMIUM2:
                return value.getMarketPremium2();
            case ACCURACY:
                return value.getAccuracy();
            case TOTALNUMBER:
                return value.getTotalNumber();
            
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
            case TOPRETURN1:
                return Double.class;
            case TOPSTOCKNAME1:
                return String.class;
            case TOPRETURN2:
                return Double.class;
            case TOPSTOCKNAME2:
                return String.class;
            case TOPRETURN3:
                return Double.class;
            case TOPSTOCKNAME3:
                return String.class;
            case AVGRETURN1:
                return Double.class;
            case AVGRETURN2:
                return Double.class;
            case SNP500RETURN:
                return Double.class;
            case MARKETPREMIUM1:
                return Double.class;
            case MARKETPREMIUM2:
                return Double.class;
            case ACCURACY:
                return Integer.class;
            case TOTALNUMBER:
                return Integer.class;
            
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
}
