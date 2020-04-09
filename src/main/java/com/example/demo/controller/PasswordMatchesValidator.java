package com.example.demo.controller;

import org.apache.commons.beanutils.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches,Object> {

    private String firstFieldName;
    private String secondFieldName;
    private String message;

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        firstFieldName= constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
        message= constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        boolean valid = false;
        try{
            final Object firstObj = BeanUtils.getProperty(o,firstFieldName);
            final Object secondObj = BeanUtils.getProperty(o,secondFieldName);

            valid = firstObj !=null && firstObj.equals(secondObj);
        }catch (Exception e){
            e.printStackTrace();
        }

        if(!valid){
            constraintValidatorContext.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(firstFieldName)
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }
        return valid;
    }
}
