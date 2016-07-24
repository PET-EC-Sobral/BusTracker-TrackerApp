package br.ufc.ec.pet.bustracker.trackerapp.types;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by santana on 23/07/16.
 */
public class Route implements Parcelable {
    int mId;
    String mName, mDescription;

    public Route(int id, String name, String description){
        mId = id;
        mName = name;
        mDescription = description;
    }

    public Route(String name, String description){
        mName = name;
        mDescription = description;
    }
    public Route(){

    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public static final Parcelable.Creator<Route> CREATOR
            = new Parcelable.Creator<Route>() {
        public Route createFromParcel(Parcel in) {
            return new Route(in);
        }

        public Route[] newArray(int size) {
            return new Route[size];
        }
    };

    private Route(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mDescription = in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeString(mDescription);
    }
}
