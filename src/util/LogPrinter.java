package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogPrinter {
	
	public static void createLogFile(Exception e) throws FileNotFoundException{
		System.out.println("Creating log file...");
		DateFormat dateFormat = new SimpleDateFormat("MM-dd_HH-mm");
		Date date = new Date();
		String filename = new String(dateFormat.format(date) + ".txt");
		System.out.println("Log file: \"" + filename + "\" created according to date. "
				+ "\nPlease check it to solve the problem.");
		PrintWriter pw = new PrintWriter(new File(filename));
		e.printStackTrace(pw);
		pw.close();
	}
}
