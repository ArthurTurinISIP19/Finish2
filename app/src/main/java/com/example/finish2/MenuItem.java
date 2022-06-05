package com.example.finish2;

public class MenuItem {
    private String name;
    private int price;
    private String iconResource;
    private String category;

    public MenuItem(String name, int price, String iconResource, String category) {
        this.name = name;
        this.price = price;
        this.iconResource = iconResource;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getIconResource() {
        return iconResource;
    }

    public void setIconResource(String iconResource) {
        this.iconResource = iconResource;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
