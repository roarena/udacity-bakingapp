package eu.rodrigocamara.bakingapp.network.interfaces;

/**
 * Created by rodrigo.camara on 05/12/2017.
 */

import java.util.List;

import eu.rodrigocamara.bakingapp.pojos.Recipe;
import retrofit2.Call;
import retrofit2.http.GET;

public interface NetworkResponse {
    @GET("/topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> loadRecipes();
}
