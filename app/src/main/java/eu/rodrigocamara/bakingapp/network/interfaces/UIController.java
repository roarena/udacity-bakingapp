package eu.rodrigocamara.bakingapp.network.interfaces;

import java.util.List;

import eu.rodrigocamara.bakingapp.pojos.Recipe;

/**
 * Created by rodrigo.camara on 05/12/2017.
 */

public interface UIController {
    void onResponseOK(List<Recipe> recipesList);

    void onResponseFail();
}