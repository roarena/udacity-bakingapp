package eu.rodrigocamara.bakingapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.rodrigocamara.bakingapp.R;
import eu.rodrigocamara.bakingapp.adapter.RecipeAdapter;
import eu.rodrigocamara.bakingapp.network.Controller;
import eu.rodrigocamara.bakingapp.network.interfaces.UIController;
import eu.rodrigocamara.bakingapp.pojos.Response;

public class MainActivity extends AppCompatActivity implements UIController {

    @BindView(R.id.pb_loading)
    ProgressBar progressBar;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private RecipeAdapter recipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Controller controller = new Controller();
        controller.start(this);

        ButterKnife.bind(this);
    }

    @Override
    public void onResponseOK(List<Response> recipesList) {
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);

        progressBar.setVisibility(View.GONE);

        RecyclerView.LayoutManager mLayoutManager;
        if (tabletSize) {
            mLayoutManager = new GridLayoutManager(this, 2);
        } else {
            mLayoutManager = new LinearLayoutManager(this);
        }

        recipeAdapter = new RecipeAdapter(this, recipesList);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recipeAdapter);
    }

    @Override
    public void onResponseFail() {
        Toast.makeText(this, R.string.load_error, Toast.LENGTH_LONG).show();
        progressBar.setVisibility(View.GONE);
    }
}
