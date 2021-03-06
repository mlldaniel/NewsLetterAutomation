/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

/**
 *
 * @author danielhwang
 */
public class TitleTag{

    /**
     * @return the dbID
     */
    public String getDbID() {
        return dbID;
    }

    /**
     * @param dbID the dbID to set
     */
    public void setDbID(String dbID) {
        this.dbID = dbID;
    }

    public TitleTag(String title, String tag, String dbID) {
        this.title = title;
        this.tag = tag;
        this.dbID = dbID;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * @param tag the tag to set
     */
    public void setTag(String tag) {
        this.tag = tag;
    }
    private String dbID;
    private String title;
    private String tag;
    private final static String DELIMITER = "\n";
    private static final Object [] FILE_HEADER = {"Ministory dbID","Title/TimeFrame","Tag"};
    
    //Write Tag to file
    public static boolean writeAsCsvTitleTagList(String fileName,List<TitleTag> titleTagList){
        
        FileWriter fileWriter = null;
        CSVPrinter csvFilePrinter = null;
        
        
//        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(DELIMITER);
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withDelimiter('\t');
                
        boolean err = false;
        try{
            fileWriter = new FileWriter(fileName);// Open File with fileWriter
            csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);// get Printer
            csvFilePrinter.printRecord(FILE_HEADER); // Write Header 
            
            for(TitleTag tag: titleTagList){
                List tagRecord = new ArrayList();
                tagRecord.add(tag.getDbID());
                tagRecord.add(tag.getTitle());
                tagRecord.add(tag.getTag());
                csvFilePrinter.printRecord(tagRecord);
            }
            System.out.println("CSV File Recorded Successfylly");
        } catch (Exception ex){
            err =true;
            System.out.println("Error in CSV File Writer ! Writing TItle - Tag table");
            ex.printStackTrace();
        } finally {
            try{
                fileWriter.flush();
                fileWriter.close();
                csvFilePrinter.close();
            } catch (IOException e){
                err = true;
                System.out.println("Error while flushing/closing fileWriter /csvPrinter !!");
                e.printStackTrace();
            } finally{
                return err;
            }
        }
        
    }
}
