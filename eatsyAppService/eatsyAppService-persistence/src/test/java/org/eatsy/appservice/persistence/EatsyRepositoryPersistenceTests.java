package org.eatsy.appservice.persistence;

import org.eatsy.appservice.persistence.model.RecipeEntity;
import org.eatsy.appservice.persistence.service.EatsyRepository;
import org.eatsy.appservice.persistence.service.EatsyRepositoryHandler;
import org.eatsy.appservice.persistence.service.EatsyRepositoryService;
import org.eatsy.appservice.testdatageneration.RecipeEntityDataFactory;
import org.eatsy.appservice.testdatageneration.constants.EatsyRecipeTestParameters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;
import java.util.UUID;

/**
 * Test class for the persistence context of the application.
 * <p>
 * Test cases will work with H2 in-memory database
 * to eliminates the need for configuring and starting an actual database.
 */
//The annotation will disable full auto-configuration applying only enable configuration relevant to JPA tests.
//tests annotated with @DataJpaTest are transactional and roll back at the end of each test
@DataJpaTest
public class EatsyRepositoryPersistenceTests {

    // TestEntityManager allows us to use EntityManager in DataJpaTests.
    // EntityManager is used by Spring to interact with the persistence context
    @Autowired
    private TestEntityManager testEntityManager;

    //The Eatsy Repository that extends the JPA interface
    @Autowired
    private EatsyRepository eatsyRepository;
    private EatsyRepositoryService eatsyRepositoryHandler;

    /**
     * Setup method before every test = Initialise the class under test
     */
    @BeforeEach
    public void setup() {
        eatsyRepositoryHandler = new EatsyRepositoryHandler();
    }

    @Test
    public void checkPersistRecipeTest() {

        //Create test RecipeEntity
        final RecipeEntity expectedRecipeEntity = RecipeEntityDataFactory
                .generateRandomRecipeEntity(EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);
        expectedRecipeEntity.setKey(UUID.randomUUID().toString());

        //Execute the method under test
        eatsyRepositoryHandler.persistRecipe(expectedRecipeEntity);

        //Do the assertion
        final Optional<RecipeEntity> actualEntityOptional = eatsyRepository.findById(expectedRecipeEntity.getKey());
        if (actualEntityOptional.isPresent()) {
            final RecipeEntity actualRecipeEntity = actualEntityOptional.get();
            Assertions.assertEquals(expectedRecipeEntity, actualRecipeEntity);
        } else {
            Assertions.fail("The persistRecipe method under test did not work as expected");
        }
    }


}

/**
 * @DataJpaTest annotation needs the spring boot configuration found in @SpringBootApplication.
 * It will not find this config as it is declared in the Controller Gradle Project.
 * Therefore, we must add one to this test code in this sub-gradle project.
 */
@SpringBootApplication
class TestApplication {
}