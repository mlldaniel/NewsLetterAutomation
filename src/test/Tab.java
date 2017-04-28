/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
/**
 *
 * @author Daniel
 */
public class Tab {
    Document doc;
    
    public Tab(){
        try{
            doc = readOuterFrameHtml("resources\\base.html");
            exportFinalHtml(doc, "output\\output.html");
        }catch(IOException e){
            e.printStackTrace();
        }
        
    }
    private void exportFinalHtml(Document doc, String fileName) throws IOException{
        
        final File f = new File(fileName);
        FileUtils.writeStringToFile(f, doc.outerHtml(), "UTF-8");

    }
    private Document readOuterFrameHtml(String fileName)throws IOException{
        File input = new File(fileName);
        Document doc = Jsoup.parse(input, "UTF-8");
        return(doc);
        
    }
}
