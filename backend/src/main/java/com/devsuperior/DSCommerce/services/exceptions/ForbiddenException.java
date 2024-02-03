package com.devsuperior.DSCommerce.services.exceptions;

public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String msg){
        super(msg);
    }
}
