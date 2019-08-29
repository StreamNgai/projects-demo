package dev.ngai.fantastic.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Weihl
 * @since 2017/7/3
 * Des:
 */
public class DiscoverTab implements Parcelable {

    public String tab;
    public String name;
    public boolean effective;

    protected DiscoverTab(Parcel in) {
        tab = in.readString();
        name = in.readString();
        effective = in.readByte() != 0;
    }

    public static final Creator<DiscoverTab> CREATOR = new Creator<DiscoverTab>() {
        @Override
        public DiscoverTab createFromParcel(Parcel in) {
            return new DiscoverTab(in);
        }

        @Override
        public DiscoverTab[] newArray(int size) {
            return new DiscoverTab[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tab);
        dest.writeString(name);
        dest.writeByte((byte) (effective ? 1 : 0));
    }
}
