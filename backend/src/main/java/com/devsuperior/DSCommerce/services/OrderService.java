package com.devsuperior.DSCommerce.services;

import com.devsuperior.DSCommerce.DTO.OrderDTO;
import com.devsuperior.DSCommerce.DTO.OrderItemDTO;
import com.devsuperior.DSCommerce.entities.*;
import com.devsuperior.DSCommerce.repositories.OrderItemRepository;
import com.devsuperior.DSCommerce.repositories.OrderRepository;
import com.devsuperior.DSCommerce.repositories.ProductRepository;
import com.devsuperior.DSCommerce.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id){
        Order order =  repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource not found!"));
        authService.validateSelfOrAdmin(order.getClient().getId());
        return new OrderDTO(order);
    }
    @Transactional
    public OrderDTO insert(OrderDTO dto){

        Order order = new Order();
        order.setMoment(Instant.now());
        order.setStatus(OrderStatus.WAITING_PAYMENT);

        User user = userService.authenticated();
        order.setClient(user);

        for(OrderItemDTO orderDto : dto.getItems()){
            Product product = productRepository.getReferenceById(orderDto.getProductId());
            OrderItem item = new OrderItem(order, product, orderDto.getQuantity(), product.getPrice());
            order.getItems().add(item);
        }

        repository.save(order);
        orderItemRepository.saveAll(order.getItems());

        return new OrderDTO(order);
    }
}
