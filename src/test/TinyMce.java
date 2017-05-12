/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import resources.HTMLManager;
import resources.ResourceHTML;
import static resources.ResourceHTML.APPEND;
import static resources.ResourceHTML.HTML;
import tab.layout.LayoutTableItem;
import ui.ChromePreviewThread;
import static ui.NewsLetterUI.tabManager;

/**
 *
 * @author danielhwang
 */
public class TinyMce implements Runnable {

    private static Long sleepInterval = new Long(1000);

    LayoutTableItem lsItem;
    boolean preview;

    ResourceHTML basedHtmlOrg;

    public TinyMce(LayoutTableItem lsItem, boolean isPreview) {
        this.lsItem = lsItem;
        this.preview = isPreview;

        tab.Tab tab = tabManager.getCurrentTab();
        HTMLManager htmlManager = tab.getResourceManager().getHtmlManager();
        basedHtmlOrg = htmlManager.getBasedHTML();
    }

    public String getContents() {
        if (preview) {
            return lsItem.getPreview();
        } else {
            return lsItem.getTitle();
        }
    }

    public void setContents(String html) {
        if (preview) {
            lsItem.setPreview(html);
        } else {
            lsItem.setTitle(html);
        }
    }

    public void run() {
        String contents = this.getContents();// Get Contents
        ResourceHTML baseHtml = new ResourceHTML(basedHtmlOrg);
        baseHtml.insertContent(contents, "td[class=bodyContainer]", HTML);

        if (System.getProperty("webdriver.chrome.driver") == null) {
            System.setProperty("webdriver.chrome.driver", "resources/common/chromedriverMac");
        }

        String workingDir = System.getProperty("user.dir");
        String resourcePath = workingDir + "/resources/common";
        String fileName = "tinymce.html";

        ResourceHTML tinyMCE = new ResourceHTML(resourcePath, fileName);
        tinyMCE.insertContent(baseHtml.getDoc(), "textarea[id=textArea]", HTML);
        HTMLManager.saveHTML(tinyMCE.getDoc(), resourcePath + "/temp.html");
        

        WebDriver driver = new ChromeDriver();
        Wait<WebDriver> wait = new WebDriverWait(driver, 30);
        driver.get("file://" + resourcePath + "/temp.html");

        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
//        
//        javascriptExecutor.executeScript("arguments[0].innerHTML = '<h1>Set text using innerHTML</h1>'", element);
        //tinyMCE.activeEditor.setContent('<span>some</span> html', {format : 'raw'});
//        
//        WebDriver driverDisplay = new ChromeDriver();
        String returnStr = (String) javascriptExecutor.executeScript("return tinyMCE.activeEditor.getContent({format : 'raw'});");
        //ResourceHTML contentWithBase = new ResourceHTML(returnStr);
        HTMLManager.saveHTML(returnStr, "temp/temp.html");
        
        WebDriver displayDriver = new ChromeDriver();
        Wait<WebDriver> wait2 = new WebDriverWait(displayDriver, 30);
        displayDriver.get("file://"+workingDir+"/temp/temp.html");
        
        while(true){
            try{
                Thread.sleep(sleepInterval);
                String nextState = (String) javascriptExecutor.executeScript("return nextState()");
                returnStr = (String) javascriptExecutor.executeScript("return tinyMCE.activeEditor.getContent({format : 'raw'});");
                
                if(nextState.equalsIgnoreCase("save")){
                    Document docToSave = Jsoup.parse(returnStr);
                    String saveStr = docToSave.select("td[class=bodyContainer]").html();
                    this.setContents(saveStr);
                    
                }else if(nextState.equalsIgnoreCase("close")){
                    driver.quit();
                    displayDriver.quit();
                    break;
                }
                //System.out.println(returnStr);
                
                //Insert To baseHTML and Save
                HTMLManager.saveHTML(returnStr, "temp/temp.html");
                
                displayDriver.navigate().refresh();
            }catch (InterruptedException ex) {
                Logger.getLogger(ChromePreviewThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
//
//        String temp = "test/test.html";
//        Document doc = Jsoup.parse(returnStr, "UTF-8");
//        HTMLManager.saveHTML(doc, temp);
//        driverDisplay.get("file://" + workingDir +"/" +temp);
//        while (true) {
//            try{
//                Thread.sleep(sleepInterval);
//                returnStr = (String) javascriptExecutor.executeScript("return tinyMCE.activeEditor.getContent({format : 'raw'});");
//                //System.out.println(returnStr);
//                
//                //Insert To baseHTML and Save
//                baseHtml.insertContent(returnStr, "td[class=bodyContainer]", HTML);
//                HTMLManager.saveHTML(baseHtml.getDoc(), temp);
//                
//                driverDisplay.navigate().refresh();
//            }catch (InterruptedException ex) {
//                Logger.getLogger(ChromePreviewThread.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
    }

    public static void main(String[] args) {
//        System.setProperty("webdriver.chrome.driver", "resources/common/chromedriverMac");
//
//        WebDriver driver = new ChromeDriver();
//        //EventFiringWebDriver eventDriver = new EventFiringWebDriver(driver);
//        
//        //EventHandler handler = new EventHandler();
//        //eventDriver.register(handler);
//        
//        
//        Wait<WebDriver> wait = new WebDriverWait(driver, 30);
//        String workingDir = System.getProperty("user.dir");
//        String fileName = workingDir + "/resources/common/tinymce.html";
//        
//        //eventDriver.get("file://" + fileName);
//        driver.get("file://" + fileName);
//        
//        String sgVal = (String) ((JavascriptExecutor)driver).executeScript("return isSaved();");
//        System.out.println(sgVal);
        //driver.get("file://" + fileName);

        //((JavascriptExecutor)driver).execute_script("tinyMCE.activeEditor.setContent('<h1>Yi Zeng</h1> TinyMCE')");
        //((JavascriptExecutor)driver).executeScript("tinyMCE.activeEditor.setContent('<h1>Yi Zeng</h1> TinyMCE')");
    }
}
