package backUp;

import tab.Item;

public class ForecastTableItem implements Item {
    public static  final int colCount = 19;
    
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
    private Double topReturn1;
    private String topStockName1;
    private Double topReturn2;
    private String topStockName2;
    private Double topReturn3;
    private String topStockName3;
    private Double avgReturn1;
    private Double avgReturn2;
    private Double snp500Return;
    private Double marketPremium1;
    private Double marketPremium2;
    private Integer accuracy;
    private Integer totalNumber;
    

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
    public static final int TOPRETURN1 = 10;
    public static final int TOPSTOCKNAME1 = 11;
    public static final int TOPRETURN2 = 12;
    public static final int TOPSTOCKNAME2 = 13;
    public static final int TOPRETURN3 = 14;
    public static final int TOPSTOCKNAME3 = 15;
    public static final int AVGRETURN1 = 16;
    public static final int AVGRETURN2 = 17;
    public static final int SNP500RETURN = 18;
    public static final int MARKETPREMIUM1 = 19;
    public static final int MARKETPREMIUM2 = 20;
    public static final int ACCURACY = 21;
    public static final int TOTALNUMBER = 22;
    /*
    private String fileName;
    private String title;
    private String link;
    private String timeFrame;
    */
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
        this.topReturn1 = 0.0;
        this.topStockName1 = "";
        this.topReturn2 = 0.0;
        this.topStockName2 = "";
        this.topReturn3 = 0.0;
        this.topStockName3 = "";
        this.avgReturn1 = 0.0;
        this.avgReturn2 = 0.0;
        this.snp500Return = 0.0;
        this.marketPremium1 = 0.0;
        this.marketPremium2 = 0.0;
        this.accuracy = 0;
        this.totalNumber = 0;
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
        this.topReturn1 = topReturn1;
        this.topStockName1 = topStockName1;
        this.topReturn2 = topReturn2;
        this.topStockName2 = topStockName2;
        this.topReturn3 = topReturn3;
        this.topStockName3 = topStockName3;
        this.avgReturn1 = avgReturn1;
        this.avgReturn2 = avgReturn2;
        this.snp500Return = snp500Return;
        this.marketPremium1 = marketPremium1;
        this.marketPremium2 = marketPremium2;
        this.accuracy = accuracy;
        this.totalNumber = totalNumber;
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
        this.topReturn1 = toDouble(topReturn1);
        this.topStockName1 = topStockName1;
        this.topReturn2 = toDouble(topReturn2);
        this.topStockName2 = topStockName2;
        this.topReturn3 = toDouble(topReturn3);
        this.topStockName3 = topStockName3;
        this.avgReturn1 = toDouble(avgReturn1);
        this.avgReturn2 = toDouble(avgReturn2);
        this.snp500Return = toDouble(snp500Return);
        this.marketPremium1 = toDouble(marketPremium1);
        this.marketPremium2 = toDouble(marketPremium2);
        this.accuracy = toInteger(accuracy);
        this.totalNumber =toInteger(totalNumber);
    }
    private static Double toDouble(String inputString){
        return inputString.length()!=0 ? Double.parseDouble(inputString) : 0.0;
    }
    private static Integer toInteger(String inputString){
        return inputString.length()!=0 ? Integer.parseInt(inputString) : 0;
    }

    @Override
    public String toString(){
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
     * @return the topReturn1
     */
    public Double getTopReturn1() {
        return topReturn1;
    }

    /**
     * @param topReturn1 the topReturn1 to set
     */
    public void setTopReturn1(Double topReturn1) {
        this.topReturn1 = topReturn1;
    }

    /**
     * @return the topStockName1
     */
    public String getTopStockName1() {
        return topStockName1;
    }

    /**
     * @param topStockName1 the topStockName1 to set
     */
    public void setTopStockName1(String topStockName1) {
        this.topStockName1 = topStockName1;
    }

    /**
     * @return the avgReturn1
     */
    public Double getAvgReturn1() {
        return avgReturn1;
    }

    /**
     * @param avgReturn1 the avgReturn1 to set
     */
    public void setAvgReturn1(Double avgReturn1) {
        this.avgReturn1 = avgReturn1;
    }

    /**
     * @return the avgReturn2
     */
    public Double getAvgReturn2() {
        return avgReturn2;
    }

    /**
     * @param avgReturn2 the avgReturn2 to set
     */
    public void setAvgReturn2(Double avgReturn2) {
        this.avgReturn2 = avgReturn2;
    }

    /**
     * @return the snp500Return
     */
    public Double getSnp500Return() {
        return snp500Return;
    }

    /**
     * @param snp500Return the snp500Return to set
     */
    public void setSnp500Return(Double snp500Return) {
        this.snp500Return = snp500Return;
    }

    /**
     * @return the marketPremium1
     */
    public Double getMarketPremium1() {
        return marketPremium1;
    }

    /**
     * @param marketPremium1 the marketPremium1 to set
     */
    public void setMarketPremium1(Double marketPremium1) {
        this.marketPremium1 = marketPremium1;
    }

    /**
     * @return the marketPremium2
     */
    public Double getMarketPremium2() {
        return marketPremium2;
    }

    /**
     * @param marketPremium2 the marketPremium2 to set
     */
    public void setMarketPremium2(Double marketPremium2) {
        this.marketPremium2 = marketPremium2;
    }

    /**
     * @return the accuracy
     */
    public Integer getAccuracy() {
        return accuracy;
    }

    /**
     * @param accuracy the accuracy to set
     */
    public void setAccuracy(Integer accuracy) {
        this.accuracy = accuracy;
    }

    /**
     * @return the totalNumber
     */
    public Integer getTotalNumber() {
        return totalNumber;
    }

    /**
     * @param totalNumber the totalNumber to set
     */
    public void setTotalNumber(Integer totalNumber) {
        this.totalNumber = totalNumber;
    }

    /**
     * @return the topReturn2
     */
    public Double getTopReturn2() {
        return topReturn2;
    }

    /**
     * @param topReturn2 the topReturn2 to set
     */
    public void setTopReturn2(Double topReturn2) {
        this.topReturn2 = topReturn2;
    }

    /**
     * @return the topStockName2
     */
    public String getTopStockName2() {
        return topStockName2;
    }

    /**
     * @param topStockName2 the topStockName2 to set
     */
    public void setTopStockName2(String topStockName2) {
        this.topStockName2 = topStockName2;
    }

    /**
     * @return the topReturn3
     */
    public Double getTopReturn3() {
        return topReturn3;
    }

    /**
     * @param topReturn3 the topReturn3 to set
     */
    public void setTopReturn3(Double topReturn3) {
        this.topReturn3 = topReturn3;
    }

    /**
     * @return the topStockName3
     */
    public String getTopStockName3() {
        return topStockName3;
    }

    /**
     * @param topStockName3 the topStockName3 to set
     */
    public void setTopStockName3(String topStockName3) {
        this.topStockName3 = topStockName3;
    }
}
