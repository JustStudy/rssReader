package com.example.likeRSS;


/**
 * Created with IntelliJ IDEA.
 * User: Ruslik
 * Date: 06.11.13
 * Time: 22:19
 * To change this template use File | Settings | File Templates.
 */
public class RssSingleItem {

    private String title;
    private String description;
    private String category;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public RssSingleItem(String title, String description, String category) {
        this.title = title;
        this.description = description;
        this.category = category;

    }

    public RssSingleItem() {
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getCategory() {
        return category;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
