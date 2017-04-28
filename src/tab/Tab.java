package tab;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JPanel;
import resources.PreferenceSetting;
import resources.ResourceManager;
import static tab.TabManager.*;
import tab.forecast.ForecastSection;
import tab.layout.LayoutSection;
import static ui.FormManaqer.LAYOUTSECTION;

public class Tab {

    
    Date sendingDate;
    
    //ui Control & other
    private Boolean generateThis;
    private int currentSectionNumber;
    private JPanel jPanelCards;

    //Resources
    private PreferenceSetting peferenceSetting;
    private ResourceManager resourceManager;

    //Sections
    private ForecastSection forecastSection;
    private LayoutSection layoutSection;
    
    //Used Ministory Form Id List
    private List<Integer> usedMiniFormList;

    public Tab(PreferenceSetting peferenceSetting) {
        sendingDate = new Date();
        
        generateThis = false;
        currentSectionNumber = FORECASTSECTION;

        this.peferenceSetting = peferenceSetting;
        resourceManager = new ResourceManager(peferenceSetting.getLanguageName(), peferenceSetting.getForecastDBDir());

        forecastSection = new ForecastSection();
        layoutSection = new LayoutSection();

        //Create Panel
        jPanelCards = new JPanel();
        jPanelCards.setLayout(new java.awt.CardLayout());
        jPanelCards.add(forecastSection.getBaseComponent(), "ForecastSection");

        jPanelCards.add(layoutSection.getBaseComponent(), "LayoutSection");

        CardLayout cL = (CardLayout) jPanelCards.getLayout();
        cL.show(jPanelCards, "ForecastSection");
        
        //Init usedMiniFormList
        usedMiniFormList = new ArrayList();
    }

    //GenerateNewsletter in Preview 
    
    
    //UI related Functions
    public void switchSectionTo(int changeTo) {
        CardLayout cl = (CardLayout) jPanelCards.getLayout();
        if (changeTo == FORECASTSECTION) {
            cl.show(jPanelCards, "ForecastSection");
            currentSectionNumber = FORECASTSECTION;
        } else {
            cl.show(jPanelCards, "LayoutSection");
            currentSectionNumber = LAYOUTSECTION;
        }

    }

    //Getter Setter
    public String getTabName() {
        return getPeferenceSetting().getLanguageName();
    }

    public JPanel getjPanelCards() {
        return jPanelCards;
    }

    public ForecastSection getForecastSection() {
        return forecastSection;
    }

    public LayoutSection getLayoutSection() {
        return layoutSection;
    }

    /**
     * @return the currentSectionNumber
     */
    public int getCurrentSectionNumber() {
        return currentSectionNumber;
    }

    /**
     * @return the resourceManager
     */
    public ResourceManager getResourceManager() {
        return resourceManager;
    }

    /**
     * @return the generateThis
     */
    public Boolean getGenerateThis() {
        return generateThis;
    }

    /**
     * @param generateThis the generateThis to set
     */
    public void setGenerateThis(Boolean generateThis) {
        this.generateThis = generateThis;
    }

    /**
     * @return the peferenceSetting
     */
    public PreferenceSetting getPeferenceSetting() {
        return peferenceSetting;
    }
    
    /**
     * @return the usedMiniFormList
     */
    public List<Integer> getUsedMiniFormList() {
        return usedMiniFormList;
    }

    /**
     * @param usedMiniFormList the usedMiniFormList to set
     */
    public void setUsedMiniFormList(List<Integer> usedMiniFormList) {
        this.usedMiniFormList = usedMiniFormList;
    }

    void updateUsedMinistoryFormDate() {
        //Save
        Date curDate = resourceManager.getMiniFormManager().updateUsedFormDate(usedMiniFormList);
        //Updating Date
        //resourceManager.getMiniFormManager().updateMiniFormList(usedMiniFormList,curDate);
    }

}
