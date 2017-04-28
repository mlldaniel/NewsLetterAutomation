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
public class CsvRowPair{//Position And row
    private Integer positionType;
    private CsvRow row;

    public CsvRowPair(Integer positionType, CsvRow row) {
        this.positionType = positionType;
        this.row = row;
    }

    /**
     * @return the positionType
     */
    public Integer getPositionType() {
        return positionType;
    }
    /**
     * @return the row
     */
    public CsvRow getRow() {
        return row;
    }

    
    
}
