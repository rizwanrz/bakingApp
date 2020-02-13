package com.example.bakingapp.rizwan.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class Steps implements Parcelable {
    private int stepId;
    private String stepLongDescription;
    private String videoUrl;
    private String thumbnailUrl;
    private String stepShortDescription;

    public Steps(int stepId, String stepShortDescription, String stepLongDescription, String videoUrl, String thumbnailUrl) {
        this.stepId = stepId;
        this.stepShortDescription = stepShortDescription;
        this.stepLongDescription = stepLongDescription;
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getStepId() {
        return stepId;
    }

    public String getStepShortDescription() {
        return stepShortDescription;
    }

    public String getStepLongDescription() {
        return stepLongDescription;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    protected Steps(Parcel in) {
        stepId = in.readInt();
        stepShortDescription = in.readString();
        stepLongDescription = in.readString();
        videoUrl = in.readString();
        thumbnailUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(stepId);
        dest.writeString(stepShortDescription);
        dest.writeString(stepLongDescription);
        dest.writeString(videoUrl);
        dest.writeString(thumbnailUrl);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Steps> CREATOR = new Parcelable.Creator<Steps>() {
        @Override
        public Steps createFromParcel(Parcel in) {
            return new Steps(in);
        }

        @Override
        public Steps[] newArray(int size) {
            return new Steps[size];
        }
    };
}


