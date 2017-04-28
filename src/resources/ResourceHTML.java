/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import java.io.File;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

/**
 *
 * @author Daniel
 */
public class ResourceHTML {

    public static final int PREPPEND = 0;
    public static final int APPEND = 1;
    public static final int HTML = 2;

    private String name;
    private String filePath;
    private Document doc;

    public ResourceHTML(ResourceHTML rHTML) {
        this.name = rHTML.getName();
        this.filePath = rHTML.getFilePath();
        this.doc = rHTML.getDoc().clone();
    }

    public ResourceHTML(String resourcePath, String fileName) {

        this.name = fileName;
        this.filePath = resourcePath + "/" + fileName;
        try {
            doc = readHtml(filePath);
            //exportFinalHtml(doc, "output\\output.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void insertContent(Document input, String location, int option) {
        //System.out.println(input.toString());
        //System.out.println(doc.toString());
        Element selected = doc.select(location).first();

        switch (option) {
            case PREPPEND:
                selected.prepend(input.toString());
                break;
            case APPEND:
                selected.append(input.toString());
                break;
            case HTML:
                selected.html(input.toString());
                break;
            default:

        }

    }
    public String extractOuterHtmlContent(String location){
        return doc.select(location).first().outerHtml();
    }
    public Element extractContent(String location){
        return doc.select(location).first();
    }
    public void insertContent(String input, String location, int option) {
//        System.out.println(input.toString());
//        System.out.println(doc.toString());
        
        Element selected = doc.select(location).first();

        switch (option) {
            case PREPPEND:
                selected.prepend(input);
                break;
            case APPEND:
                selected.append(input);
                break;
            case HTML:
                selected.html(input);
                break;
            default:

        }

    }
    private static String changeStyleAttr(String originalAttr, String changeKey, String changeValue) {

        String[] codeSet = originalAttr.split(";");
        String returnAttr = "";
        for (String code : codeSet) {
            if (code.contains(changeKey) == false) {
                returnAttr += code + ";";
            }
        }
        returnAttr += changeKey + ":" + changeValue + ";";
        return returnAttr;
    }
    private static String getAttrStyleValue(String orgAttr, String key){
        String[] splitAtt = orgAttr.split(";");
        for(String attItem: splitAtt){
            if(attItem.contains(key)){
                attItem = attItem.trim();
                String val = attItem.substring(key.length());
                return val.trim();
            }
        }
        return "";
    }
    
    private static double ptToPxEquation(String getPt){
        getPt = getPt.substring(0, getPt.indexOf("pt"));
        double ptVal = Double.parseDouble(getPt);
        double pxVal = ptVal / 0.70;
        return pxVal;
    }
    public static String convertPtToPxStr(String htmlString){
        Document doc = Jsoup.parse(htmlString, "UTF-8");
        Elements elements = doc.select("*");
        for(Element el : elements){
            String att = el.attr("style");
            if(att.contains("font-size:")==false)
                continue;
            String getPt = getAttrStyleValue(att, "font-size:");
            
            if(getPt.contains("pt")==false)
                continue;
            double pxVal = ptToPxEquation(getPt);
            String changedAttr = changeStyleAttr(att,"font-size",String.format("%.2f", pxVal)+"px");
            
            System.out.println(changedAttr);
            
            //Set the text
            el.attr("style", changedAttr);
        }
        return doc.toString();
    }
    
    
    public static Document convertPtToPxDoc(Document html){
        Document doc = html;
        Elements elements = doc.select("*");
        for(Element el : elements){
            String att = el.attr("style");
            if(att.contains("font-size:")==false)
                continue;
            String getPt = getAttrStyleValue(att, "font-size:");
            
            if(getPt.contains("pt")==false)
                continue;
            double pxVal = ptToPxEquation(getPt);
            String changedAttr = changeStyleAttr(att,"font-size",String.format("%.2f", pxVal)+"px");
            
            System.out.println(changedAttr);
            
            //Set the text
            el.attr("style", changedAttr);
        }
        return doc;
    }
    public void insertAttrContent(String input, String location, String attribute) {
        //System.out.println(input.toString());
        //System.out.println(doc.toString());
        doc.select(location).attr(attribute, input);

    }

    private Document readHtml(String fileName) throws IOException {
        File input = new File(fileName);
        Document inputDoc = Jsoup.parse(input, "UTF-8");
        return (inputDoc);
    }

    public Document getDoc() {
        return doc;
    }

    public Document getDocWithoutWrap() {
        Document doc = new Cleaner(Whitelist.simpleText()).clean(getDoc());
        doc.outputSettings().escapeMode(EscapeMode.xhtml);
        return doc;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the filePath
     */
    public String getFilePath() {
        return filePath;
    }
    
    
}
