package com.devsuperior.DSCommerce.repositories;

import com.devsuperior.DSCommerce.entities.Product;
import com.devsuperior.DSCommerce.entities.User;
import com.devsuperior.DSCommerce.projections.UserDetailsProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(nativeQuery = true, value = """
			SELECT tb_user.email AS username, tb_user.password, tb_role.id AS roleId, tb_role.authority
			FROM tb_user
			INNER JOIN tb_user_role ON tb_user.id = tb_user_role.user_id
			INNER JOIN tb_role ON tb_role.id = tb_user_role.role_id
			WHERE tb_user.email = :email
		""")
    List<UserDetailsProjection> searchUserAndRolesByEmail(String email);

	User findByEmail(String email);

	@Query("SELECT obj FROM User obj WHERE UPPER(obj.name) LIKE UPPER(CONCAT('%', :name, '%'))")
	Page<User> searchByName(String name, Pageable pageable);

	@Query("SELECT obj FROM User obj WHERE UPPER(obj.name) LIKE UPPER(CONCAT('%', :name, '%'))")
	User findByName(String name);
}




