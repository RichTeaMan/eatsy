package org.eatsy.appservice.persistence.service;

import org.eatsy.appservice.persistence.model.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA Repository interface for CRUD operations in the Eatsy database
 */
@Repository
public interface EatsyRepository extends JpaRepository<RecipeEntity, String> {


}
