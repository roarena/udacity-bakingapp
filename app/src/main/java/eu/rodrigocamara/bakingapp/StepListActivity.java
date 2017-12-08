package eu.rodrigocamara.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import net.cachapa.expandablelayout.ExpandableLayout;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.rodrigocamara.bakingapp.adapter.IngredientsAdapter;
import eu.rodrigocamara.bakingapp.dummy.DummyContent;
import eu.rodrigocamara.bakingapp.pojos.IngredientsItem;
import eu.rodrigocamara.bakingapp.pojos.Response;

import java.util.List;

/**
 * An activity representing a list of Steps. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class StepListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private Response response;
    private IngredientsAdapter ingredientsAdapter;
    private StepListItems stepListItems;
    @BindView(R.id.stepslist)
    View stepList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);
        response = Parcels.unwrap(getIntent().getParcelableExtra("Parcel"));
        ingredientsAdapter = new IngredientsAdapter(this, response.getIngredients());
        ButterKnife.bind(this);
        stepListItems = new StepListItems();
        ButterKnife.bind(stepListItems, stepList);

        if (findViewById(R.id.step_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.step_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        stepListItems.rvIngredients.setLayoutManager(mLayoutManager);
        stepListItems.rvIngredients.setItemAnimator(new DefaultItemAnimator());
        stepListItems.rvIngredients.setAdapter(ingredientsAdapter);
        stepListItems.ivArrow.setTag("DOWN");
        stepListItems.rlIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stepListItems.expandableLayout.toggle();
                toggleArrow();
            }
        });
    }

    private void toggleArrow() {
        if (stepListItems.ivArrow.getTag().equals("DOWN")) {
            stepListItems.ivArrow.setImageResource(R.drawable.ic_keyboard_arrow_up_white_48dp);
            stepListItems.ivArrow.setTag("UP");
        } else {
            stepListItems.ivArrow.setImageResource(R.drawable.ic_keyboard_arrow_down_white_48dp);
            stepListItems.ivArrow.setTag("DOWN");
        }
    }

    static class StepListItems {
        @BindView(R.id.expandable_view)
        ExpandableLayout expandableLayout;

        @BindView(R.id.rv_ingredients)
        RecyclerView rvIngredients;

        @BindView(R.id.rl_ingredients)
        RelativeLayout rlIngredients;

        @BindView(R.id.iv_arrow)
        ImageView ivArrow;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, response, mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final StepListActivity mParentActivity;
        private final Response mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(StepDetailFragment.ARG_ITEM_ID, item.id);
                    StepDetailFragment fragment = new StepDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.step_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, StepDetailActivity.class);
                    intent.putExtra(StepDetailFragment.ARG_ITEM_ID, item.id);

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(StepListActivity parent,
                                      Response items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.step_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            if (mValues.getSteps().get(position).getShortDescription() != null) {
                holder.tvShortDesc.setText(mValues.getSteps().get(position).getShortDescription());
            }
            holder.itemView.setTag(mValues.getId());
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.getNumberSteps();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tv_shortDesc)
            TextView tvShortDesc;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }
}
