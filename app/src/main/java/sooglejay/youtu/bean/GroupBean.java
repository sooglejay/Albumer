package sooglejay.youtu.bean;

import android.os.Parcel;
import android.os.Parcelable;

import sooglejay.youtu.model.Model;

/**
 * Created by JammyQtheLab on 2015/12/1.
 */
public class GroupBean implements Model, Parcelable {
    private String name;
    private boolean isSelected;

    @Override
    public String toString() {
        return "GroupBean{" +
                "name='" + name + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeByte(isSelected ? (byte) 1 : (byte) 0);
    }

    public GroupBean() {
    }

    protected GroupBean(Parcel in) {
        this.name = in.readString();
        this.isSelected = in.readByte() != 0;
    }

    public static final Parcelable.Creator<GroupBean> CREATOR = new Parcelable.Creator<GroupBean>() {
        public GroupBean createFromParcel(Parcel source) {
            return new GroupBean(source);
        }

        public GroupBean[] newArray(int size) {
            return new GroupBean[size];
        }
    };
}
