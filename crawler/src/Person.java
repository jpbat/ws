import java.util.ArrayList;

@SuppressWarnings("unused")
public class Person {
	
	private String name;
	private String birthDate;
	private String picture;
	private String id;
	private String miniBio;
	private String birthPlace;
	private boolean director;
	private boolean actor;
	private ArrayList<String> jobCategories;
	private ArrayList<String> knownFor;
	
	public Person(String id, String name, String birthDate, String picture, boolean director,
				boolean actor, ArrayList<String> jobCategories, String miniBio, String birthPlace,
				ArrayList<String> knownFor) {
		this.id = id;
		this.name = name;
		this.birthDate = birthDate;
		this.director = director;
		this.actor = actor;
		this.picture = picture;
		this.jobCategories = jobCategories;
		this.miniBio = miniBio;
		this.birthPlace = birthPlace;
		this.knownFor = knownFor;
	}

	public void setDirector(boolean director) {
		this.director = director;
	}

	public void setActor(boolean actor) {
		this.actor = actor;
	}
	
	public String toString() {
		return "Person [name=" + name + ", birthDate=" + birthDate + ", picture=" + picture
				+ ", director=" + director + ", actor=" + actor + "]";
	}
}
