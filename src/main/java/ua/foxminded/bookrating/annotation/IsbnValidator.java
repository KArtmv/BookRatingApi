package ua.foxminded.bookrating.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;
import ua.foxminded.bookrating.persistance.repo.BookRepository;

@Component
@RequiredArgsConstructor
public class IsbnValidator implements ConstraintValidator<Isbn, Object> {

    private final BookRepository bookRepository;
    private String bookIdFieldName;
    private String isbnFieldName;

    @Override
    public void initialize(Isbn constraintAnnotation) {
        this.bookIdFieldName = constraintAnnotation.bookId();
        this.isbnFieldName = constraintAnnotation.isbn();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        Object bookId = new BeanWrapperImpl(o).getPropertyValue(this.bookIdFieldName);
        Object isbn = new BeanWrapperImpl(o).getPropertyValue(this.isbnFieldName);

        addConstraintViolation(constraintValidatorContext);

        return bookRepository.findByIsbn(String.valueOf(isbn)).map(value -> bookId != null && value.getId().equals(bookId)).orElse(true);
    }

    private void addConstraintViolation(ConstraintValidatorContext constraintValidatorContext) {
        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate(constraintValidatorContext.getDefaultConstraintMessageTemplate())
                .addPropertyNode(isbnFieldName).addConstraintViolation();
    }
}
