package com.devsuperior.DSCommerce.projections;

import com.devsuperior.DSCommerce.entities.OrderStatus;

import java.time.Instant;

public interface OrderProjection {

    String getStatus();
    String getName();
    Long getId();
    Instant getMoment();
    String getProductName();
    Double getPrice();
    Integer getQuantity();
    String getImg_Url();
}
