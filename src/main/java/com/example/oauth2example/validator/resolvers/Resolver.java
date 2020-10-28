package com.example.oauth2example.validator.resolvers;

import javax.validation.ConstraintValidatorContext;

public class Resolver {

    protected void addConstraintViolation(ConstraintValidatorContext context, String errorMessage) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(errorMessage)
                .addConstraintViolation();
    }

    protected void addConstraintViolation(ConstraintValidatorContext context, String errorMessage, String ...args) {
        if(args == null || args.length == 0) {
            this.addConstraintViolation(context, errorMessage);
            return;
        }

        context.disableDefaultConstraintViolation();
        ConstraintValidatorContext.ConstraintViolationBuilder builder = context.buildConstraintViolationWithTemplate(errorMessage);
        ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext buliderContext = null;
        
        for (String arg : args) {
            if(buliderContext == null) {
                buliderContext = builder.addPropertyNode(arg);
                continue;
            }
            
            buliderContext = buliderContext.addPropertyNode(arg);
        }

        buliderContext.addConstraintViolation();
    }
}