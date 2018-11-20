package dev.ngai.fantastic.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Ngai
 * @since 2018/1/22
 * Des:
 */
public class Imginfo implements Parcelable {

    public String url;
    public String referer;

    public Imginfo(String url, String referer) {
        this.url = url;
        this.referer = referer;
    }

    protected Imginfo(Parcel in) {
        url = in.readString();
        referer = in.readString();
    }

    public static final Creator<Imginfo> CREATOR = new Creator<Imginfo>() {
        @Override
        public Imginfo createFromParcel(Parcel in) {
            return new Imginfo(in);
        }

        @Override
        public Imginfo[] newArray(int size) {
            return new Imginfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(referer);
    }
}
