package org.eatsy.appservice.service;

import org.apache.commons.lang3.StringUtils;
import org.eatsy.appservice.domain.Recipe;
import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.model.mappers.RecipeMapper;
import org.eatsy.appservice.persistence.model.RecipeEntity;
import org.eatsy.appservice.persistence.service.EatsyRepositoryService;
import org.eatsy.appservice.testdatageneration.RecipeModelDataFactory;
import org.eatsy.appservice.testdatageneration.constants.EatsyRecipeTestParameters;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface for recipe test mockito mock creation utility methods.
 */
public interface RecipeMockFactory {

    /**
     * Only the Service module is under test. The Mapper and Persistence modules that the Recipe Factory interacts with
     * need to be mocked to ensure these tests are RecipeFactory unit tests.
     * This method creates mocks for the EatsyRepository and RecipeMapper services for a give list of input recipe models.
     *
     * @param inputRecipeModelList   randomly generated recipe model test data.
     * @param recipeMapperHandler    mock implementation of the RecipeMapper
     * @param eatsyRepositoryHandler mock implementation of the EatsyRepositoryService
     */
    static void createMocksForRecipeMapperAndEatsyRepositoryServices(
            final List<RecipeModel> inputRecipeModelList, final RecipeMapper recipeMapperHandler, final EatsyRepositoryService eatsyRepositoryHandler) {

        for (final RecipeModel currentInputRecipeModel : inputRecipeModelList) {
            createMocksForRecipeMapperAndEatsyRepositoryServices(currentInputRecipeModel, recipeMapperHandler, eatsyRepositoryHandler);
        }

    }

    /**
     * Only the Service module is under test. The Mapper and Persistence modules that the Recipe Factory interacts with
     * need to be mocked to ensure these tests are RecipeFactory unit tests.
     * This method creates mocks for the EatsyRepository and RecipeMapper services for a given input recipe model.
     *
     * @param inputRecipeModel       randomly generated recipe model test data.
     * @param recipeMapperHandler    mock implementation of the RecipeMapper
     * @param eatsyRepositoryHandler mock implementation of the EatsyRepositoryService
     */
    static void createMocksForRecipeMapperAndEatsyRepositoryServices(
            final RecipeModel inputRecipeModel, final RecipeMapper recipeMapperHandler, final EatsyRepositoryService eatsyRepositoryHandler) {

        //Configure the RecipeMapper mocks to return mocked data when it's mapper methods are called from the RecipeFactory.
        final Recipe mockedDomainRecipe = createMockDomainRecipe(inputRecipeModel); //unique key assigned.
        Mockito.when(recipeMapperHandler.mapModelToDomain(inputRecipeModel)).thenReturn(mockedDomainRecipe);

        //Configure the eatsyRepository Mock to return the mocked data (Recipe Entity) when the eatsyRepository is called.
        final RecipeEntity mockedPersistedRecipeEntity = createMockRecipeEntity(mockedDomainRecipe);
        Mockito.when(eatsyRepositoryHandler.persistRecipe(mockedPersistedRecipeEntity)).thenReturn(mockedPersistedRecipeEntity);
        Mockito.when(recipeMapperHandler.mapDomainToEntity(mockedDomainRecipe)).thenReturn(mockedPersistedRecipeEntity);

        //Configure the RecipeMapperMock to return the mocked data (with the unique keys in the cache) when the MapperService is called.
        final RecipeModel mockedCreatedRecipeModelWithKey = createMockRecipeModelFromDomain(mockedDomainRecipe);
        Mockito.when(recipeMapperHandler.mapDomainToModel(mockedDomainRecipe)).thenReturn(mockedCreatedRecipeModelWithKey);
    }

    /**
     * Creates a random list of recipes via the createRecipe service method.
     * Whilst The RecipeMapper and EatsyRepository services are mocked during this method, the in-memory recipeCache is updated
     *
     * @return list of recipeModels that correspond to the domain recipes in the in-memory cache.
     */
    static List<RecipeModel> createRecipesInCache(final RecipeFactoryHandler recipeFactoryHandler, final RecipeMapper recipeMapperHandler, final EatsyRepositoryService eatsyRepositoryHandler) {
        //Create a list of input recipe models, to be added to the cache.
        final List<RecipeModel> inputRecipeModelList = RecipeModelDataFactory.generateRecipeModelsList(
                EatsyRecipeTestParameters.MAX_NUMBER_OF_RECIPES, EatsyRecipeTestParameters.MAX_INGREDIENT_SET_SIZE, EatsyRecipeTestParameters.MAX_METHOD_MAP_SIZE);

        //The generated inputRecipeModels will need their unique IDs to match what is assigned when they get put in the recipe cache
        final List<RecipeModel> inputRecipeModelWithKeysList = new ArrayList<>();

        //Mock the services that are not being tested through these unit tests
        RecipeMockFactory.createMocksForRecipeMapperAndEatsyRepositoryServices(inputRecipeModelList, recipeMapperHandler, eatsyRepositoryHandler);

        //Add the recipes to the recipe cache via the createRecipe method (Mapper and repository services mocked).
        for (final RecipeModel currentInputRecipeModel : inputRecipeModelList) {
            final RecipeModel newRecipeModel = recipeFactoryHandler.createRecipe(currentInputRecipeModel);
            //Take the randomly generated key out of the assertion
            //Recipe key randomly generated, so they will never match and one wasn't assigned to the inputRecipeModel
            currentInputRecipeModel.setKey(newRecipeModel.getKey());
            inputRecipeModelWithKeysList.add(currentInputRecipeModel);
        }
        return inputRecipeModelWithKeysList;
    }

    /**
     * Mocks the recipeMapper service interactions when a list of Recipe Entities would need to be mapped to a list of domain recipes.
     *
     * @param recipeMapperHandler      mock implementation of the RecipeMapper
     * @param expectedRecipeEntityList randomly generated recipe entity list test data.
     */
    static void createMockDomainRecipesFromEntityRecipes(final RecipeMapper recipeMapperHandler, final List<RecipeEntity> expectedRecipeEntityList) {
        for (final RecipeEntity currentEntity : expectedRecipeEntityList) {
            final Recipe expectedUpdateDomainRecipe = createMockRecipe(currentEntity);
            Mockito.when(recipeMapperHandler.mapEntityToDomain(currentEntity)).thenReturn(expectedUpdateDomainRecipe);
        }
    }

    /**
     * Creates a Recipe Model object from a Recipe Domain object that has been added to the recipeCache
     *
     * @param mockedDomainRecipe recipe domain object.
     * @return A Recipe Model object.
     */
    static RecipeModel createMockRecipeModelFromDomain(final Recipe mockedDomainRecipe) {

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
    static RecipeEntity createMockRecipeEntity(final Recipe inputRecipeDomain) {

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
     * Creates a Recipe object from a Recipe Entity object
     *
     * @param inputRecipeEntity recipe model object.
     * @return A Recipe object.
     */
    static Recipe createMockRecipe(final RecipeEntity inputRecipeEntity) {

        Recipe recipe = null;
        //The recipeEntity to be mapped must not be null and the recipeEntity must have a name.
        if (null != inputRecipeEntity && StringUtils.isNotEmpty(inputRecipeEntity.getName().trim())) {

            recipe = new Recipe
                    .RecipeBuilder(inputRecipeEntity.getName())
                    .withIngredientSet(inputRecipeEntity.getIngredientSet())
                    .withMethod(inputRecipeEntity.getMethodMap())
                    .withSpecifiedKey(inputRecipeEntity.getKey())
                    .build();
        }
        return recipe;

    }

    /**
     * Creates a Recipe Domain object from a Recipe Model object
     *
     * @param inputRecipeModel recipe model object.
     * @return A Recipe Domain object.
     */
    static Recipe createMockDomainRecipe(final RecipeModel inputRecipeModel) {

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

    /**
     * Creates a Recipe Domain object from a Recipe Model object with a specific key.
     *
     * @param inputRecipeModel recipe model object.
     * @param specifiedKey     unique string UUID to use for the key.
     * @return A Recipe Domain object.
     */
    static Recipe createMockDomainRecipeWithSpecifiedKey(final RecipeModel inputRecipeModel, final String specifiedKey) {

        Recipe domainRecipe = null;
        //The recipe model to be mapped must not be null and the recipe must have a name.
        if (null != inputRecipeModel && StringUtils.isNotEmpty(inputRecipeModel.getName().trim())) {

            domainRecipe = new Recipe
                    .RecipeBuilder(inputRecipeModel.getName())
                    .withIngredientSet(inputRecipeModel.getIngredientSet())
                    .withMethod(inputRecipeModel.getMethod())
                    .withSpecifiedKey(specifiedKey)
                    .build();

        }
        return domainRecipe;

    }

    /**
     * Creates a list of Recipe Entity objects from a list of Recipe Model objects
     *
     * @param inputRecipeModelList list recipe model object.
     * @return A list of Recipe Entity object.
     */
    static List<RecipeEntity> createMockRecipeEntity(final List<RecipeModel> inputRecipeModelList) {

        final List<RecipeEntity> recipeEntityList = new ArrayList<>();

        for (final RecipeModel currentRecipeModel : inputRecipeModelList) {

            RecipeEntity recipeEntity = null;
            //The recipe to be mapped must not be null and the recipe must have a name.
            if (null != currentRecipeModel && StringUtils.isNotEmpty(currentRecipeModel.getName().trim())) {

                recipeEntity = new RecipeEntity();
                //Map key.
                recipeEntity.setKey(currentRecipeModel.getKey());
                //Map name.
                recipeEntity.setName(currentRecipeModel.getName());
                //Map set of ingredients.
                recipeEntity.setIngredientSet(currentRecipeModel.getIngredientSet());
                //Map method.
                recipeEntity.setMethodMap(currentRecipeModel.getMethod());

            }
            recipeEntityList.add(recipeEntity);

        }

        return recipeEntityList;
    }

    /**
     * Creates a Recipe Entity object from a Recipe Model object
     *
     * @param inputRecipeModel recipe model object.
     * @return A Recipe Entity object.
     */
    static RecipeEntity createMockRecipeEntity(final RecipeModel inputRecipeModel) {

        RecipeEntity recipeEntity = null;
        //The recipe to be mapped must not be null and the recipe must have a name.
        if (null != inputRecipeModel && StringUtils.isNotEmpty(inputRecipeModel.getName().trim())) {

            recipeEntity = new RecipeEntity();
            //Map key.
            recipeEntity.setKey(inputRecipeModel.getKey());
            //Map name.
            recipeEntity.setName(inputRecipeModel.getName());
            //Map set of ingredients.
            recipeEntity.setIngredientSet(inputRecipeModel.getIngredientSet());
            //Map method.
            recipeEntity.setMethodMap(inputRecipeModel.getMethod());

        }
        return recipeEntity;

    }

}
