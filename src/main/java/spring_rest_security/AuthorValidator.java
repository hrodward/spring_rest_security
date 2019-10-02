package spring_rest_security;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AuthorValidator implements ConstraintValidator<Author, String> {

	List<String> authors = Arrays.asList("Santideva", "Marie Kondo", "Martin Fowler", "mkyong");

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		return authors.contains(value);

	}

}
