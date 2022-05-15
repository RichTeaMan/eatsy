package org.eatsy.appservice.controller.application.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eatsy.appservice.model.RecipeModel;
import org.eatsy.appservice.service.RecipeFactory;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * API Controller unit tests with Mockito.
 */
//These two annotations tell Mockito to create the mocks based on the @Mock annotation and enable autowired
@SpringBootTest
@AutoConfigureMockMvc
public class ApiControllerTest {

    //mockMvc auto-configured and part of the dependencies directly loaded for this test class
    @Autowired
    MockMvc mockMvc;

    //ObjectMapper auto-configured and part of the dependencies directly loaded for this test class
    @Autowired
    ObjectMapper objectMapper;

    //Tells Mockito to mock the RecipeFactory instance
    @Mock
    RecipeFactory recipeFactoryHandler;

    @Test
    public void checkAddRecipeSuccess() throws Exception {

        //Setup - create a recipeModel object for mocking the RecipeFactory service whilst the /add endpoint
        // (in the REST controller) is under test.
        String recipeName = "Cocopops cereal";
        Set<String> ingredientSet = new HashSet<>();
        ingredientSet.add("Cocopops");
        ingredientSet.add("Milk");
        Map<Integer, String> method = new HashMap<>();
        method.put(1, "Put Cocopops in bowl");
        method.put(2, "Add milk");
        method.put(3, "Allow milk to soak in the chocolatey goodness");
        RecipeModel recipeModel = new RecipeModel();
        recipeModel.setName(recipeName);
        recipeModel.setIngredientSet(ingredientSet);
        recipeModel.setMethod(method);

        //Configure the mock to return the recipeModel when the createRecipeModel is called.
        //This chain method mocks the createRecipe() method call in the RecipeFactory, so every time the method is
        // called within the controller (triggered later in this test), it will return the specified value
        // in the parameter of the thenReturn() method.
        // In this case it returns the pre-set recipeModel (defined in this test setup), instead of actually calling
        // the createRecipe() method in the RecipeFactory service.
        Mockito.when(recipeFactoryHandler.createRecipe(recipeModel)).thenReturn(recipeModel);

        //Executes some code of the class under test. In this case, build the mock request that will hit the
        // "/add" endpoint and trigger the above chain method.
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/add")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(recipeModel));

        //Execute the test and assert the response is as expected.
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk());

    }

}
