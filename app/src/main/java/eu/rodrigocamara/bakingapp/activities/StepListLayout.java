package eu.rodrigocamara.bakingapp.activities;

import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import net.cachapa.expandablelayout.ExpandableLayout;

import butterknife.BindView;
import eu.rodrigocamara.bakingapp.R;

/**
 * Created by Ro on 09/12/2017.
 */

public class StepListLayout {
    @BindView(R.id.expandable_view)
    ExpandableLayout expandableLayout;

    @BindView(R.id.rv_ingredients)
    RecyclerView rvIngredients;

    @BindView(R.id.rl_ingredients)
    RelativeLayout rlIngredients;

    @BindView(R.id.iv_arrow)
    ImageView ivArrow;
}
