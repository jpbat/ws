import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class IMDBCrawler {

	Crawler c;
	MovieList ml;
	
	private Logger logger;
	
	public IMDBCrawler() throws IOException{
		this.logger = new Logger("IMDb Crawler");
		this.c = new Crawler(this.logger);
		this.ml = new MovieList();
	}
	
	private void start(int slaves, int slaveId) {
		ArrayList<Movie> movies = new ArrayList<Movie>();
		ArrayList<Person> persons = new ArrayList<Person>();
		ArrayList<Studio> studios = new ArrayList<Studio>();
		
		//for (int year = 1874; year < 2024; year++) {
		for (int year = 1995 + slaveId; ; year = year + slaves) {
			movies = this.c.get(year);
			System.out.println(movies.size() + " movies fetched from " + year + "!");
			moviesToJSON(movies, year);
			new DataFixer(year);
			persons = this.c.parsePersons();
			System.out.println(persons.size() + " persons fetched from " + year + "!");
			personsToJSON(persons, year);

			studios = this.c.parseStudios();
			System.out.println(studios.size() + " studios fetched from " + year + "!");
			studiosToJSON(studios, year);
		}	
	}
	
	private static void moviesToJSON(ArrayList<Movie> movies, int year) {
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		String json = gson.toJson(movies);
		
		try {
			FileWriter writer = new FileWriter("../output/movies_" + year + ".json");
			writer.write(json);
			writer.close();
	 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void personsToJSON(ArrayList<Person> persons, int year) {
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		String json = gson.toJson(persons);
		
		try {
			FileWriter writer = new FileWriter("../output/persons_" + year + ".json");
			writer.write(json);
			writer.close();
	 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void studiosToJSON(ArrayList<Studio> studios, int year) {
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		String json = gson.toJson(studios);
		
		try {
			FileWriter writer = new FileWriter("../output/studios_" + year + ".json");
			writer.write(json);
			writer.close();
	 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		
		IMDBCrawler imdb = null;
		int slaves, slaveId;
		
		if (args.length < 2) {
			System.out.println("java Crawler [slaves] [slaveId]");
			return;
		}

		slaves = Integer.parseInt(args[0]);
		slaveId = Integer.parseInt(args[1]);
		
		System.out.println("There are " + slaves + " slaves, my id is: " + slaveId);
		
		try {
			imdb = new IMDBCrawler();
		} catch (IOException e) {
			imdb = null;
			System.out.println(Logger.logFileError);
			e.printStackTrace();
		}
		
		if (imdb != null) {
			imdb.start(slaves, slaveId);
		}
	}
}
