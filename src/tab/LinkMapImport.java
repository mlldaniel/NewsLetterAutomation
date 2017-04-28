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
public class LinkMapImport extends LinkMap{
    public Double iKnowFirstAvg;
    public LinkMapImport(String linkedTitle, String unlinkedTitle, String link, String iKnowFirstAvg){
        super(linkedTitle,unlinkedTitle,link);
        Double avg = Double.parseDouble(iKnowFirstAvg);
        
        this.iKnowFirstAvg = avg;
    }
    public LinkMapImport(LinkMap linkMap, String iKnowFirstAvg){
        super(linkMap.linkedTitle,linkMap.unlinkedTitle,linkMap.link);
        Double avg = Double.parseDouble(iKnowFirstAvg);
        
        this.iKnowFirstAvg = avg;
    }
}
