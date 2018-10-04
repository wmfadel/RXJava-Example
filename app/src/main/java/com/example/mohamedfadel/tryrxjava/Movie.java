package com.example.mohamedfadel.tryrxjava;

import java.io.Serializable;
import java.util.ArrayList;

public class Movie implements Serializable{

    private String title;                   // title_long
    private String trailer;                 // yt_trailer_code
    private float rate;                     // ratting
    private ArrayList<String> genres;       // genres
    private String description;             // description_full
    private String language;                // language
    private String backgroundURL;           // background_image
    private String coverURL;                // medium_cover_image
    private String date;                    // date_uploaded
    private ArrayList<Torrent> torrents;    // torrents

    public Movie() {
    }

    public Movie(String title, String trailer, float rate, ArrayList<String> genres,
                 String description, String language, String backgroundURL, String coverURL,
                 String date) {
        this.title = title;
        this.trailer = trailer;
        this.rate = rate;
        this.genres = genres;
        this.description = description;
        this.language = language;
        this.backgroundURL = backgroundURL;
        this.coverURL = coverURL;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getBackgroundURL() {
        return backgroundURL;
    }

    public void setBackgroundURL(String backgroundURL) {
        this.backgroundURL = backgroundURL;
    }

    public String getCoverURL() {
        return coverURL;
    }

    public void setCoverURL(String coverURL) {
        this.coverURL = coverURL;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<Torrent> getTorrents() {
        return torrents;
    }

    public void setTorrents(ArrayList<Torrent> torrents) {
        this.torrents = torrents;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", trailer='" + trailer + '\'' +
                ", rate=" + rate +
                ", genres=" + genres +
                ", description='" + description + '\'' +
                ", language='" + language + '\'' +
                ", backgroundURL='" + backgroundURL + '\'' +
                ", coverURL='" + coverURL + '\'' +
                ", date='" + date + '\'' +
                ", torrents=" + torrents +
                '}';
    }

}
