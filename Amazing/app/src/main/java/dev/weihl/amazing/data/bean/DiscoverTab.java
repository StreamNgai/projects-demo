package dev.weihl.amazing.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author Weihl
 * @since 2017/7/3
 * Des:
 */
@Entity
public class DiscoverTab implements Parcelable{
    @Id(autoincrement = true)
    Long id;
    public String tab;
    public String name;
    public boolean effective;
    @Generated(hash = 1177708070)
    public DiscoverTab(Long id, String tab, String name, boolean effective) {
        this.id = id;
        this.tab = tab;
        this.name = name;
        this.effective = effective;
    }
    @Generated(hash = 1246399418)
    public DiscoverTab() {
    }

    protected DiscoverTab(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
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

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTab() {
        return this.tab;
    }
    public void setTab(String tab) {
        this.tab = tab;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean getEffective() {
        return this.effective;
    }
    public void setEffective(boolean effective) {
        this.effective = effective;
    }

    @Override
    public String toString() {
        return "DiscoverTab{" +
                "id=" + id +
                ", tab='" + tab + '\'' +
                ", name='" + name + '\'' +
                ", effective=" + effective +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(tab);
        dest.writeString(name);
        dest.writeByte((byte) (effective ? 1 : 0));
    }
}
