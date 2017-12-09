package eu.rodrigocamara.bakingapp.network;

import android.util.Log;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import butterknife.BindView;
import eu.rodrigocamara.bakingapp.C;
import eu.rodrigocamara.bakingapp.R;
import eu.rodrigocamara.bakingapp.network.interfaces.NetworkResponse;
import eu.rodrigocamara.bakingapp.network.interfaces.UIController;
import eu.rodrigocamara.bakingapp.pojos.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by rodrigo.camara on 05/12/2017.
 */

public class Controller implements Callback<List<Response>> {
    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";
    private UIController uiController;

    public void start(UIController uiController) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        NetworkResponse networkResponse = retrofit.create(NetworkResponse.class);

        Call<List<Response>> call = networkResponse.loadRecipes();
        call.enqueue(this);
        this.uiController = uiController;
    }

    @Override
    public void onResponse(Call<List<Response>> call, retrofit2.Response<List<Response>> response) {
        if (response.isSuccessful()) {
            List<Response> recipesList = response.body();
            uiController.onResponseOK(recipesList);
        } else {
            Log.e(C.LOG_TAG, String.valueOf(response.errorBody()));
        }
    }

    @Override
    public void onFailure(Call<List<Response>> call, Throwable t) {
        t.printStackTrace();
        uiController.onResponseFail();
    }
}
