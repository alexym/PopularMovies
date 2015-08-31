package alexym.com.popularmovies.Rest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexym on 22/08/15.
 */
public class ReviewsAndTrailers {

    private Trailers trailers;
    private Reviews reviews;
    private int runtime;

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public Trailers getTrailers() {
        return trailers;
    }

    public void setTrailers(Trailers trailers) {
        this.trailers = trailers;
    }

    public Reviews getReviews() {
        return reviews;
    }

    public void setReviews(Reviews reviews) {
        this.reviews = reviews;
    }

    public class Trailers {
        private List<Youtube> youtube = new ArrayList<Youtube>();

        public List<Youtube> getYoutube() {
            return youtube;
        }

        public void setYoutube(List<Youtube> youtube) {
            this.youtube = youtube;
        }

        public Trailers() {

        }
    }

    public class Reviews {

        private Integer page;
        private List<Result> results = new ArrayList<Result>();
        private Integer totalPages;
        private Integer totalResults;

        public Reviews() {

        }

        public Integer getPage() {
            return page;
        }

        public void setPage(Integer page) {
            this.page = page;
        }

        public List<Result> getResults() {
            return results;
        }

        public void setResults(List<Result> results) {
            this.results = results;
        }

        public Integer getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(Integer totalPages) {
            this.totalPages = totalPages;
        }

        public Integer getTotalResults() {
            return totalResults;
        }

        public void setTotalResults(Integer totalResults) {
            this.totalResults = totalResults;
        }

    }


}

