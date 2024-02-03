package com.devsuperior.DSCommerce.repositories;

import com.devsuperior.DSCommerce.entities.OrderItem;
import com.devsuperior.DSCommerce.entities.OrderItemPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK> {

}
