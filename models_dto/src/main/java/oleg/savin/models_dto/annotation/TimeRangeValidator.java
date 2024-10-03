package oleg.savin.models_dto.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class TimeRangeValidator implements ConstraintValidator<ValidTimeRange, Object> {

    private String startField;
    private String endField;

    @Override
    public void initialize(ValidTimeRange constraintAnnotation) {
        this.startField = constraintAnnotation.startField();
        this.endField = constraintAnnotation.endField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Field startFieldObject = value.getClass().getDeclaredField(startField);
            Field endFieldObject = value.getClass().getDeclaredField(endField);

            startFieldObject.setAccessible(true);
            endFieldObject.setAccessible(true);

            LocalDateTime start = (LocalDateTime) startFieldObject.get(value);
            LocalDateTime end = (LocalDateTime) endFieldObject.get(value);

            if (start == null || end == null) {
                return true;
            }

            return start.isBefore(end);
        } catch (Exception e) {
            return false;
        }
    }
}
