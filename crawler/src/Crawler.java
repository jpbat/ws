import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {
	
	private final String base = "http://www.imdb.com";
	private Logger l;
	public ArrayList<String> directorsUrls, actorsUrls, studiosUrls;
	public HashMap<String, Integer> map;
	
	public Crawler (Logger l) {
		this.l = l;
		this.directorsUrls = new ArrayList<String>();
		this.actorsUrls = new ArrayList<String>();
		this.studiosUrls = new ArrayList<String>();
		this.map = new HashMap<String, Integer>();
	}

	private Document getDocument(String url) {
		
		Document doc = null;
		try {
			doc = Jsoup.connect(url).userAgent("Chrome").header("Accept-Language", "en-US").get();
			return doc;
		} catch (IllegalArgumentException e) {
			this.l.log(Logger.failedWeb + url);
			return null;
		} catch (MalformedURLException e1) {
			this.l.log(Logger.failedWeb + url);
			return null;
		} catch (IOException e2) {
			this.l.log(Logger.failedWeb + url);
			return null;
		}
	}
	
	private Movie parseMovie(String url, String name) {
		
		this.l.log(Logger.start + name);
		Movie m = new Movie();
		m.genres = new Genres();
		m.stars = new Stars();
		m.studios = new Studios();
		m.directors = new Directors();
		m.genres.genre = new ArrayList<String>();
		m.stars.star = new ArrayList<String>();
		m.directors.director = new ArrayList<String>();
		m.studios.studio = new ArrayList<String>();
		
		String[] aux = url.split("/");
		m.id = aux[aux.length - 1];
		
		Document doc = getDocument(url);
		
		if (doc == null) {
			this.l.log(Logger.finishFailed + name);
			return null;
		}
		
		m.name = doc.select("#overview-top h1 .itemprop").text();
		m.image = doc.select("#img_primary img").attr("src");
		m.description = doc.select("#titleStoryLine .canwrap p").text();
		m.launchDate = doc.select("#overview-top .infobar .nobr a").text();
		m.duration = doc.select("#overview-top .infobar time").text();
		
		ArrayList<String> directors = new ArrayList<>();
		for (Element e : doc.select("[itemprop=director] a")) {
			String director = e.attr("href").split("/")[2];
			directors.add(director);
			
			if (!map.containsKey(director)) {
				this.map.put(director, 0);
				this.directorsUrls.add(e.attr("href"));
			}
		}
		m.directors.director.addAll(directors);

		String score = doc.select("#overview-top .star-box-giga-star").text();
		if (score.length() == 0) {
			m.score = -1;
		} else {
			m.score = Float.parseFloat(score);
		}
		
		ArrayList<String> genres = new ArrayList<>();
		for (Element e : doc.select("#overview-top .infobar a .itemprop")) {
			genres.add(e.text());
		}
		m.genres.genre.addAll(genres);
		
		ArrayList<String> stars = new ArrayList<>();
		try {
			for (Element e : doc.select("#overview-top .txt-block h4").get(2).parent().getElementsByTag("a")) {
				String star = e.attr("href").split("/")[2];;
				if (star.indexOf("See full") != -1)
					break;
				
				if (!map.containsKey(star)) {
					this.map.put(star, 0);
					this.actorsUrls.add(e.attr("href"));
				}
				
				stars.add(star);
			}
		} catch (Exception e) {
		}
		m.stars.star.addAll(stars);
		
		ArrayList<String> studios = new ArrayList<String>();
		for (Element e : doc.select(".txt-block [itemprop=creator] a")) {
			String studio = e.attr("href").split("/")[2].split("\\?")[0];
			studios.add(studio);
			
			if (!map.containsKey(studio)) {
				this.map.put(studio, 0);
				this.studiosUrls.add(e.attr("href"));
			}
		}
		
		m.studios.studio.addAll(studios);
		
		this.l.log(Logger.finishSuccess + name);
		
		return m;
	}
	
	private MovieList parseElements(Elements elements) {
		MovieList retval = new MovieList();
		retval.movie = new ArrayList<Movie>();
		
		for (int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);
			System.out.print("Working: " + 100 * i / elements.size() + "%\r");
			
			String aux = e.attr("href");

			if (aux.indexOf("/title/") == 0) {
				Movie m = parseMovie(base + aux, e.text());
				if (m != null)
					retval.movie.add(m);
					
			} else {
				this.l.log(Logger.failedMatch + aux);
			}
		}
		
		return retval;
	}
	
	public ArrayList<Movie> get(int year) {
		
		Document doc;
		MovieList ml;
		Elements elements;
		String url, filter = ".results tr .title>a";
		ArrayList<Movie> movies = new ArrayList<Movie>();

		System.out.println("Fetching movies from " + year);

		url = this.base + "/year/" + year;
		System.out.println(url);
		doc = getDocument(url);
		
		if (doc == null) {
			return new ArrayList<Movie>();
		}
		
		elements = doc.select(filter);
		
		if (elements.size() == 0) {
			return new ArrayList<Movie>();
		}

		ml = parseElements(elements);
		
		for (Movie m : ml.getMovie()) {
			movies.add(m);
		}

		for (int page = 51; page < 0 ;page += 50) {
		//for (int page = 51; ;page += 50) {
			url = this.base + "/search/title?sort=moviemeter,asc&start=" + page + "&title_type=feature&year=" + year;

			System.out.println(url);
			
			doc = getDocument(url);

			if (doc == null) {
				continue;
			}

			elements = doc.select(filter);
			
			if (elements.size() == 0) {
				break;
			}
			
			ml = parseElements(elements);
			
			for (Movie m : ml.getMovie()) {
				movies.add(m);
			}
		}

		return movies;
	}
	
	public ArrayList<String> getDirectorsUrls () {
		return this.directorsUrls;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Person> parsePersons() {
		
		Document doc;
		Date d;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String name, birthDate, picture, id, birthPlace, miniBio;
		ArrayList<Person> retval = new ArrayList<Person>();
		ArrayList<String> jobCategories = new ArrayList<String>();
		ArrayList<String> knownFor = new ArrayList<String>();
		
		System.out.println("Starting to fetch info from: " + this.directorsUrls.size() + " directors");
		
		for (int i = 0; i < this.directorsUrls.size(); i++) {
			
			System.out.print("Working: " + 100 * i / this.directorsUrls.size() + "% (" +  (i+1) + "/" + 
								+ this.directorsUrls.size() + ")\r");
			
			jobCategories.clear();
			knownFor.clear();
			String url = this.directorsUrls.get(i);
			if (!url.startsWith("/name")) {
				continue;
			}
			doc = getDocument(base + url);
			if (doc == null) {
				i--;
				continue;
			}
			name = doc.select("#overview-top .header .itemprop").text();
			birthDate = doc.select("time").attr("datetime");
			
			try {
				d = df.parse(birthDate);
				Calendar cal = Calendar.getInstance();
				cal.setTime(d);
				
				birthDate = cal.get(Calendar.YEAR) + "-";
				
				if (cal.get(Calendar.MONTH) + 1 < 10) {
					birthDate += "0";
				}
				birthDate += cal.get(Calendar.MONTH) + 1 + "-";
				
				if (cal.get(Calendar.DAY_OF_MONTH) < 10) {
					birthDate += "0";
				}
				birthDate += cal.get(Calendar.DAY_OF_MONTH) + "T0:00:00";
				
			} catch (Exception e) {
				birthDate = ("00-00-00T0:00:00");
			}
			
			picture = doc.select("#name-poster").attr("src");
			
			for (Element e: doc.select("#name-job-categories a")) {
				jobCategories.add(e.text());
			}
			
			id = url.split("/")[2];
			
			Elements els = doc.select("#knownfor a");
			for (int j = 0; j < els.size(); j+=2) {
				knownFor.add(els.get(j).attr("href").split("/")[2].split("\\?")[0]);
			}
			
			//WE NEED TO GET DEEPER!!!
			doc = getDocument(base + "/name/" + id + "/bio");

			try {
				miniBio = doc.select("#overviewTable tr a").last().text();
			} catch (Exception e) {
				miniBio = "(not available)";
			}
		
			try {
				birthPlace = doc.select("#bio_content .soda.odd p").first().text();
			} catch (Exception e) {
				birthPlace = "(not available)";
			}
			
			Person p = new Person(id, name, birthDate, picture, false, true, (ArrayList<String>) jobCategories.clone(), birthPlace, miniBio, (ArrayList<String>) knownFor.clone());
			retval.add(p);
		}
		
		System.out.println("Starting to fetch info from: " + this.actorsUrls.size() + " actors");
		
		for (int i = 0; i < this.actorsUrls.size(); i++) {
			
			System.out.print("Working: " + 100 * i / this.actorsUrls.size() + "% (" +  (i+1) + "/" + 
					+ this.actorsUrls.size() + ")\r");
			
			jobCategories.clear();
			knownFor.clear();
			String url = this.actorsUrls.get(i);
			if (!url.startsWith("/name")) {
				continue;
			}
			doc = getDocument(base + url);
			if (doc == null) {
				i--;
				continue;
			}
			name = doc.select("#overview-top .header .itemprop").text();
			birthDate = doc.select("time").attr("datetime");
			
			try {
				d = df.parse(birthDate);
				Calendar cal = Calendar.getInstance();
				cal.setTime(d);
				
				birthDate = cal.get(Calendar.YEAR) + "-";
				
				if (cal.get(Calendar.MONTH) + 1 < 10) {
					birthDate += "0";
				}
				birthDate += cal.get(Calendar.MONTH) + 1 + "-";
				
				if (cal.get(Calendar.DAY_OF_MONTH) < 10) {
					birthDate += "0";
				}
				birthDate += cal.get(Calendar.DAY_OF_MONTH);
				
			} catch (Exception e) {
				birthDate = ("00-00-00");
			}
			
			picture = doc.select("#name-poster").attr("src");
			
			for (Element e: doc.select("#name-job-categories a")) {
				jobCategories.add(e.text());
			}
			
			id = url.split("/")[2];
			
			Elements els = doc.select("#knownfor a");
			for (int j = 0; j < els.size(); j+=2) {
				knownFor.add(els.get(j).attr("href").split("/")[2].split("\\?")[0]);
			}
			
			//WE NEED TO GET DEEPER!!!
			doc = getDocument(base + "/name/" + id + "/bio");

			try {
				miniBio = doc.select("#overviewTable tr a").last().text();
			} catch (Exception e) {
				miniBio = "(not available)";
			}
		
			try {
				birthPlace = doc.select("#bio_content .soda.odd p").first().text();
			} catch (Exception e) {
				birthPlace = "(not available)";
			}
			
			Person p = new Person(id, name, birthDate, picture, true, false, (ArrayList<String>) jobCategories.clone(), birthPlace, miniBio, (ArrayList<String>) knownFor.clone());
			retval.add(p);
		}
		
		return retval;
	}
	
	public ArrayList<Studio> parseStudios() {
		ArrayList<Studio> retval = new ArrayList<Studio>();
		
		Document doc;
		System.out.println("Starting to fetch info from: " + this.studiosUrls.size() + " studios");
		
		for (int i = 0; i < this.studiosUrls.size(); i++) {
			
			System.out.print("Working: " + 100 * i / this.studiosUrls.size() + "% (" +  (i+1) + "/" + 
								+ this.studiosUrls.size() + ")\r");
			
			String url = this.studiosUrls.get(i);
			if (!url.startsWith("/company")) {
				continue;
			}
			
			doc = getDocument(base + url);
			if (doc == null) {
				i--;
				continue;
			}
			
			String id = this.studiosUrls.get(i).split("/")[2].split("\\?")[0];
			String name = doc.select(".title").text();
			Studio s = new Studio(id, name);
			retval.add(s);
		}
		
		return retval;
	}
	
	private Serie fetchSerie(String id) {
		String aux;
		Serie s = new Serie();
		s.genres = new Genres();
		s.stars = new Stars();
		s.studios = new Studios();
		s.genres.genre = new ArrayList<String>();
		s.stars.star = new ArrayList<String>();
		s.studios.studio = new ArrayList<String>();
		
		Document doc = getDocument(this.base + "/title/" + id);
		
		s.name = doc.select("#overview-top h1 .itemprop").text();
		s.image = doc.select("#img_primary img").attr("src");
		s.description = doc.select("#titleStoryLine .canwrap p").text();
		try {
			s.start = Integer.parseInt(doc.select(".header .nobr").text().split("\\–")[0].substring(1));
			aux = doc.select(".header .nobr").text().split("\\–")[1];
			s.end = Integer.parseInt(aux.substring(0, aux.length() - 1));
		} catch (NumberFormatException e) {
			s.end = 0;
		}
		s.duration = Integer.parseInt(doc.select("#overview-top .infobar time").text().split(" ")[0]);

		String score = doc.select("#overview-top .star-box-giga-star").text();
		if (score.length() == 0) {
			s.score = -1;
		} else {
			s.score = Float.parseFloat(score);
		}
		
		ArrayList<String> genres = new ArrayList<>();
		for (Element e : doc.select("#overview-top .infobar a .itemprop")) {
			genres.add(e.text());
		}
		s.genres.genre.addAll(genres);
		
		ArrayList<String> stars = new ArrayList<>();
		try {
			Elements es = doc.select("[itemprop=actors] a");
			for (Element e : es) {
				String star = e.attr("href").split("/")[2];;
				if (star.indexOf("See full") != -1)
					break;
				stars.add(star);
			}
		} catch (Exception e) {
		}
		s.stars.star.addAll(stars);
		
		ArrayList<String> studios = new ArrayList<String>();
		for (Element e : doc.select(".txt-block [itemprop=creator] a")) {
			String studio = e.attr("href").split("/")[2].split("\\?")[0];
			studios.add(studio);
		}
		
		s.studios.studio.addAll(studios);
		
		int seasons;
		for (int i = 0; ; i++) {
			aux = doc.select(".seasons-and-year-nav div a").get(i).text();
			try {
				seasons = Integer.parseInt(aux);
			} catch (NumberFormatException e) {
				continue;
			}
			break;
		}
		s.setSeasons(seasons);
		s.setId(id);
		
		return s;
		
	}
	
	public ArrayList<Serie> parseKnownFor(ArrayList<Person> persons) {

		String id;
		Document doc;
		ArrayList<Serie> retval = new ArrayList<Serie>();
		
		System.out.println("Starting to fetch series from: " + persons.size() + " actors to find series");
		
		for (int j = 0; j < persons.size(); j++) {
			
			System.out.print("Working: " + 100 * j / persons.size() + "% (" +  (j+1) + "/" + 
					+ persons.size() + ")\r");
			
			for (int i = 0; i < persons.get(j).getKnownFor().size(); i++) {
				id = persons.get(j).getKnownFor().get(i);
				
				doc = getDocument(base + "/title/" + id);
				if (doc == null) {
					i--;
					continue;
				}
				
				String infobar = doc.select(".infobar").text();
				
				boolean isSeries = infobar.contains("TV Series");
				
				if (isSeries == false && this.map.containsKey(id) == false) {
					continue;
				}
				
				this.map.put(id, 0);
				retval.add(fetchSerie(id));
			}
		}
		
		return retval;
	}
}

