package com.example.hassan.bakingappfinal.activity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.hassan.bakingappfinal.R;
import com.example.hassan.bakingappfinal.adapter.AdapterStep;
import com.example.hassan.bakingappfinal.fragment.IngredientStepDetailFragment;
import com.example.hassan.bakingappfinal.model.ModelIngredient;
import com.example.hassan.bakingappfinal.model.ModelRecipe;
import com.example.hassan.bakingappfinal.database.RecipeContract.RecipeEntry;

import java.util.ArrayList;

import static com.example.hassan.bakingappfinal.adapter.AdapterRecipe.RECIPE;
import static com.example.hassan.bakingappfinal.adapter.AdapterStep.STEPS;
import static com.example.hassan.bakingappfinal.database.RecipeContract.RECIPE_CONTENT_URI;

public class Step extends AppCompatActivity implements AdapterStep.OnStepListener{


    private boolean isTablet;
    private ModelRecipe recipe;
    public static final String PANES = "panes";
    public static final String POSITION = "position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        recipe = getIntent().getParcelableExtra(RECIPE);
        isTablet = findViewById(R.id.detail_linear_layout) != null;
    }

    @Override
    public void onStepSelected(int position) {
        Bundle bundle = new Bundle();

        if (isTablet) {
            IngredientStepDetailFragment fragment = new IngredientStepDetailFragment();
            bundle.putInt(POSITION, position);
            bundle.putBoolean(PANES, isTablet);
            bundle.putParcelableArrayList(STEPS, recipe.getSteps());
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_ingredient_container, fragment)
                    .commit();
        }
        else if (isTablet && position == 0) {
            IngredientStepDetailFragment fragment = new IngredientStepDetailFragment();
            bundle.putInt(POSITION, position);
            bundle.putBoolean(PANES, isTablet);
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_ingredient_container, fragment)
                    .commit();
        }
        else {
            bundle.putInt(POSITION, position);
            bundle.putBoolean(PANES, isTablet);
            bundle.putParcelableArrayList(STEPS, recipe.getSteps());
            Intent intent = new Intent(Step.this, Detail.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_step, menu);
        return true;
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_favorite:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    if (isFavorite()) {
                        deleteRecipe();
                        item.setIcon(R.drawable.ic_favorite_normal);
                        Toast.makeText(this, String.format(getString(R.string.favorite_removed_message), recipe.getName()), Toast.LENGTH_LONG).show();
                    } else {
                        addRecipe();
                        item.setIcon(R.drawable.ic_favorite_added);
                        Toast.makeText(this, String.format(getString(R.string.favorite_added_message), recipe.getName()), Toast.LENGTH_LONG).show();
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.action_favorite);
        if (isFavorite()) {
            menuItem.setIcon(R.drawable.ic_favorite_added);
        } else {
            menuItem.setIcon(R.drawable.ic_favorite_normal);
        }
        return true;
    }


    private void getAll(ArrayList<ModelIngredient> ingredients) {

        for (ModelIngredient ingredient : ingredients) {
            ContentValues values = new ContentValues();
            values.put(RecipeEntry.COLUMN_RECIPE_ID, recipe.getId());
            values.put(RecipeEntry.COLUMN_RECIPE_NAME, recipe.getName());
            values.put(RecipeEntry.COLUMN_INGREDIENT_NAME, ingredient.getAll());
            values.put(RecipeEntry.COLUMN_INGREDIENT_MEASURE, ingredient.getMeasure());
            values.put(RecipeEntry.COLUMN_INGREDIENT_QUANTITY, ingredient.getQuantity());
            getContentResolver().insert(RECIPE_CONTENT_URI, values);
        }
    }

    synchronized private void addRecipe() {
        getContentResolver().delete(RECIPE_CONTENT_URI, null, null);
        getAll(recipe.getAlls());
    }

    synchronized private void deleteRecipe() {
        getContentResolver().delete(RECIPE_CONTENT_URI, null, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private boolean isFavorite() {
        String[] projection = {RecipeEntry.COLUMN_RECIPE_ID};
        String selection = RecipeEntry.COLUMN_RECIPE_ID + " = " + recipe.getId();
        @SuppressLint("Recycle") Cursor cursor = getContentResolver().query(RECIPE_CONTENT_URI,
                projection,
                selection,
                null,
                null,
                null);

        return (cursor != null ? cursor.getCount() : 0) > 0;
    }

}
