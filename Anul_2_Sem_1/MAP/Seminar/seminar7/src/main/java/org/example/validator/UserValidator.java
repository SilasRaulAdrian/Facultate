package org.example.validator;

import org.example.model.User;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User entity) throws ValidationException {
        StringBuilder errors = new StringBuilder();
        if(entity.getUsername() == null || entity.getUsername().isBlank())
            errors.append("Username invalid!\n");
        if(entity.getCredits() < 0)
            errors.append("Credits number invalid!\n");
        if(entity.getCreatedAt() == null)
            errors.append("Created at date invalid!\n");

        if(!errors.isEmpty())
            throw new ValidationException(errors.toString());
    }
}
