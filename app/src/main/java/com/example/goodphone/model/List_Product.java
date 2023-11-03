package com.example.goodphone.model;

public class List_Product {
    public String idUser;
    public boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public  String id;
    public int quantity;
    public String nameProduct;
    public double getSumRating() {
        return sumRating;
    }

    public void setSumRating(double sumRating) {
        this.sumRating = sumRating;
    }
    public String url_img_product;

    public int image_Main,price,sold;
    public double sumRating;
    public boolean  favourite;

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
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

    public void setSold(int sold) {
        this.sold = sold;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public List_Product(String id, String url_img_product, String nameProduct) {
        this.id = id;
        this.url_img_product = url_img_product;
        this.nameProduct = nameProduct;

    }

    public List_Product(String id, String url_img_product, String nameProduct, int price, int sold, double sumRating) {
        this.id = id;
        this.url_img_product = url_img_product;
        this.nameProduct = nameProduct;
        this.price = price;
        this.sold = sold;
        this.sumRating = sumRating;
    }
    public List_Product(String uId,String id, String url_img_product, String nameProduct, int price,  int quantity) {
        this.idUser = uId;
        this.id = id;
        this.url_img_product = url_img_product;
        this.nameProduct = nameProduct;
        this.price = price;
        this.quantity = quantity;
    }
    public List_Product(String id,  int quantity,int price,String name, String url) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.nameProduct = name;
        this.url_img_product = url;
    }

}
