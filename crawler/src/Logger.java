import java.io.IOException;
import java.sql.Timestamp;


public class Logger {
	
	//Errors
	public static String failedWeb = "Unable to fetch webpage: ";
	public static String logFileError = "Unable to write to log file!";
	public static String jsonFileError = "Unable to write to json file!";
	public static String flush = "Unable to flush!";
	public static String start = "Starting to fetch ";
	public static String finishFailed = "Failed to fetch ";
	public static String failedMatch = "The tag href isn't a movie! ";
	public static String finishSuccess = "Finished to fetch ";

	//File paths	
	private String logPath = "../logs/log";
	
	public MyFile logFile;
	
	public Logger(String app) throws IOException {
		String timestamp = new Timestamp((new java.util.Date()).getTime()).toString();
		String name = logPath + "_" + app + "_" + timestamp + ".txt";
		this.logFile = new MyFile(name);
	}
	
	public void terminate() throws IOException {
		this.logFile.close();
	}
	
	public void log(String s) {
		String time = new Timestamp(new java.util.Date().getTime()).toString();
		while (time.length() < 23) {
			time += '0';
		}
		
		try {
			this.logFile.writeln("[" + time + "] " + s);
		} catch (IOException e) {
			System.out.println("[" + time + "] " + Logger.logFileError);
			System.out.println("[" + time + "] " + s);
		}
		
		try {
			this.logFile.flush();
		} catch (IOException e) {
			this.log(Logger.flush);
		}
	}
	
}
