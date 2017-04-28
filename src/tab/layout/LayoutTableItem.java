/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tab.layout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import tab.Item;

/**
 *
 * @author Daniel
 */
public class LayoutTableItem implements Item {

    /**
     * @return the ready
     */
    public boolean isReady() {
        return ready;
    }

    /**
     * @param ready the ready to set
     */
    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public static final int colCount = 5;

    private Integer position;
    private String type;
    //String preview
    private Document title;
    private String parametersNeeded;

    private Document preview;

    private boolean ready;

    public static final int POSITION = 0;
    public static final int TYPE = 1;
    public static final int TITLE = 2;
    public static final int PARAMETERNEEDED = 3;
    public static final int PREVIEW = 4;
    //"position","type","preview","parameters needed"

    public LayoutTableItem(String type) {
        this.position = -1;
        this.type = type;
        this.title = Jsoup.parse("<div id='innerTitleDiv'></div>");
        this.parametersNeeded = "";
        this.preview = new Document("");
        ready = false;
    }

    public LayoutTableItem() {
        this.position = -1;
        this.type = "";
        this.title = new Document("");
        this.parametersNeeded = "";
        this.preview = new Document("");
        ready = false;
    }

    /**
     * @return the position
     */
    public Integer getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(Integer position) {
        this.position = position;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the preview
     */
    public String getPreview() {
        return preview.html();
    }

    public Document getPreviewDoc() {
        return preview;
    }

    /**
     * @param preview the preview to set
     */
    public void setPreview(Document preview) {
        this.preview = preview;
    }

    public void setPreview(String preview) {
        this.preview = Jsoup.parse(preview);
    }

    /**
     * @return the parametersNeeded
     */
    public String getParametersNeeded() {
        return parametersNeeded;
    }

    /**
     * @param parametersNeeded the parametersNeeded to set
     */
    public void setParametersNeeded(String parametersNeeded) {
        this.parametersNeeded = parametersNeeded;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title.html();
    }
    public Document getTitleDoc() {
        return title;
    }
    public Document getTitleDocument() {
        return title;
    }

    public boolean checkTitleHasText() {
        return title.hasText();
        //return title.getElementById("innerTitleDiv").hasText();
    }

    /**
     * @param title the title to set
     */
    public void setTitle(Document title) {
        this.title = title;
    }

    public void setTitle(String title) {
        this.title = Jsoup.parse(title);;
    }
}
