package ui;

import java.awt.CardLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import javax.swing.JFileChooser;
import resources.ResourceManager;
import tab.Tab;
import tab.layout.LayoutSection;
import tab.layout.LayoutTableItem;
import static tab.layout.LayoutTableItem.POSITION;
import static tab.layout.LayoutTableItem.PREVIEW;
import static tab.layout.LayoutTableItem.TITLE;
import tab.layout.LayoutTableModel;
import test.TinyMce;
import static ui.NewsLetterUI.*;

public class FormManaqer {

    public static final int FORECASTSECTION = 0;
    public static final int LAYOUTSECTION = 1;
    public static final String[] cardName = {"card2", "card3"};
    public static String[] layoutTypeList = LayoutSection.layoutTypeList;

    public static boolean editAllow = true;

    //GenerateNewsletter(Preview)
    public static void generateNewsletter() {
        Tab curTab = tabManager.getCurrentTab();
        int[] selecteRows = curTab.getLayoutSection().getJTable().getSelectedRows();
        int totalRowCount = curTab.getLayoutSection().getJTable().getRowCount();
        if (selecteRows.length > 0) {// if something has been selected, it will generate specific Item(s)
            //Generate & Return the first Item in selected Item List
            LayoutTableItem firstItem = tabManager.generateNewsletter(curTab, selecteRows, jXDatePicker.getDate());
            
            //Selected the first one
            setEnabledLayoutForm(true);
            layoutFormUpdate(firstItem);
            
        } else if (selecteRows.length == 0) {// if nothing is selected, it will generate all of them
            //Generate Everything
            int[] allRows = new int[totalRowCount];
            IntStream.range(0, totalRowCount).forEach(val -> allRows[val] = val);
            LayoutTableItem firstItem = tabManager.generateNewsletter(curTab, allRows, jXDatePicker.getDate());
            
            //Selected the first one
            setEnabledLayoutForm(true);
            layoutFormUpdate(firstItem);
        } 
    }

    //Save Newsletter
    public static void saveNewsletter() {
        Tab curTab = tabManager.getCurrentTab();
        tabManager.saveNewsletter(curTab, jXDatePicker.getDate());
    }
    
    public static void previewNewsletter(){
        Tab curTab = tabManager.getCurrentTab();
        tabManager.previewNewsletter(curTab, jXDatePicker.getDate());
    }

    //Common Function For all Section
    public static void switchSectionTo(int changeTo) {
        editAllow = false;

        switchFormSectionTo(changeTo);
        switchTabSectionTo(changeTo);

        editAllow = true;
    }

    public static void switchFormSectionTo(int changeTo) {
        editAllow = false;
        CardLayout formCL = (CardLayout) jPanelCardForm.getLayout();

        if (changeTo == FORECASTSECTION) {
            formCL.show(jPanelCardForm, cardName[FORECASTSECTION]);
        } else {
            formCL.show(jPanelCardForm, cardName[LAYOUTSECTION]);
        }
        editAllow = true;
    }

    public static void switchTabSectionTo(int changeTo) {
        editAllow = false;

        if (changeTo == FORECASTSECTION) {
            tabManager.switchSectionTo(FORECASTSECTION);
        } else {
            tabManager.switchSectionTo(LAYOUTSECTION);
        }
        editAllow = true;
    }

    //Forecast Section
    public static void submitEnglishLink() {
        System.out.println("Fetching Link From Text Area");
        //Fetch Link List<String> Link
        List<String> linkList = fetchingLink(jTextAreaEnglishLink.getText(), "\n");
        tabManager.loadForecastDatabase(linkList);
        //tabManager.loadForecastDataForEachLanguage(linkList);

    }

    private static List<String> fetchingLink(String textArea, String splitBy) {
        List<String> linkList = new ArrayList();
        String[] links = textArea.split(splitBy);
        for (String link : links) {
            if (link.isEmpty() == false) {
                linkList.add(link);
            }
        }

        return linkList;
    }

    public static void forecastFormChanged() {// From button to Tab
        if (editAllow == false) {
            return;
        }
        Tab tab = tabManager.getCurrentTab();
        //tab.setGenerateThis((boolean) jCheckBoxGenerateThis.isSelected());
    }

//    public static void forecastFormUpdate(boolean generateThis) {
//        editAllow = false;
//
//        jCheckBoxGenerateThis.setSelected(generateThis);
//
//        editAllow = true;
//    }

    //Layout Section Add & Delete
    public static void addLayoutItem() {
        String selectedType = (String) jComboBoxLayoutTypeChoice.getSelectedItem();
        LayoutTableItem newItem = new LayoutTableItem(selectedType);
        
        //Add to the Layout Table and also Select it
        LayoutTableItem addedItem = tabManager.addLayoutItem(newItem);
        
        //update forma
        setEnabledLayoutForm(true);
        layoutFormUpdate(addedItem);
    }//From UI to Layout Section

    public static void selectDeselectAll() {
        Tab curTab = tabManager.getCurrentTab();
        LayoutSection ls = curTab.getLayoutSection();
        if (ls.getJTable().getSelectionModel().isSelectionEmpty()) {//Select All
            ls.getJTable().selectAll();
        } else {//Deselect All
            ls.getJTable().getSelectionModel().clearSelection();
        }
    }

    public static void deleteSelectedLayoutItem() {
        tabManager.deleteSelectedLayoutItem();
        setEnabledLayoutForm(false);
    }//From UI to Layout Section

    //Layout Section Update Form & Table
    public static void layoutFormChanged() {
        if (editAllow == false) {
            return;
        }
        LayoutTableItem changedItem = new LayoutTableItem();
        changedItem.setPosition((Integer) jSpinnerPosition.getValue());
        //changedItem.setType((String) jComboBoxLayoutTypeChoosen.getSelectedItem());
        //changedItem.setTitle(jEditorPaneLayoutTitle.getText());
        changedItem.setParametersNeeded((String) jTextAreaParameters.getText());
        //changedItem.setPreview(jEditorPaneLayoutPreview.getText());
        
        
        boolean success = tabManager.changeLayoutItem(changedItem);
        if (success == false) {
            setEnabledLayoutForm(false);
        }
    }//From UI to Layout Section

    public static void restoreFormPositionNumberTo(Integer y) {
        jSpinnerPosition.setValue((Object) y);
    }

    public static void layoutFormUpdate(LayoutTableItem item) {
        editAllow = false;

        jSpinnerPosition.setValue((Object) item.getPosition());
        jComboBoxLayoutTypeChoosen.setSelectedItem((Object) item.getType());
        jEditorPaneLayoutTitle.setText(item.getTitle());
        jTextAreaParameters.setText(item.getParametersNeeded());
        jEditorPaneLayoutPreview.setText(item.getPreview());

        editAllow = true;
    }//From Layout Section to UI Form (table -> Form)

    //Layout Save & Load
    public static void saveLoadCurrentLayout(java.awt.event.ActionEvent evt, boolean regenerateNewsletter) {
        Tab curTab = tabManager.getCurrentTab();
        LayoutTableModel ltModel = curTab.getLayoutSection().getLayoutTableModel();
        
        
        //Load Layout
        if (evt.getSource() == jButtonLayoutSectionLoadLayout || evt.getSource() == jButtonLayoutSectionLoadLayoutNPreview) {
            int returnVal = jFileChooserLayout.showOpenDialog(new javax.swing.JPanel());
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = jFileChooserLayout.getSelectedFile();
                //This is where a real application would open the file.
                int tableLen = ResourceManager.loadLayoutTable(file, ltModel);

                //Re-Generate Newsletter After Loading
                int[] allRows = new int[tableLen];
                IntStream.range(0, tableLen).forEach(val -> allRows[val] = val);
                if (regenerateNewsletter) {
                    tabManager.generateNewsletter(curTab, allRows, jXDatePicker.getDate());
                }

                System.out.println("Opening Layout: " + file.getName() + ".");
            } else {
                System.out.println("Open Laout command cancelled by user.");
            }
        } else {//Save Layout
            int returnVal = jFileChooserLayout.showSaveDialog(new javax.swing.JPanel());
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = jFileChooserLayout.getSelectedFile();
                //This is where a real application would save the file.
                ResourceManager.saveLayoutTable(file, ltModel);

                System.out.println("Saving Layout: " + file.getName() + ".");
            } else {
                System.out.println("Save Laout command cancelled by user.");
            }
        }
//        
//        try {
//            ObjectOutputStream out = new ObjectOutputStream(
//            new FileOutputStream(file));
//            out.writeObject(tableModel.getDataVector());
//            out.close();
//        }
//        catch (Exception ex) {
//            ex.printStackTrace();
//        }

    }

    //Title HTML Editor
    public static void openTitleHtmlEditor() {
        //Get current HTML 
        Tab curTab = tabManager.getCurrentTab();
        LayoutSection ls = curTab.getLayoutSection();
        int selectedRow = ls.getJTable().getSelectedRow();
        if (selectedRow < 0) {
            System.out.println("Select a Row");
            return;
        }
        LayoutTableItem lsItem = ls.getLayoutTableModel().getItemAt(selectedRow);
        String html = lsItem.getTitle();

        //Open with html editor
        titleHtmlEditor.open(html);
    }

    public static void closeTitleHtmlEditor(boolean save) {
        if (save) {
            //Get current HTML 
            Tab curTab = tabManager.getCurrentTab();
            LayoutSection ls = curTab.getLayoutSection();
            int selectedRow = ls.getJTable().getSelectedRow();
            if (selectedRow < 0) {
                System.out.println("Select a Row");
                return;
            }
            LayoutTableItem lsItem = ls.getLayoutTableModel().getItemAt(selectedRow);
            String html = titleHtmlEditor.saveNClose();
            //Change
            lsItem.setTitle(html);
            
            //Change Ready Status
            lsItem.setReady(false);
            
            //Fire Change TO Model
            ls.getLayoutTableModel().fireTableCellUpdated(selectedRow, TITLE);
            ls.getLayoutTableModel().fireTableCellUpdated(selectedRow, POSITION);
            
            
            //ls.getLayoutTableModel().updateRow(selectedRow, lsItem);
        } else {
            titleHtmlEditor.discardNclose();
        }
    }

    //Preview HTML Editor
    public static void openPreviewHtmlEditor() {
        //Get current HTML 
        Tab curTab = tabManager.getCurrentTab();
        LayoutSection ls = curTab.getLayoutSection();
        int selectedRow = ls.getJTable().getSelectedRow();
        if (selectedRow < 0) {
            System.out.println("Select a Row");
            return;
        }
        LayoutTableItem lsItem = ls.getLayoutTableModel().getItemAt(selectedRow);
        String html = lsItem.getPreview();

        TinyMce mce = new TinyMce(lsItem,true);
        Thread t = new Thread(mce);
        t.start();
        
        //previewHtmlEditor.open(html);
    }
    
    public static void openPreviewHtmlEditor2() {
        //Get current HTML 
        Tab curTab = tabManager.getCurrentTab();
        LayoutSection ls = curTab.getLayoutSection();
        int selectedRow = ls.getJTable().getSelectedRow();
        if (selectedRow < 0) {
            System.out.println("Select a Row");
            return;
        }
        LayoutTableItem lsItem = ls.getLayoutTableModel().getItemAt(selectedRow);
        String html = lsItem.getPreview();

        previewHtmlEditor.open(html);
        
    }

    public static void closePreviewHtmlEditor(boolean save) {
        if (save) {
            //Get current HTML 
            Tab curTab = tabManager.getCurrentTab();
            LayoutSection ls = curTab.getLayoutSection();
            int selectedRow = ls.getJTable().getSelectedRow();
            if (selectedRow < 0) {
                System.out.println("Select a Row");
                return;
            }
            LayoutTableItem lsItem = ls.getLayoutTableModel().getItemAt(selectedRow);
            String html = previewHtmlEditor.saveNClose();
            lsItem.setPreview(html);
            
            //Change Ready Status
            lsItem.setReady(true);
            
            ls.getLayoutTableModel().fireTableCellUpdated(selectedRow, PREVIEW);
            //ls.getLayoutTableModel().updateRow(selectedRow, lsItem);
        } else {
            previewHtmlEditor.discardNclose();
        }
    }

    //Color Picker
    public static void colorChooser() {
        jDialogColorChooser.setVisible(true);
        jDialogColorChooser.setBounds(0, 400, 636, 500);
    }

    //Enable Layout Form & Buttons
    public static void setEnabledLayoutForm(boolean activate) {
        jSpinnerPosition.setEnabled(activate);
//        jComboBoxLayoutTypeChoosen.setEnabled(activate);

        jButtonEditLayoutTitle.setEnabled(activate);
//        jEditorPaneLayoutTitle.setEnabled(activate);

        jTextAreaParameters.setEnabled(activate);

        jButtonEditLayoutPreview.setEnabled(activate);
//        jEditorPaneLayoutPreview.setEditable(activate);
    }

    public static void setEnabledLayoutButtons(boolean activate) {
        jButtonLayoutSectionAdd.setEnabled(activate);
        jButtonLayoutSectionSub.setEnabled(activate);
        jButtonLayoutSectionSaveLayout.setEnabled(activate);
        jButtonLayoutSectionLoadLayout.setEnabled(activate);
        jButtonLayoutSectionGenerateNewsletter.setEnabled(activate);
        jComboBoxLayoutTypeChoice.setEnabled(activate);

    }

}
