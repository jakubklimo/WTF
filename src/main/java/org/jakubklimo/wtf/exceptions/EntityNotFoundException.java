package org.jakubklimo.wtf.exceptions;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String entityName, String entityId) {
        super(entityName + " with ID " + entityId + " not found");
    }
}
