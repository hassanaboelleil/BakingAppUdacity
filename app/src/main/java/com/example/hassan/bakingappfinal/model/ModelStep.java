package com.example.hassan.bakingappfinal.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ModelStep implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("shortDescription")
    private String shortDescription;
    @SerializedName("description")
    private String description;
    @SerializedName("videoURL")
    private String videoUrl;
    @SerializedName("thumbnailURL")
    private String thumbnailUrl;

    public ModelStep() {
    }

    protected ModelStep(Parcel in) {
        this.id = in.readInt();
        this.shortDescription = in.readString();
        this.description = in.readString();
        this.videoUrl = in.readString();
        this.thumbnailUrl = in.readString();
    }

    public static final Creator<ModelStep> CREATOR = new Creator<ModelStep>() {
        @Override
        public ModelStep createFromParcel(Parcel source) {
            return new ModelStep(source);
        }

        @Override
        public ModelStep[] newArray(int size) {
            return new ModelStep[size];
        }
    };

    public int getId() {
        return id;
    }

    public ModelStep setId(int id) {
        this.id = id;
        return this;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public ModelStep setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ModelStep setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public ModelStep setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
        return this;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public ModelStep setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.shortDescription);
        dest.writeString(this.description);
        dest.writeString(this.videoUrl);
        dest.writeString(this.thumbnailUrl);
    }
}
