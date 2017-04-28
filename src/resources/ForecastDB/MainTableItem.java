/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources.ForecastDB;

import java.util.List;

/**
 *
 * @author Daniel
 */
public class MainTableItem {
    private int id;
    
    private String title;
    private String link;
    
    private String positionType;
    private String packageName;
    private String subpackageName;
    private String csvFilePath;
    private String timeFrame;
    private String forecastDate;
    private String targetDate;
    
    private List<CsvTable> csvTableList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPositionType() {
        return positionType;
    }

    public void setPositionType(String positionType) {
        this.positionType = positionType;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getSubpackageName() {
        return subpackageName;
    }

    public void setSubpackageName(String subpackageName) {
        this.subpackageName = subpackageName;
    }

    public String getTimeFrame() {
        return timeFrame;
    }

    public void setTimeFrame(String timeFrame) {
        this.timeFrame = timeFrame;
    }

    public String getForecastDate() {
        return forecastDate;
    }

    public void setForecastDate(String forecastDate) {
        this.forecastDate = forecastDate;
    }

    public String getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(String targetDate) {
        this.targetDate = targetDate;
    }

    public List<CsvTable> getCsvTableList() {
        return csvTableList;
    }

    public void setCsvTableList(List<CsvTable> csvTableList) {
        this.csvTableList = csvTableList;
    }

    public String getCsvFilePath() {
        return csvFilePath;
    }

    public void setCsvFilePath(String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }
}
