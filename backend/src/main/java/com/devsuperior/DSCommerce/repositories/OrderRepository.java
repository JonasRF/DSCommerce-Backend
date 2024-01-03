package com.devsuperior.DSCommerce.repositories;

import com.devsuperior.DSCommerce.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
