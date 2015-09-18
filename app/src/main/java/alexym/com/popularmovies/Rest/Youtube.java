package alexym.com.popularmovies.Rest;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Cloudco on 31/08/15.
 */
public class Youtube implements Parcelable {

    private String name;
    private String source;

    public Youtube(String name, String source) {
        this.name = name;
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.source);
    }

    protected Youtube(Parcel in) {
        this.name = in.readString();
        this.source = in.readString();
    }

    public static final Parcelable.Creator<Youtube> CREATOR = new Parcelable.Creator<Youtube>() {
        public Youtube createFromParcel(Parcel source) {
            return new Youtube(source);
        }

        public Youtube[] newArray(int size) {
            return new Youtube[size];
        }
    };
}