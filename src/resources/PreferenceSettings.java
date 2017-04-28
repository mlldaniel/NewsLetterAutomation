/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.configuration2.*;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import tab.Tab;

/**
 *
 * @author Daniel
 */
public class PreferenceSettings {

    

    List<PreferenceSetting> preferenceSettingList;

    private static Map<String, String> optionParamMap;

    String filePath;

    private String forecastDBBasedDir;

    public PreferenceSettings(String filePath) {//Open PreferenceSettings File
        preferenceSettingList = new ArrayList();
        this.filePath = filePath;

        Configurations configs = new Configurations();
        File file = new File(filePath);
        try {
            XMLConfiguration config = configs.xml(file);
            //Common Settings
            forecastDBBasedDir = config.getString("ForecastDB.baseDir");
            List<String> optionParamNameList
                    = config.getList(String.class, "optionParameters.optionParameter[@typeName]");
            List<String> optionParamContentList
                    = config.getList(String.class, "optionParameters.optionParameter");
            optionParamMap = new HashMap();

            for (int i = 0; i < optionParamNameList.size(); i++) {
                optionParamMap.put(optionParamNameList.get(i), optionParamContentList.get(i));
            }

            List<String> languageNameList = config.getList(String.class, "Language[@name]");
            List<String> languageDateFormatList = config.getList(String.class, "Language.dateFormat");
            List<String> languageLocaleFormatList = config.getList(String.class, "Language.locale");
            for (int i = 0; i < languageNameList.size(); i++) {
                String langName = languageNameList.get(i);
                String dateFormat = languageDateFormatList.get(i);
                String localeCode = languageLocaleFormatList.get(i);
                System.out.println("Language : " + langName + " " + dateFormat + " "+localeCode);

                PreferenceSetting ps = new PreferenceSetting(langName);
                ps.setForecastDBDir(forecastDBBasedDir + langName + "/LinkForecastData/");
                ps.setDateFormat(dateFormat,localeCode);
                preferenceSettingList.add(ps);
            }
        } catch (ConfigurationException cex) {
            System.out.println(cex.toString());
        }
    }

    public List<Tab> tabCreator() {
        List<Tab> tabList = new ArrayList();
        for (PreferenceSetting ps : preferenceSettingList) {
            tabList.add(new Tab(ps));
        }
        return tabList;
    }

    public static String getTypeParameterValue(String type) {
        String returnObj = optionParamMap.get(type);
        if (returnObj == null) {
            return "";
        }

        return returnObj;
    }
    
    /**
     * @return the optionParamMap
     */
    public static Map<String, String> getOptionParamMap() {
        return optionParamMap;
    }

}
