package org.eatsy.appservice.service;

import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.model.mappers.RecipeMapper;
import org.eatsy.appservice.persistence.service.EatsyRepositoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

/**
 * Recipe Factory unit tests for the Delete Recipe Method
 */
//Define lifecycle of tests to be per method rather than per class. Allows use of @BeforeEach
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class DeleteRecipeTests {

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
     * Check the recipe factory correctly deletes the specified recipe
     * and returns the updated list of all recipes
     * <p>
     * In order to test deletion, recipes must be pre-existing in the recipe cache, therefore recipes will be created as part of the setup.
     * Therefore, this test has a dependency on the CreateRecipe method in the service under test (RecipeFactoryHandler)
     */
    @Test
    public void checkDeleteAllRecipes() {

        //Setup and Mocking (eatsyRepositoryHandler class mocked (and it's delete method is void return type)).
        //In order to test deletion, recipes must be pre-existing in the recipe cache, therefore recipes will be created as part of the setup

        //1) Add recipes to the cache as setup for the test (so deletion can occur)
        final List<RecipeModel> inputRecipeModelWithKeysList = RecipeMockFactory.createRecipesInCache(
                recipeFactoryHandler, recipeMapperHandler, eatsyRepositoryHandler);

        //2) Test - Delete a recipe

        //Select a recipe in the list for deletion at random
        final int randomListIndex = (int) ((Math.random() * inputRecipeModelWithKeysList.size()));
        final String uniqueKeyOfRecipeToDelete = inputRecipeModelWithKeysList.get(randomListIndex).getKey();
        final RecipeModel recipeModelToBeDeleted = inputRecipeModelWithKeysList.get(randomListIndex);

        //Delete the recipe
        final List<RecipeModel> actualUpdatedRecipeModelList = recipeFactoryHandler.deleteRecipeAndReturnUpdatedRecipeList(uniqueKeyOfRecipeToDelete);

        //3) Assert - check the recipe requested for deletion is no longer in the list of recipes, Check all the others are.
        System.out.println(inputRecipeModelWithKeysList);
        System.out.println(actualUpdatedRecipeModelList);
        Assertions.assertTrue(inputRecipeModelWithKeysList.containsAll(actualUpdatedRecipeModelList));
        Assertions.assertEquals((inputRecipeModelWithKeysList.size() - 1), actualUpdatedRecipeModelList.size());
        Assertions.assertFalse(actualUpdatedRecipeModelList.contains(recipeModelToBeDeleted));//check the recipe to be deleted is in-fact gone.
    }

}
