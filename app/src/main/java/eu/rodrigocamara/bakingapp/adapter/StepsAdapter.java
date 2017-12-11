package eu.rodrigocamara.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.rodrigocamara.bakingapp.C;
import eu.rodrigocamara.bakingapp.R;
import eu.rodrigocamara.bakingapp.activities.StepDetailActivity;
import eu.rodrigocamara.bakingapp.activities.StepDetailFragment;
import eu.rodrigocamara.bakingapp.activities.StepListActivity;
import eu.rodrigocamara.bakingapp.pojos.Recipe;
import eu.rodrigocamara.bakingapp.pojos.Recipe$$Parcelable;

/**
 * Created by Ro on 09/12/2017.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder> {
    private final StepListActivity mParentActivity;
    private final Recipe recipe;
    private final boolean mTwoPane;

    public StepsAdapter(StepListActivity parent, Recipe recipe, boolean twoPane) {
        this.recipe = recipe;
        this.mParentActivity = parent;
        this.mTwoPane = twoPane;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_shortDesc)
        TextView tvShortDesc;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.step_list_content, parent, false);
        return new StepsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (recipe.getSteps().get(position).getShortDescription() != null) {
            holder.tvShortDesc.setText(recipe.getSteps().get(position).getShortDescription());
        }
        holder.itemView.setTag(recipe.getId());
        holder.itemView.setOnClickListener(getClickListener(position));
    }

    private View.OnClickListener getClickListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putInt(C.STEP, position);
                    arguments.putParcelable(C.RECIPE,new Recipe$$Parcelable(recipe));
                    StepDetailFragment fragment = new StepDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.step_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, StepDetailActivity.class);
                    intent.putExtra(C.STEP, position);
                    intent.putExtra(C.RECIPE,new Recipe$$Parcelable(recipe));
                    context.startActivity(intent);
                }
            }
        };
    }

    @Override
    public int getItemCount() {
        return recipe.getNumberSteps();
    }
}