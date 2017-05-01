/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import resources.HTMLManager;
import resources.ResourceHTML;
import static resources.ResourceHTML.APPEND;
import static resources.ResourceHTML.HTML;
import tab.Tab;
import static ui.NewsLetterUI.tabManager;

/**
 *
 * @author Daniel
 */
public class ChromePreviewThread implements Runnable {

    private ToolbarCustomizing.ModifiedEditor editor;
    private JFrame frame;

    private static Long sleepInterval = new Long(1000);
    ResourceHTML basedHtmlOrg;

    public ChromePreviewThread(ToolbarCustomizing.ModifiedEditor editor, JFrame frame) {
        this.editor = editor;
        this.frame = frame;

        Tab tab = tabManager.getCurrentTab();
        HTMLManager htmlManager = tab.getResourceManager().getHtmlManager();
        basedHtmlOrg = htmlManager.getBasedHTML();
    }

    public void run() {
        String editorText = editor.getText();
        //Convert pt to px

        //save
        String workingDir = System.getProperty("user.dir");
        String fileName = workingDir + "/temp/temp.html";
        final File f = new File(fileName);

        ResourceHTML baseHtml = new ResourceHTML(basedHtmlOrg);
        baseHtml.insertContent(editorText, "td[class=bodyContainer]", APPEND);
        HTMLManager.saveHTML(baseHtml.getDoc(), fileName);

        //Keep Load/Convert & Refresh
        WebDriver driver = new ChromeDriver();
        Wait<WebDriver> wait = new WebDriverWait(driver, 30);

        driver.get("file://" + fileName);
        //new WebDriverBackedSelenium(driver, "file:///D:/folder/abcd.html");

        //driver.get("http://www.naver.com");
        while (true) {
            if (frame.isVisible() == false) {
                System.out.println("Stopping Thread/Chrome");
                driver.quit();
                return;
            }

            try {
                Thread.sleep(sleepInterval);
                //Get the Text, Save and Refresh
                editorText = editor.getText(); // get
                System.out.println("Thread Running@");

                //Convert PT to Px
                String convertedText = ResourceHTML.convertPtToPxStr(editorText);

                //Inser to BaseHtml and Save
                baseHtml.insertContent(convertedText, "td[class=bodyContainer]", HTML);
                HTMLManager.saveHTML(baseHtml.getDoc(), fileName);

                //Refresh Chrome
                driver.navigate().refresh();

            } catch (InterruptedException ex) {
                Logger.getLogger(ChromePreviewThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public boolean shouldStop() {
        if (frame.isVisible() == false) {
            return true;
        } else {
            return false;
        }
    }

}
