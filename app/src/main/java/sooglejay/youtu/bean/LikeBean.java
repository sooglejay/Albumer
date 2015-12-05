package sooglejay.youtu.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by JammyQtheLab on 2015/12/1.
 */

@DatabaseTable(tableName = "tb_like_name")
public class LikeBean implements Parcelable {
    @DatabaseField
    private String imagePath;
    @DatabaseField
    private String person_id;//用户id

    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    private boolean isSelected;//在adapter 中辅助显示



    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @DatabaseField(generatedId = true)
    private int id;

    public LikeBean() {
    }

    public static Creator<LikeBean> getCREATOR() {
        return CREATOR;
    }

    @Override
    public String toString() {
        return "LikeBean{" +
                "imagePath='" + imagePath + '\'' +
                ", person_id='" + person_id + '\'' +
                ", isSelected=" + isSelected +
                ", id=" + id +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imagePath);
        dest.writeString(this.person_id);
        dest.writeByte(isSelected ? (byte) 1 : (byte) 0);
        dest.writeInt(this.id);
    }

    protected LikeBean(Parcel in) {
        this.imagePath = in.readString();
        this.person_id = in.readString();
        this.isSelected = in.readByte() != 0;
        this.id = in.readInt();
    }

    public static final Creator<LikeBean> CREATOR = new Creator<LikeBean>() {
        public LikeBean createFromParcel(Parcel source) {
            return new LikeBean(source);
        }

        public LikeBean[] newArray(int size) {
            return new LikeBean[size];
        }
    };
}
