package alexym.com.popularmovies.Rest;

/**
 * Created by Cloudco on 31/08/15.
 */
public class Youtube {

    private String name;
    private String source;

    public Youtube(String name, String source) {
        this.name = name;
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }


}