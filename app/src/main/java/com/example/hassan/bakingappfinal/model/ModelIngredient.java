package com.example.hassan.bakingappfinal.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ModelIngredient implements Parcelable {
    @SerializedName("quantity")
    private int quantity;
    @SerializedName("measure")
    private String measure;
    @SerializedName("ingredient")
    private String ingredient;

    public ModelIngredient() {
    }

    protected ModelIngredient(Parcel in) {
        this.quantity = in.readInt();
        this.measure = in.readString();
        this.ingredient = in.readString();
    }

    public static final Creator<ModelIngredient> CREATOR = new Creator<ModelIngredient>() {
        @Override
        public ModelIngredient createFromParcel(Parcel source) {
            return new ModelIngredient(source);
        }

        @Override
        public ModelIngredient[] newArray(int size) {
            return new ModelIngredient[size];
        }
    };

    public int getQuantity() {
        return quantity;
    }

    public ModelIngredient setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getMeasure() {
        return measure;
    }

    public ModelIngredient setMeasure(String measure) {
        this.measure = measure;
        return this;
    }

    public String getAll() {
        return ingredient;
    }

    public ModelIngredient setIngredient(String ingredient) {
        this.ingredient = ingredient;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.quantity);
        dest.writeString(this.measure);
        dest.writeString(this.ingredient);
    }

    @Override
    public String toString() {
        return "ModelIngredient{" +
                "quantity=" + quantity +
                ", measure='" + measure + '\'' +
                ", ingredient='" + ingredient + '\'' +
                '}';
    }
}
