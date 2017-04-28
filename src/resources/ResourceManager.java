/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import resources.ForecastDB.ForecastDataManager;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import resources.ministory.MinistoryFormManager;
import tab.LinkMap;
import tab.LinkMapImport;
import tab.layout.LayoutTableItem;
import tab.layout.LayoutTableModel;

/**
 *
 * @author Daniel
 */
public class ResourceManager {

    String languageName;
    String resourcePath;

    //Database Of Forecast
    private ForecastDataManager forecastDataManager; //Load SQLite from ForecastProgram

    //HTML Manager
    private HTMLManager htmlManager;
    String htmlManagerDir;

    //Import Resources
    private String wpPath;
    private String catExcludePath;

    //Ministory Resources
    private String miniFormPath;
    private MinistoryFormManager miniFormManager;
    
    
    public ResourceManager(String languageName, String forecastDataManagerDir) {
        String curDir = System.getProperty("user.dir");
        this.languageName = languageName;
        this.resourcePath = curDir + "/resources/" + languageName + "/";

        wpPath = resourcePath + "WPExports/";
        catExcludePath = resourcePath + "CategoriesToExclude/";
        //forecastDataManagerDir =  "C:/Users/Daniel/ForecastAutomation(Test)/output/"+languageName+"/LinkForecastData/sqlite.db";  //"resources/"+languageName+"/"+
        forecastDataManagerDir = forecastDataManagerDir + "sqlitedb.db";
        forecastDataManager = new ForecastDataManager(forecastDataManagerDir);

        htmlManagerDir = resourcePath + "HtmlForms/";
        htmlManager = new HTMLManager(htmlManagerDir);

        //Load Data Ministory Form
        miniFormPath = resourcePath + "MiniStoryDB/miniStoryDB.xlsx";
        miniFormManager = new MinistoryFormManager(miniFormPath);
        
    }

    //Import Wordpress items
    public List<LinkMapImport> readWpExport(String fileName, List<String> catListToExclude) {// writeWPExportToResource for import
        List<LinkMapImport> linkMapList = new ArrayList();
        String thisFilePath = wpPath + fileName;

        Configurations configs = new Configurations();
        File file = new File(thisFilePath);

        try {
            //XMLConfiguration config = configs.xml(file);
            HierarchicalConfiguration configH = configs.xml(file);

            List<HierarchicalConfiguration> itemList = configH.configurationsAt("channel.item");

            //For Each Post Item
            for (HierarchicalConfiguration item : itemList) {
                boolean pass = false;
                List<HierarchicalConfiguration> catList = item.configurationsAt("category");

                //Check Category List
                for (HierarchicalConfiguration cat : catList) {
                    //Filter category domain 
                    if (cat.getString("[@domain]").equals("category") == false) {
                        continue;
                    }

                    //System.out.println(cat.getString(""));
                    //if cagetory doamin check if is category to exclude
                    //catListToExclude.stream().anyMatch(s -> s.equalsIgnoreCase(cat.getString("")));
                    if (catListToExclude.contains(cat.getString(""))) {
                        pass = true;
                        break;
                    }

                }

                //if pass var is true just continue
                if (pass) {
                    continue;
                }

                //If everything is good Add to Link Map List
                String title = item.getString("title");
                String link = item.getString("link");

                String contentEncoded = item.getString("content:encoded");
                String avg = parseAvgOrHitRatio(contentEncoded);
                linkMapList.add(new LinkMapImport(LinkMap.forecastDataToLinkMap(title, link, ":"), avg));
            }

        } catch (ConfigurationException cex) {
            System.out.println(cex.toString());
        } catch (Exception ex) {
            System.out.println(ex.toString());
        } finally {
            return linkMapList;
        }

    }

    public List<LinkMapImport> readWpExportFixed(String fileName, List<String> catListToExclude) {// writeWPExportToResource for import
        List<LinkMapImport> linkMapList = new ArrayList();
        String filePath = wpPath + fileName;

        try {
            System.out.println("Reading XML File : " + filePath);
            File inputFile = new File(filePath);

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("item");
            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    //Filter Out if Category is not needed
                    NodeList categoryNodeList = eElement.getElementsByTagName("category");
                    boolean ignoreThisItem = false;
                    for (int j = 0; j < categoryNodeList.getLength(); j++) {
                        Node catNode = categoryNodeList.item(j);
                        Element catElement = (Element) catNode;
                        if (catElement.getAttribute("domain").equals("category")) {
                            if (catListToExclude.contains(catElement.getTextContent())) {
                                ignoreThisItem = true;
                                break;
                            }
                        }
                    }
                    //Check if It is excluded
                    if(ignoreThisItem)
                        continue;

                    String title = eElement.getElementsByTagName("title")
                            .item(0)
                            .getTextContent();
                    String link = eElement.getElementsByTagName("link")
                            .item(0)
                            .getTextContent();
                    String contentEncoded = eElement.getElementsByTagName("content:encoded")
                            .item(0)
                            .getTextContent();
                    String parsedValue = parseAvgOrHitRatio(contentEncoded);
                    linkMapList.add(new LinkMapImport(LinkMap.forecastDataToLinkMap(title, link, ":"), parsedValue));
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            return linkMapList;
        }

    }

    public String parseAvgOrHitRatio(String content) {

        String valueDivider = "I Know First Average: </strong>";
        int indexToCut = content.indexOf(valueDivider);

        if (indexToCut == -1) {
            valueDivider = "I Know First Hit Ratio: </strong>";
            indexToCut = content.indexOf(valueDivider);
            if (indexToCut == -1) {
                return "0.0";
            }
        }

        indexToCut += valueDivider.length();
        String returnObj = content.substring(indexToCut);
        returnObj = returnObj.split("%")[0];

        return returnObj;
    }

    //get Exclude Categories file list
    public List<String> getCategoryExcludeList(String fileName) {
        List<String> returnList = new ArrayList();
        String thisFilePath = catExcludePath + fileName.trim();

        Configurations configs = new Configurations();
        File file = new File(thisFilePath);

        try {
            XMLConfiguration config = configs.xml(file);

            returnList = config.getList(String.class, "category");
            System.out.println("Category Exception List read : ");
            returnList.forEach(System.out::println);
        } catch (ConfigurationException cex) {
            System.out.println(cex.toString());
        } finally {
            return returnList;
        }

    }

    //Save Load Layout
    public static void saveLayoutTable(File file, LayoutTableModel tableModel) {
        try {
            final XMLEncoder encoder
                    = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(file)));
            encoder.writeObject(tableModel.getLayoutTableItemList());
            encoder.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static int loadLayoutTable(File file, LayoutTableModel tableModel) {
        int size = 0;
        try {
            final XMLDecoder decoder = new XMLDecoder(new FileInputStream(file));
            final List<LayoutTableItem> listFromFile = (List<LayoutTableItem>) decoder.readObject();
            tableModel.setLayoutTableItemList(listFromFile);
            decoder.close();

            size = listFromFile.size();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return size;
    }

    //Getters & Setters
    /**
     * @return the forecastDataManager
     */
    public ForecastDataManager getForecastDataManager() {
        return forecastDataManager;
    }

    /**
     * @return the htmlManager
     */
    public HTMLManager getHtmlManager() {
        return htmlManager;
    }

    /**
     * @return the miniFormManager
     */
    public MinistoryFormManager getMiniFormManager() {
        return miniFormManager;
    }
    

}
