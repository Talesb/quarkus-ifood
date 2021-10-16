package com.github.talesb.ifood.infra;

import javax.validation.ConstraintValidatorContext;

public interface DTO {

	boolean isValid(ConstraintValidatorContext constraintValidatorContext);

}
