package eu.rodrigocamara.bakingapp.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.rodrigocamara.bakingapp.R;
import eu.rodrigocamara.bakingapp.pojos.IngredientsItem;

/**
 * Created by rodrigo.camara on 08/12/2017.
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.MyViewHolder> {
    private Context mContext;
    private List<IngredientsItem> ingredientsItems;


    public IngredientsAdapter(Context mContext, List<IngredientsItem> ingredientsItems) {
        this.mContext = mContext;
        this.ingredientsItems = ingredientsItems;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvIngredientName)
        TextView tvIngredientName;

        @BindView(R.id.tvIngredientDetail)
        TextView getTvIngredientDetail;

        public MyViewHolder(View view) {
            super(view);
            try {
                ButterKnife.bind(this, view);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_item, parent, false);

        return new IngredientsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvIngredientName.setText(ingredientsItems.get(position).getIngredient());
        holder.tvIngredientName.setTypeface(null, Typeface.BOLD);
        holder.getTvIngredientDetail.setText(ingredientsItems.get(position).getQuantity() + " " + ingredientsItems.get(position).getMeasure());
    }

    @Override
    public int getItemCount() {
        return ingredientsItems.size();
    }


}