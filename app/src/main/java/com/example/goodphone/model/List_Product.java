package com.example.goodphone.model;

public class List_Product {
    public  String id;

    public String nameProduct;
    public double getSumRating() {
        return sumRating;
    }

    public void setSumRating(double sumRating) {
        this.sumRating = sumRating;
    }
    public String url_img_product;

    public int image_Main;
    public double sumRating,price,sold;
    public boolean  favourite;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public String getUrl_img_product() {
        return url_img_product;
    }

    public void setUrl_img_product(String url_img_product) {
        this.url_img_product = url_img_product;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getImage_Main() {
        return image_Main;
    }

    public void setImage_Main(int image_Main) {
        this.image_Main = image_Main;
    }

    public double getSold() {
        return sold;
    }

    public void setSold(double sold) {
        this.sold = sold;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public List_Product(String id, String url_img_product, String nameProduct, double price, double sold, double sumRating) {
        this.id = id;
        this.url_img_product = url_img_product;
        this.nameProduct = nameProduct;
        this.price = price;
        this.sold = sold;
        this.sumRating = sumRating;

    }
}
