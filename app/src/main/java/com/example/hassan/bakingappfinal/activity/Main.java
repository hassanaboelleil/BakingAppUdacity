package com.example.hassan.bakingappfinal.activity;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hassan.bakingappfinal.R;
import com.example.hassan.bakingappfinal.adapter.AdapterRecipe;
import com.example.hassan.bakingappfinal.model.ModelRecipe;
import com.example.hassan.bakingappfinal.network.ApiClient;
import com.example.hassan.bakingappfinal.network.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.hassan.bakingappfinal.adapter.AdapterRecipe.RECIPE;

public class Main extends AppCompatActivity {

    private ArrayList<ModelRecipe> recipeArrayList;
    @BindView(R.id.recyclerview_recipe)
    RecyclerView recipeRecyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recipeRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        } else {
            recipeRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }

        if (savedInstanceState != null) {
            recipeArrayList = savedInstanceState.getParcelableArrayList(RECIPE);
            setData();
        } else {
            loadRecipes();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(RECIPE, recipeArrayList);
    }

    private void setData() {
        progressBar.setVisibility(View.INVISIBLE);
        AdapterRecipe recipeAdapter = new AdapterRecipe(Main.this, recipeArrayList);
        recipeRecyclerView.setAdapter(recipeAdapter);
    }

    private void loadRecipes() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        final Type TYPE = new TypeToken<ArrayList<ModelRecipe>>() {
        }.getType();
        Call<JsonArray> call = apiInterface.getRecipe();
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                recipeArrayList = new Gson().fromJson(response.body(), TYPE);
                setData();
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(Main.this, R.string.error_no_internet, Toast.LENGTH_LONG).show();
            }
        });
    }


}
