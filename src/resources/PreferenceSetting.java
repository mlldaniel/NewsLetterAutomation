/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author Daniel
 */
public class PreferenceSetting{
    private String forecastDBDir;
    private String languageName;
    private String saveDir;
    
    private SimpleDateFormat dateFormat;
    public PreferenceSetting(String languageName){
        this.languageName = languageName;
        this.saveDir= "output/"+languageName+"/";
    }

    public String getLanguageName() {
        return languageName;
    }

    /**
     * @return the forecastDBDir
     */
    public String getForecastDBDir() {
        return forecastDBDir;
    }

    /**
     * @param forecastDBDir the forecastDBDir to set
     */
    public void setForecastDBDir(String forecastDBDir) {
        this.forecastDBDir = forecastDBDir;
    }

    /**
     * @return the saveDir
     */
    public String getSaveDir() {
        return saveDir;
    }
    
        /**
     * @return the dateFormat
     */
    public String convertDateToNative(Date date) {
        
        if(date == null)
            return dateFormat.format(new Date());
        else
            return dateFormat.format(date);
    }

    /**
     * @param dateFormat the dateFormat to set
     * @param localeCode
     */
    public void setDateFormat(String dateFormat, String localeCode) {
        this.dateFormat = new SimpleDateFormat(dateFormat,new Locale(localeCode));
    }
}
