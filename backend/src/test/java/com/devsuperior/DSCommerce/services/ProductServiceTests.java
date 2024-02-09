package com.devsuperior.DSCommerce.services;

import com.devsuperior.DSCommerce.DTO.ProductDTO;
import com.devsuperior.DSCommerce.DTO.ProductMinDTO;
import com.devsuperior.DSCommerce.entities.Product;
import com.devsuperior.DSCommerce.repositories.ProductRepository;
import com.devsuperior.DSCommerce.services.exceptions.DataBaseException;
import com.devsuperior.DSCommerce.services.exceptions.ResourceNotFoundException;
import com.devsuperior.DSCommerce.tests.ProductFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService service;
    @Mock
    private ProductRepository repository;
    private String productName;
    private long existingId, nonExistingId, dependentyId;
    private Product product;
    private ProductDTO productDTO;
    private PageImpl<Product> page;
    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        nonExistingId = 2L;
        dependentyId = 3L;
        productName = "the Rings";

        product = ProductFactory.createProduct(productName);
        productDTO = new ProductDTO(product);
        page = new PageImpl<>(List.of(product));

        when(repository.findById(existingId)).thenReturn(Optional.ofNullable(product));
        when(repository.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        when(repository.getReferenceById(existingId)).thenReturn(product);
        when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        doNothing().when(repository).deleteById(existingId);
        doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
        doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentyId);

        when(repository.searchByName(any(), (Pageable)any())).thenReturn(page);
        when(repository.save(any())).thenReturn(product);
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists() {
        ProductDTO result = service.FindById(existingId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingId);
        Assertions.assertEquals(result.getName(), product.getName());
    }

    @Test
    public void findByIdShouldReturnResourceNotFoundExceptionWhenIdNonExistingId() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.FindById(nonExistingId);
        });
    }

    @Test
    public void findAllShouldReturnPageProductDTO(){
        Pageable pageable = PageRequest.of(0, 12);

        Page<ProductMinDTO> result = service.findAll(productName, pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getSize(), 1);
        Assertions.assertEquals(result.iterator().next().getName(), productName);
    }

    @Test
    public void insertShouldReturnProductDTO() {

        ProductDTO result = service.insert(productDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), product.getId());
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists() {

        ProductDTO result = service.update(existingId, productDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingId);
        Assertions.assertEquals(result.getName(), productDTO.getName());
    }

    @Test
    public void updateShouldReturnResourceNotFoundExceptionWhenIdDoesNotExist() {

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.update(nonExistingId, productDTO);
        });
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists(){

        Assertions.assertDoesNotThrow(() -> {
            service.delete(existingId);
        });
    }

    @Test
    public void deleteShouldDoThrowResourceNotFoundExceptionWhenIdDoesNotExist(){

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });
    }

    @Test
    public void deleteShouldDoThrowDataBaseExceptionWhenIdIsDependent(){

        Assertions.assertThrows(DataBaseException.class, () -> {
            service.delete(dependentyId);
        });
    }
}
