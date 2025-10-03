package com.ddevuss.weather.oracle.common.utils;

import com.ddevuss.weather.oracle.common.entity.ConstraintKey;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.function.Supplier;

public class UniqueConstraintTranslator {

    private Supplier<String> message = () -> "Entity already exists.";
    private final DataIntegrityViolationException root;
    private final boolean matches;

    private UniqueConstraintTranslator(DataIntegrityViolationException e, ConstraintKey key) {
        this.root = e;
        this.matches = isThisConstraint(e, key);
    }

    public static UniqueConstraintTranslator checkConstraint(DataIntegrityViolationException e, ConstraintKey key) {
        return new UniqueConstraintTranslator(e, key);
    }

    public UniqueConstraintTranslator withMessage(Supplier<String> message) {
        this.message = message;
        return this;
    }

    public UniqueConstraintTranslator withMessage(String message) {
        this.message = () -> message;
        return this;
    }

    public void throwIfMatches() {
        if (matches) throw new DataIntegrityViolationException(message.get(), root);
    }

    public DataIntegrityViolationException translateOrOriginal() {
        if (matches) return new DataIntegrityViolationException(message.get(), root);

        return root;
    }

    private static boolean isThisConstraint(DataIntegrityViolationException e, ConstraintKey key) {
        ConstraintViolationException constraintViolationException = (ConstraintViolationException) e.getCause();
        String constraintName = constraintViolationException.getConstraintName();

        return key.getKey().equals(constraintName);
    }
}
