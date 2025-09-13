package com.ddevuss.weather.oracle.utils;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;

public class DuplicateConstraintChecker {

    public static boolean isThisConstraint(DataIntegrityViolationException e, String constraintKeyName) {
        ConstraintViolationException constraintViolationException = (ConstraintViolationException) e.getCause();
        String constraintName = constraintViolationException.getConstraintName();

        return constraintKeyName.equals(constraintName);
    }
}
