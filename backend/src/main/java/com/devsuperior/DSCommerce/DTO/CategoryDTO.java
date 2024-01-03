package com.devsuperior.DSCommerce.DTO;

import com.devsuperior.DSCommerce.entities.Category;

import java.util.List;

public class CategoryDTO {

    private Long id;
    private String name;

    public  CategoryDTO(){
    }

    public CategoryDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public CategoryDTO(Category entity){
        id = entity.getId();
        name = entity.getName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}


