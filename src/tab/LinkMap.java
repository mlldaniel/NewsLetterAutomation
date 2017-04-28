/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tab;

/**
 *
 * @author Daniel
 */
public class LinkMap {
    String linkedTitle;
    String unlinkedTitle;
    String link;

    
    public LinkMap(String linkedTitle, String unlinkedTitle, String link) {
        this.linkedTitle = linkedTitle;
        this.unlinkedTitle = unlinkedTitle;
        this.link = link;
    }
    
    public static LinkMap forecastDataToLinkMap(String wholeTitle, String link, String separator) {
        String[] titles = wholeTitle.split(separator);
        
        switch (titles.length) {
            case 2:
                return new LinkMap(titles[0].trim(),titles[1].trim(),link.trim());
            case 1:
                return new LinkMap(titles[0].trim(),"",link.trim());
            default:
                return new LinkMap("No Link","No Link",link.trim());
        }
    }
}
