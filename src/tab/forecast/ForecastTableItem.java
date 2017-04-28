package tab.forecast;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import resources.ForecastDB.CsvRow;
import resources.ForecastDB.CsvRowPair;
import resources.ForecastDB.CsvTable;
import tab.Item;

public class ForecastTableItem implements Item {

    public static final int colCount = 10;

    private Integer position;

    private String title;
    private String link;

    private String positionType;
    private String packageName;
    private String subpackageName;
    private String csvFilePath;
    private String timeFrame;
    private String forecastDate;
    private String targetDate;

    private int id;

    private List<CsvTable> csvTableList;

    public static final int POSITION = 0;

    public static final int TITLE = 1;
    public static final int LINK = 2;

    public static final int POSITIONTYPE = 3;
    public static final int PACKAGENAME = 4;
    public static final int SUBPACKAGENAME = 5;
    public static final int CSVFILEPATH = 6;
    public static final int TIMEFRAME = 7;
    public static final int FORECASTDATE = 8;
    public static final int TARGETDATE = 9;

    /*
    private String fileName;
    private String title;
    private String link;
    private String timeFrame;
     */
    public ForecastTableItem() {

    }

    public ForecastTableItem(String link, String csvFilePath) {
        this.position = -1;
        this.title = "";
        this.link = link;
        this.positionType = "";
        this.packageName = "";
        this.subpackageName = "";
        this.csvFilePath = csvFilePath;
        this.timeFrame = "";
        this.forecastDate = "";
        this.targetDate = "";
    }

    public ForecastTableItem(Integer position, String title, String link, String positionType, String packageName, String subpackageName, String csvFilePath, String timeFrame, String forecastDate, String targetDate, Double topReturn1, String topStockName1, Double topReturn2, String topStockName2, Double topReturn3, String topStockName3, Double avgReturn1, Double avgReturn2, Double snp500Return, Double marketPremium1, Double marketPremium2, Integer accuracy, Integer totalNumber) {
        this.position = position;
        this.title = title;
        this.link = link;
        this.positionType = positionType;
        this.packageName = packageName;
        this.subpackageName = subpackageName;
        this.csvFilePath = csvFilePath;
        this.timeFrame = timeFrame;
        this.forecastDate = forecastDate;
        this.targetDate = targetDate;
    }

    public ForecastTableItem(Integer position, String title, String link, String positionType, String packageName, String subpackageName, String csvFilePath, String timeFrame, String forecastDate, String targetDate, String topReturn1, String topStockName1, String topReturn2, String topStockName2, String topReturn3, String topStockName3, String avgReturn1, String avgReturn2, String snp500Return, String marketPremium1, String marketPremium2, String accuracy, String totalNumber) {
        this.position = position;
        this.title = title;
        this.link = link;
        this.positionType = positionType;
        this.packageName = packageName;
        this.subpackageName = subpackageName;
        this.csvFilePath = csvFilePath;
        this.timeFrame = timeFrame;
        this.forecastDate = forecastDate;
        this.targetDate = targetDate;
    }

    private static Double toDouble(String inputString) {
        return inputString.length() != 0 ? Double.parseDouble(inputString) : 0.0;
    }

    private static Integer toInteger(String inputString) {
        return inputString.length() != 0 ? Integer.parseInt(inputString) : 0;
    }

    public static CsvRowPair stockReturnRankOf(List<CsvTable> csvTableList, int th) {
        List<CsvRowPair> csvRowPairList = new ArrayList();

        for (CsvTable table : csvTableList) {
            List<CsvRow> csvRowList = table.getCsvRowList().stream()
                    .filter(item -> item.getAccuracy() == 1)
                    .collect(Collectors.toList());

            csvRowList.stream()
                    .forEach(item -> csvRowPairList.add(new CsvRowPair(table.getPositionNumber(), item)));
        }
        if (csvRowPairList.size() > 0 && csvRowPairList.size() > th && th > -1) {
            return csvRowPairList.get(th);
        } else {
            return csvRowPairList.get(csvRowPairList.size() - 1);
        }
    }

    public String stockReturnStringRankOf(int th) {
        List<CsvRowPair> csvRowPairList = new ArrayList();

        for (CsvTable table : csvTableList) {
            List<CsvRow> csvRowList = table.getCsvRowList().stream()
                    .filter(item -> item.getAccuracy() == 1)
                    .collect(Collectors.toList());

            csvRowList.stream()
                    .forEach(item -> csvRowPairList.add(new CsvRowPair(table.getPositionNumber(), item)));
        }
        if (csvRowPairList.size() > 0 && csvRowPairList.size() > th && th > -1) {
            return Double.toString(((CsvRow) csvRowPairList.get(th).getRow()).getReturnValue());
        } else {
            return Double.toString(((CsvRow) csvRowPairList.get(csvRowPairList.size() - 1).getRow()).getReturnValue());
        }
    }
    public String getSNPReturn(){
        return csvTableList.get(0).getSnp500Return();
    }
    public String getAvgReturnTabNumb(int i) {
        if (csvTableList.size() <= i) {
            return "";
        } else {
            return csvTableList.get(i).getiKnowFirstAvgReturn();
        }
    }

    public String getPremiumTabNumb(int i) {
        if (csvTableList.size() <= i) {
            return "";
        } else {
            Double avg = Double.parseDouble(csvTableList.get(i).getiKnowFirstAvgReturn());
            Double snp = Double.parseDouble(csvTableList.get(i).getSnp500Return());
            return String.format("%.2f", avg - snp);
        }
    }

    public String getTotalAccuracyNumb() {
        int count = 0;
        for (CsvTable table : csvTableList) {
            count += table.getCsvRowList().stream().filter(i -> i.getAccuracy() == 1).count();
        }
        return String.valueOf(count);
    }

    public String getTotalNumb() {
        int count = 0;
        for (CsvTable table : csvTableList) {
            count += table.getCsvRowList().size();
        }
        return String.valueOf(count);
    }
    public Double highestINFAverage(){
        int tableSize = csvTableList.size();
        if(tableSize==2){//Long & Short
            double l = parseDoubleAllLang(csvTableList.get(0).getiKnowFirstAvgReturn());
            double s = parseDoubleAllLang(csvTableList.get(1).getiKnowFirstAvgReturn());
            return l>=s ? l : s;
        }else if(tableSize==1){
            return parseDoubleAllLang(csvTableList.get(0).getiKnowFirstAvgReturn());
        }else{
            System.out.println("Error! There is no CSV table for this Forecast ...");
            return 0.0;
        }
    }
    private static double parseDoubleAllLang(String numbStr){
        double returnNumb=0.0;
        try{
            if(numbStr.contains(",")){
                numbStr = numbStr.replace(",", ".");
            }
            returnNumb = Double.parseDouble(numbStr);
        }catch(Exception e){
            System.out.println("This Number can't be read: " + numbStr);
        }finally{
            return returnNumb;
        }
        
    }
    public CsvRowPair stockRankOf(int th) {
        List<CsvRowPair> csvRowPairList = new ArrayList();
        List<CsvRowPair> csvRowPairSortedList = new ArrayList();
        for (CsvTable table : csvTableList) {
            List<CsvRow> csvRowList = table.getCsvRowList().stream()
                    .filter(item -> item.getAccuracy() == 1)
                    .collect(Collectors.toList());

            csvRowList.stream()
                    .forEach(item -> csvRowPairList.add(new CsvRowPair(table.getPositionNumber(), item)));
        }
        csvRowPairSortedList = csvRowPairList.stream()
                .sorted((i1, i2) -> Double.compare(i2.getRow().getReturnValue(),i1.getRow().getReturnValue()))
                .collect(Collectors.toList());
        /*
                .sorted((i1, i2) -> Double.compare(i2.stockRankOf(0).getRow().getReturnValue(),i1.stockRankOf(0).getRow().getReturnValue()))
                    .collect(Collectors.toList());
                */
        
        if (csvRowPairList.size() > 0 && csvRowPairList.size() > th && th > -1) {
            return csvRowPairSortedList.get(th);
        } else {
            return csvRowPairSortedList.get(csvRowPairList.size() - 1);
        }
    }

    public static void sortCsvTable(List<CsvTable> csvTableList) {
        csvTableList.stream().forEach(table -> CsvTable.sortCsvRow(table.getCsvRowList()));
        System.out.println("====================");
        for (CsvTable table : csvTableList) {
            table.getCsvRowList().forEach(item -> System.out.println(item.getSymbol() + " - " + item.getReturnValue()));
        }
    }

    @Override
    public String toString() {
        return getTitle();
    }

    /**
     * @return the position
     */
    public int getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the link
     */
    public String getLink() {
        return link;
    }

    /**
     * @param link the link to set
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * @return the positionType
     */
    public String getPositionType() {
        return positionType;
    }

    /**
     * @param positionType the positionType to set
     */
    public void setPositionType(String positionType) {
        this.positionType = positionType;
    }

    /**
     * @return the packageName
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * @param packageName the packageName to set
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * @return the subpackageName
     */
    public String getSubpackageName() {
        return subpackageName;
    }

    /**
     * @param subpackageName the subpackageName to set
     */
    public void setSubpackageName(String subpackageName) {
        this.subpackageName = subpackageName;
    }

    /**
     * @return the csvFilePath
     */
    public String getCsvFilePath() {
        return csvFilePath;
    }

    /**
     * @param csvFilePath the csvFilePath to set
     */
    public void setCsvFilePath(String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }

    /**
     * @return the timeFrame
     */
    public String getTimeFrame() {
        return timeFrame;
    }

    /**
     * @param timeFrame the timeFrame to set
     */
    public void setTimeFrame(String timeFrame) {
        this.timeFrame = timeFrame;
    }

    /**
     * @return the forecastDate
     */
    public String getForecastDate() {
        return forecastDate;
    }

    /**
     * @param forecastDate the forecastDate to set
     */
    public void setForecastDate(String forecastDate) {
        this.forecastDate = forecastDate;
    }

    /**
     * @return the targetDate
     */
    public String getTargetDate() {
        return targetDate;
    }

    /**
     * @param targetDate the targetDate to set
     */
    public void setTargetDate(String targetDate) {
        this.targetDate = targetDate;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    public List<CsvTable> getCsvTableList() {
        return csvTableList;
    }

    public void setCsvTableList(List<CsvTable> csvTableList) {
        this.csvTableList = csvTableList;
    }

}
