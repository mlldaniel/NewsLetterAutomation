/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources.ministory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import static resources.ministory.MinistoryFormManager.CELL_TYPE_NUMERIC;
import static resources.ministory.MinistoryFormManager.CELL_TYPE_STRING;

/**
 *
 * @author Daniel
 */
public class MinistoryFormItem {

    final static int NUMBER = 0;
    final static int RESPONDERCODE = 1;
    final static int OLDTITLE = 2;
    final static int TEXT = 3;
    final static int CODETITLE = 4;
    final static int FORECASTNR = 5;
    final static int TIMEFRAME = 6;
    final static int RANK = 7;
    final static int PARAMETERSMAP = 8;
    final static int TYPE = 9;
    final static int EDITED = 10;
    final static int LASTUSED = 11;

    private Integer number;
    private Integer responderCode;
    private String oldTitle;
    private String text;
    private String codeTitle;
    private Integer forecastNr;
    private String timeFrame;
    private Integer rank;
    private Map<String, String> parametersMap;
    private String type;
    private Boolean edited;
    private Date lastUsed;

    static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    public MinistoryFormItem() {
    }

    public MinistoryFormItem(MinistoryFormItem another) {
        this.number = another.getNumber();
        this.responderCode = another.getResponderCode();
        this.oldTitle = another.getOldTitle();
        this.text = another.getText();
        this.codeTitle = another.getCodeTitle();
        this.forecastNr = another.getForecastNr();
        this.timeFrame = another.getTimeFrame();
        this.rank = another.getRank();
        this.parametersMap = another.getParametersMap();
        this.type = another.getText();
        this.edited = another.getEdited();
        this.lastUsed = another.getLastUsed();
    }

    public void setEmptyValue(int colNum) {
        switch (colNum) {
            case NUMBER:
                setNumber(-1);
                break;
            case RESPONDERCODE:
                setResponderCode(-1);
                break;
            case OLDTITLE:
                setOldTitle("");
                break;
            case TEXT:
                setText("");
                break;
            case CODETITLE:
                setCodeTitle("");
                break;
            case FORECASTNR:
                setForecastNr(-1);
                break;
            case TIMEFRAME:
                setTimeFrame("");
                break;
            case RANK:
                setRank(9999);
                break;
            case PARAMETERSMAP:
                Map<String, String> paramTempMap = new HashMap();
                setParametersMap(paramTempMap);
                break;
            case TYPE:
                setType("");
                break;
            case EDITED:
                int bool = -1;
                boolean edited = (bool == 1) ? true : false;
                setEdited(edited);
                break;
            case LASTUSED:
                setLastUsed(stringToDate("01/01/2000"));
                break;
        }
    }

    public void setValue(int colNum, Cell cell) {
        switch (colNum) {
            case NUMBER:
                setNumber(getCellInteger(cell));
                break;
            case RESPONDERCODE:
                setResponderCode(getCellInteger(cell));
                break;
            case OLDTITLE:
                setOldTitle(getCellString(cell));
                break;
            case TEXT:
                setText(getCellString(cell));
                break;
            case CODETITLE:
                setCodeTitle(getCellString(cell));
                break;
            case FORECASTNR:
                setForecastNr(getCellInteger(cell));
                break;
            case TIMEFRAME:
                setTimeFrame(getCellString(cell));
                break;
            case RANK:
                setRank(getCellInteger(cell));
                break;
            case PARAMETERSMAP:
                Map<String, String> paramTempMap = new HashMap();

                String parameters = getCellString(cell);
                String[] paramArr = parameters.split(",");
                for (String param : paramArr) {
                    String key = param.split("=")[0].trim();
                    String value = param.split("=")[1].replace("\"", "").trim();
                    //if it has value
                    if (value.isEmpty() == false && value.trim().length() > 0) {
                        paramTempMap.put(key, value);
                    }
                }
                setParametersMap(paramTempMap);
                break;
            case TYPE:
                setType(getCellString(cell));
                break;
            case EDITED:
                int bool = getCellInteger(cell);
                boolean edited = (bool == 1) ? true : false;
                setEdited(edited);
                break;
            case LASTUSED:
                setLastUsed(getCellDate(cell));
                break;
        }
    }

    public static Date stringToDate(String stringDateFormat) {
        //dateFormat
        Date convertedDateFormat = null;
        try {
            convertedDateFormat = dateFormat.parse(stringDateFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDateFormat;
    }

    public static String dateToString(Date dateDateFormat) {
        String stringDateFormat = dateFormat.format(dateDateFormat);
        return stringDateFormat;
    }

    private Date getCellDate(Cell cell) {
        if (DateUtil.isCellDateFormatted(cell)) {
            return cell.getDateCellValue();
        } else {
            return new Date();
        }
    }

    private String getCellString(Cell cell) {
        switch (cell.getCellType()) {
            case CELL_TYPE_STRING:
                String a = cell.getStringCellValue();
                return a;
            case CELL_TYPE_NUMERIC:
                Double numb = cell.getNumericCellValue();
                return numb.toString();
            default:
                return "";
        }
    }

    private Integer getCellInteger(Cell cell) {
        switch (cell.getCellType()) {
            case CELL_TYPE_STRING:
                String a = cell.getStringCellValue();
                int returnNum;
                try {
                    if(a.contains(","))
                        a = a.split(",")[0];
                    
                    returnNum = Integer.parseInt(a);
                } catch (NumberFormatException ex) {
                    System.out.println(ex.toString());
                } finally {
                    returnNum = -1;
                }

                return returnNum;
            case CELL_TYPE_NUMERIC:
                Double numb = cell.getNumericCellValue();

                return numb.intValue();
            default:
                return -1;
        }
    }

    @Override
    public String toString() {
        return "MinistoryFormItem{" + "number=" + number + ", responderCode=" + responderCode + ", oldTitle=" + oldTitle + ", text=" + text + ", codeTitle=" + codeTitle + ", forecastNr=" + forecastNr + ", timeFrame=" + timeFrame + ", rank=" + rank + ", parametersMap=" + parametersMap + ", type=" + type + ", edited=" + edited + ", lastUsed=" + lastUsed + '}';
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getResponderCode() {
        return responderCode;
    }

    public void setResponderCode(Integer responderCode) {
        this.responderCode = responderCode;
    }

    public String getOldTitle() {
        return oldTitle;
    }

    public void setOldTitle(String oldTitle) {
        this.oldTitle = oldTitle;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCodeTitle() {
        return codeTitle;
    }

    public void setCodeTitle(String codeTitle) {
        this.codeTitle = codeTitle;
    }

    public Integer getForecastNr() {
        return forecastNr;
    }

    public void setForecastNr(Integer forecastNr) {
        this.forecastNr = forecastNr;
    }

    public String getTimeFrame() {
        return timeFrame;
    }

    public void setTimeFrame(String timeFrame) {
        this.timeFrame = timeFrame;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Map<String, String> getParametersMap() {
        return parametersMap;
    }

    public void setParametersMap(Map<String, String> parametersMap) {
        this.parametersMap = parametersMap;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getEdited() {
        return edited;
    }

    public void setEdited(Boolean edited) {
        this.edited = edited;
    }

    public Date getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(Date lastUsed) {
        this.lastUsed = lastUsed;
    }

}
