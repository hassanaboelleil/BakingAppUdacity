package com.example.hassan.bakingappfinal.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hassan.bakingappfinal.R;
import com.example.hassan.bakingappfinal.model.ModelRecipe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterStep extends RecyclerView.Adapter<AdapterStep.IngredientStepViewHolder> {

    public static final String STEPS = "steps";
    private final OnStepListener onStepListener;
    private Context context;
    private ModelRecipe modelRecipe;

    //          interface clickLitenser
    public interface OnStepListener {
        void onStepSelected(int position);
    }


    //              constructor
    public AdapterStep(Context context, ModelRecipe recipe, OnStepListener listener) {
        this.context = context;
        this.modelRecipe = recipe;
        onStepListener = listener;
    }

    //                  inflate items with layout
    @Override
    public IngredientStepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.step, parent, false);
        return new IngredientStepViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(IngredientStepViewHolder holder, int position) {
        holder.onBind(position);
    }

    //          return the size of array
    @Override
    public int getItemCount() {
        return modelRecipe.getSteps().size();
    }


    class IngredientStepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.textview_ingredient_step)
        TextView mIngredientStepTextView;


        public IngredientStepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }


        void onBind(int position) {
            mIngredientStepTextView.setText(modelRecipe.getSteps().get(position).getShortDescription());
        }


        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            onStepListener.onStepSelected(clickedPosition);

        }
    }
}
