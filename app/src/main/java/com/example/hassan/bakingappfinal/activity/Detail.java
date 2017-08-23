package com.example.hassan.bakingappfinal.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.hassan.bakingappfinal.R;
import com.example.hassan.bakingappfinal.fragment.IngredientStepDetailFragment;
import com.example.hassan.bakingappfinal.model.ModelStep;

import java.util.ArrayList;

import static com.example.hassan.bakingappfinal.adapter.AdapterStep.STEPS;

public class Detail extends AppCompatActivity {

    int position;
    boolean twoPane;
    ArrayList<ModelStep> stepArrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            stepArrayList = getIntent().getParcelableArrayListExtra(STEPS);
            position = getIntent().getIntExtra(Step.POSITION, 0);
            twoPane = getIntent().getBooleanExtra(Step.PANES, false);

            IngredientStepDetailFragment fragment = new IngredientStepDetailFragment();

            Bundle bundle = new Bundle();
            bundle.putInt(Step.POSITION, position);
            bundle.putBoolean(Step.PANES, twoPane);
            bundle.putParcelableArrayList(STEPS, stepArrayList);
            fragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_container, fragment)
                    .commit();

        }
    }

}
