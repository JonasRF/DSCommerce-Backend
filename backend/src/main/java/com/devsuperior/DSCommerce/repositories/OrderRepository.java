package com.devsuperior.DSCommerce.repositories;

import com.devsuperior.DSCommerce.entities.Order;
import com.devsuperior.DSCommerce.projections.OrderProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(nativeQuery = true, value = """
            SELECT tb_order.status, tb_user.name, tb_order.id, tb_order.moment, tb_product.name AS productName, tb_order_item.price, tb_order_item.quantity, tb_product.img_url
            FROM tb_order
            INNER JOIN tb_user ON tb_user.id = tb_order.client_id
            INNER JOIN tb_order_item ON tb_order_item.order_id = tb_order.id
            INNER JOIN tb_product  ON tb_product.id = tb_order_item.product_id
            WHERE tb_user.id = :userId
            
            """)
    List<OrderProjection> findAllOrderToUser(Long userId);
}
