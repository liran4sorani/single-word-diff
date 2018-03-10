package genlog;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class LogManager {
	private LocalDateTime countTime;
	private ArrayList<String> wordList;
	private Random randomGenerator;
	public String wordListFile;
	
	public LogManager (String sinceTime,String wordListFile) {
		this.wordListFile = wordListFile;
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			this.countTime  = LocalDateTime.parse(sinceTime, formatter);
		}
		catch (DateTimeParseException e) {
			System.out.println("Exception in init , date format should if : yyyy-MM-dd HH:mm");
			System.exit(1);
		}
		try {
			Scanner s = new Scanner(new File(wordListFile));
			this.wordList = new ArrayList<String>();
			while (s.hasNext()){
				this.wordList.add(s.next());
			}
			s.close();			
		}
		catch (Exception e) {
			System.out.printf("Exception in init , file %s does not exist or invalid.\n",wordListFile);
			System.exit(2);
		}
		this.randomGenerator = new Random();
	}
	
	private  String[] genLine(Long delta) {
		int sentenceLength = 2 + this.randomGenerator.nextInt(10);
		String sentence = "";
		for(int i=0; i<sentenceLength; i++) {
			sentence  += " " + this.wordList.get(this.randomGenerator.nextInt(this.wordList.size()));
		}
		sentence = sentence.trim();
		this.countTime = this.countTime.plusSeconds(delta);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		String formattedString = this.countTime.format(formatter);		
		return new String[] {formattedString ,sentence} ;
	}
	public ArrayList<String> getBulk(int rowNum) {
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<Integer> repIdx = new ArrayList<Integer>();
		ArrayList<String> repSentences = new ArrayList<String>();
		
		int cnt = 0;
		while (cnt < rowNum) {
			int val = this.randomGenerator.nextInt(rowNum);
			repIdx.add(val);
			cnt += val;
		}
		for(int i=0; i<rowNum; i++) {
			String[] rowItems = this.genLine((long) 2);
			if (repIdx.indexOf(i) != -1) {
				//replicate record and change one word
				String newSentence;
				String [] parts = rowItems[1].split(" ");
				int randIdx = this.randomGenerator.nextInt(parts.length);
				String newWord = this.wordList.get(this.randomGenerator.nextInt(this.wordList.size()));
				parts[randIdx] = newWord;
				repSentences.add(String.join(" ", parts));
			}
			result.add(rowItems[0] + " " + rowItems[1] + "\n");
		}
		for (String repSentence : repSentences) {
			int randLoc = this.randomGenerator.nextInt(rowNum-1);
			if (repIdx.indexOf(randLoc) != -1) {
				if (randLoc == rowNum-1 ) {
					randLoc -= 1;
				} else {
					randLoc += 1;
				}
			}
			String orig = result.get(randLoc);
			String [] origParts = orig.split(" ");
			result.set(randLoc, origParts[0]+" " + origParts[1] + " " + repSentence + "\n");
		}
		return result;
	}
}