package org.eatsy.appservice.controller.application.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target({TYPE, METHOD, CONSTRUCTOR})

/**
 * Excludes classes and methods from the JaCoCo coveralls unit test reports
 * by annotating them with this custom annotation.
 *
 * This should be used on methods where it wouldn't make sense to unit test them. For example,
 * The 'main' method with serves as the Spring Boot entry point to build and run the serve the application
 */
public @interface Generated {
}
