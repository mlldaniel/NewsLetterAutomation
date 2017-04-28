/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources.ForecastDB;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Daniel
 */
public class CsvTable {
    private int id;
    private int positionNumber;
    private String forecastDate;
    private String targetDate;
    private String snp500Return;
    private String iKnowFirstAvgReturn;
    private String mainTableId;
    
    private List<CsvRow> csvRowList;
    
    public Double stockReturnRankOf(int rank){
        Double topStock;
        csvRowList = csvRowList.stream().filter(i -> i.getAccuracy() ==1 )
            .sorted((r1,r2)->Double.compare(r1.getReturnValue(), r2.getReturnValue()))
            .collect(Collectors.toList());
        return csvRowList.get(rank).getReturnValue();
    }
    public static void sortCsvRow(List<CsvRow> list){
        list.stream().sorted((r1,r2) -> Double.compare(r1.getReturnValue(), r2.getReturnValue()));
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPositionNumber() {
        return positionNumber;
    }

    public void setPositionNumber(int positionNumber) {
        this.positionNumber = positionNumber;
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

    public String getSnp500Return() {
        return snp500Return;
    }

    public void setSnp500Return(String snp500Return) {
        this.snp500Return = snp500Return;
    }

    public String getMainTableId() {
        return mainTableId;
    }

    public void setMainTableId(String mainTableId) {
        this.mainTableId = mainTableId;
    }

    public List<CsvRow> getCsvRowList() {
        return csvRowList;
    }

    public void setCsvRowList(List<CsvRow> csvRowList) {
        this.csvRowList = csvRowList;
    }

    public String getiKnowFirstAvgReturn() {
        return iKnowFirstAvgReturn;
    }
    
    public void setiKnowFirstAvgReturn(String iKnowFirstAvgReturn) {
        this.iKnowFirstAvgReturn = iKnowFirstAvgReturn;
    }
}
