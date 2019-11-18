package com.example.indika.myapplication;

public class ReadingItem {
    private String title;
    private String category;
    private String type;
    private String id;

    public ReadingItem( String id, String title, String category, String type) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.type = type;
    }
    public String getId(){
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getType() {
        return type;
    }
}
