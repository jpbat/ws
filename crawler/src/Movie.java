
public class Movie {

    protected String name;
    protected String id;
    protected String image;
    protected double score;
    protected String duration;
    protected String launchDate;
    protected Genres genres;
    protected Directors directors;
    protected Studios studios;
    protected Stars stars;
    protected String description;

    public String getName() {
        return name;
    }
    
    public String getId() {
        return id;
    }

    public void setName(String value) {
        this.name = value;
    }

    public void setId(String value) {
        this.id = value;
    }
    
    public String getImage() {
        return image;
    }

    public void setImage(String value) {
        this.image = value;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double value) {
        this.score = value;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String value) {
        this.duration = value;
    }

    public String getLaunchDate() {
        return launchDate;
    }

    public void setLaunchDate(String value) {
        this.launchDate = value;
    }

    public Genres getGenres() {
        return genres;
    }

    public void setGenres(Genres value) {
        this.genres = value;
    }

    public Directors getDirector() {
        return directors;
    }

    public void setDirector(Directors value) {
        this.directors = value;
    }

    public Studios getStudio() {
        return studios;
    }

    public void setStudio(Studios value) {
        this.studios = value;
    }

    public Stars getStars() {
        return stars;
    }

    public void setStars(Stars value) {
        this.stars = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String value) {
        this.description = value;
    }
    
    public String toString() {
    	return "\t" + this.name + "\n" +
    			"\tfrom: " + this.directors + "\n" +
    			"\tscore: " + this.score + "\n";
    }
}
