package com.devsuperior.DSCommerce.tests;

import com.devsuperior.DSCommerce.entities.Role;
import com.devsuperior.DSCommerce.entities.User;

import java.time.LocalDate;

public class UserFactory {

    public static User createClientUser() {
        User user = new User(1L, "Maria Brown", "maria@gmail.com", "988888888", LocalDate.parse("2001-07-25"), "$2a$10$CER3Frba2cEhnkAuDMng7OS0jkElywv180cKOdGci5QuFOBFliB46");
        user.addRole(new Role(1L, "ROLE_CLIENT"));
        return user;
    }

    public static User createAdminUser() {
        User user = new User(2L, "Alex Green", "alex@gmail.com", "977777777", LocalDate.parse("1987-12-13"), "$2a$10$CER3Frba2cEhnkAuDMng7OS0jkElywv180cKOdGci5QuFOBFliB46");
        user.addRole(new Role(2L, "ROLE_ADMIN"));
        return user;
    }

    public static User createCustomAdminUser(Long id, String name) {
        User user = new User(2L, "Alex Green", "alex@gmail.com", "977777777", LocalDate.parse("1987-12-13"), "$2a$10$CER3Frba2cEhnkAuDMng7OS0jkElywv180cKOdGci5QuFOBFliB46");
        user.addRole(new Role(2L, "ROLE_ADMIN"));
        return user;
    }
}
