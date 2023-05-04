package com.olinonee.mongodb.quickstart.entity;

import java.util.List;

/**
 * 电影实体
 *
 * @author olinH, olinone666@gmail.com
 * @version v1.0.0
 * @since 2023-05-04
 */
public class Movie {
    String plot;
    List<String> genres;
    String title;

    public String getPlot() {
        return plot;
    }
    public void setPlot(String plot) {
        this.plot = plot;
    }
    public List<String> getGenres() {
        return genres;
    }
    public void setGenres(List<String> genres) {
        this.genres = genres;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    @Override
    public String toString() {
        return "Movie [\n  plot=" + plot + ",\n  genres=" + genres + ",\n  title=" + title + "\n]";
    }
}
