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
     * This method creates mocks for the EatsyRepository and RecipeMapper services for a given list of input recipe models.
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
     * Creates a random list of recipes via the createRecipe service method to ensure it goes in the cache.
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
     * @param expectedRecipeEntityList randomly generated recipe entity list test data.
     */
    static List<Recipe> createMockDomainRecipesFromEntityRecipes(final List<RecipeEntity> expectedRecipeEntityList) {

        final List<Recipe> expectedDomainRecipes = new ArrayList<>();

        for (final RecipeEntity currentEntity : expectedRecipeEntityList) {
            final Recipe expectedUpdateDomainRecipe = createMockRecipe(currentEntity);
            expectedDomainRecipes.add(expectedUpdateDomainRecipe);
        }
        return expectedDomainRecipes;
    }

    /**
     * Only the Service module is under test. The Mapper and Persistence modules that the Recipe Factory interacts with
     * need to be mocked to ensure these tests are RecipeFactory unit tests.
     * This method creates mocks for the EatsyRepository and RecipeMapper services.
     *
     * @param inputRecipeModel randomly generated recipe model test data.
     */
    static void createMocksForMapperAndPersistenceServicesInCreateOrUpdateRecipeTests(
            final RecipeModel inputRecipeModel, final EatsyRepositoryService eatsyRepositoryHandler, final RecipeMapper recipeMapperHandler) {

        //Configure the eatsyRepository Mock to return the mocked data (persisted Recipe Entity) when the eatsyRepository is called.
        final RecipeEntity mockedPersistedRecipeEntity = RecipeMockFactory.createMockRecipeEntity(inputRecipeModel);
        Mockito.when(eatsyRepositoryHandler.persistRecipe(mockedPersistedRecipeEntity)).thenReturn(mockedPersistedRecipeEntity);

        //Configure the RecipeMapper mock to return mocked data when it's mapper methods are called from the RecipeFactory.
        final Recipe mockedDomainRecipe = RecipeMockFactory.createMockDomainRecipe(inputRecipeModel);
        Mockito.when(recipeMapperHandler.mapModelToDomain(inputRecipeModel)).thenReturn(mockedDomainRecipe);
        Mockito.when(recipeMapperHandler.mapDomainToEntity(mockedDomainRecipe)).thenReturn(mockedPersistedRecipeEntity);
        Mockito.when(recipeMapperHandler.mapEntityToDomain(mockedPersistedRecipeEntity)).thenReturn(mockedDomainRecipe);
        Mockito.when(recipeMapperHandler.mapDomainToModel(mockedDomainRecipe)).thenReturn(inputRecipeModel);
    }

    /**
     * Only the Service module is under test. The Mapper and Persistence modules that the Recipe Factory interacts with
     * need to be mocked to ensure these tests are RecipeFactory unit tests.
     * This method creates mocks for the EatsyRepository and RecipeMapper services.
     *
     * @param expectedRecipeModelList randomly generated list of recipe model test data.
     */
    static void createMocksForMapperAndPersistenceServicesInRetrieveAllRecipeServiceMethod(
            final List<RecipeModel> expectedRecipeModelList, final EatsyRepositoryService eatsyRepositoryHandler, final RecipeMapper recipeMapperHandler) {

        //Mock the response for when the service under test calls the persistence layer eatsyRepositoryHandler.retrieveAllRecipes() method.
        final List<RecipeEntity> expectedRecipeEntityList = RecipeMockFactory.createMockRecipeEntityList(expectedRecipeModelList);
        Mockito.when(eatsyRepositoryHandler.retrieveAllRecipes()).thenReturn(expectedRecipeEntityList);
        //Mock the response for each time the service under test calls the mapper layer recipeMapperHandler.mapEntityToDomain(testEntity) method for a given test entity.
        final List<Recipe> expectedRecipeList = RecipeMockFactory.createMockDomainRecipesFromEntityRecipes(expectedRecipeEntityList);
        for (final RecipeEntity currentEntity : expectedRecipeEntityList) {
            final Recipe expectedUpdatedDomainRecipe = RecipeMockFactory.createMockRecipe(currentEntity);
            Mockito.when(recipeMapperHandler.mapEntityToDomain(currentEntity)).thenReturn(expectedUpdatedDomainRecipe);
        }
        //Mock the response for each time the service under test calls the mapper layer recipeMapperHandler.mapDomainToModel(testRecipe) method for a given test entity.
        for (final Recipe currentRecipe : expectedRecipeList) {
            final RecipeModel expectedRecipeModel = RecipeMockFactory.createMockRecipeModelFromDomain(currentRecipe);
            Mockito.when(recipeMapperHandler.mapDomainToModel(currentRecipe)).thenReturn(expectedRecipeModel);
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
            //Map uploader
            recipeModel.setUploader(mockedDomainRecipe.getUploader());
            //Map recipe summary
            recipeModel.setRecipeSummary(mockedDomainRecipe.getRecipeSummary());
            //Map thumbsUp count
            recipeModel.setThumbsUpCount(mockedDomainRecipe.getThumbsUpCount());
            //Map thumbsDown count
            recipeModel.setThumbsDownCount(mockedDomainRecipe.getThumbsDownCount());
            //Map tags
            recipeModel.setTags(mockedDomainRecipe.getTags());
            //Map set of ingredients.
            recipeModel.setIngredients(mockedDomainRecipe.getIngredients());
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
        if (null != inputRecipeDomain
                && StringUtils.isNotEmpty(inputRecipeDomain.getName().trim())
                && StringUtils.isNotEmpty(inputRecipeDomain.getUploader().trim())
                && StringUtils.isNotEmpty(inputRecipeDomain.getRecipeSummary().trim())) {

            recipeEntity = new RecipeEntity();
            //Map key.
            recipeEntity.setKey(inputRecipeDomain.getKey());
            //Map name.
            recipeEntity.setName(inputRecipeDomain.getName());
            //Map uploader
            recipeEntity.setUploader(inputRecipeDomain.getUploader());
            //Map recipe summary
            recipeEntity.setRecipeSummary(inputRecipeDomain.getRecipeSummary());
            //Map thumbsUp count
            recipeEntity.setThumbsUpCount(inputRecipeDomain.getThumbsUpCount());
            //Map thumbsDown count
            recipeEntity.setThumbsDownCount(inputRecipeDomain.getThumbsDownCount());
            //Map tags
            recipeEntity.setTags(inputRecipeDomain.getTags());
            //Map set of ingredients.
            recipeEntity.setIngredientsMap(inputRecipeDomain.getIngredients());
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
        //The recipeEntity to be mapped must not be null and the recipeEntity must have a name, uploader and summary.
        if (null != inputRecipeEntity
                && StringUtils.isNotEmpty(inputRecipeEntity.getName().trim())
                && StringUtils.isNotEmpty(inputRecipeEntity.getUploader().trim())
                && StringUtils.isNotEmpty(inputRecipeEntity.getRecipeSummary().trim())) {

            recipe = new Recipe
                    .RecipeBuilder(
                    inputRecipeEntity.getName(),
                    inputRecipeEntity.getUploader(),
                    inputRecipeEntity.getRecipeSummary())
                    .withTags(inputRecipeEntity.getTags())
                    .withIngredients(inputRecipeEntity.getIngredientsMap())
                    .withMethod(inputRecipeEntity.getMethodMap())
                    .withThumbsUpCount(inputRecipeEntity.getThumbsUpCount())//override newly generated 0 count to ensure no loss of data
                    .withThumbsDownCount(inputRecipeEntity.getThumbsDownCount())//override newly generated 0 count to ensure no loss of data
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
        if (null != inputRecipeModel
                && StringUtils.isNotEmpty(inputRecipeModel.getName().trim())
                && StringUtils.isNotEmpty(inputRecipeModel.getUploader().trim())
                && StringUtils.isNotEmpty(inputRecipeModel.getRecipeSummary().trim())) {

            domainRecipe = new Recipe
                    .RecipeBuilder(
                    inputRecipeModel.getName(),
                    inputRecipeModel.getUploader(),
                    inputRecipeModel.getRecipeSummary())
                    .withTags(inputRecipeModel.getTags())
                    .withThumbsUpCount(inputRecipeModel.getThumbsUpCount()) //override newly generated 0 count to ensure no loss of data
                    .withThumbsDownCount(inputRecipeModel.getThumbsDownCount()) //override newly generated 0 count to ensure no loss of data
                    .withIngredients(inputRecipeModel.getIngredients())
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
        if (null != inputRecipeModel
                && StringUtils.isNotEmpty(inputRecipeModel.getName().trim())
                && StringUtils.isNotEmpty(inputRecipeModel.getUploader().trim())
                && StringUtils.isNotEmpty(inputRecipeModel.getRecipeSummary().trim())) {

            domainRecipe = new Recipe
                    .RecipeBuilder(
                    inputRecipeModel.getName(),
                    inputRecipeModel.getUploader(),
                    inputRecipeModel.getRecipeSummary())
                    .withTags(inputRecipeModel.getTags())
                    .withThumbsUpCount(inputRecipeModel.getThumbsUpCount()) //override newly generated 0 count to ensure no loss of data
                    .withThumbsDownCount(inputRecipeModel.getThumbsDownCount()) //override newly generated 0 count to ensure no loss of data
                    .withIngredients(inputRecipeModel.getIngredients())
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
    static List<RecipeEntity> createMockRecipeEntityList(final List<RecipeModel> inputRecipeModelList) {

        final List<RecipeEntity> recipeEntityList = new ArrayList<>();

        for (final RecipeModel currentRecipeModel : inputRecipeModelList) {

            RecipeEntity recipeEntity = null;
            //The recipe to be mapped must not be null and the recipe must have a name.
            if (null != currentRecipeModel
                    && StringUtils.isNotEmpty(currentRecipeModel.getName().trim())
                    && StringUtils.isNotEmpty(currentRecipeModel.getUploader().trim())
                    && StringUtils.isNotEmpty(currentRecipeModel.getRecipeSummary().trim())) {

                recipeEntity = new RecipeEntity();
                //Map key.
                recipeEntity.setKey(currentRecipeModel.getKey());
                //Map name.
                recipeEntity.setName(currentRecipeModel.getName());
                //Map uploader
                recipeEntity.setUploader(currentRecipeModel.getUploader());
                //Map recipe summary
                recipeEntity.setRecipeSummary(currentRecipeModel.getRecipeSummary());
                //Map thumbsUp count
                recipeEntity.setThumbsUpCount(currentRecipeModel.getThumbsUpCount());
                //Map thumbsDown count
                recipeEntity.setThumbsDownCount(currentRecipeModel.getThumbsDownCount());
                //Map tags
                recipeEntity.setTags(currentRecipeModel.getTags());
                //Map set of ingredients.
                recipeEntity.setIngredientsMap(currentRecipeModel.getIngredients());
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
        if (null != inputRecipeModel
                && StringUtils.isNotEmpty(inputRecipeModel.getName().trim())
                && StringUtils.isNotEmpty(inputRecipeModel.getUploader().trim())
                && StringUtils.isNotEmpty(inputRecipeModel.getRecipeSummary().trim())) {

            recipeEntity = new RecipeEntity();
            //Map key.
            recipeEntity.setKey(inputRecipeModel.getKey());
            //Map name.
            recipeEntity.setName(inputRecipeModel.getName());
            //Map uploader
            recipeEntity.setUploader(inputRecipeModel.getUploader());
            //Map recipe summary
            recipeEntity.setRecipeSummary(inputRecipeModel.getRecipeSummary());
            //Map thumbsUp count
            recipeEntity.setThumbsUpCount(inputRecipeModel.getThumbsUpCount());
            //Map thumbsDown count
            recipeEntity.setThumbsDownCount(inputRecipeModel.getThumbsDownCount());
            //Map tags
            recipeEntity.setTags(inputRecipeModel.getTags());
            //Map set of ingredients.
            recipeEntity.setIngredientsMap(inputRecipeModel.getIngredients());
            //Map method.
            recipeEntity.setMethodMap(inputRecipeModel.getMethod());

        }
        return recipeEntity;

    }

}
