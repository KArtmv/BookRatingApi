package ua.foxminded.bookrating.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class FilterValidator implements ConstraintValidator<Filter, Object> {

    private String authorFieldName;
    private String publisherFieldName;

    @Override
    public void initialize(Filter constraintAnnotation) {
        this.authorFieldName = constraintAnnotation.author();
        this.publisherFieldName = constraintAnnotation.publisher();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(o);
        addConstraintViolation(constraintValidatorContext);

        return beanWrapper.getPropertyValue(this.publisherFieldName) != null || beanWrapper.getPropertyValue(this.authorFieldName) != null;
    }

    private void addConstraintViolation(ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        context.buildConstraintViolationWithTemplate("At least authors or publishers must be provided.")
                .addPropertyNode(authorFieldName)
                .addConstraintViolation();

        context.buildConstraintViolationWithTemplate("At least authors or publishers must be provided.")
                .addPropertyNode(publisherFieldName)
                .addConstraintViolation();
    }
}
