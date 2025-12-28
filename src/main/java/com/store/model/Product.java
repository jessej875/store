package com.store.model;

public class Product {
    private final int id;
    private final String title;
    private final String description;
    private final double price;
    private final String image;
    private final String category;

    public Product(int id, String title, String description, double price, String image, String category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.image = image;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public String getCategory() {
        return category;
    }
}