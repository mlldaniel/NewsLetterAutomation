/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.jsoup.nodes.Document;
import tab.Item;

/**
 *
 * @author Daniel
 */
public class HTMLManager {

    int BUTTON = 0;
    int MINISTORY = 1;
    int OTHER = 3;

    public static final int HEADER = 0;
    public static final int BODY = 1;
    public static final int FOOTER = 2;
    String[] basedHtmlLoc = {"td[class=headerContainer]", "td[class=bodyContainer]", "td[class=footerContainer]"};
    String[] forecastTableHtmlLoc = {};
    
    String resourceDirectory;

    private ResourceHTML basedHTML;
    private ResourceHTML buttonHTML;
    private ResourceHTML forecastTableHTML;
    private ResourceHTML forecastTableLiHTML;
    private ResourceHTML importHTML;
    private ResourceHTML importListItemHTML;
    private ResourceHTML miniHTML;
    private ResourceHTML customHTML;

    public HTMLManager(String resourceDir) {// For Next Version
        this.resourceDirectory = resourceDir;

        basedHTML = new ResourceHTML(resourceDirectory, "base.html");
        buttonHTML = new ResourceHTML(resourceDirectory, "button.html");
        forecastTableHTML = new ResourceHTML(resourceDirectory, "forecastTable.html");
        forecastTableLiHTML = new ResourceHTML(resourceDirectory, "forecastTableListItem.html");
        importHTML = new ResourceHTML(resourceDirectory, "import.html");
        importListItemHTML = new ResourceHTML(resourceDirectory, "importListItem.html");
        miniHTML = new ResourceHTML(resourceDirectory,"ministory.html");
        customHTML = new ResourceHTML(resourceDirectory,"custom.html");
        //basedHTML.insertContent(forecastTableHTML.getDoc(), basedHtmlLoc[BODY], BUTTON);
        //basedHTML.insertContent(buttonHTML.getDoc(), basedHtmlLoc[BODY], ResourceHTML.APPEND);
        //testSaveHTML(buttonHTML.getDoc(),"output/test/"+buttonHTML.getName());
        //testSaveHTML(basedHTML.getDoc(), "output/test/" + basedHTML.getName());
    }
    public HTMLManager() {//Previous Version when htmlmanager was in common resources
        resourceDirectory = "resources/common/";

        basedHTML = new ResourceHTML(resourceDirectory, "base.html");
        buttonHTML = new ResourceHTML(resourceDirectory, "button.html");
        forecastTableHTML = new ResourceHTML(resourceDirectory, "forecastTable.html");
        forecastTableLiHTML = new ResourceHTML(resourceDirectory, "forecastTableListItem.html");
        importHTML = new ResourceHTML(resourceDirectory, "import.html");
        importListItemHTML = new ResourceHTML(resourceDirectory, "importListItem.html");
        //basedHTML.insertContent(forecastTableHTML.getDoc(), basedHtmlLoc[BODY], BUTTON);
        //basedHTML.insertContent(buttonHTML.getDoc(), basedHtmlLoc[BODY], ResourceHTML.APPEND);
        //testSaveHTML(buttonHTML.getDoc(),"output/test/"+buttonHTML.getName());
        //testSaveHTML(basedHTML.getDoc(), "output/test/" + basedHTML.getName());
    }
    /////////////////// TEST

    public static void saveHTML(Document doc, String savingLocation) {
        try {
            saveFinalHtml(doc, savingLocation);
            System.out.println(savingLocation + "Saved!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void saveHTML(String docStr, String savingLocation) {
        try {
            saveFinalHtml(docStr, savingLocation);
            System.out.println(savingLocation + "Saved!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /////////////////// TEST END
    public Document generateHTML(List<Item> docList) {
        Document content;
        for (Item item : docList) {
//            switch(item.getType()){
//                case BUTTON:
//                    content += buttonHTML.insertContent(item.getPreview());
//                case MINISTORY:
//                    content += ministoryHTML.insertContent(item.getPreview());
//            }
        }
        return null; // return baseHTML <= docList 
    }
//    public void saveNewsletter(List<Item> docList){
//        
//        try{
//            saveFinalHtml(testGenerateHTML().getDoc(),"output/savedNewsletter.html");
//
//        }catch(IOException e){
//            e.printStackTrace();
//        }
//    }

    private static void saveFinalHtml(Document doc, String fileName) throws IOException {
        //System.out.println(doc.outerHtml());
        final File f = new File(fileName);
        FileUtils.writeStringToFile(f, doc.outerHtml(), "UTF-8");

    }
    private static void saveFinalHtml(String docStr, String fileName) throws IOException {
        //System.out.println(doc.outerHtml());
        final File f = new File(fileName);
        FileUtils.writeStringToFile(f, docStr, "UTF-8");

    }

    /**
     * @return the basedHTML
     */
    public ResourceHTML getBasedHTML() {
        return basedHTML;
    }

    /**
     * @return the buttonHTML
     */
    public ResourceHTML getButtonHTML() {
        return buttonHTML;
    }

    /**
     * @return the forecastTableHTML
     */
    public ResourceHTML getForecastTableHTML() {
        return forecastTableHTML;
    }

    /**
     * @return the forecastTableLiHTML
     */
    public ResourceHTML getForecastTableLiHTML() {
        return forecastTableLiHTML;
    }

    /**
     * @return the importHTML
     */
    public ResourceHTML getImportHTML() {
        return importHTML;
    }

    /**
     * @return the importListItemHTML
     */
    public ResourceHTML getImportListItemHTML() {
        return importListItemHTML;
    }

    public ResourceHTML getMiniHTML() {
        return miniHTML;
    }

    /**
     * @return the customHTML
     */
    public ResourceHTML getCustomHTML() {
        return customHTML;
    }
}
