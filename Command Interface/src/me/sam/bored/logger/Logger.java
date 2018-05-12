package me.sam.bored.logger;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

	public enum Severity{ INFO, WARNING, ERROR, FATAL_ERROR, DEBUG }
	
	/**
	 * Logs obj.toString()
	 * 
	 * @param s Severity Level (0- INFO, 1- WARNING, 2- ERROR, 3- FATAL_ERROR, 4- DEBUG)
	 * @param obj Object to print out
	 */
	public static void log(int s, Object obj){
		if(s >= 0 && s <= 4)
			log(Severity.values()[s], obj);
	}
	
	/**
	 * Logs obj.toString()
	 * 
	 * @param sev Severity Level
	 * @param obj Object to print out
	 */
	public static void log(Severity sev, Object obj) {
		String time = new SimpleDateFormat("[HH:mm:ss]").format(new Date());
		if(sev == Severity.ERROR || sev == Severity.FATAL_ERROR){
			System.err.printf("%s%s: %s%n", time, sev, obj);
			if(sev == Severity.FATAL_ERROR)
				System.exit(1);
		}else System.out.printf("%s%s: %s%n", time, sev, obj);
	}
	
	/**
	 * Prints out a blank line
	 */
	public static void line(){ System.out.println(); }

	/**
	 * Logs obj as info
	 * Format: [HH:mm:ss]INFO: obj.toString()
	 * 
	 * @param obj
	 */
	public static void info(Object obj){ log(0, obj); }

	/**
	 * Logs obj as warning
	 * Format: [HH:mm:ss]WARNING: obj.toString()
	 * 
	 * @param obj
	 */
	public static void warning(Object obj){ log(1, obj); }

	/**
	 * Logs obj as an error
	 * Format: [HH:mm:ss]ERROR: obj.toString()
	 * 
	 * @param obj
	 */
	public static void error(Object obj){ log(2, obj); }
	
	/**
	 * Logs obj as a Fatal Error.
	 * Logging something as a Fatal Error will shut the program down.
	 * Format: [HH:mm:ss]FATAL_ERROR: obj.toString()
	 * 
	 * @param obj
	 */
	public static void fatalError(Object obj){ log(3, obj); }
	
	/**
	 * Logs obj as debug
	 * Format: [HH:mm:ss]DEBUG: obj.toString()
	 * 
	 * @param obj
	 */
	public static void debug(Object obj){ log(4, obj); }
}
