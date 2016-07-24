package br.ufc.ec.pet.bustracker.trackerapp.types;

import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by santana on 23/07/16.
 */
public class Bus implements Parcelable {
    int mId;
    double mLatitude;
    double mLongitude;

    public Bus(){

    }
    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    private Bus(Parcel in) {
        mId = in.readInt();
        mLatitude = in.readDouble();
        mLongitude = in.readDouble();
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        this.mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        this.mLongitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeDouble(mLatitude);
        dest.writeDouble(mLongitude);
    }

    public static final Parcelable.Creator<Bus> CREATOR
            = new Parcelable.Creator<Bus>() {
        public Bus createFromParcel(Parcel in) {
            return new Bus(in);
        }

        public Bus[] newArray(int size) {
            return new Bus[size];
        }
    };
}
