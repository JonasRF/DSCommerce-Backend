package com.devsuperior.DSCommerce.DTO;

import com.devsuperior.DSCommerce.entities.Role;
import com.devsuperior.DSCommerce.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class UserDTO implements Serializable {
	private Long id;
	@NotBlank(message = "Campo obrigatorio")
	private String name;
	@Email(message = "Favor colocar um email valido")
	private String email;
	private String phone;
	private LocalDate birthDate;
	Set<RoleDTO> roles = new HashSet<>();

	public UserDTO(Long id, String name, String email, String phone, LocalDate birthDate) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.birthDate = birthDate;
	}

	public UserDTO(User entity) {
		id = entity.getId();
		name = entity.getName();
		email = entity.getEmail();
		phone = entity.getPhone();
		birthDate = entity.getBirthDate();
		entity.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
	}

	public UserDTO() {
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPhone() {
		return phone;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public Set<RoleDTO> getRoles() {
		return roles;
	}
}