package com.devsuperior.DSCommerce.tests;

import com.devsuperior.DSCommerce.entities.Category;
import com.devsuperior.DSCommerce.entities.Product;

public class ProductFactory {

    public static Product createProduct(){
        Category category = CategoryFactory.createCategory();
        Product product = new Product(1L, "The Lord of the Rings", "Lorem ipsum dolor sit amet", 90.5, "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg");
        product.getCategories().add(category);
        return product;
    }

    public static Product createProduct(String name){
        Product product = createProduct();
        product.setName(name);
        return product;
    }
}
