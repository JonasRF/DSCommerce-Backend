package com.devsuperior.DSCommerce.controllers;

import com.devsuperior.DSCommerce.DTO.OrderDTO;
import com.devsuperior.DSCommerce.DTO.OrderDTOUser;
import com.devsuperior.DSCommerce.DTO.ProductDTO;
import com.devsuperior.DSCommerce.DTO.ProductMinDTO;
import com.devsuperior.DSCommerce.services.OrderService;
import com.devsuperior.DSCommerce.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/orders")
public class OrderController {

    @Autowired
    private OrderService service;

    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_CLIENT', 'ROLE_ADMIN')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<OrderDTO> findById(@PathVariable Long id) {
            OrderDTO dto = service.findById(id);
            return ResponseEntity.ok().body(dto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<OrderDTO>> findAll() {
        List<OrderDTO> dto = service.findAll();
        return ResponseEntity.ok().body(dto);
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    @GetMapping(value = "/{order}/{orderByUser}")
    public ResponseEntity<List<OrderDTOUser>> findAllOrderWithUser() {
        List<OrderDTOUser> dto = service.findAllOrderWithUser();
        return ResponseEntity.ok().body(dto);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<OrderDTO> update(@PathVariable Long id, @Valid @RequestBody OrderDTO dto) {
        dto = service.update(id, dto);
        return ResponseEntity.ok().body(dto);
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<OrderDTO> insert(@Valid @RequestBody OrderDTO dto) {
        dto = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }
}
