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

/**
 * Recipe Factory unit tests for the Edit Recipe Method
 */
//Define lifecycle of tests to be per method rather than per class. Allows use of @BeforeEach
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class EditRecipeTests {

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
     * Check the Recipe Factory can edit an existing recipe.
     *
     * In order to test editing recipes, recipes must be pre-existing in the recipe cache, therefore recipes will be created as part of the setup.
     * Therefore, this test has a dependency on the CreateRecipe method in the service under test (RecipeFactoryHandler)
     */
    @Test
    public void checkEditRecipeNameSuccess() {

        //Setup
        //Create a list of recipes to be added to the recipeCache
        final List<RecipeModel> expectedRecipeModelList = RecipeModelDataFactory.generateRecipeModelsList(
                EatsyRecipeTestParameters.MAX_NUMBER_OF_RECIPES, EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);

        //Populate the recipeCache with the randomly generated recipe list
        for (final RecipeModel currentRecipeModel : expectedRecipeModelList) {
            final RecipeModel createdRecipeModel = recipeFactoryHandler.createRecipe(currentRecipeModel);

            //To ensure the test doesn't fail on the randomly generated UUIDs when doing object comparisons.
            currentRecipeModel.setKey(createdRecipeModel.getKey());
        }

        //Get a new name to edit the recipe with
        final String newRecipeName = RecipeModelDataFactory.generateRandomRecipeModel(
                EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE).getName();
        //Select a recipe in the list for the recipeName edit at random
        final int randomListIndex = (int) ((Math.random() * expectedRecipeModelList.size()));
        final String uniqueKeyOfRecipeToEdit = expectedRecipeModelList.get(randomListIndex).getKey();

        //Expected - What we expect the recipeModel to be after the name update (also what we will be submitting to the service as the updated model)
        final RecipeModel expectedUpdatedRecipeModel = expectedRecipeModelList.get(randomListIndex);
        expectedUpdatedRecipeModel.setName(newRecipeName);

        //Test - edit one of the recipe names in the recipeCache (This returned recipeModel will have a new key)
        final RecipeModel actualUpdatedRecipeModel = recipeFactoryHandler.updateRecipe(uniqueKeyOfRecipeToEdit, expectedUpdatedRecipeModel);
        //To ensure test doesn't fail on the randomly generated UUIDs
        expectedUpdatedRecipeModel.setKey(actualUpdatedRecipeModel.getKey());

        //Assert
        Assertions.assertEquals(expectedUpdatedRecipeModel, actualUpdatedRecipeModel);

    }

}
