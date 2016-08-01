package br.ufc.ec.pet.bustracker.trackerapp.types;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by santana on 01/08/16.
 */
public class Message implements Parcelable{
    private String mMessage;
    private String mTitle;

    public Message(String title, String message) {
        this.mMessage = message;
        this.mTitle = title;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        this.mMessage = message;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }
    public static final Parcelable.Creator<Message> CREATOR
            = new Parcelable.Creator<Message>() {
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    private Message(Parcel in) {
        mMessage = in.readString();
        mTitle = in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mMessage);
        dest.writeString(mTitle);
    }
}
