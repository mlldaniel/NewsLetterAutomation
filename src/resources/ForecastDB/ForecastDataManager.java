/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources.ForecastDB;

import exceptions.DataNotAvailableException;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import tab.forecast.ForecastTableItem;
import static tab.forecast.ForecastTableItem.*;

/**
 *
 * @author Daniel
 */
public class ForecastDataManager {

    //Load Forecast Data(Read DB)
    static final int MAINTABLE = 0;
    static final int CSVTABLE = 1;
    static final int CSVROW = 2;
    String mainTable;
    String csvTable;
    String csvRow;

    String dbLocation;

    boolean fileExist;
    public static final String[] colName = {"position", "title", "link", "positionType",
        "packageName", "subpackageName", "csvFilePath", "timeFrame", "forecastDate",
        "targetDate", "topReturn1", "topStockName1", "topReturn2", "topStockName2",
        "topReturn3", "topStockName3", "avgReturn1", "avgReturn2", "snp500Return",
        "marketPremium1", "marketPremium2", "accuracy", "totalNumber"};

    public ForecastDataManager(String dbLocation) {
        this.mainTable = "mainTable";
        this.csvTable = "csvTable";
        this.csvRow = "csvRow";

        this.dbLocation = dbLocation;

        checkDataAvailability();
        //findForecastItem(LINK,"http://iknowfirst.com/fr-commodity-prediction-based-on-algo-trading-returns-up-to-7-99-in-7-days");
    }

    private String makeDBbackUP(String dbLocation) {
        String backupLoc = dbLocation + "BACKUPFROMPROGRAM";
        try {
            FileUtils.copyFile(new File(dbLocation), new File(backupLoc));
        } catch (IOException ex) {
            Logger.getLogger(ForecastDataManager.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Back UP Failed for : " + dbLocation);
        }

        return backupLoc;
    }

    private void checkNRestoreDB(String dbLocation, String backupLoc) {
        File backupFile = new File(backupLoc);
        File file = new File(dbLocation);

        try {
            long backupCSUM = FileUtils.checksumCRC32(backupFile);
            long csum = FileUtils.checksumCRC32(file);
            if (csum == backupCSUM) {//No Problem
                System.out.println("No Prob");
            } else {
                System.out.println("Something is wrong with your file both doesn't match");
                //Resotre file
                FileUtils.copyFile(backupFile, file);
                
            }

        } catch (IOException ex) {
            Logger.getLogger(ForecastDataManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ForecastTableItem findForecastItem(int columnNumb, String key) {
        //Making BackUP
        String backupLoc = makeDBbackUP(dbLocation);

        String col = colName[columnNumb];
        System.out.println("Looking for " + key + "  from Column: " + col + " in " + dbLocation);
        ForecastTableItem row = null;
        try {
            //Select Row From Main Table of DB
            //row = (ForecastTableItem) selectItem(mainTable, col, key);
            row = selectItemPrepareStatement(mainTable, col, key);

            List<CsvTable> csvTableList = (List<CsvTable>) (Object) selectItemListPrepareStatement(csvTable, "mainTableId", String.valueOf(row.getId()));
            row.setCsvTableList(csvTableList);

            for (CsvTable table : csvTableList) {
                List<CsvRow> cRList = new ArrayList();
                cRList = (List<CsvRow>) (Object) selectItemListPrepareStatement(csvRow, "csvTableId", String.valueOf(table.getId()));
                table.setCsvRowList(cRList);
            }

            if (row != null) {
                System.out.println("Sucess! : Title is " + row.toString());
                return row;
            } else if (columnNumb == CSVFILEPATH) {
                System.out.println("Unable to Find CSV File Path " + key);
                return new ForecastTableItem("", key);
            } else {
                System.out.println("Unable to Find Link " + key);
                return new ForecastTableItem(key, "");
            }
        } catch (NullPointerException ex) {
            if (columnNumb == CSVFILEPATH) {
                System.out.println(ex.getMessage());
                System.out.println("Unable to Find CSV File Path " + key);
            } else {
                System.out.println(ex.getMessage());
                System.out.println("Unable to Find Link " + key);
            }
        } finally {
            checkNRestoreDB(dbLocation,backupLoc);
            if (row != null) {
                System.out.println("Sucess! : Title is " + row.toString());
                return row;
            } else if (columnNumb == CSVFILEPATH) {
                return new ForecastTableItem("", key);
            } else {
                return new ForecastTableItem(key, "");
            }
        }

    }

    public void checkDataAvailability() {
        System.out.println("Checking DataBase Availability on " + dbLocation);
        //ForecastTableItem row = selectRowWith("ID","1");
        String sql = "SELECT * FROM " + mainTable;
        ForecastTableItem mtRow = (ForecastTableItem) doQuery(sql, dbLocation, mainTable);
        List<Object> cTObjList = selectItemList(csvTable, "mainTableId", String.valueOf(mtRow.getId()));

        List<CsvTable> cTList = new ArrayList();
        cTList = (List<CsvTable>) (Object) cTObjList;

        List<CsvRow> cRList = new ArrayList();
        for (CsvTable tab : cTList) {
            cRList = (List<CsvRow>) (Object) selectItemList(csvRow, "csvTableId", String.valueOf(tab.getId()));
        }

        if (cRList.get(0) != null) {
            System.out.println("DataBase Available!");
            //System.out.println(row.toString());
        } else {
            throw new DataNotAvailableException("Forecast Data for this Lanugage is not available \n"
                    + "please Check the path: " + dbLocation + " in Table Name : " + mainTable);
        }
    }

    private ForecastTableItem selectItemPrepareStatement(String tableName, String columnName, String name) {
        ForecastTableItem item = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbLocation);
            System.out.println("Selecting item from tableName: " + tableName + " of col: " + columnName + " : " + name);

            String query = "SELECT * FROM " + tableName + " WHERE " + columnName + "= ? COLLATE NOCASE;";
            pstmt = conn.prepareStatement(query);

            pstmt.setString(1, name);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                if (tableName.equalsIgnoreCase("mainTable")) {
                    item = new ForecastTableItem();
                    item.setId(rs.getInt("Id"));
                    item.setTitle(rs.getString("title"));
                    item.setLink(rs.getString("link").toLowerCase());
                    item.setPositionType(rs.getString("positionType"));
                    item.setPackageName(rs.getString("packageName"));
                    item.setCsvFilePath(rs.getString("csvFilePath"));
                    item.setSubpackageName(rs.getString("subpackageName"));
                    item.setTimeFrame(rs.getString("timeFrame"));
                    item.setForecastDate(rs.getString("forecastDate"));
                    item.setTargetDate(rs.getString("targetDate"));
                }

            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                }
            };
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            };
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            };
        }

        return item;
    }

    private Object selectItem(String tableName, String columnName, String name) {
        Object item = null;
        String sql = "SELECT * FROM " + tableName + " WHERE " + columnName + "='" + name + "';";
        item = doQuery(sql, dbLocation, tableName);

        return item;
    }

    private List<Object> selectItemList(String tableName, String columnName, String name) {
        List<Object> itemList = null;
        String sql = "SELECT * FROM " + tableName + " WHERE " + columnName + "='" + name + "';";
        itemList = doQueryList(sql, dbLocation, tableName);

        return itemList;
    }

    private List<Object> selectItemListPrepareStatement(String tableName, String columnName, String name) {
        List<Object> returnItemList = new ArrayList();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbLocation);
            System.out.println("Selecting item List from tableName: " + tableName + " of col: " + columnName + " : " + name);
            //conn.setAutoCommit(false);
            //System.out.println("Opened database successfully");
            String query = "SELECT * FROM " + tableName + " WHERE " + columnName + "=?";// + name + "';";
            pstmt = conn.prepareStatement(query);

            pstmt.setInt(1, Integer.parseInt(name));
            rs = pstmt.executeQuery();
            //stmt.executeUpdate(sql);

            while (rs.next()) {
                switch (tableName) {
                    case "csvTable":
                        CsvTable item2 = new CsvTable();
                        item2.setId(rs.getInt("Id"));
                        item2.setPositionNumber(rs.getInt("positionNumber"));
                        item2.setForecastDate(rs.getString("forecastDate"));
                        item2.setTargetDate(rs.getString("targetDate"));
                        item2.setSnp500Return(rs.getString("snp500Return"));
                        item2.setiKnowFirstAvgReturn(rs.getString("iKnowFirstAvgReturn"));
                        item2.setMainTableId(rs.getString("mainTableId"));

                        returnItemList.add(item2);
                        break;
                    case "csvRow":
                        CsvRow item3 = new CsvRow();
                        item3.setId(rs.getInt("Id"));
                        item3.setSymbol(rs.getString("symbol"));
                        item3.setPrediction(Integer.parseInt(rs.getString("prediction")));
                        item3.setReturnValue(parseDoubleAllLang(rs.getString("returnValue")));
                        item3.setAccuracy(Integer.parseInt(rs.getString("accuracy")));
                        item3.setCsvTableId(rs.getInt("csvTableId"));

                        returnItemList.add(item3);

                        break;

                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                }
            };
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            };
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            };
        }

        //System.out.println("Records created successfully");
        return returnItemList;
    }

    public static List<Object> doQueryList(String sql, String dbName, String tableName) {
        List<Object> returnItemList = new ArrayList();
        Connection c = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + dbName);
            c.setAutoCommit(false);
            //System.out.println("Opened database successfully");

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            //stmt.executeUpdate(sql);

            while (rs.next()) {
                switch (tableName) {
                    case "csvTable":
                        CsvTable item2 = new CsvTable();
                        item2.setId(rs.getInt("Id"));
                        item2.setPositionNumber(rs.getInt("positionNumber"));
                        item2.setForecastDate(rs.getString("forecastDate"));
                        item2.setTargetDate(rs.getString("targetDate"));
                        item2.setSnp500Return(rs.getString("snp500Return"));
                        item2.setiKnowFirstAvgReturn(rs.getString("iKnowFirstAvgReturn"));
                        item2.setMainTableId(rs.getString("mainTableId"));

                        returnItemList.add(item2);
                        break;
                    case "csvRow":
                        CsvRow item3 = new CsvRow();
                        item3.setId(rs.getInt("Id"));
                        item3.setSymbol(rs.getString("symbol"));
                        item3.setPrediction(Integer.parseInt(rs.getString("prediction")));
                        item3.setReturnValue(parseDoubleAllLang(rs.getString("returnValue")));
                        item3.setAccuracy(Integer.parseInt(rs.getString("accuracy")));
                        item3.setCsvTableId(rs.getInt("csvTableId"));

                        returnItemList.add(item3);

                        break;

                }
            }

            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        //System.out.println("Records created successfully");
        return returnItemList;
    }

    private static double parseDoubleAllLang(String numbStr) {
        if (numbStr.contains(",")) {
            numbStr = numbStr.replace(",", ".");
        }
        return Double.parseDouble(numbStr);
    }

    public static Object doQuery(String sql, String dbName, String tableName) {
        Object returnItem = null;
        Connection c = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + dbName);
            c.setAutoCommit(false);
            //System.out.println("Opened database successfully");

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            //stmt.executeUpdate(sql);

            if (rs.next()) {
                if (tableName.equalsIgnoreCase("mainTable")) {
                    ForecastTableItem item1 = new ForecastTableItem();
                    item1.setId(rs.getInt("Id"));
                    item1.setTitle(rs.getString("title"));
                    item1.setLink(rs.getString("link"));
                    item1.setPositionType(rs.getString("positionType"));
                    item1.setPackageName(rs.getString("packageName"));
                    item1.setCsvFilePath(rs.getString("csvFilePath"));
                    item1.setSubpackageName(rs.getString("subpackageName"));
                    item1.setTimeFrame(rs.getString("timeFrame"));
                    item1.setForecastDate(rs.getString("forecastDate"));
                    item1.setTargetDate(rs.getString("targetDate"));
                    returnItem = item1;
                }

            }

            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        //System.out.println("Records created successfully");
        return returnItem;
    }

    private ForecastTableItem selectRowWith(String columnName, String name) {
        ForecastTableItem item = null;
        String sql = "SELECT * FROM " + mainTable + " WHERE " + columnName + "='" + name + "';";
        item = doQueryAndGet(sql, dbLocation);

        return item;
    }

    public static ForecastTableItem doQueryAndGet(String sql, String dbName) {
        ForecastTableItem item = null;
        Connection c = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + dbName);
            c.setAutoCommit(false);
            //System.out.println("Opened database successfully");

            stmt = c.createStatement();
            rs = stmt.executeQuery(sql);
            //stmt.executeUpdate(sql);

            if (rs.next()) {
                item = new ForecastTableItem(rs.getInt("ID"),
                        rs.getString("title"), rs.getString("link"), rs.getString("positionType"),
                        rs.getString("packageName"), rs.getString("subpackageName"), rs.getString("csvFilePath"),
                        rs.getString("timeFrame"), rs.getString("forecastDate"), rs.getString("targetDate"),
                        rs.getString("topReturn1"), rs.getString("topStockName1"), rs.getString("topReturn2"),
                        rs.getString("topStockName2"), rs.getString("topReturn3"), rs.getString("topStockName3"),
                        rs.getString("avgReturn1"), rs.getString("avgReturn2"), rs.getString("snp500Return"),
                        rs.getString("marketPremium1"), rs.getString("marketPremium2"), rs.getString("accuracy"),
                        rs.getString("totalNumber"));

//                        new TopStockDescription(rs.getString("shortName"),
//                        rs.getString("longName"),
//                        rs.getString("contentText"),
//                        false);
            }

            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        //System.out.println("Records created successfully");
        return item;
    }

    //Find Data using link
    //FInd Data using csvFileLocation
}
