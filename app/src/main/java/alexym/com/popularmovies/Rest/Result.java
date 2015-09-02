package alexym.com.popularmovies.Rest;

/**
 * Created by Cloudco on 31/08/15.
 */
public class Result {

    private String id;
    private String author;
    private String content;
    private String url;

    public Result(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}