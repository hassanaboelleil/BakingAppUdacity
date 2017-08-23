package com.example.hassan.bakingappfinal.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hassan.bakingappfinal.R;
import com.example.hassan.bakingappfinal.model.ModelIngredient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterIngredient extends RecyclerView.Adapter<AdapterIngredient.IngredientViewHolder> {

    private ArrayList<ModelIngredient> ingredientArrayList;

    public AdapterIngredient(ArrayList<ModelIngredient> ingredients) {
        this.ingredientArrayList = ingredients;
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textview_ingredient)
        TextView ingredientTextview;
        @BindView(R.id.textview_measure)
        TextView measureTextView;
        @BindView(R.id.textview_quantity)
        TextView quantityTextView;


        public IngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void onBind(int position){
            measureTextView.setText(ingredientArrayList.get(position).getMeasure());
            quantityTextView.setText(Integer.toString(ingredientArrayList.get(position).getQuantity()));
            ingredientTextview.setText(ingredientArrayList.get(position).getAll());
        }


    }


    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient, parent, false);
        return new IngredientViewHolder(rootView);

    }

    @Override
    public void onBindViewHolder(IngredientViewHolder holder, int position) {
        holder.onBind(position);
    }
    @Override
    public int getItemCount() {
        return ingredientArrayList.size() ;
    }




}
