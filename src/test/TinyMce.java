/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import tab.layout.LayoutTableItem;
import ui.wysiwyg.EventHandler;

/**
 *
 * @author danielhwang
 */
public class TinyMce implements Runnable {

    private static Long sleepInterval = new Long(1000);
    
    LayoutTableItem lsItem;
    boolean preview;
    public TinyMce(LayoutTableItem lsItem, boolean isPreview){
        this.lsItem = lsItem;
        this.preview = isPreview;
    }
    public String getContents(){
        if(preview){
            return lsItem.getPreview();
        }else{
            return lsItem.getTitle();
        }
    }
    public void setContents(String html){
        if(preview){
            lsItem.setPreview(html);
        }else{
            lsItem.setTitle(html);
        }
    }
    public void run(){
        String contents = getContents();
        
        if(System.getProperty("webdriver.chrome.driver")==null){
            System.setProperty("webdriver.chrome.driver", "resources/common/chromedriverMac");
        }
        
        WebDriver driver = new ChromeDriver();
        //open TinyMCE
        Wait<WebDriver> wait = new WebDriverWait(driver, 30);
        String workingDir = System.getProperty("user.dir");
        String fileName = workingDir + "/resources/common/tinymce.html";
        
        driver.get("file://" + fileName);
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
