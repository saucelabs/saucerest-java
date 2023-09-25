package com.saucelabs.saucerest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.platform.commons.util.AnnotationUtils;

@AllArgsConstructor
@Getter
public class MappedParameterContext implements ParameterContext {
    private final int index;
    private final Parameter parameter;
    private final Optional<Object> target;

    @Override
    public boolean isAnnotated(Class<? extends Annotation> annotationType) {
        return AnnotationUtils.isAnnotated(parameter, annotationType);
    }

    @Override
    public <A extends Annotation> Optional<A> findAnnotation(Class<A> annotationType) {
        return AnnotationUtils.findAnnotation(parameter, annotationType);
    }

    @Override
    public <A extends Annotation> List<A> findRepeatableAnnotations(Class<A> annotationType) {
        return AnnotationUtils.findRepeatableAnnotations(parameter, annotationType);
    }
}
