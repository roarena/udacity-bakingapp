package eu.rodrigocamara.bakingapp.network;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import eu.rodrigocamara.bakingapp.C;
import eu.rodrigocamara.bakingapp.network.interfaces.NetworkResponse;
import eu.rodrigocamara.bakingapp.network.interfaces.UIController;
import eu.rodrigocamara.bakingapp.pojos.Recipe;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by rodrigo.camara on 05/12/2017.
 */

public class Controller implements Callback<List<Recipe>> {

    private UIController uiController;

    public void start(UIController uiController) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(C.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        NetworkResponse networkResponse = retrofit.create(NetworkResponse.class);

        Call<List<Recipe>> call = networkResponse.loadRecipes();
        call.enqueue(this);
        this.uiController = uiController;
    }

    @Override
    public void onResponse(Call<List<Recipe>> call, retrofit2.Response<List<Recipe>> response) {
        if (response.isSuccessful()) {
            List<Recipe> recipesList = response.body();
            uiController.onResponseOK(recipesList);
        } else {
            Log.e(C.LOG_TAG, String.valueOf(response.errorBody()));
        }
    }

    @Override
    public void onFailure(Call<List<Recipe>> call, Throwable t) {
        t.printStackTrace();
        uiController.onResponseFail();
    }
}
