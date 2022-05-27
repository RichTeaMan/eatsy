import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.eatsy.appservice.domain.Recipe;
import org.junit.jupiter.api.Test;

/**
 * Unit test class for Recipe domain object
 */
public class RecipeTests {

    /**
     * Checks the equals contract of the recipe object in case any fields have been missed.
     */
    @Test
    public void TestRecipeEqualsContract() {
        EqualsVerifier.forClass(Recipe.class).verify();
    }

    /**
     * Checks toStringMethod contains all fields and nothing is missing.
     */
    @Test
    public void testToStringMethod() {
        ToStringVerifier.forClass(Recipe.class)
                .withClassName(NameStyle.SIMPLE_NAME)
                .verify();
    }


}
