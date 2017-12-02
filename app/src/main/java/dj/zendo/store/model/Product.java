package dj.zendo.store.model;

import android.graphics.Bitmap;

import java.math.BigDecimal;

public class Product {

    private int id;
    private String name;
    private String item;
    private String category;
    private BigDecimal price;
    private String imagePath;
    private Bitmap image;

    public Product() {

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return this.name;
    }

    public String getItem() {
        return item;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}