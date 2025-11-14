package com.example.campusexpensemanager.models;

import android.content.Context;

import com.example.campusexpensemanager.Data.DatabaseContract;

public class Category {
    private int id;
    private String name;
    private String description;
    private String iconName;

    // Constructor, getters, setters
    public Category(int id, String name, String description, String iconName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.iconName = iconName;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getIconName() { return iconName; }


    public void setId(int id) { this.id = id;}
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setIconName(String iconName) { this.iconName = iconName; }

    public int getIconResId(Context context) {
        return context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());
    }
}
