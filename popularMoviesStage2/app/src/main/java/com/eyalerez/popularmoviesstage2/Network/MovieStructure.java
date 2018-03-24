package com.eyalerez.popularmoviesstage2.Network;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Erez_PC on 3/16/2018.
 */

public class MovieStructure implements Parcelable {
    String title;
    String id;
    String poster_path;
    String backdrop_path;
    String overview;
    float vote_average;
    String release_date;

    public MovieStructure(String title, String poster_path, String backdrop_path, String overview, float vote_average, String release_date) {
        this.title = title;
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
        this.overview = overview;
        this.vote_average = vote_average;
        this.release_date = release_date;
    }

    public static final Creator<MovieStructure> CREATOR = new Creator<MovieStructure>() {
        @Override
        public MovieStructure createFromParcel(Parcel in) {
            return new MovieStructure(in);
        }

        @Override
        public MovieStructure[] newArray(int size) {
            return new MovieStructure[size];
        }
    };

    public String getPoster_path() {
        return poster_path;
    }
    public String getTitle() {
        return title;
    }
    public String getRelease_date() {return release_date;}
    public float getVote_average() {return vote_average;}
    public String getOverview() {return overview;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(poster_path);
        parcel.writeString(backdrop_path);
        parcel.writeString(overview);
        parcel.writeFloat(vote_average);
        parcel.writeString(release_date);
        parcel.writeString(id.toString());
    }

    public MovieStructure(Parcel parcel){
        title = parcel.readString();
        poster_path = parcel.readString();
        backdrop_path = parcel.readString();
        overview = parcel.readString();
        vote_average = parcel.readFloat();
        release_date = parcel.readString();
        id = parcel.readString();
    }

    public String toString(){
        return (title);
    }

    public String getMovieID() {
        return id;
    }
}