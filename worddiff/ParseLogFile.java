package worddiff;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class ParseLogFile {
	public static void main(String[] args) {
		SentenceDiff sd = new SentenceDiff();
		long startMilli;
		if (args.length != 1) {
			System.out.println("ERROR - a single argument is expected , that is the LogFile name");
			System.exit(1);
		}
		String logFile = args[0];
		try {
			// Open the file
			FileInputStream fstream = new FileInputStream(logFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			String strLine;

			//Read File Line By Line
			startMilli =  System.currentTimeMillis();
			System.out.printf("====\n");
			while ((strLine = br.readLine()) != null)   {
				String res = sd.checkWordDiff(strLine);
				if (res != null) 
					System.out.printf("\n%s",res);
			}
			System.out.printf("\n====\n");
			System.out.printf("Total time to print word diffs is : %d ms\n", System.currentTimeMillis()-startMilli);
			//Close the input stream
			br.close();			
		}
		catch (Exception e) {
			System.out.printf("File %s is invalid or not found, pls check and rerun", logFile);
			System.out.println(e.toString());
		}
	}
}
