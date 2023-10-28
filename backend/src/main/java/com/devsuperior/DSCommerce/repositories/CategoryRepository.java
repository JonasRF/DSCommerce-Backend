package com.devsuperior.DSCommerce.repositories;

import com.devsuperior.DSCommerce.entities.Category;
import com.devsuperior.DSCommerce.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
