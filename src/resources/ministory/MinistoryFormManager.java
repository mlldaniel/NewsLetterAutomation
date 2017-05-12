/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources.ministory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import tab.forecast.ForecastTableItem;

/**
 *
 * @author Daniel
 */
public class MinistoryFormManager {

    public static final int CELL_TYPE_NUMERIC = 0;
    public static final int CELL_TYPE_STRING = 1;
    public static final int CELL_TYPE_FORMULA = 2;
    public static final int CELL_TYPE_BLANK = 3;
    public static final int CELL_TYPE_BOOLEAN = 4;
    public static final int CELL_TYPE_ERROR = 5;

    List<MinistoryFormItem> miniFormList;
    String filePath;
    Map<String, MinistoryVar> varMap;

    public MinistoryFormManager(String filePath) {
        this.filePath = filePath;
        miniFormList = loadAllFromExcel(filePath);

        varMap = new HashMap();
        //no need for Time frame because there is only one
        varMap.put("title", new MinistoryVar("title"));
        varMap.put("link", new MinistoryVar("link"));
        varMap.put("positionType", new MinistoryVar("positionType"));
        varMap.put("packageName", new MinistoryVar("packageName"));
        varMap.put("subpackageName", new MinistoryVar("subpackageName"));
        varMap.put("timePeriod", new MinistoryVar("timePeriod"));
        varMap.put("forecastDate", new MinistoryVar("forecastDate"));
        varMap.put("targetDate", new MinistoryVar("targetDate"));

        varMap.put("topReturn1", new MinistoryVar("topReturn", 1));
        varMap.put("topStockName1", new MinistoryVar("topStockName", 1));
        varMap.put("topReturn2", new MinistoryVar("topReturn", 2));
        varMap.put("topStockName2", new MinistoryVar("topStockName", 2));
        varMap.put("topReturn3", new MinistoryVar("topReturn", 3));
        varMap.put("topStockName3", new MinistoryVar("topStockName", 3));
        varMap.put("topReturn4", new MinistoryVar("topReturn", 4));
        varMap.put("topStockName4", new MinistoryVar("topStockName", 4));

        varMap.put("avgReturn1", new MinistoryVar("avgReturn", 1));
        varMap.put("avgReturn2", new MinistoryVar("avgReturn", 2));

        varMap.put("snp500Return", new MinistoryVar("snp500Return"));

        varMap.put("marketPremium1", new MinistoryVar("marketPremium", 1));
        varMap.put("marketPremium2", new MinistoryVar("marketPremium", 2));

        varMap.put("accuracy", new MinistoryVar("accuracy"));
        varMap.put("totalNumber", new MinistoryVar("totalNumber"));
    }

    public void updateMiniFormList(List<Integer> usedMiniFormList, Date curDate) {
        //Updating Date of the used one
        miniFormList.forEach(item -> {
            if (usedMiniFormList.contains(item.getNumber())) {
                item.setLastUsed(curDate);
                System.out.println("wow Updated " + item.getNumber());
            }
        });

    }

    public Date updateUsedFormDate(List<Integer> usedMiniFormList) {
        FileInputStream fileInStream = null;
        FileOutputStream fileOutStream = null;
        Date curDate = new Date();
        try {
            System.out.println("Updating Used Ministory Form");
            String filePath = this.filePath;
            fileInStream = checkFileExist(filePath);

            //Get the workbook instance for XLSX file 
            XSSFWorkbook workbook = new XSSFWorkbook(fileInStream);
            XSSFSheet spreadsheet = workbook.getSheetAt(0);

            final int MY_MINIMUM_COLUMN_COUNT = 12;
            int rowStart = 1;
            int rowEnd = spreadsheet.getLastRowNum();//Math.max(1400, spreadsheet.getLastRowNum());
            int writeCount = usedMiniFormList.size();
            for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
                if (writeCount <= 0)// if no more thing to write
                {
                    break;
                }

                Row row = spreadsheet.getRow(rowNum);
                if (row == null) {
                    // This whole row is empty
                    continue;
                }
                Cell cell = row.getCell(0, Row.RETURN_BLANK_AS_NULL);
                //Cell dateCell = row.getCell(11, Row.RETURN_BLANK_AS_NULL);
                if (cell != null) {
                    Double numb = cell.getNumericCellValue();
                    Integer number = numb.intValue();

                    for (int containNumb : usedMiniFormList) {
                        if (containNumb == number) {
                            Cell dateCell = row.getCell(11, Row.RETURN_BLANK_AS_NULL);
                            if (dateCell == null) {
                                dateCell = row.createCell(11);
                            }
                            dateCell.setCellType(CELL_TYPE_NUMERIC);
                            dateCell.setCellValue(curDate);
                            writeCount--;
                        }
                    }

//                    if(usedMiniFormList.contains(number)){// Set Date
//                        if(dateCell == null)
//                            dateCell = row.createCell(11);
//                        dateCell.setCellType(CELL_TYPE_NUMERIC);
//                        dateCell.setCellValue(curDate);
//                    }
                } else {// If cell is NULL skip
                    continue;
                }

            }

            fileOutStream = new FileOutputStream(filePath);
            workbook.write(fileOutStream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            Logger.getLogger(MinistoryFormItem.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fileInStream.close();
                fileOutStream.close();
            } catch (IOException ex) {
                Logger.getLogger(MinistoryFormManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            return curDate;
        }
    }

    private List<MinistoryFormItem> loadAllFromExcel(String filePath) {
        System.out.println("Loading MiniStory Form Excel Database");
        List<MinistoryFormItem> miniFormList = new ArrayList();
        boolean cleanSucess = true;
        try {
            System.out.println("Reading Ministory DB");
            FileInputStream fileStream = checkFileExist(filePath);

            //Get the workbook instance for XLSX file 
            XSSFWorkbook workbook = new XSSFWorkbook(fileStream);
            XSSFSheet spreadsheet = workbook.getSheetAt(0);
            //XSSFRow row;

            final int MY_MINIMUM_COLUMN_COUNT = 12;
            int rowStart = 1;
            int rowEnd = Math.max(1400, spreadsheet.getLastRowNum());
            //Startfrom row 1
            for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
                Row r = spreadsheet.getRow(rowNum);
                if (r == null) {
                    // This whole row is empty
                    continue;
                }
                MinistoryFormItem miniFormTemp = new MinistoryFormItem();
                int lastColumn = Math.max(r.getLastCellNum(), MY_MINIMUM_COLUMN_COUNT);

                for (int cellNum = 0; cellNum < lastColumn; cellNum++) {
                    Cell cell = r.getCell(cellNum, Row.RETURN_BLANK_AS_NULL);
                    if (cell == null) {
                        // The spreadsheet is empty in this cell
                        miniFormTemp.setEmptyValue(cellNum);
                    } else {
                        // Fill the cell's contents to MiniForm Obj
                        miniFormTemp.setValue(cellNum, cell);
                    }
                }

                miniFormList.add(miniFormTemp);

            }

            fileStream.close();
        } catch (FileNotFoundException e) {
            cleanSucess = false;
            e.printStackTrace();
        } catch (IOException e) {
            cleanSucess = false;
            e.printStackTrace();
        } catch (Exception ex) {
            cleanSucess = false;
            Logger.getLogger(MinistoryFormItem.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (cleanSucess) {
                System.out.println("All Read Without Error");
            } else {
                System.out.println("There were some Error(s)");
            }
        }

        return miniFormList;
    }

    private static FileInputStream checkFileExist(String fileName) throws Exception {

        File file = new File(fileName);
        if (file.isFile() && file.exists()) {
            System.out.println(fileName + " file open successfully.");
            return new FileInputStream(file);
        } else {
            System.out.println("File doesnt exist : " + fileName);
            throw new Exception();
        }
    }

    public List<MinistoryFormItem> selectFormList(String timeFrame, List<ForecastTableItem> itemList) {
        List<MinistoryFormItem> miniFormTemp;

        //filter Time Frame
        miniFormTemp = miniFormList.stream()
                .filter(item -> (item.getTimeFrame().equalsIgnoreCase(timeFrame)
                || item.getTimeFrame().equalsIgnoreCase("ANY")))
                .collect(Collectors.toList());

        //filter with ForecastNr equal or less
        miniFormTemp = miniFormTemp.stream()
                .filter(item -> (item.getForecastNr() == itemList.size() || item.getForecastNr() == itemList.size() - 1))
                .collect(Collectors.toList());

        //filter Position Type => ANY + PostionType of Item (exclude what doesn't match)
        //Select where param is empty or match
        String positionTypeVal = itemList.get(0).getPositionType();
        miniFormTemp = miniFormTemp.stream()
                .filter(itemForm -> (itemForm.getParametersMap().get("positionType") == null) || (itemForm.getParametersMap().get("positionType").equalsIgnoreCase(positionTypeVal)))
                .collect(Collectors.toList());

        //Sort With Rank
        miniFormTemp = miniFormTemp.stream()
                .sorted((f1, f2) -> Integer.compare(f1.getRank(), f2.getRank()))
                .collect(Collectors.toList());

        //Sort With  Last Used
        Collections.sort(miniFormTemp, new Comparator<MinistoryFormItem>() {
            public int compare(MinistoryFormItem o1, MinistoryFormItem o2) {
                if (o1.getLastUsed() == null || o2.getLastUsed() == null) {
                    return 0;
                } else {
                    return o1.getLastUsed().compareTo(o2.getLastUsed());
                }
            }
        });

        return miniFormTemp;
    }

    public String makeLink(String content, String link) {
        return "<a href=" + "'" + link + "'>" + content + "</a>";
    }

    public MinistoryFormItem replaceSharpVar(int miniNumber, MinistoryFormItem miniForm, List<ForecastTableItem> itemList, String urlTagStr) {
        String title = miniForm.getCodeTitle();
        String titleNumbering = String.valueOf(miniNumber) + ". ";
        String text = miniForm.getText();

        boolean replaceTitle = true;
        String boldTagS = "<em>";
        String boldTagE = "</em>";
        //replace Title Tag
        if (title.isEmpty()) {
            title = titleNumbering + "[ENTER TITLE HERE PLEASE]";
            replaceTitle = false;
        } else {//Replace Title Tags
            title = titleNumbering + title;
            title = title.replace(varMap.get("topReturn1").getVar(1), itemList.get(0).stockReturnStringRankOf(0));
        }
        if (text.isEmpty()) {
            text = "Form Text is empty Please check the Form";
        } else {
            text = text.replace("###timeFrame###", itemList.get(0).getTimeFrame());
            for (int i = 1; i <= itemList.size(); i++) {
                //text = text.replace(varMap.get("title").getVar(0), Double.toString(itemList.get(0).stockReturnStringRankOf(0)));
                ForecastTableItem item = itemList.get(i - 1);
                text = text.replace(varMap.get("title").getVar(i), item.getTitle())
                        .replace(varMap.get("link").getVar(i), item.getLink())
                        .replace(varMap.get("positionType").getVar(i), item.getPositionType())
                        .replace(varMap.get("packageName").getVar(i), makeLink(item.getPackageName(), item.getLink() + urlTagStr))
                        .replace(varMap.get("subpackageName").getVar(i), item.getSubpackageName())
                        .replace(varMap.get("timePeriod").getVar(i), item.getTimeFrame())
                        .replace(varMap.get("forecastDate").getVar(i), item.getForecastDate())
                        .replace(varMap.get("targetDate").getVar(i), item.getTargetDate())
                        .replace(varMap.get("topReturn1").getVar(i), boldTagS + item.stockRankOf(0).getRow().getReturnValueStr() + boldTagE)
                        .replace(varMap.get("topStockName1").getVar(i), boldTagS + item.stockRankOf(0).getRow().getSymbol() + boldTagE)
                        .replace(varMap.get("topReturn2").getVar(i), boldTagS + item.stockRankOf(1).getRow().getReturnValueStr() + boldTagE)
                        .replace(varMap.get("topStockName2").getVar(i), boldTagS + item.stockRankOf(1).getRow().getSymbol() + boldTagE)
                        .replace(varMap.get("topReturn3").getVar(i), boldTagS + item.stockRankOf(2).getRow().getReturnValueStr() + boldTagE)
                        .replace(varMap.get("topStockName3").getVar(i), boldTagS + item.stockRankOf(2).getRow().getSymbol() + boldTagE)
                        .replace(varMap.get("topReturn4").getVar(i), boldTagS + item.stockRankOf(3).getRow().getReturnValueStr() + boldTagE)
                        .replace(varMap.get("topStockName4").getVar(i), boldTagS + item.stockRankOf(3).getRow().getSymbol() + boldTagE)
                        .replace(varMap.get("avgReturn1").getVar(i), item.getAvgReturnTabNumb(0))
                        .replace(varMap.get("avgReturn2").getVar(i), item.getAvgReturnTabNumb(1))
                        .replace(varMap.get("snp500Return").getVar(i), item.getSNPReturn())
                        .replace(varMap.get("marketPremium1").getVar(i), item.getPremiumTabNumb(0))
                        .replace(varMap.get("marketPremium2").getVar(i), item.getPremiumTabNumb(1))
                        .replace(varMap.get("accuracy").getVar(i), item.getTotalAccuracyNumb()) // Count accuray
                        .replace(varMap.get("totalNumber").getVar(i), item.getTotalNumb()); //count total number
                if (replaceTitle) {
                    title = title.replace(varMap.get("title").getVar(i), item.getTitle())
                            .replace(varMap.get("link").getVar(i), item.getLink())
                            .replace(varMap.get("positionType").getVar(i), item.getPositionType())
                            .replace(varMap.get("packageName").getVar(i), makeLink(item.getPackageName(), item.getLink() + urlTagStr))
                            .replace(varMap.get("subpackageName").getVar(i), item.getSubpackageName())
                            .replace(varMap.get("timePeriod").getVar(i), item.getTimeFrame())
                            .replace(varMap.get("forecastDate").getVar(i), item.getForecastDate())
                            .replace(varMap.get("targetDate").getVar(i), item.getTargetDate())
                            .replace(varMap.get("topReturn1").getVar(i), item.stockRankOf(0).getRow().getReturnValueStr())
                            .replace(varMap.get("topStockName1").getVar(i), item.stockRankOf(0).getRow().getSymbol())
                            .replace(varMap.get("topReturn2").getVar(i), item.stockRankOf(1).getRow().getReturnValueStr())
                            .replace(varMap.get("topStockName2").getVar(i), item.stockRankOf(1).getRow().getSymbol())
                            .replace(varMap.get("topReturn3").getVar(i), item.stockRankOf(2).getRow().getReturnValueStr())
                            .replace(varMap.get("topStockName3").getVar(i), item.stockRankOf(2).getRow().getSymbol())
                            .replace(varMap.get("topReturn4").getVar(i), item.stockRankOf(3).getRow().getReturnValueStr())
                            .replace(varMap.get("topStockName4").getVar(i), item.stockRankOf(3).getRow().getSymbol())
                            .replace(varMap.get("avgReturn1").getVar(i), item.getAvgReturnTabNumb(0))
                            .replace(varMap.get("avgReturn2").getVar(i), item.getAvgReturnTabNumb(1))
                            .replace(varMap.get("snp500Return").getVar(i), item.getSNPReturn())
                            .replace(varMap.get("marketPremium1").getVar(i), item.getPremiumTabNumb(0))
                            .replace(varMap.get("marketPremium2").getVar(i), item.getPremiumTabNumb(1))
                            .replace(varMap.get("accuracy").getVar(i), item.getTotalAccuracyNumb()) // Count accuray
                            .replace(varMap.get("totalNumber").getVar(i), item.getTotalNumb()); //count total number
                }
            }

        }

        miniForm.setCodeTitle(title);
        miniForm.setText(text);
        return miniForm;
    }

    public MinistoryFormItem dressItemWithForm(int miniNumber, String timeFrame, List<ForecastTableItem> itemList, String urlTagStr) {

        List<MinistoryFormItem> miniFormListTemp;
        MinistoryFormItem miniFormSelected;

        //Select Form List From What was load from DB
        miniFormListTemp = selectFormList(timeFrame, itemList);

        if (miniFormListTemp.size() > 0) {
            //Get the first Form of the Selected List
            miniFormSelected = miniFormListTemp.get(0);
            MinistoryFormItem clonedForm = new MinistoryFormItem(miniFormSelected);
            miniFormSelected = replaceSharpVar(miniNumber, clonedForm, itemList, urlTagStr);

            return miniFormSelected;
        } else {
            return null;
        }
    }

}

class MinistoryVar {

    String varText;
    Integer varNumber;

    public MinistoryVar(String varText, Integer varNumber) {
        this.varText = varText;
        this.varNumber = varNumber;
    }

    public MinistoryVar(String varText) {
        this.varText = varText;
        this.varNumber = null;
    }

    public String getVar(int i) {
        System.out.println("###" + varText + String.valueOf(i) + getVarNumb() + "###");
        return "###" + varText + String.valueOf(i) + getVarNumb() + "###";
    }

    private String getVarNumb() {
        if (varNumber != null) {
            return String.valueOf(varNumber);
        } else {
            return "";
        }
    }
}
