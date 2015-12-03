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

    @Override
    public String toString() {
        return "LikeBean{" +
                "imagePath='" + imagePath + '\'' +
                ", id=" + id +
                '}';
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imagePath);
        dest.writeInt(this.id);
    }

    public LikeBean() {
    }

    protected LikeBean(Parcel in) {
        this.imagePath = in.readString();
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
