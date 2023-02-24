package org.eatsy.appservice.testdatageneration.annotations;

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
 * The GenerateAndPersistRandomRecipes class which sits within the test project and is not application code.
 * This is code to generate test data and POST to the application.
 * Therefore, it is not production code to be unit tested.
 */
public @interface Generated {
}
