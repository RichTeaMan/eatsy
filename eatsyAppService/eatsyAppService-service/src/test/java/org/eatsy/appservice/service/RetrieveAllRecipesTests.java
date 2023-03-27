package org.eatsy.appservice.service;

import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.model.mappers.RecipeMapper;
import org.eatsy.appservice.persistence.model.RecipeEntity;
import org.eatsy.appservice.persistence.service.EatsyRepositoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;


/**
 * Recipe Factory unit tests for the RetrieveAllRecipes Method.
 * This test class also calls the createRecipe service method as it is the only way to get recipes in the cache.
 * This diagram shows which services have been mocked and what is being tested.
 * <p>
 * |            |    *Mocked*    |                        Layer under test                     |    *Mocked*    |
 * |            |  [Controller]  |                          [Service]                          | [Persistence]  |
 * | GET/POST   |                |     *Mocked*                                  *Mocked*      |                |
 * |            |                |-{Map to domain}-> |                      |-{Map to Entity}->|                |
 * |  ------>   |                |                   |                      |                  |(persist recipe)|
 * | [Model]    |                |                   |                      |                  |                |
 * | <------    |                |     *Mocked*      |                      |    *Mocked*      |                |
 * |            |                | <-{Map to model}- |  (update the cache)  |<-{Map to domain}-|                |
 * |        <--Model--      <--Model--               | <retrieveAllRecipes> |             <--Entity--           |
 * |            |                |                   |                      |                  |                |
 * |            |                |                                                             |                |
 */
//Define lifecycle of tests to be per method rather than per class. Allows use of @BeforeEach
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class RetrieveAllRecipesTests {

    //Create a mock implementation of the RecipeMapper. These unit tests are only concerned with the service module not the mapper module.
    @Mock
    private RecipeMapper recipeMapperHandler;

    //Create a mock implementation of the EatsyRepositoryService. These unit tests are only concerned with the service module not the persistence module.
    @Mock
    private EatsyRepositoryService eatsyRepositoryHandler;

    /**
     * Class under test.
     */
    //Injects the dependent mocks (marked with @Mock) for a recipe factory instance.
    @InjectMocks
    private RecipeFactoryHandler recipeFactoryHandler;

    @BeforeEach
    public void setup() {
        //Initialise the mock objects upon initialisation of Junit tests.
        MockitoAnnotations.openMocks(this);
        //Initialise the class under test and inject the mocks.
        recipeFactoryHandler = new RecipeFactoryHandler(recipeMapperHandler, eatsyRepositoryHandler);
    }

    /**
     * Check the recipe factory correctly returns all recipes.
     * This test also calls the createRecipe service method as it is the only way to get recipes in the cache.
     */
    @Test
    public void checkRetrieveAllRecipes() {

        //Setup and mocking
        //Add recipes to the cache as setup for the test (so retrieving can occur).
        // (The service createMethod has to be called to add recipes to the cache.)
        final List<RecipeModel> expectedRecipeModelList = RecipeMockFactory.createRecipesInCache(
                recipeFactoryHandler, recipeMapperHandler, eatsyRepositoryHandler);
        //Mock eatsyRepositoryHandler interactions
        final List<RecipeEntity> expectedRecipeEntityList = RecipeMockFactory.createMockRecipeEntity(expectedRecipeModelList);
        Mockito.when(eatsyRepositoryHandler.retrieveAllRecipes()).thenReturn(expectedRecipeEntityList);
        RecipeMockFactory.createMockDomainRecipesFromEntityRecipes(recipeMapperHandler, expectedRecipeEntityList);

        //Test
        final List<RecipeModel> actualRecipeModelsList = recipeFactoryHandler.retrieveAllRecipes();

        //Assert
        Assertions.assertEquals(expectedRecipeModelList, actualRecipeModelsList);

    }


}
