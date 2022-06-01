package org.eatsy.appservice.persistence.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eatsy.appservice.persistence.model.RecipeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Class for utilising JPA repository methods and interacting with the database.
 * Tagged with Configuration, EnableJpaRepositories and EntityScan to ensure bean
 * created and spring Dependency Injection correctly take place.
 */
@Configuration
@EnableJpaRepositories
@EntityScan(basePackageClasses = RecipeEntity.class)
public class EatsyRepositoryHandler implements EatsyRepositoryService {

    //logger
    private static final Logger logger = LogManager.getLogger();

    //The Eatsy Repository that extends the JPA interface
    @Autowired
    private EatsyRepository eatsyRepository;

    /**
     * Persists the RecipeEntity object to the database.
     *
     * @param recipeEntity the recipe to be persisted.
     * @return the recipeEntity that has been successfully persisted.
     */
    @Override
    public RecipeEntity persistNewRecipe(final RecipeEntity recipeEntity) {

        logger.debug("Persisting a new recipe entity object called" + recipeEntity.getName());

        final RecipeEntity persistedRecipeEntity = eatsyRepository.save(recipeEntity);

        return persistedRecipeEntity;

    }

}
