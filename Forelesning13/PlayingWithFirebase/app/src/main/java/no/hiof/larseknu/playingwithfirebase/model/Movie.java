package no.hiof.larseknu.playingwithfirebase.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by larseknu on 25.10.2017.
 */

public class Movie implements Parcelable {

    private String uid;
    private String title;
    private String description;
    private String releaseDate;
    private String posterUrl;

    public Movie() { }

    public Movie(String uid, String title, String description, String releaseDate, String posterUrl) {
        this.uid = uid;
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
        this.posterUrl = posterUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof Movie) {
            Movie otherMovie = (Movie) obj;
            result = (this.getUid().equals(otherMovie.getUid()));
        }
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uid);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.releaseDate);
        dest.writeString(this.posterUrl);
    }

    protected Movie(Parcel in) {
        this.uid = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        long tmpReleaseDate = in.readLong();
        this.releaseDate = in.readString();
        this.posterUrl = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
