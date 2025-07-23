package com.example.store.Domain;

public class CategoryDomain {
    private String ImagePath;
    private String Name;

    private int id;

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return Name ;
    }

    public CategoryDomain() {

    }
}
