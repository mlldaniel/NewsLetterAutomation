package tab;

import helper.OsCheck;
import helper.UrlTagger;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import resources.ForecastDB.ForecastDataManager;
import resources.HTMLManager;
import resources.PreferenceSettings;
import resources.ResourceHTML;
import static resources.ResourceHTML.APPEND;
import static resources.ResourceHTML.HTML;
import static resources.ResourceHTML.PREPPEND;
import resources.ResourceManager;
import resources.TitleTag;
import resources.ministory.MinistoryFormItem;
import resources.ministory.MinistoryFormManager;
import tab.forecast.*;
import static tab.forecast.ForecastTableItem.*;
import tab.layout.*;
import ui.FormManaqer;
//import static ui.FormManaqer.forecastFormUpdate;
import static ui.FormManaqer.layoutFormUpdate;
import static ui.FormManaqer.setEnabledLayoutForm;

public class TabManager {

    static final int FORECASTSECTION = 0;
    static final int LAYOUTSECTION = 1;

    private JTabbedPane jTabbedPane;

    private PreferenceSettings preferenceSettings;
    static String preferenceSettingsDir = "resources/PreferenceSettings.xml";

    //COmmon Resource
    //HTMLManager htmlManager;
    ForecastDataManager forecastDataManagerKey;

    private List<Tab> tabList;//Language List
    private int currentTabNumber;
    private int currentRowNumber;

    private int ministoryNumber;
    
    private Date dateUrl;
    private int ministoryOrder;
    private int buttonOrder = 1;
    private int wpImportOrder = 1;
    private int customOrder = 1;
        
    public TabManager(JTabbedPane jTabbedPane) {
        this.jTabbedPane = jTabbedPane;

        this.currentTabNumber = 0;
        this.currentRowNumber = -1;

        //Ready for Preference Setting
        preferenceSettings = new PreferenceSettings(preferenceSettingsDir);

        //Chrome Driver Location
        String chromeDriveLoc = "resources/common/";
        OsCheck.OSType ostype = OsCheck.getOperatingSystemType();
        switch (ostype) {
            case Windows:
                chromeDriveLoc += "chromedriverWin.exe";
                break;
            case MacOS:
                chromeDriveLoc += "chromedriverMac";
                break;
            case Linux:
                chromeDriveLoc += "chromedriverLinux64";
                break;
            case Other:
                break;
        }
        System.setProperty("webdriver.chrome.driver", chromeDriveLoc);
        //Common Resource
//        htmlManager = new HTMLManager();
        //Create Tab for each Language
        tabList = preferenceSettings.tabCreator();

        //Extract all and put in to jTabbedPane
        setupTabListComponents();

        //ForecastDataManagerKey = As English ForecastDataManager
        forecastDataManagerKey = tabList.stream()
                .filter(item -> item.getTabName().equalsIgnoreCase("English")).findFirst()
                .get().getResourceManager().getForecastDataManager();
    }

    //Generate Newsletter
    private Map<String, String> hashingParam(String parameters) {
        Elements elements = Jsoup.parse(parameters, "", Parser.xmlParser()).select("*");
        Map<String, String> paramHash = new HashMap();

        elements.remove(0);//remove #root tag which we don't need
        for (Element element : elements) {
            paramHash.put(element.tagName(), element.text());
        }
        return paramHash;
    }

    private String changeStyleAttr(String originalAttr, String changeKey, String changeValue) {

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

    private void generateForecastTableNSet(LayoutTableItem item, ForecastSection fSection, HTMLManager htmlManager) {
        //parameter Hasing
        String parameters = item.getParametersNeeded();
        Map<String, String> paramHashed = hashingParam(parameters);
        
        String blockGenStr = paramHashed.get("blockGeneration");
        if(blockGenStr != null && blockGenStr.equalsIgnoreCase("true")){
            System.out.println("blockGeneration param True, this won't be generated");
            return;
        }
        
        //Location List: name = ForecastTableTitle ForecastTableContent
        ResourceHTML forecastTableHTML
                = new ResourceHTML(htmlManager.getForecastTableHTML());

        //If User input some text ( including tags)
        if (item.checkTitleHasText()) {
            Document titleHtml = item.getTitleDocument().clone();
            forecastTableHTML.insertContent(titleHtml, "div[name=ForecastTableTitle]", HTML);
        } else {// if It doesn't has Any Text 
            Element titleHtml = forecastTableHTML.extractContent("div[name=ForecastTableTitle]");
            item.setTitle(titleHtml.child(0).toString());
            System.out.println(item.getTitle());
        }
        
        //UrlTagString
        String urlTagStr = UrlTagger.tag(dateUrl, item.getType());
        
        //Input the content 
        boolean first = true;
        List<LinkMap> reverseLinkMap = fSection.getForecastLinkMapList();
        Collections.reverse(reverseLinkMap);
        for (LinkMap linkMap : reverseLinkMap) {
            ResourceHTML li = new ResourceHTML(htmlManager.getForecastTableLiHTML());
            li.insertContent((linkMap.linkedTitle), "a", HTML);
            li.insertAttrContent(linkMap.link + urlTagStr, "a", "href");
            li.insertContent((linkMap.unlinkedTitle), "span[name=unlinkedTitle]", HTML);
            System.out.println(li.getDoc().toString());
            if (first) {
                forecastTableHTML.insertContent(li.getDoc(), "ul[name=ForecastTableContent]", HTML);
                first = false;
            } else {
                forecastTableHTML.insertContent(li.getDoc(), "ul[name=ForecastTableContent]", PREPPEND);
            }
        }
        

        //set to the preview
        item.setPreview(forecastTableHTML.getDoc());
    }

    private void generateButtonNSet(LayoutTableItem item, HTMLManager htmlManager) {
        //Location List: name = ForecastTableTitle ForecastTableContent
        ResourceHTML buttonHtml = new ResourceHTML(htmlManager.getButtonHTML());
        //parameter Hasing
        String parameters = item.getParametersNeeded();
        Map<String, String> paramHashed = hashingParam(parameters);

        String buttonTitle = paramHashed.get("buttonTitle");
        String buttonLink = paramHashed.get("buttonLink");
        String backgroundColor = paramHashed.get("background-color");
        
        //Integer Get Current Story Number
        String numberStr = paramHashed.get("number");
        Integer number = 0;
        if (numberStr == null || numberStr.isEmpty()) {
            System.out.println("Parameter Doens't exist Error in numbering Button, it will be set to 0");
        } else {
            Scanner scanner = new Scanner(numberStr);
            if (scanner.hasNextInt()) {
                number = scanner.nextInt();
            }
        }
        //UrlTagString
        String urlTagStr = UrlTagger.tag(dateUrl, item.getType() , number);
        
        //Apply parameters
        if (buttonTitle != null && buttonTitle.isEmpty() == false) {
            buttonHtml.insertContent(buttonTitle, "a", HTML);//between tag <a></a>
            buttonHtml.insertAttrContent(buttonTitle, "a", "title");
        }

        if (buttonLink != null && buttonLink.isEmpty() == false) {
            buttonHtml.insertAttrContent(buttonLink + urlTagStr, "a", "href");
        }

        
        
        if (backgroundColor != null && backgroundColor.isEmpty() == false) {
            String styleAttr = buttonHtml.getDoc().select("table[name=mainBody]").attr("style");
            String changedAttr = changeStyleAttr(styleAttr, "background-color", backgroundColor);
            buttonHtml.insertAttrContent(changedAttr, "table[name=mainBody]", "style");
        }
        
        
        if(item.getTitleDoc() != null)
            item.setTitle(buttonHtml.getDoc());
        
        item.setPreview(buttonHtml.getDoc());

    }

    private void generateImport(LayoutTableItem item, ResourceManager resourceManager) {
        System.out.println("Generating A Import");
        //Html form
        HTMLManager htmlManager = resourceManager.getHtmlManager();

        ResourceHTML importHtml = new ResourceHTML(htmlManager.getImportHTML());

        //parameter Hasing
        String parameters = item.getParametersNeeded();
        Map<String, String> paramHashed = hashingParam(parameters);
        
        String blockGenStr = paramHashed.get("blockGeneration");
        if(blockGenStr != null && blockGenStr.equalsIgnoreCase("true")){
            System.out.println("blockGeneration param True, this won't be generated");
            return;
        }
        //Integer Get Current Story Number
        String numberStr = paramHashed.get("number");
        Integer number = 0;
        if (numberStr == null || numberStr.isEmpty()) {
            System.out.println("Parameter Doens't exist Error in numbering Import, it will be set to 0");
        } else {
            Scanner scanner = new Scanner(numberStr);
            if (scanner.hasNextInt()) {
                number = scanner.nextInt();
            }
        }
        //UrlTagString
        String urlTagStr = UrlTagger.tag(dateUrl, item.getType() , number);
        
        //Perapare Categories To Exclude Filtering
        String catToExclude = paramHashed.get("categoriesToExclude");
        String[] categoryList = catToExclude.split(";");
        List<String> catListToExclude = new ArrayList();
        for (String cat : categoryList) {
            if (cat.contains("###")) {
                cat = cat.replace("###", "");
                List<String> excludeFile = resourceManager.getCategoryExcludeList(cat);
                if (excludeFile.size() > 0) {
                    catListToExclude.addAll(excludeFile);
                }
            } else if (cat.equals("")) {
                break;
            } else {
                catListToExclude.add(cat);
            }

        }

        //Prepare file Name (resource Name)
        String resourceName = paramHashed.get("resourceName");
        if (resourceName==null || resourceName.isEmpty()) {
            System.out.println("Please Insert resource Name fo this Row: " + item.getPosition());
            return;
        }

        //Prepare border length
        String border = paramHashed.get("border");
        if (border != null && border.isEmpty() == false) {
            importHtml.insertAttrContent(border, "table[name=mainBody]", "border");
        }

        //prepare backgroundColor
        String backgroundColor = paramHashed.get("background-color");
        if (backgroundColor != null && backgroundColor.isEmpty() == false) {
            String styleAttr = importHtml.getDoc().select("table[name=mainBody]").attr("style");
            String changedAttr = changeStyleAttr(styleAttr, "background-color", backgroundColor);
            importHtml.insertAttrContent(changedAttr, "table[name=mainBody]", "style");
        }

        //Generate LinkMapList From Resource Manager
        List<LinkMapImport> linkMapList = resourceManager.readWpExportFixed(resourceName, catListToExclude);
        
        
        //Get Sorting Policy
        String sortByAvgStr = paramHashed.get("sortByAvg");
        boolean sortByAvg = false;
        //If User had entered something
        if(sortByAvgStr != null){
            if(sortByAvgStr.equalsIgnoreCase("true"))
                sortByAvg = true;
        }
        
        //Sort it according to Avg
        if(sortByAvg){
            linkMapList = linkMapList.stream()
                    .sorted((v1, v2) -> Double.compare(v2.iKnowFirstAvg, v1.iKnowFirstAvg))
                    .collect(Collectors.toList());
        }else{//sort From New to old
            Collections.reverse(linkMapList);
        }
//        System.out.println("List reodered according to I know firs Avg");
//        linkMapList.forEach(a-> System.out.println(a.iKnowFirstAvg));

        //Limit Number of List
        String limitNumberStr = paramHashed.get("limitNumber");

        if (limitNumberStr != null && (limitNumberStr.isEmpty() == false)) {
            int limitNumber = Integer.parseInt(limitNumberStr);
            if (linkMapList.size() > limitNumber) {
                linkMapList = linkMapList.subList(0, limitNumber);
//                int deleteNumber = linkMapList.size() - limitNumber;
//                for (int i = 1; i <= deleteNumber; i++) {
//                    linkMapList.remove(linkMapList.size() - i);
//                }
            }
        }

        //If User input some text ( including tags)
        if (item.checkTitleHasText()) {
            Document titleHtml = item.getTitleDocument().clone();
            importHtml.insertContent(titleHtml, "div[name=title]", HTML);
        } else {//if nothing insereted use default Text & extract it and show t
            Element titleHtml = importHtml.extractContent("div[name=title]");
            item.setTitle(titleHtml.child(0).toString());
            System.out.println(item.getTitle());
        }

        //Input the content 
        boolean first = true;
        //Collections.reverse(linkMapList);
        for (LinkMap linkMap : linkMapList) {
            ResourceHTML li = new ResourceHTML(htmlManager.getImportListItemHTML());
            li.insertContent((linkMap.linkedTitle), "a", HTML);
            li.insertAttrContent(linkMap.link+ urlTagStr, "a", "href");
            li.insertContent((linkMap.unlinkedTitle), "span[name=unlinkedTitle]", HTML);
            //System.out.println(li.getDoc().toString());
            if (first) {
                importHtml.insertContent(li.getDoc(), "ul[name=ContentList]", HTML);
                first = false;
            } else {
                importHtml.insertContent(li.getDoc(), "ul[name=ContentList]", APPEND);
            }
        }

        //set to the preview
        item.setPreview(importHtml.getDoc());

    }

    private void generateCustom(LayoutTableItem item, ResourceManager resourceManager) {
        //Html form
        HTMLManager htmlManager = resourceManager.getHtmlManager();

        ResourceHTML customHtml = new ResourceHTML(htmlManager.getCustomHTML());
        Document finalOutput =null;
        //parameter Hasing
        String parameters = item.getParametersNeeded();
        Map<String, String> paramHashed = hashingParam(parameters);

//        //Set Title from Param
//        String title = paramHashed.get("title");
//        if (title != null & title.isEmpty() == false) {
//            customHtml.insertContent(title, "span[id=customTitle]", HTML);
//        }
//        //Set Text from Param
//        String content = paramHashed.get("text");
//        if (content != null & content.isEmpty() == false) {
//            customHtml.insertContent(content, "span[id=customContent]", HTML);
//        }
        //Check if Title(Content for Custom) Exist
        if(item.getTitleDoc()== null){
            //fill with the EMPTY DOC
            item.setTitle(new Document(""));
        }
        //If It is Emtpy Make new
        if(item.getTitleDoc().text().isEmpty()){
            //Prepare border
            String border = paramHashed.get("border");
            if (border != null && border.isEmpty() == false) {
                customHtml.insertAttrContent(border, "table[name=mainBody]", "border");
            }

            //prepare backgroundColor
            String backgroundColor = paramHashed.get("background-color");
            if (backgroundColor != null && backgroundColor.isEmpty() == false) {
                String styleAttr = customHtml.getDoc().select("table[name=mainBody]").attr("style");
                String changedAttr = changeStyleAttr(styleAttr, "background-color", backgroundColor);
                customHtml.insertAttrContent(changedAttr, "table[name=mainBody]", "style");
            }
            finalOutput = customHtml.getDoc();
        }else{//If there is something Just change border/BG Color
            Document updatedDoc = item.getTitleDoc();
            //Update border
            String borderVal = paramHashed.get("border");
            if (borderVal != null && borderVal.isEmpty() == false) {
                updatedDoc.select("table[name=mainBody]").attr("border", borderVal);
                //customHtml.insertAttrContent(border, "table[name=mainBody]", "border");
            }

            //prepare backgroundColor
            String backgroundColor = paramHashed.get("background-color");
            if (backgroundColor != null && backgroundColor.isEmpty() == false) {
                String styleAttr = customHtml.getDoc().select("table[name=mainBody]").attr("style");
                String changedAttr = changeStyleAttr(styleAttr, "background-color", backgroundColor);
                //customHtml.insertAttrContent(changedAttr, "table[name=mainBody]", "style");
                updatedDoc.select("table[name=mainBody]").attr("style", changedAttr);
            }
            finalOutput = updatedDoc;
        }
        
        
        //Set Title
        item.setTitle(finalOutput);
        //set to the preview
        item.setPreview(finalOutput);

    }

    private int generateMinistory(LayoutTableItem item, ForecastSection fSection, ResourceManager resourceManager) {
        //Used Form Number
        int usedFormNumb = -1;
        
        //Html Form
        HTMLManager miniHtmlManager = resourceManager.getHtmlManager();
        ResourceHTML miniHtml = new ResourceHTML(miniHtmlManager.getMiniHTML());

        //Ministory Data
        Map<String, List<ForecastTableItem>> miniStoryDataObjMap = fSection.getMiniStoryDataObjMap();
        //Minsotry FormManager
        MinistoryFormManager miniFormManager = resourceManager.getMiniFormManager();

        //List of ForecastTableItem for this minitory
        List<ForecastTableItem> itemList;

        //Parameter Hashing
        String parameters = item.getParametersNeeded();
        Map<String, String> paramHashed = hashingParam(parameters);
        
        String blockGenStr = paramHashed.get("blockGeneration");
        if(blockGenStr != null && blockGenStr.equalsIgnoreCase("true")){
            System.out.println("blockGeneration param True, this won't be generated");
            return -1;
        }
        
        //Get Current Ministory Item timeframe
        String timeFrame = paramHashed.get("timeFrame");
        if (timeFrame == null || timeFrame.isEmpty()) {//Error and Return if empty
            System.out.println("You forgot to put timeFrame for Ministory Item Position: " + item.getPosition());
            return -1;
        } else {
            itemList = miniStoryDataObjMap.get(timeFrame);
        }

        //Integer Get Current Story Number
        String numberStr = paramHashed.get("number");
        Integer number = 0;
        if (numberStr == null || numberStr.isEmpty()) {
            System.out.println("Parameter Doens't exist Error in numbering ministory, it will be set to 0");
        } else {
            Scanner scanner = new Scanner(numberStr);
            if (scanner.hasNextInt()) {
                number = scanner.nextInt();
            }
        }

        //Check If that the Item for this TImeFrame Exist
        if (itemList == null || itemList.isEmpty()) { //Doen'st exist
            System.out.println("The Timeframe you have entered doesn't exist in the forecast Link you have entered " + item.getPosition());
            return -1;
        }
        //UrlTagString
        String urlTagStr = UrlTagger.tag(dateUrl, item.getType() , number);
        
        //Exist
        MinistoryFormItem dressForm = miniFormManager.dressItemWithForm(number, timeFrame, itemList, urlTagStr);
        
        if(dressForm == null){
            System.out.println("Error No MinistoryForm for this timeFrame: "+timeFrame);
            return -1;
        }
        //Keep record of the id(number) that is was used
        usedFormNumb = dressForm.getNumber();

        //Insert Title
        miniHtml.insertContent(dressForm.getCodeTitle(), "strong[id=miniTitle]", HTML);

        //Insert Data
        miniHtml.insertContent(dressForm.getText(), "span[id=miniContent]", HTML);

        //Set to the preview
        item.setPreview(miniHtml.getDoc());
        
        return usedFormNumb;
    }

//    private List<LayoutTableItem> ministoryNumbering(List<LayoutTableItem> layoutTableItemList) {
//
//        //Ministory Numbering
//        ministoryNumber = 1;
//
//        layoutTableItemList.stream().forEach(item -> {
//            System.out.println(item.getParametersNeeded());
//            if (item.getType().equalsIgnoreCase("miniStory")) {
//                String curParameters = item.getParametersNeeded();
//                String miniNumberingParam = "<number>" + ministoryNumber + "</number>";
//                if (curParameters.contains("<number></number>")) {
//                    curParameters = curParameters.replace("<number></number>", miniNumberingParam);
//                } else if (curParameters.contains("<number>")) {//has something between <number>CCCC</number>
//                    curParameters = curParameters.replaceAll("<number>.</number>", miniNumberingParam);
//                } else {
//                    curParameters += "\n" + miniNumberingParam;
//                }
//                item.setParametersNeeded(curParameters);
//
//                ministoryNumber++;
//            }
//        });
//
//        return layoutTableItemList;
//    }
    private String numberingParameter(String curParameters, int number){
        
        String miniNumberingParam = "<number>" + number + "</number>";
        if (curParameters.contains("<number></number>")) {
            curParameters = curParameters.replace("<number></number>", miniNumberingParam);
        } else if (curParameters.contains("<number>")) {//has something between <number>CCCC</number>
            curParameters = curParameters.replaceAll("<number>.</number>", miniNumberingParam);
        } else {
            curParameters += "\n" + miniNumberingParam;
        }
        
        return curParameters;
    }
    private List<LayoutTableItem> numberingEachType(List<LayoutTableItem> layoutTableItemList) {
        ministoryOrder = 1;
        buttonOrder = 1;
        wpImportOrder = 1;
        customOrder = 1;
        
        layoutTableItemList.stream().forEach(item -> {
            System.out.println(item.getParametersNeeded());
            String curParameters = item.getParametersNeeded();
            String changedParameters = "";
            
            switch (item.getType()){
                case "miniStory":
                    changedParameters = numberingParameter(curParameters,ministoryOrder);
                    item.setParametersNeeded(changedParameters);

                    ministoryOrder++;
                    break;
                case "button":
                    changedParameters = numberingParameter(curParameters,buttonOrder);
                    item.setParametersNeeded(changedParameters);

                    buttonOrder++;
                    break;
                case "import":
                    changedParameters = numberingParameter(curParameters,wpImportOrder);
                    item.setParametersNeeded(changedParameters);

                    wpImportOrder++;
                    break;
                case "custom":
                    changedParameters = numberingParameter(curParameters,customOrder);
                    item.setParametersNeeded(changedParameters);

                    customOrder++;
                    break;
                default:
                    break;
            }
        });

        return layoutTableItemList;
    }


    public LayoutTableItem  generateNewsletter(Tab tab, int[] selectedRows, Date date) {
        this.dateUrl = date;
        //Prepare Resources
        //Forecast
        HTMLManager htmlManager = tab.getResourceManager().getHtmlManager();
        ForecastSection fSection = tab.getForecastSection();
        fSection.prepareDataForNewsletterGeneration();

        //Layout
        LayoutSection lSection = tab.getLayoutSection();
        LayoutTableModel ltModel = lSection.getLayoutTableModel();
        List<LayoutTableItem> layoutTableItemList = ltModel.getLayoutTableItemList();

        //Numbering
        layoutTableItemList = numberingEachType(layoutTableItemList);

        //Used Ministory Form List to keep record
        List<Integer> usedMiniFormList = new ArrayList();
        

        for (int i = 0; i < selectedRows.length; i++) {
            LayoutTableItem item = layoutTableItemList.get(selectedRows[i]);
            switch (item.getType()) {
                case "forecastTable":
                    generateForecastTableNSet(item, fSection, htmlManager);
                    item.setReady(!item.getPreview().isEmpty());
                    break;
                case "miniStory":
                    int usedMiniFormNumb = generateMinistory(item, fSection, tab.getResourceManager());
                    usedMiniFormList.add(usedMiniFormNumb);
                    item.setReady(usedMiniFormNumb > 0);
                    break;
                case "button":
                    generateButtonNSet(item, htmlManager);
                    item.setReady(!item.getPreview().isEmpty());
                    break;
                case "import":
                    generateImport(item, tab.getResourceManager());
                    item.setReady(!item.getPreview().isEmpty());
                    break;
                case "custom":
                    generateCustom(item, tab.getResourceManager());
                    item.setReady(!item.getPreview().isEmpty());
                    break;
                default:

            }
            
        }
        //Update Things
        ltModel.fireTableDataChanged();
        //Update UsedMiniForm Number List
        tab.setUsedMiniFormList(usedMiniFormList);
        
        //selecte row and return first generated row(Item)
        if(selectedRows != null && selectedRows.length > 0){
            lSection.selectRow(selectedRows[0]);
            return lSection.getLayoutTableModel().getItemAt(selectedRows[0]);
        }else{// not possible but if nothing is been generated
            return null;
        }
    }
    public void previewNewsletter(Tab tab, Date date){
        dateUrl = (date==null) ? new Date() : date;
        System.out.println("Previewing Newsletter : " + tab.getPeferenceSetting().getLanguageName());
        //if (tab.getGenerateThis()) {
        HTMLManager htmlManager = tab.getResourceManager().getHtmlManager();
        LayoutSection lSection = tab.getLayoutSection();
        LayoutTableModel ltModel = lSection.getLayoutTableModel();
        List<LayoutTableItem> layoutTableItemList = ltModel.getLayoutTableItemList();

        //Numbering Again in case User had change order put not Generated
        layoutTableItemList = numberingEachType(layoutTableItemList);
        //Update links with the re-Numbered system and save as CSV
        layoutTableItemList = updateLinks(layoutTableItemList,tab.getTabName());

        //Check if Nothing is red
        long notReadyNumber = layoutTableItemList.stream().filter(item->!item.isReady()).count();
        if(notReadyNumber>0)
            JOptionPane.showMessageDialog(null, "Warning: Some of the Row Might not have been (re)Generated after a change");

        //Get Base HTML
        ResourceHTML baseHtml = new ResourceHTML(htmlManager.getBasedHTML());
        //Set Date with Native Date Format
        baseHtml.insertContent(tab.getPeferenceSetting().convertDateToNative(date), "span[id=newsLetterDate]", HTML);

        //Add Layout Items
        for (LayoutTableItem row : layoutTableItemList) {
            if (row.getPreview().isEmpty()) {
                continue;
            }
            //baseHtml.insertContent(row.getPreview(), "td[class=headerContainer]", APPEND);
            Document convertedPreview = ResourceHTML.convertPtToPxDoc(row.getPreviewDoc());
            baseHtml.insertContent(convertedPreview, "td[class=bodyContainer]", APPEND);
        }
        //Output File Location and File Name
        String workingDir = System.getProperty("user.dir");
        String outputFile = workingDir+"/temp/previewNewsletter.html";

        //Temporarely Saving File to view it
        HTMLManager.saveHTML(baseHtml.getDoc(), outputFile);
        
        //open with with with with with with with chrome
        WebDriver driver = new ChromeDriver();
        Wait<WebDriver> wait = new WebDriverWait(driver, 30);
        
        driver.get("file://"+outputFile);
    }

    public void saveNewsletter(Tab tab, Date date) {
        dateUrl = (date==null) ? new Date() : date;
        System.out.println("Saving Newsletter : " + tab.getPeferenceSetting().getLanguageName());
        //if (tab.getGenerateThis()) {
        HTMLManager htmlManager = tab.getResourceManager().getHtmlManager();
        LayoutSection lSection = tab.getLayoutSection();
        LayoutTableModel ltModel = lSection.getLayoutTableModel();
        List<LayoutTableItem> layoutTableItemList = ltModel.getLayoutTableItemList();

        //Numbering Again in case User had change order put not Generated
        layoutTableItemList = numberingEachType(layoutTableItemList);
        //Update links with the re-Numbered system and save as CSV
        layoutTableItemList = updateLinks(layoutTableItemList,tab.getTabName());

        //Check if Nothing is red
        long notReadyNumber = layoutTableItemList.stream().filter(item->!item.isReady()).count();
        if(notReadyNumber>0)
            JOptionPane.showMessageDialog(null, "Warning: Some of the Row Might not have been (re)Generated after a change");

        //Get Base HTML
        ResourceHTML baseHtml = new ResourceHTML(htmlManager.getBasedHTML());
        //Set Date with Native Date Format
        baseHtml.insertContent(tab.getPeferenceSetting().convertDateToNative(date), "span[id=newsLetterDate]", HTML);

        //Add Layout Items
        for (LayoutTableItem row : layoutTableItemList) {
            if (row.getPreview().isEmpty()) {
                continue;
            }
            //baseHtml.insertContent(row.getPreview(), "td[class=headerContainer]", APPEND);
            Document convertedPreview = ResourceHTML.convertPtToPxDoc(row.getPreviewDoc());
            baseHtml.insertContent(convertedPreview, "td[class=bodyContainer]", APPEND);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMMMdd'T'HH-mm-ss");
        String todaysDate = dateFormat.format(new Date());
        //Output File Location and File Name
        String outputFile = tab.getPeferenceSetting().getSaveDir() + "output"+todaysDate+".html";

        //Saving File
        HTMLManager.saveHTML(baseHtml.getDoc(), outputFile);

        //Post Process for Ministory
        tab.updateUsedMinistoryFormDate();

        System.out.println(tab.getTabName()+") Done Generating File : "+outputFile);
        JOptionPane.showMessageDialog(null, tab.getTabName()+") Done Generating File : "+outputFile);
        
        
    }

    //Forecast
    //This is triggered when user insert Links in textArea (Anywhere Forecast Section)
    public void loadForecastDataForEachLanguage(List<String> linkList) {//Load t
        // Get key from Forecast DB
        List<ForecastTableItem> keyItemList = new ArrayList();
        for (String link : linkList) {
            keyItemList.add(forecastDataManagerKey.findForecastItem(LINK, link));
        }
        List<String> csvPathLinkList = keyItemList.stream().map(ForecastTableItem::getCsvFilePath)
                .collect(Collectors.toList());
        //ftItemList.stream().forEach(e -> System.out.println(e.getTitle()));

        //Load data from other Language Database based on csvpath from other
        for (Tab tab : tabList) {
            ForecastDataManager fdMan = tab.getResourceManager().getForecastDataManager();
            ForecastTableModel ftModel = tab.getForecastSection().getForecastTableModel();

            List<ForecastTableItem> itemList = csvPathLinkList
                    .stream()
                    //.forEach(a -> fdMan.findForecastItem(CSVFILEPATH, a))
                    .map(a -> fdMan.findForecastItem(CSVFILEPATH, a))
                    .collect(Collectors.toList());
            itemList.forEach(a -> System.out.println(a.getCsvFilePath()));

            itemList.stream().forEach(a -> ftModel.addRow(a));
        }

    }

    public void loadForecastDatabase(List<String> linkList) {//Load t
        // Get key from Forecast DB
        List<ForecastTableItem> keyItemList = new ArrayList();
        for (String link : linkList) {
            keyItemList.add(forecastDataManagerKey.findForecastItem(LINK, link));
        }
        List<String> csvPathLinkList = keyItemList.stream().map(ForecastTableItem::getCsvFilePath)
                .collect(Collectors.toList());
        //ftItemList.stream().forEach(e -> System.out.println(e.getTitle()));

        //Load data from other Language Database based on csvpath from other
        for (Tab tab : tabList) {
            //Forecast DB
            ForecastDataManager fdMan = tab.getResourceManager().getForecastDataManager();

            //Forecast Table(Same variable as ui)
            ForecastTableModel ftModel = tab.getForecastSection().getForecastTableModel();
            ftModel.clearRows();
            
            //Get Row from DB using CSV Link Path as Key
            List<ForecastTableItem> itemList = csvPathLinkList
                    .stream()
                    //.forEach(a -> fdMan.findForecastItem(CSVFILEPATH, a))
                    .map(a -> fdMan.findForecastItem(CSVFILEPATH, a))
                    .collect(Collectors.toList());
            //print
            itemList.forEach(a -> System.out.println(a.getCsvFilePath()));

            //Set Load Row into Forecast Table
            itemList.stream().forEach(a -> {
                //Add Only Non Empty One
                if (a.getTitle().isEmpty() == false) {
                    ftModel.addRow(a);
                }
            });
        }

    }

    //Layout
    public LayoutTableItem addLayoutItem(LayoutTableItem item) {
        Tab tab = getCurrentTab();
        LayoutSection layoutSection = tab.getLayoutSection();

        LayoutTableItem addedItem = layoutSection.getLayoutTableModel().addRow(item);
        layoutSection.selectRow(addedItem.getPosition());
        return addedItem;
    }

    public void deleteSelectedLayoutItem() {
        Tab tab = getCurrentTab();
        LayoutSection layoutSection = tab.getLayoutSection();
        int[] selectedRows = layoutSection.getJTable().getSelectedRows();
        Arrays.sort(selectedRows);
        for (int i = selectedRows.length - 1; i >= 0; i--) {
            layoutSection.getLayoutTableModel().deleteRow(selectedRows[i]);
        }
    }

    public boolean changeLayoutItem(LayoutTableItem item) {
        Tab tab = getCurrentTab();
        LayoutSection layoutSection = tab.getLayoutSection();
        int[] selectedRows = layoutSection.getJTable().getSelectedRows();
        if (selectedRows.length != 1) {
            System.out.println("Please select one Row to Edit");
            return false;
        }
        //update Row
        int updatePosition = layoutSection.getLayoutTableModel().updateRow(selectedRows[0], item);
        if (updatePosition != -1) { // Success
            //Select again
            layoutSection.getJTable().getSelectionModel().setSelectionInterval(updatePosition, updatePosition);
            //update the form
            FormManaqer.layoutFormUpdate(layoutSection.getLayoutTableModel().getItemAt(updatePosition));
            
        } else { //Failed go back
            //layoutSection.getJTable().getSelectionModel().setSelectionInterval(updatePosition, updatePosition);
            //FormManaqer.layoutFormUpdate(layoutSection.getLayoutTableModel().getItemAt(updatePosition));
            FormManaqer.restoreFormPositionNumberTo(selectedRows[0]);
        }
        return true;
    }

    //Setup
    private void setupTabListComponents() {
        jTabbedPane.remove(0);
        //Add
        for (Tab tab : tabList) {
            jTabbedPane.add(tab.getTabName(), tab.getjPanelCards());
        }

        jTabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPaneStateChanged(evt);
            }
        });
    }

    public JTabbedPane getjTabbedPane() {
        return jTabbedPane;
    }

    //Common func
    public Tab getCurrentTab() {
        currentTabNumber = jTabbedPane.getSelectedIndex();
        return tabList.get(currentTabNumber);
    }

    public Section getCurrentSection(Tab tab) {
        if (tab.getCurrentSectionNumber() == FORECASTSECTION) {
            return tab.getForecastSection();
        } else {//LAYOUTSECTION
            return tab.getLayoutSection();
        }
    }

    public void switchSectionTo(int changeTo) {
        Tab tab = getCurrentTab();
        tab.switchSectionTo(changeTo);
    }

    //When User change Tab
    private void jTabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {

        Tab curTab = getCurrentTab();

        //Get Section and Change the form Section
        int section = curTab.getCurrentSectionNumber();
        ui.FormManaqer.switchFormSectionTo(section);

        //Update Form for Forecast
        boolean generateThis = curTab.getGenerateThis();
        //forecastFormUpdate(generateThis);

        //Update Form for Layout
        LayoutSection ls = curTab.getLayoutSection();
        int lsRow = ls.getJTable().getSelectedRow();
        if (lsRow >= 0) {
            setEnabledLayoutForm(true);
            LayoutTableItem ltItem = ls.getLayoutTableModel().getItemAt(lsRow);
            layoutFormUpdate(ltItem);
        } else {
            setEnabledLayoutForm(false);
        }

    }

    private List<LayoutTableItem> updateLinks(List<LayoutTableItem> layoutTableItemList, String langName) {
        List<TitleTag> titleTagList = new ArrayList();
        
        Document previewDoc= null;
        Elements allLinks=null; 
        for(LayoutTableItem item : layoutTableItemList){
            String urlTagStr ="";
            String title = item.getTitleDoc().text().trim();
            if(title.length()>50)
                title = title.substring(0, 49);
            
            //parameter Hasing
            String parameters = item.getParametersNeeded();
            Map<String, String> paramHashed = hashingParam(parameters);

            switch(item.getType()){
                case "forecastTable":
                    // Forecast Table
                    //UrlTagString
                    urlTagStr = UrlTagger.tag(dateUrl, item.getType());

                    previewDoc = item.getPreviewDoc();
                    allLinks = previewDoc.select("a");

                    for(int linkIndex = 0; linkIndex < allLinks.size(); linkIndex++){
                        Element a = allLinks.get(linkIndex);
                        String linkStr = a.attr("href");
                        if(linkStr.contains("?utm_source")){
                            //update link
                            int replacePoint = linkStr.indexOf("?utm_source");
                            linkStr = linkStr.substring(0, replacePoint);
                            linkStr = linkStr + urlTagStr;
                        }else{
                            //add behind if it doesn't exist
                            linkStr = linkStr + urlTagStr;
                        }
                        a.attr("href", linkStr);

                    }
                    break;
                case "miniStory":
                    String timeFrame = paramHashed.get("timeFrame");
                    title = timeFrame==null ? "timeFrameNULL" : timeFrame;
//                case "button":
//                case "import":
//                case "custom":
                default:
                    //Integer Get Current Story Number
                    String numberStr = paramHashed.get("number");
                    Integer number = 0;
                    if (numberStr == null || numberStr.isEmpty()) {
                        System.out.println("Parameter Doens't exist Error in numbering ministory, it will be set to 0");
                    } else {
                        Scanner scanner = new Scanner(numberStr);
                        if (scanner.hasNextInt()) {
                            number = scanner.nextInt();
                        }
                    }
                    //UrlTagString
                    urlTagStr = UrlTagger.tag(dateUrl, item.getType() , number);

                    previewDoc = item.getPreviewDoc();
                    allLinks = previewDoc.select("a");

                    for(int linkIndex = 0; linkIndex < allLinks.size(); linkIndex++){
                        Element a = allLinks.get(linkIndex);
                        String linkStr = a.attr("href");
                        if(linkStr.contains("?utm_source")){
                            //update link
                            int replacePoint = linkStr.indexOf("?utm_source");
                            linkStr = linkStr.substring(0, replacePoint);
                            linkStr = linkStr + urlTagStr;
                        }else{
                            //add behind if it doesn't exist
                            linkStr = linkStr + urlTagStr;
                        }
                        a.attr("href", linkStr);

                    }
            }
            
            //add to the List
            titleTagList.add(new TitleTag(title,urlTagStr.substring(12)));
            
        }
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMMMdd'T'HH-mm-ss");
        String dateStr = dateFormat.format(dateUrl);
        //Logging title - Tag pair as CSV
        Boolean success = TitleTag.writeAsCsvTitleTagList("output/"+langName+"/urlTag/"+dateStr+".csv", titleTagList);
        if(!success)
            System.out.println("There was error Saving CSV Title/tab ");
        else
            System.out.println("CSV Title/Tab successfully saved");
        
        return layoutTableItemList;
    }

}


