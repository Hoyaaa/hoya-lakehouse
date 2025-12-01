package kr.ac.nsu.hakbokgs.main.advertising;

import com.google.firebase.Timestamp;

public class Advertisement {

    private String title;
    private String url;
    private Timestamp expiration;
    private String imageUrl;

    public Advertisement() {}

    public Advertisement(String title, String url, Timestamp expiration, String imageUrl) {
        this.title = title;
        this.url = url;
        this.expiration = expiration;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public Timestamp getExpiration() {
        return expiration;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setExpiration(Timestamp expiration) {
        this.expiration = expiration;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
