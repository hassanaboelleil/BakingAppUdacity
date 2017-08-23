package com.example.hassan.bakingappfinal.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hassan.bakingappfinal.R;
import com.example.hassan.bakingappfinal.adapter.AdapterIngredient;
import com.example.hassan.bakingappfinal.adapter.AdapterStep;
import com.example.hassan.bakingappfinal.model.ModelIngredient;
import com.example.hassan.bakingappfinal.model.ModelRecipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.hassan.bakingappfinal.adapter.AdapterRecipe.RECIPE;

public class MasterListFragment extends Fragment {


    private ArrayList<ModelIngredient> ingredientsList;
    private AdapterIngredient adapterIngredient;
    private AdapterStep adapterStep;
    private ModelRecipe modelRecipe;

    @BindView(R.id.recyclerview_ingredients)
    RecyclerView ingredients;
    @BindView(R.id.recyclerview_steps)
    RecyclerView mIngredientStepRecyclerView;

    private AdapterStep.OnStepListener mClickListener;

    //          constructor
    public MasterListFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            modelRecipe = savedInstanceState.getParcelable(RECIPE);
        }
        final View rootView = inflater.inflate(R.layout.fragment_step, container, false);
        ButterKnife.bind(this, rootView);
        modelRecipe = getActivity().getIntent().getParcelableExtra(RECIPE);
        ingredientsList = modelRecipe.getAlls();

        adapterStep = new AdapterStep(getContext(), modelRecipe, mClickListener);
        mIngredientStepRecyclerView.setAdapter(adapterStep);

        adapterIngredient = new AdapterIngredient(ingredientsList);
        ingredients.setAdapter(adapterIngredient);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(RECIPE, modelRecipe);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mClickListener = (AdapterStep.OnStepListener) context;
        } catch (ClassCastException e) {
            Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
        }
    }

}
