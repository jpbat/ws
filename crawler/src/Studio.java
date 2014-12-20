import java.util.ArrayList;

@SuppressWarnings("unused")
public class Studio {
	
	private String name;
	private String id;
	
	public Studio(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public String toString() {
		return "Person [name=" + name + "]";
	}
}
