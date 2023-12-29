package com.devsuperior.DSCommerce.repositories;

import com.devsuperior.DSCommerce.entities.PasswordEncoder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordRecoverRepository extends JpaRepository<PasswordEncoder, Long> {
}
