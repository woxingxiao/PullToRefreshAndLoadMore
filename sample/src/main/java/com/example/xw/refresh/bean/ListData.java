package com.example.xw.refresh.bean;

public class ListData {

    private String publishedAt;
    private String url;

    public String getPublishedAt() {
        return publishedAt == null ? "" : publishedAt.split("T")[0];
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
