package genlog;

import java.util.ArrayList;
import java.util.Formatter;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class GenerateLog {
	private Formatter output;
	
	
	
	public static void main(String[] args) {
		LogManager lm = new LogManager("1973-03-23 13:01","C:\\temp\\task\\worddiff\\dictionary_english.dic");
		int bulkSize = 100;
		int numBulks = 1;
		String outFile = "log_records.txt";
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
	              new FileOutputStream(outFile)	))) {
			for(int i=0; i<numBulks; i++) {
				ArrayList <String> bulk = lm.getBulk(bulkSize);
				for(String sentence : bulk) {
					writer.write(sentence);
				}
			}
			writer.close();
			
		} catch (Exception e) {
			System.out.printf("ERROR: file is invalid or not found\n");
			e.printStackTrace();
			System.exit(2);
			
		}		
		
	}
	
}

