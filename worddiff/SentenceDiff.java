package worddiff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class SentenceDiff {
	private HashMap<Integer, ArrayList<ArrayList<Sentence>>> dictMap;
	
	public SentenceDiff () {
		this.dictMap = new HashMap<Integer, ArrayList<ArrayList<Sentence>>>();
	}
	protected Sentence convert2Sentence(String sentence) {
		
		char[] c = sentence.toCharArray();
		LinkedList<String> ll = new LinkedList<String>();
		int index = 18;
		// First 17 chars are DATE no need to parse.
		ll.add(sentence.substring(0,16));
        
		for(int i=18;i<c.length;i++) {
		    if(c[i] == ' ' || i==c.length-1) {
		        ll.add(sentence.substring(index,i));
		        index = i+1;
		    }
		}

		String[] arr = new String[ll.size()-1];//Only content no need for initial word which is the date
		Integer[] hcodes = new Integer[ll.size()-1];
		Iterator<String> iter = ll.iterator();
	    String word = null;
		
	    String sdate = iter.next(); //We skip the first word which is the Date
		for(index = 0; iter.hasNext(); index++) {
			word = iter.next();
			hcodes[index] = word.hashCode();
			arr[index] = word;
		}

		return new Sentence(arr,hcodes,sentence,sdate);
		
	}
	
	public String checkWordDiff(String strLine) {
		Sentence sentence = this.convert2Sentence(strLine);

		//In case the length is new , just add to the dictionary and return nothing.
		if (!this.dictMap.containsKey(sentence.getWords().length)) {
			ArrayList<Sentence> newCluster = new ArrayList<Sentence>();
			newCluster.add(sentence);
			ArrayList<ArrayList<Sentence>> newList = new ArrayList<ArrayList<Sentence>>();;
			newList.add(newCluster);
			this.dictMap.put(sentence.getWords().length, newList);
			return null;
		}
		ArrayList<ArrayList<Sentence>> nList = this.dictMap.get(sentence.getWords().length);
		boolean isSimilar = false;
		Iterator<ArrayList<Sentence>> iterator = nList.iterator();
		while (iterator.hasNext()) {
			ArrayList<Sentence> sList = iterator.next();
			Sentence cSentence = sList.get(0); //First one is to compare with, the rest are part of the cluster
			int wordsDiff = 0;
			int diffLoc = 0;
			Integer [] compared_hcodes =  cSentence.getHcodes();
			Integer [] hcodes = sentence.getHcodes();
			for (int i=0;i<hcodes.length;i++) {
				if (hcodes[i].intValue() != compared_hcodes[i].intValue()) {
					wordsDiff++;
					diffLoc = i; 
				}
			}
			if (wordsDiff == 1) {
				sList.add(sentence);
				return getDiffSentence(cSentence,sentence,diffLoc);
			}
		}
		// In case not similar sentence was found, need to create a new cluster
		if (!isSimilar) {
			ArrayList<Sentence> newCluster = new ArrayList<Sentence>();
			newCluster.add(sentence);
			nList.add(newCluster);
		}
		return null;

	}
	
	private String getDiffSentence(Sentence orig,Sentence target,int location) {
		String result;
		result = orig.getSentence() + "\n" + target.getSentence() + "\n" + "The changing word was: " 
				+ orig.getWords()[location] + ", " + target.getWords()[location] + "\n";
		return result;
	}
	
	
}

class Sentence {
	private String [] words;
	private Integer[] hcodes;
	private String sentence;
	private String date;
	
	public Sentence (String[] words, Integer[] hcodes,String sentence, String date) {
		this.words = words;
		this.hcodes = hcodes;
		this.sentence = sentence;
		this.date = date;
	}
	
	public String[] getWords() {
		return this.words;
	}

	public Integer[] getHcodes() {
		return this.hcodes;
	}

	public String getSentence() {
		return this.sentence;
	}

	public String getDate() {
		return this.date;
	}
	
	
}