package org.eatsy.appservice.persistence;

import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.eatsy.appservice.persistence.model.RecipeEntity;
import org.junit.jupiter.api.Test;

import javax.persistence.Id;

/**
 * Unit test class for Recipe entity object
 */
public class RecipeEntityTests {

    /**
     * Checks the equals contract of the recipe entity object in case any fields have been missed.
     */
    @Test
    public void TestRecipeEqualsContract() {
        EqualsVerifier.forClass(RecipeEntity.class)
                .withIgnoredAnnotations(Id.class)
                .verify();
    }

    /**
     * Checks toStringMethod contains all fields and nothing is missing.
     */
    @Test
    public void testToStringMethod() {
        ToStringVerifier.forClass(RecipeEntity.class)
                .withClassName(NameStyle.SIMPLE_NAME)
                .verify();
    }

}
