package org.eatsy.appservice.service;

import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.model.mappers.RecipeMapper;
import org.eatsy.appservice.persistence.service.EatsyRepositoryService;
import org.eatsy.appservice.testdatageneration.RecipeModelDataFactory;
import org.eatsy.appservice.testdatageneration.constants.EatsyRecipeTestParameters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

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
     */
    @Test
    public void checkDeleteForSpecifiedRecipe() {

        //Setup and Mocking (eatsyRepositoryHandler class mocked (and it's delete method is void return type)).

        //1) In order to test deletion, recipes must be pre-existing therefore recipes will be created and
        //returned via the mocks as part of the setup
        final List<RecipeModel> inputRecipeModelList = RecipeModelDataFactory.generateRecipeModelsList(
                EatsyRecipeTestParameters.MAX_NUMBER_OF_RECIPES, EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);
        //Add unique key to each recipeModel in the list
        inputRecipeModelList.forEach(currentRecipeModel -> currentRecipeModel.setKey(UUID.randomUUID().toString()));

        //2) Test - Delete a recipe

        //Select a recipe in the list for deletion at random
        final int randomListIndex = (int) ((Math.random() * inputRecipeModelList.size()));
        final String uniqueKeyOfRecipeToDelete = inputRecipeModelList.get(randomListIndex).getKey();
        final RecipeModel recipeModelToBeDeleted = inputRecipeModelList.get(randomListIndex);

        //Delete the recipe
        final List<RecipeModel> actualUpdatedRecipeModelList = recipeFactoryHandler.deleteRecipeAndReturnUpdatedRecipeList(uniqueKeyOfRecipeToDelete);

        //3) Assert - check the recipe requested for deletion is no longer in the list of recipes, Check all the others are.
        System.out.println(inputRecipeModelList);
        System.out.println(actualUpdatedRecipeModelList);
        Assertions.assertTrue(inputRecipeModelList.containsAll(actualUpdatedRecipeModelList));
        Assertions.assertEquals((inputRecipeModelList.size() - 1), actualUpdatedRecipeModelList.size());
        Assertions.assertFalse(actualUpdatedRecipeModelList.contains(recipeModelToBeDeleted));//check the recipe to be deleted is in-fact gone.
    }

}
