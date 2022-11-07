package org.eatsy.appservice.service;

import org.apache.commons.lang3.StringUtils;
import org.eatsy.appservice.domain.Recipe;
import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.model.mappers.RecipeMapper;
import org.eatsy.appservice.persistence.model.RecipeEntity;
import org.eatsy.appservice.persistence.service.EatsyRepositoryService;
import org.eatsy.appservice.testdatageneration.RecipeModelDataFactory;
import org.eatsy.appservice.testdatageneration.constants.EatsyRecipeTestParameters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
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

        //1) Add recipes to the cache as setup for the test

        //Create a list of input recipe models, to be added to the cache.
        final List<RecipeModel> inputRecipeModelList = RecipeModelDataFactory.generateRecipeModelsList(
                EatsyRecipeTestParameters.MAX_NUMBER_OF_RECIPES, EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);

        //The generated inputRecipeModels will need their unique IDs to match what is assigned when they get put in the recipe cache
        final List<RecipeModel> inputRecipeModelWithKeysList = new ArrayList<>();

        //Mock the services that are not being tested through these unit tests
        createMocksForRecipeMapperAndEatsyRepositoryServices(inputRecipeModelList);

        //Add the recipes to the recipe cache via the createRecipe method (Mapper and repository services mocked).
        for (final RecipeModel currentInputRecipeModel : inputRecipeModelList) {
            final RecipeModel newRecipeModel = recipeFactoryHandler.createRecipe(currentInputRecipeModel);
            //Take the randomly generated key out of the assertion
            //Recipe key randomly generated, so they will never match and one wasn't assigned to the inputRecipeModel
            currentInputRecipeModel.setKey(newRecipeModel.getKey());
            inputRecipeModelWithKeysList.add(currentInputRecipeModel);
        }

        //2) Test - Delete the recipe

        //Select a recipe in the list for deletion at random
        final int randomListIndex = (int) ((Math.random() * inputRecipeModelWithKeysList.size()));
        final String uniqueKeyOfRecipeToDelete = inputRecipeModelWithKeysList.get(randomListIndex).getKey();
        final RecipeModel recipeModelToBeDeleted = inputRecipeModelWithKeysList.get(randomListIndex);

        //Delete the recipe
        final List<RecipeModel> actualUpdatedRecipeModelList = recipeFactoryHandler.deleteRecipe(uniqueKeyOfRecipeToDelete);

        //3) Assert - check the recipe requested for deletion is no longer in the list of recipes, Check all the others are.
        Assertions.assertTrue(inputRecipeModelWithKeysList.containsAll(actualUpdatedRecipeModelList));
        Assertions.assertEquals((inputRecipeModelWithKeysList.size() - 1), actualUpdatedRecipeModelList.size());
        Assertions.assertFalse(actualUpdatedRecipeModelList.contains(recipeModelToBeDeleted));//check the recipe to be deleted is in-fact gone.
    }

    //Test methods below.

    /**
     * Only the Service module is under test. The Mapper and Persistence modules that the Recipe Factory interacts with
     * need to be mocked to ensure these tests are RecipeFactory unit tests.
     * This method creates mocks for the EatsyRepository and RecipeMapper services for a give list of input recipe models.
     *
     * @param inputRecipeModelList randomly generated recipe model test data.
     */
    private void createMocksForRecipeMapperAndEatsyRepositoryServices(final List<RecipeModel> inputRecipeModelList) {

        for (final RecipeModel currentInputRecipeModel : inputRecipeModelList) {
            createMocksForRecipeMapperAndEatsyRepositoryServices(currentInputRecipeModel);
        }

    }

    /**
     * Only the Service module is under test. The Mapper and Persistence modules that the Recipe Factory interacts with
     * need to be mocked to ensure these tests are RecipeFactory unit tests.
     * This method creates mocks for the EatsyRepository and RecipeMapper services for a given input recipe model.
     *
     * @param inputRecipeModel randomly generated recipe model test data.
     */
    private void createMocksForRecipeMapperAndEatsyRepositoryServices(final RecipeModel inputRecipeModel) {

        //Configure the RecipeMapper mocks to return mocked data when it's mapper methods are called from the RecipeFactory.
        final Recipe mockedDomainRecipe = createMockDomainRecipe(inputRecipeModel); //unique key assigned.
        Mockito.when(recipeMapperHandler.mapModelToDomain(inputRecipeModel)).thenReturn(mockedDomainRecipe);

        //Configure the eatsyRepository Mock to return the mocked data (Recipe Entity) when the eatsyRepository is called.
        final RecipeEntity mockedPersistedRecipeEntity = createMockRecipeEntity(mockedDomainRecipe);
        Mockito.when(eatsyRepositoryHandler.persistNewRecipe(mockedPersistedRecipeEntity)).thenReturn(mockedPersistedRecipeEntity);
        Mockito.when(recipeMapperHandler.mapDomainToEntity(mockedDomainRecipe)).thenReturn(mockedPersistedRecipeEntity);

        //Configure the RecipeMapperMock to return the mocked data (with the unique keys in the cache) when the MapperService is called.
        final RecipeModel mockedCreatedRecipeModelWithKey = createMockRecipeModelFromDomain(mockedDomainRecipe);
        Mockito.when(recipeMapperHandler.mapDomainToModel(mockedDomainRecipe)).thenReturn(mockedCreatedRecipeModelWithKey);
    }

    /**
     * Creates a Recipe Model object from a Recipe Domain object that has been added to the recipeCache
     *
     * @param mockedDomainRecipe recipe domain object.
     * @return A Recipe Model object.
     */
    private static RecipeModel createMockRecipeModelFromDomain(final Recipe mockedDomainRecipe) {

        RecipeModel recipeModel = null;
        //The recipe to be mapped must not be null and the recipe must have a name.
        if (null != mockedDomainRecipe && StringUtils.isNotEmpty(mockedDomainRecipe.getName().trim())) {

            recipeModel = new RecipeModel();
            //Map key.
            recipeModel.setKey(mockedDomainRecipe.getKey());
            //Map name.
            recipeModel.setName(mockedDomainRecipe.getName());
            //Map set of ingredients.
            recipeModel.setIngredientSet(mockedDomainRecipe.getIngredientSet());
            //Map method.
            recipeModel.setMethod(mockedDomainRecipe.getMethod());

        }
        return recipeModel;

    }

    /**
     * Creates a Recipe Entity object from a Recipe Domain object
     *
     * @param inputRecipeDomain recipe model object.
     * @return A Recipe Entity object.
     */
    private static RecipeEntity createMockRecipeEntity(final Recipe inputRecipeDomain) {

        RecipeEntity recipeEntity = null;
        //The recipe to be mapped must not be null and the recipe must have a name.
        if (null != inputRecipeDomain && StringUtils.isNotEmpty(inputRecipeDomain.getName().trim())) {

            recipeEntity = new RecipeEntity();
            //Map key.
            recipeEntity.setKey(inputRecipeDomain.getKey());
            //Map name.
            recipeEntity.setName(inputRecipeDomain.getName());
            //Map set of ingredients.
            recipeEntity.setIngredientSet(inputRecipeDomain.getIngredientSet());
            //Map method.
            recipeEntity.setMethodMap(inputRecipeDomain.getMethod());

        }
        return recipeEntity;

    }

    /**
     * Creates a Recipe Domain object from a Recipe Model object
     *
     * @param inputRecipeModel recipe model object.
     * @return A Recipe Domain object.
     */
    private static Recipe createMockDomainRecipe(final RecipeModel inputRecipeModel) {

        Recipe domainRecipe = null;
        //The recipe model to be mapped must not be null and the recipe must have a name.
        if (null != inputRecipeModel && StringUtils.isNotEmpty(inputRecipeModel.getName().trim())) {

            domainRecipe = new Recipe
                    .RecipeBuilder(inputRecipeModel.getName())
                    .withIngredientSet(inputRecipeModel.getIngredientSet())
                    .withMethod(inputRecipeModel.getMethod())
                    .build();

        }
        return domainRecipe;

    }


}
