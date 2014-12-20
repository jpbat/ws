import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

//this class serves only to cleanup the input. modify with caution
public class DataFixer implements Runnable {
	
	private Thread t;
	private int year;
	
	public DataFixer(int year) {
		this.year = year;
		this.t = new Thread(this);
		t.start();
	}
	
	private void toJSON(ArrayList<Movie> movies) {
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		String json = gson.toJson(movies);
		
		try {
			FileWriter writer = new FileWriter("../output/movies_" + this.year + ".json");
			writer.write(json);
			writer.close();
	 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		Date d;
		Movie m;
		int index;
		FileReader fr;
		String duration;
		JsonArray jArray;
		DateFormat df = new SimpleDateFormat("dd MMM yyyy");
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		ArrayList<Movie> movies =  new ArrayList<Movie>();
		
		try {
			fr = new FileReader("../output/movies_" + this.year + ".json");
			jArray = parser.parse(fr).getAsJsonArray();
			for (JsonElement j : jArray) {
				m = gson.fromJson(j, Movie.class);
				
				//Fixing a bug with rounding the movie score
				if (m.getScore() > 0) {
					int score = (int) (m.getScore() * 10);
					m.setScore((double)score/10);
				}
				
				//Fixing the date format
				String launch = m.getLaunchDate();
				if (launch.length() > 0) {
					index = launch.indexOf(" (");
					
					launch = (String) launch.subSequence(0, index);
					int size = (launch.split(" ")).length;
					
					if (size == 1) {
						launch = "1 January " + launch;
					} else if (size == 2) {
						launch = "1 " + launch;
					}

					try {
						d = df.parse(launch);
					} catch (Exception e) {
						System.out.println(launch);
						break;
					}
					
					m.setLaunchDate(d.toString());
					
				}
				
				//Fixing the duration
				duration = m.getDuration(); 
				if (duration.length() > 0 ) {
					duration = duration.split(" ")[0];
					m.setDuration(duration);
				}
				
				movies.add(m);
			}
		toJSON(movies);
		} catch (IOException e) {
		}
		
		System.out.println("[" + year + "] " + movies.size() + " movies processed");
	}
}
