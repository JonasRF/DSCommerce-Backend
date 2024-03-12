package com.devsuperior.DSCommerce.services;

import com.devsuperior.DSCommerce.DTO.OrderDTO;
import com.devsuperior.DSCommerce.entities.Order;
import com.devsuperior.DSCommerce.entities.OrderItem;
import com.devsuperior.DSCommerce.entities.Product;
import com.devsuperior.DSCommerce.entities.User;
import com.devsuperior.DSCommerce.repositories.OrderItemRepository;
import com.devsuperior.DSCommerce.repositories.OrderRepository;
import com.devsuperior.DSCommerce.repositories.ProductRepository;
import com.devsuperior.DSCommerce.services.exceptions.ForbiddenException;
import com.devsuperior.DSCommerce.services.exceptions.ResourceNotFoundException;
import com.devsuperior.DSCommerce.tests.OrderFactory;
import com.devsuperior.DSCommerce.tests.ProductFactory;
import com.devsuperior.DSCommerce.tests.UserFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class OrderServiceTests {

    @InjectMocks
    private OrderService service;
    @Mock
    private OrderRepository repository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private AuthService authService;
    @Mock
    private UserService userService;
    private Long existingOrderId, nonExistingOrderId;
    private Long existingProductId, nonExistingProductId;
    private Order order;
    private Product product;
    private OrderDTO orderDTO;
    private User admin, client, otherClient;
    @BeforeEach
    void setUp() throws Exception{
        existingOrderId = 1L;
        nonExistingOrderId = 2L;

        existingProductId = 1L;
        nonExistingProductId = 2L;

        admin = UserFactory.createAdminUser();
        client = UserFactory.createClientUser();

        order = OrderFactory.createOrder(client);

        product = ProductFactory.createProduct();

        orderDTO = new OrderDTO(order);

        when(productRepository.getReferenceById(existingProductId)).thenReturn(product);
        when(productRepository.getReferenceById(nonExistingProductId)).thenThrow(EntityNotFoundException.class);

        when(repository.findById(existingOrderId)).thenReturn(Optional.of(order));
        when(repository.findById(nonExistingOrderId)).thenReturn(Optional.empty());

        when(repository.save(any())).thenReturn(order);
        when(orderItemRepository.saveAll(any())).thenReturn(new ArrayList<>(order.getItems()));
    }

    @Test
    public void findByIdShouldReturnOrderDTOWhenIdExistsAndAdminLogged() {

        doNothing().when(authService).validateSelfOrAdmin(admin.getId());

        OrderDTO result = service.findById(existingOrderId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingOrderId);
    }

    @Test
    public void findByIdShouldReturnOrderDTOWhenIdExistsAndClientLogged() {

        doNothing().when(authService).validateSelfOrAdmin(client.getId());

        OrderDTO result = service.findById(existingOrderId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingOrderId);
    }

    @Test
    public void findByIdShouldThrowsForbiddenExceptionWhenIdExistsAndOtherClientLogged() {

        doThrow(ForbiddenException.class).when(authService).validateSelfOrAdmin(any());

        Assertions.assertThrows(ForbiddenException.class, () -> {
            service.findById(existingOrderId);
        });
    }

    @Test
    public void findByIdShouldThrowsResourceNotFoundExceptionWhenIdDoesNotExistsAndOtherClientLogged() {

        doThrow(ResourceNotFoundException.class).when(authService).validateSelfOrAdmin(any());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingOrderId);
        });
    }

    @Test
    public void insertShouldReturnOrderDTOWhenAdminLogged() {

      when(userService.authenticated()).thenReturn(admin);

      OrderDTO result = service.insert(orderDTO);

      Assertions.assertNotNull(result);
    }

    @Test
    public void insertShouldReturnOrderDTOWhenClientLogged() {

        when(userService.authenticated()).thenReturn(client);

        OrderDTO result = service.insert(orderDTO);

        Assertions.assertNotNull(result);
    }

    @Test
    public void insertShouldReturnThrowsUserNameNotFoundExceptionWhenUserNotLogged() {

        doThrow(UsernameNotFoundException.class).when(userService).authenticated();

        order.setClient(new User());
        orderDTO = new OrderDTO(order);

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            service.insert(orderDTO);
        });
    }

    @Test
    public void insertShouldReturnEntityNotFoundExceptionWhenOrderProductIdDoesNotExist() {

        when(userService.authenticated()).thenReturn(client);

        product.setId(nonExistingProductId);
        OrderItem orderItem = new OrderItem(order, product, 2, 10.0);
        order.getItems().add(orderItem);

        orderDTO = new OrderDTO(order);

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            service.insert(orderDTO);
        });
    }
}
