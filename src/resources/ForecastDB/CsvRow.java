/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources.ForecastDB;

/**
 *
 * @author Daniel
 */
public class CsvRow {
    private int id;
    private String symbol;
    private Integer prediction;
    private Double returnValue;
    private Integer accuracy;
    private int csvTableId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Integer getPrediction() {
        return prediction;
    }

    public void setPrediction(Integer prediction) {
        this.prediction = prediction;
    }

    public Double getReturnValue() {
        return returnValue;
    }
    
    public String getReturnValueStr() {
        return String.format("%.2f", returnValue);
    }

    public void setReturnValue(Double returnValue) {
        this.returnValue = returnValue;
    }

    public Integer getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Integer accuracy) {
        this.accuracy = accuracy;
    }

    public int getCsvTableId() {
        return csvTableId;
    }

    public void setCsvTableId(int csvTableId) {
        this.csvTableId = csvTableId;
    }
    
}
