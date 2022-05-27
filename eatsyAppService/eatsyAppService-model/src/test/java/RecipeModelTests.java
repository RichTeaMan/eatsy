import com.jparams.verifier.tostring.NameStyle;
import com.jparams.verifier.tostring.ToStringVerifier;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.eatsy.appservice.model.RecipeModel;
import org.junit.jupiter.api.Test;

/**
 * Unit test class for Recipe model object
 */
public class RecipeModelTests {

    /**
     * Checks the equals contract of the recipe model object in case any fields have been missed.
     */
    @Test
    public void TestRecipeEqualsContract() {
        EqualsVerifier.forClass(RecipeModel.class)
                .suppress(Warning.STRICT_INHERITANCE)
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }

    /**
     * Checks toStringMethod contains all fields and nothing is missing.
     */
    @Test
    public void testToStringMethod() {
        ToStringVerifier.forClass(RecipeModel.class)
                .withClassName(NameStyle.SIMPLE_NAME)
                .verify();
    }

}
