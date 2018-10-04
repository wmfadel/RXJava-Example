package com.example.mohamedfadel.tryrxjava;

import java.io.Serializable;

public class Torrent implements Serializable {

    private String url;                 // url
    private String quality;             // quality
    private String  size;               // size

    public Torrent(String url, String quality, String size) {
        this.url = url;
        this.quality = quality;
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Torrent{" +
                "url='" + url + '\'' +
                ", quality='" + quality + '\'' +
                ", size='" + size + '\'' +
                '}';
    }
}
