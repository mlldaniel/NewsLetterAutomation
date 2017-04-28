package test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.util.List;
import org.apache.commons.configuration2.*;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
/**
 *
 * @author Daniel
 */
public class CommonConfig {
    public static void main1(String args[]){
        Configurations configs = new Configurations();
        File file = new File("test/paths.xml");
        System.out.println(file.getName());
            try
            {
                XMLConfiguration config = configs.xml("test/paths.xml");
                String stage = config.getString("processing[@stage]");
                List<String> paths = config.getList(String.class, "processing.paths.path");
                System.out.println("Processing stage: "+ stage);
                for(String p : paths)
                    System.out.println("processing.paths: "+ p);
                String lang = config.getString("language[@lang]");
                String item = config.getString("language.item");
                System.out.println("Lnag : " + lang);
                System.out.println("item : " + item);
            }
            catch (ConfigurationException cex)
            {
                //System.out.println(cex.toString());
            }

    }
}
