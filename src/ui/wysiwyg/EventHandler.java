/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.wysiwyg;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;

public class EventHandler implements WebDriverEventListener {

    @Override
    public void beforeAlertAccept(WebDriver wd) {
    }

    @Override
    public void afterAlertAccept(WebDriver wd) {
    }

    @Override
    public void afterAlertDismiss(WebDriver wd) {
    }

    @Override
    public void beforeAlertDismiss(WebDriver wd) {
    }

    @Override
    public void beforeNavigateTo(String string, WebDriver wd) {
        System.out.println("beforeNavigateTo");
    }
    
    //After Navigate is Complete
    @Override
    public void afterNavigateTo(String string, WebDriver wd) {
    }

    @Override
    public void beforeNavigateBack(WebDriver wd) {
    }

    @Override
    public void afterNavigateBack(WebDriver wd) {
    }

    @Override
    public void beforeNavigateForward(WebDriver wd) {
    }

    @Override
    public void afterNavigateForward(WebDriver wd) {
        
    }

    @Override
    public void beforeNavigateRefresh(WebDriver wd) {
    }

    @Override
    public void afterNavigateRefresh(WebDriver wd) {
    }

    @Override
    public void beforeFindBy(By by, WebElement we, WebDriver wd) {
    }

    @Override
    public void afterFindBy(By by, WebElement we, WebDriver wd) {
    }

    @Override
    public void beforeClickOn(WebElement we, WebDriver wd) {
    }

    @Override
    public void afterClickOn(WebElement we, WebDriver wd) {
        System.out.println("Clicked on: " + we.toString());

    }

    @Override
    public void beforeChangeValueOf(WebElement we, WebDriver wd, CharSequence[] css) {
    }

    @Override
    public void afterChangeValueOf(WebElement we, WebDriver wd, CharSequence[] css) {
    }

    @Override
    public void beforeScript(String string, WebDriver wd) {
        System.out.println(string + wd);
    }

    @Override
    public void afterScript(String string, WebDriver wd) {
        System.out.println(string + wd);
    }

    @Override
    public void onException(Throwable thrwbl, WebDriver wd) {
    }


}
