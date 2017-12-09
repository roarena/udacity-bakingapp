package eu.rodrigocamara.bakingapp.network.interfaces;

import java.util.List;

import eu.rodrigocamara.bakingapp.pojos.Response;

/**
 * Created by rodrigo.camara on 05/12/2017.
 */

public interface UIController {
    void onResponseOK(List<Response> recipesList);

    void onResponseFail();
}