package com.example.android.myapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Anugrah on 7/10/17.
 */

public class Trailer implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    /**
     * Variable Declaration
     */
    @SerializedName("id")
    private String id;
    @SerializedName("iso_639_1")
    private String iso_639_1;
    @SerializedName("iso_3166_1")
    private String iso_3166_1;
    @SerializedName("key")
    private String key;
    @SerializedName("name")
    private String name;
    @SerializedName("site")
    private String site;
    @SerializedName("size")
    private Integer size;
    @SerializedName("type")
    private String type;

    /**
     * Trailer Class Constructor
     */
    public Trailer(String id, String iso_639_1, String iso_3166_1, String key,
                   String name, String site, Integer size, String type) {
        this.id = id;
        this.iso_639_1 = iso_639_1;
        this.iso_3166_1 = iso_3166_1;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;
    }

    /**
     * Get method to fill data from API and set method to fill the variable with the data
     */
    public String getVideoId() { return id;}
    public void setVideoId(String id) { this.id = id;}

    public String getIso_639_1() { return iso_639_1;}
    public void setIso_639_1(String iso_639_1) { this.iso_639_1 = iso_639_1;}

    public String getIso_3166_1() { return iso_3166_1;}
    public void setIso_3166_1(String iso_3166_1) { this.iso_3166_1 = iso_3166_1;}

    public String getKey() { return key;}
    public void setKey(String key) { this.key = key; }
    public String getVideoThumbnail() { return "http://img.youtube.com/vi/" + key + "/0.jpg";}
    public String getVideoUrl() { return "http://www.youtube.com/watch?v=" + key ;}

    public String getName() { return  name;}
    public void setName(String name) { this.key = name; }

    public String getSite() { return  site;}
    public void setSite(String site) { this.site = site; }

    public Integer getSize() { return  size;}
    public void setSize(Integer size) { this.size = size; }

    public String getType() { return  type;}
    public void setType(String type) { this.key = type; }

    /**
     * Parcelling
     */
    public Trailer(Parcel in) {
        this.id = in.readString();
        this.iso_639_1 = in.readString();
        this.iso_3166_1 = in.readString();
        this.key = in.readString();
        this.name = in.readString();
        this.site = in.readString();
        this.size = in.readInt();
        this.type = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.iso_639_1);
        dest.writeString(this.iso_3166_1);
        dest.writeString(this.key);
        dest.writeString(this.name);
        dest.writeString(this.site);
        dest.writeInt(this.size);
        dest.writeString(this.type);
    }

    @Override
    public String toString() {
        return "Trailer{" +
                "id='" + id + '\'' +
                ", iso_639_1='" + iso_639_1 + '\'' +
                ", iso_3166_1='" + iso_3166_1 + '\'' +
                ", key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", site='" + site + '\'' +
                ", size='" + size + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
