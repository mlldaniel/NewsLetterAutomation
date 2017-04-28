/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author danielhwang
 */
public class UrlTagger {
    private static String tagName = "?utm_source=newsletter";
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMMMyyyy", new Locale("en_US"));
    public static String tag(Date date, String type, Integer pos){
        String dateStr ="";
        if(date == null)
            dateStr = dateFormat.format(new Date());
        else
            dateStr = dateFormat.format(date);
        
        return tagName +"-"+ dateStr +"-"+ type + pos;
    }
    
    public static String tag(Date date, String type){
        String dateStr ="";
        if(date == null)
            dateStr = dateFormat.format(new Date());
        else
            dateStr = dateFormat.format(date);
        
        return tagName +"-"+ dateStr +"-"+ type;
    }
    
    
}
