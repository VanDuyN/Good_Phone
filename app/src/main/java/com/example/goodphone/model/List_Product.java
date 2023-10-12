package com.example.goodphone.model;

public class List_Product {
    public String nameProduct;

    public String getUrl_img_product() {
        return url_img_product;
    }

    public void setUrl_img_product(String url_img_product) {
        this.url_img_product = url_img_product;
    }

    public int getSumRating() {
        return sumRating;
    }

    public void setSumRating(int sumRating) {
        this.sumRating = sumRating;
    }

    public String url_img_product;

    public int getImage_Main() {
        return image_Main;
    }


    public void setImage_Main(int image_Main) {
        this.image_Main = image_Main;
    }

    public int price, image_Main;
    public int sold;
    public int sumRating;
    public boolean  favourite;

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public int getSungRating() {
        return sumRating;
    }

    public void setSungRating(int sungRating) {
        this.sumRating = sungRating;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public List_Product(String url_img_product,String nameProduct, int price, int sold, int sungRating) {
        this.url_img_product = url_img_product;
        this.nameProduct = nameProduct;
        this.price = price;
        this.sold = sold;
        this.sumRating = sungRating;

    }
}
