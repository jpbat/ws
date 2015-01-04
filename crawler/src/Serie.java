
public class Serie {

    protected String name;
    protected String id;
    protected String image;
    protected double score;
    protected int duration;
    protected int start;
    protected int end;
    protected Genres genres;
    protected Studios studios;
    protected Stars stars;
    protected String description;
    protected int seasons;

    public String getName() {
        return name;
    }
    
    public String getId() {
        return id;
    }

    public void setName(String value) {
        this.name = value;
    }
    
    public void setSeasons(int value) {
        this.seasons = value;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int value) {
        this.duration = value;
    }

    public void setStart(int value) {
        this.start = value;
    }


    public void setEnd(int value) {
        this.end = value;
    }

    public Genres getGenres() {
        return genres;
    }

    public void setGenres(Genres value) {
        this.genres = value;
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

	@Override
	public String toString() {
		return "Serie [name=" + name + ", id=" + id + ", image=" + image
				+ ", score=" + score + ", duration=" + duration + ", start="
				+ start + ", end=" + end + ", genres=" + genres + ", studios="
				+ studios + ", stars=" + stars + ", description=" + description
				+ ", seasons=" + seasons + "]";
	}
}
