package test;

/**
 * @author ashraf
 *https://examples.javacodegeeks.com/core-java/apache/commons/csv-commons/writeread-csv-files-with-apache-commons-csv-example/
 */
public class CsvWriteReadTest {

	/**
	 * @param args
	 */
	public static void main1(String[] args) {
		
		String fileName = "output/test.csv";//System.getProperty("user.home")+"/student.csv";
		
		System.out.println("Write CSV file:");
		CsvFileWriter.writeCsvFile(fileName);
		
		System.out.println("\nRead CSV file:");
		CsvFileReader.readCsvFile(fileName);

	}

}