package tab.forecast;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import tab.LinkMap;
import tab.Section;
import static ui.FormManaqer.FORECASTSECTION;

public class ForecastSection implements Section {

    private ForecastTableModel forecastTableModel;
    private JTable jTable;
    private Component baseComponent;

    //Data for Newsletter Generation_
    private List<LinkMap> forecastLinkMapList;
    private Map<String, List<ForecastTableItem>> miniStoryDataObjMap;

    public int whoAmI() {
        return FORECASTSECTION;
    }

    public ForecastSection() {

        forecastTableModel = new ForecastTableModel();
        jTable = new JTable(getForecastTableModel());
        baseComponent = new JScrollPane(jTable);

    }

    //Generating Newsletter Related Functions
    //1 - Create Link Map
    //2 - ministory Data Obj List
    public void prepareDataForNewsletterGeneration() {
        System.out.print("Generating ForecastResource and miniStoryDataObjArray");

        List<ForecastTableItem> ftItemList = this.getForecastTableModel().getForecastTableItemList();

        //Create List of LinkMap
        forecastLinkMapList = ftItemList
                .stream()
                .map(a -> LinkMap.forecastDataToLinkMap(a.getTitle(), a.getLink(), ":"))
                .collect(Collectors.toList());

        //miniStoryDataObjMap = new HashMap();
        miniStoryDataObjMap = new TreeMap<String, List<ForecastTableItem>>(String.CASE_INSENSITIVE_ORDER);
        //Prepare Mnistory Data Object List
        for (ForecastTableItem item : ftItemList) {
            String timeFrame = item.getTimeFrame();
            if (miniStoryDataObjMap.containsKey(timeFrame)) {//If Does contains This Timeframe
                List<ForecastTableItem> thisItemList = miniStoryDataObjMap.get(timeFrame);
                thisItemList.add(item);
            } else {//if first time
                List<ForecastTableItem> thisItemList = new ArrayList();
                thisItemList.add(item);
                miniStoryDataObjMap.put(timeFrame, thisItemList);
            }
        }

        //Sort according To I Know First Avg
        for (Map.Entry<String, List<ForecastTableItem>> entry : miniStoryDataObjMap.entrySet()) {
            List<ForecastTableItem> itemList = entry.getValue();
            //Sort Csv Table of a item of ItemList
            itemList.stream().forEach(item -> ForecastTableItem.sortCsvTable(item.getCsvTableList()));

            //Sort Between ForecastTableItem using I Know First Avg
            itemList = itemList.stream()
                    .sorted((i1, i2) -> Double.compare(i2.highestINFAverage(),i1.highestINFAverage()))
                    .collect(Collectors.toList());
            
            entry.setValue(itemList);
            //itemList.stream().sorted()
            if (itemList.size() > 1) {
                System.out.println(itemList.get(0).getTimeFrame());
                for (ForecastTableItem item : itemList) {
                    System.out.println(item.getTitle());
                    System.out.println(item.stockRankOf(0).getRow().getReturnValue());
                }
            }
        }

        System.out.println(" - Success!");
    }

    public ForecastTableItem getCurrentRow() {
        int rowNumber = jTable.getSelectedRow();
        if (rowNumber > -1 && rowNumber < getForecastTableModel().getRowCount()) {
            return getForecastTableModel().getItemAt(rowNumber);
        } else {
            return null;
        }
    }

    public Component getBaseComponent() {
        return baseComponent;
    }

    /**
     * @return the forecastTableModel
     */
    public ForecastTableModel getForecastTableModel() {
        return forecastTableModel;
    }

    /**
     * @return the forecastLinkMapList
     */
    public List<LinkMap> getForecastLinkMapList() {
        return forecastLinkMapList;
    }

    /**
     * @return the miniStoryDataObjMap
     */
    public Map<String, List<ForecastTableItem>> getMiniStoryDataObjMap() {
        return miniStoryDataObjMap;
    }
}
