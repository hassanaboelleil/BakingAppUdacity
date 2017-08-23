package com.example.hassan.bakingappfinal.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ModelRecipe implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("ingredients")
    private ArrayList<ModelIngredient> ingredients;
    @SerializedName("steps")
    private ArrayList<ModelStep> steps;
    @SerializedName("servings")
    private int servings;
    @SerializedName("image")
    private String imageUrl;

    public ModelRecipe() {
    }

    public static final Creator<ModelRecipe> CREATOR = new Creator<ModelRecipe>() {
        @Override
        public ModelRecipe createFromParcel(Parcel source) {
            return new ModelRecipe(source);
        }

        @Override
        public ModelRecipe[] newArray(int size) {
            return new ModelRecipe[size];
        }
    };

    protected ModelRecipe(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.ingredients = in.createTypedArrayList(ModelIngredient.CREATOR);
        this.steps = in.createTypedArrayList(ModelStep.CREATOR);
    }

    public int getServings() {
        return servings;
    }

    public ModelRecipe setServings(int servings) {
        this.servings = servings;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ModelRecipe setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public int getId() {
        return id;
    }

    public ModelRecipe setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ModelRecipe setName(String name) {
        this.name = name;
        return this;
    }

    public ArrayList<ModelIngredient> getAlls() {
        return ingredients;
    }

    public ModelRecipe setIngredients(ArrayList<ModelIngredient> ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    public ArrayList<ModelStep> getSteps() {
        return steps;
    }

    public ModelRecipe setSteps(ArrayList<ModelStep> steps) {
        this.steps = steps;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeTypedList(this.ingredients);
        dest.writeTypedList(this.steps);
    }
}
