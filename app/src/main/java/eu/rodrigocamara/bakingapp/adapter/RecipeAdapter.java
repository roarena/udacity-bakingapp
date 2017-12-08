package eu.rodrigocamara.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.rodrigocamara.bakingapp.R;
import eu.rodrigocamara.bakingapp.StepListActivity;
import eu.rodrigocamara.bakingapp.pojos.Response;
import eu.rodrigocamara.bakingapp.pojos.Response$$Parcelable;

/**
 * Created by rodrigo.camara on 06/12/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.MyViewHolder> {
    private Context mContext;
    private List<Response> recipeList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_recipeName)
        TextView tvRecipeName;

        @BindView(R.id.tv_recipeServings)
        TextView tvRecipeServings;

        @BindView(R.id.tv_recipeSteps)
        TextView tvRecipeSteps;

        @BindView(R.id.iv_cardBG)
        ImageView ivCardBG;

        public MyViewHolder(View view) {
            super(view);
            try {
                ButterKnife.bind(this, view);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public RecipeAdapter(Context mContext, List<Response> recipeList) {
        this.mContext = mContext;
        this.recipeList = recipeList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Response recipe = recipeList.get(position);
        holder.tvRecipeName.setText(recipe.getName());
        holder.tvRecipeServings.setText(String.valueOf(recipe.getServings()));
        holder.tvRecipeSteps.setText(String.valueOf(recipe.getNumberSteps()));
        holder.tvRecipeName.setOnClickListener(clickListener(recipe));
        //TODO SetImage with Picasso.
    }
    private View.OnClickListener clickListener(final Response recipe) {
        return new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, StepListActivity.class);
                intent.putExtra("Parcel", new Response$$Parcelable(recipe));
                mContext.startActivity(intent);
            }
        };
    }
    @Override
    public int getItemCount() {
        return recipeList.size();
    }


}
