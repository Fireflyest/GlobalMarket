package com.fireflyest.market.bean;

import java.util.List;

/**
 * last version
 *
 * @author Fireflyest
 * @version 1.0.0
 * @since 2022/7/30
 */

public class Latest {


    @com.google.gson.annotations.SerializedName("author")
    private AuthorDTO author;
    @com.google.gson.annotations.SerializedName("name")
    private String name;
    @com.google.gson.annotations.SerializedName("published_at")
    private String publishedAt;
    @com.google.gson.annotations.SerializedName("assets")
    private java.util.List<AssetsDTO> assets;
    @com.google.gson.annotations.SerializedName("body")
    private String body;

    public Latest(){
    }

    public AuthorDTO getAuthor() {
        return author;
    }

    public String getName() {
        return name;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public List<AssetsDTO> getAssets() {
        return assets;
    }

    public String getBody() {
        return body;
    }

    public static class AuthorDTO {
        @com.google.gson.annotations.SerializedName("login")
        private String login;

        public String getLogin() {
            return login;
        }
    }

    public static class AssetsDTO {
        @com.google.gson.annotations.SerializedName("name")
        private String name;
        @com.google.gson.annotations.SerializedName("size")
        private Long size;
        @com.google.gson.annotations.SerializedName("download_count")
        private Integer downloadCount;
        @com.google.gson.annotations.SerializedName("updated_at")
        private String updatedAt;
        @com.google.gson.annotations.SerializedName("browser_download_url")
        private String browserDownloadUrl;

        public String getName() {
            return name;
        }

        public Long getSize() {
            return size;
        }

        public Integer getDownloadCount() {
            return downloadCount;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public String getBrowserDownloadUrl() {
            return browserDownloadUrl;
        }

    }
}
