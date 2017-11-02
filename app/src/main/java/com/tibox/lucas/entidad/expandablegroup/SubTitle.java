package com.tibox.lucas.entidad.expandablegroup;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by desa02 on 12/09/2017.
 */

public class SubTitle implements Parcelable {

    private String name;
    private String subname;
    private Drawable image;

    public SubTitle(String name, String subname, Drawable image ) {
        this.name = name;
        this.subname = subname;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubname() {
        return subname;
    }

    public void setSubname(String subname) {
        this.subname = subname;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.subname);
        dest.writeValue(this.image);
    }

    protected SubTitle(Parcel in) {
        this.name = in.readString();
        this.subname = in.readString();
        this.image = (Drawable) in.readValue(Drawable.class.getClassLoader());
    }

    public static final Parcelable.Creator<SubTitle> CREATOR = new Parcelable.Creator<SubTitle>() {
        @Override
        public SubTitle createFromParcel(Parcel source) {
            return new SubTitle(source);
        }

        @Override
        public SubTitle[] newArray(int size) {
            return new SubTitle[size];
        }
    };


}
