package com.ebda3.Model;

/**
 * Created by work on 15/10/2017.
 */

public class Product  {
    public String ProductImage;
    public String ProductID;
    public String ProductName;
    public String ProductPrice;
    public String ProductCategoryName;
    public String ProductCategoryID;
    public String ProductTypeName;
    public String ProductTypeID;

    public Product()
    {
        ProductImage = "";
        ProductID = "";
        ProductName = "";
        ProductPrice = "";
        ProductCategoryName="";
        ProductCategoryID="";
        ProductTypeName="";
        ProductTypeID="";

    }

    public Product(String productImage, String productID, String productName, String productPrice, String productCategoryName, String productCategoryID, String productTypeName, String productTypeID) {
        ProductImage = productImage;
        ProductID = productID;
        ProductName = productName;
        ProductPrice = productPrice;
        ProductCategoryName = productCategoryName;
        ProductCategoryID = productCategoryID;
        ProductTypeName = productTypeName;
        ProductTypeID = productTypeID;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getProductImage() {
        return ProductImage;
    }

    public void setProductImage(String productImage) {
        ProductImage = productImage;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice;
    }

    public String getProductCategoryName() {
        return ProductCategoryName;
    }

    public void setProductCategoryName(String productCategoryName) {
        ProductCategoryName = productCategoryName;
    }

    public String getProductCategoryID() {
        return ProductCategoryID;
    }

    public void setProductCategoryID(String productCategoryID) {
        ProductCategoryID = productCategoryID;
    }

    public String getProductTypeName() {
        return ProductTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        ProductTypeName = productTypeName;
    }

    public String getProductTypeID() {
        return ProductTypeID;
    }

    public void setProductTypeID(String productTypeID) {
        ProductTypeID = productTypeID;
    }
}
