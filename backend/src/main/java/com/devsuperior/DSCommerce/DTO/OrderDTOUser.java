package com.devsuperior.DSCommerce.DTO;

import com.devsuperior.DSCommerce.projections.OrderProjection;
import java.time.Instant;

public class OrderDTOUser {
    private String status;
    private String name;
    private Long id;
    private Instant moment;
    private String productName;
    private Double price;
    private Integer quantity;
    private String img_url;

    public OrderDTOUser() {}

    public OrderDTOUser(String status, String name, Long id, Instant moment, String productName, Double price, Integer quantity, String img_url) {
        this.status = status;
        this.name = name;
        this.id = id;
        this.moment = moment;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.img_url = img_url;
    }

    public OrderDTOUser(OrderProjection orderProjection){
        status = orderProjection.getStatus();
        name = orderProjection.getName();
        id = orderProjection.getId();
        moment = orderProjection.getMoment();
        productName = orderProjection.getProductName();
        price = orderProjection.getPrice();
        quantity = orderProjection.getQuantity();
        img_url = orderProjection.getImg_Url();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getMoment() {
        return moment;
    }

    public void setMoment(Instant moment) {
        this.moment = moment;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
