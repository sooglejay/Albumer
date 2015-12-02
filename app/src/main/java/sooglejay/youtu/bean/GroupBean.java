package sooglejay.youtu.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by JammyQtheLab on 2015/12/1.
 */

@DatabaseTable(tableName = "tb_group_name")
public class GroupBean implements Parcelable {
    @DatabaseField
    private String name;

    @DatabaseField
    private boolean isSelected;//这个字段是用于 adapter的显示的


    public boolean isUsedForIdentify() {
        return isUsedForIdentify;
    }

    public void setIsUsedForIdentify(boolean isUsedForIdentify) {
        this.isUsedForIdentify = isUsedForIdentify;
    }

    @DatabaseField
    private boolean isUsedForIdentify;//这个字段是用于是否做为人脸识别的groupid



    @DatabaseField(generatedId = true)
    private int id;
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

    public GroupBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeByte(isSelected ? (byte) 1 : (byte) 0);
        dest.writeByte(isUsedForIdentify ? (byte) 1 : (byte) 0);
        dest.writeInt(this.id);
    }

    protected GroupBean(Parcel in) {
        this.name = in.readString();
        this.isSelected = in.readByte() != 0;
        this.isUsedForIdentify = in.readByte() != 0;
        this.id = in.readInt();
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
