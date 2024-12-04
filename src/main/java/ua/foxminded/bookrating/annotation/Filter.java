package ua.foxminded.bookrating.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {FilterValidator.class})
public @interface Filter {

    String message() default "";

    String author();

    String publisher();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface List {
        Filter[] value();
    }
}
