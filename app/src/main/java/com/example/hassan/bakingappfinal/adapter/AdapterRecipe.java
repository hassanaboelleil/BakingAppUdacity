package com.example.hassan.bakingappfinal.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hassan.bakingappfinal.R;
import com.example.hassan.bakingappfinal.activity.Step;
import com.example.hassan.bakingappfinal.model.ModelRecipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterRecipe extends RecyclerView.Adapter<AdapterRecipe.RecipeViewHolder> {

    private Context context;
    private ArrayList<ModelRecipe> recipeArrayList;
    public static final String RECIPE = "recipe";

    public AdapterRecipe(Context context, ArrayList<ModelRecipe> recipes) {
        this.context = context;
        this.recipeArrayList = recipes;
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.recipe_imageview)
        ImageView recipeImageView;
        @BindView(R.id.textview_recipe_name)
        TextView recipeName;
        @BindView(R.id.textview_recipe_servings)  TextView servings;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void onBind(int position) {
            if (!recipeArrayList.isEmpty()) {
                if (!recipeArrayList.get(position).getImageUrl().isEmpty()) {
                    recipeImageView.setVisibility(View.VISIBLE);
                    Picasso.with(context)
                            .load(recipeArrayList.get(position).getImageUrl())
                            .into(recipeImageView);
                }else
                {
                    recipeImageView.setImageResource(R.drawable.mmmm);
                }
                recipeName.setText(recipeArrayList.get(position).getName());
                servings.setText(itemView.getContext().getString(R.string.servings) + " " + recipeArrayList.get(position).getServings());
            }
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, Step.class);
            intent.putExtra(RECIPE, recipeArrayList.get(getAdapterPosition()));
            context.startActivity(intent);
        }
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe, parent, false);
        return new RecipeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecipeViewHolder holder,int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return recipeArrayList.size();
    }

}
