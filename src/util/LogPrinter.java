package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogPrinter {

	public static void createLogFile(Exception e) {
		System.out.println("An error occured. Creating log file...");
		DateFormat dateFormat = new SimpleDateFormat("MM-dd_HH-mm");
		Date date = new Date();
		String filename = new String(dateFormat.format(date) + ".txt");
		try { 
			File file = new File("logs\\" + filename);
			file.getParentFile().mkdirs();
			PrintWriter pw = new PrintWriter(file);
			e.printStackTrace(pw);
			pw.close();
			System.out.println("Log file: \"" + filename + "\" created according to date. "
					+ "\nPlease check it to solve the problem."
					+ "\nAlso, here is the stackTrace for your conveience:");
			e.printStackTrace();
		} catch (FileNotFoundException fnfe) {
			System.out.println("Error while creating the log file!");
			fnfe.printStackTrace();
		}

	}
}
