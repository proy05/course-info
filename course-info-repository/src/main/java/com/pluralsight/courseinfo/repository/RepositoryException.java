package com.pluralsight.courseinfo.repository;

import java.sql.SQLException;

public class RepositoryException extends RuntimeException { //extending Runtimeexception means client code doesnt need to catch/throw the exception
    public RepositoryException(String message, SQLException e) {
         super(message, e);
    }
}
