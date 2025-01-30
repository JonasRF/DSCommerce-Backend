package com.devsuperior.DSCommerce.services;

import com.devsuperior.DSCommerce.DTO.CategoryDTO;
import com.devsuperior.DSCommerce.DTO.ProductDTO;
import com.devsuperior.DSCommerce.DTO.ProductMinDTO;
import com.devsuperior.DSCommerce.DTO.UriDTO;
import com.devsuperior.DSCommerce.entities.Category;
import com.devsuperior.DSCommerce.entities.Product;
import com.devsuperior.DSCommerce.repositories.CategoryRepository;
import com.devsuperior.DSCommerce.repositories.ProductRepository;
import com.devsuperior.DSCommerce.services.exceptions.DataBaseException;
import com.devsuperior.DSCommerce.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private S3Service s3Service;

    @Autowired
    private ProductRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public ProductDTO FindById(Long id){
        Optional<Product> obj =  repository.findById(id);
        Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Resource not found!"));
        return new ProductDTO(entity);
    }
    @Transactional(readOnly = true)
    public Page<ProductMinDTO> findAll( String name, Pageable pageable) {
        Page<Product> result = repository.searchByName(name, pageable);
        return result.map(ProductMinDTO::new);
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        Product entity = new Product();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        try{
            Product entity = repository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new ProductDTO(entity);
        }
    catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Resource not found!");
     }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if(!repository.existsById(id)){
            throw new ResourceNotFoundException("Resource not found!");
        }
        try{
            repository.deleteById(id);
        }
        catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Resource not found!");
        }
        catch (DataIntegrityViolationException e){
            throw new DataBaseException("Falha de integridade referencial!");
        }
    }
    private void copyDtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImgUrl(dto.getImgUrl());

        entity.getCategories().clear();
        for(CategoryDTO catDTO : dto.getCategories()){
            Category cat = new Category();
            cat.setId(catDTO.getId());
            cat.setName(catDTO.getName());
            entity.getCategories().add(cat);
        }
    }

    public UriDTO uploadFile(MultipartFile file) {
        URL url = s3Service.uploadFile(file);
        return new UriDTO(url.toString());
    }
}
