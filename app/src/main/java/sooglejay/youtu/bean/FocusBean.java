package sooglejay.youtu.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by JammyQtheLab on 2015/12/1.
 */

@DatabaseTable(tableName = "tb_focus_name")
public class FocusBean implements Parcelable {
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

    public FocusBean() {
    }

    protected FocusBean(Parcel in) {
        this.imagePath = in.readString();
        this.id = in.readInt();
    }

    public static final Creator<FocusBean> CREATOR = new Creator<FocusBean>() {
        public FocusBean createFromParcel(Parcel source) {
            return new FocusBean(source);
        }

        public FocusBean[] newArray(int size) {
            return new FocusBean[size];
        }
    };
}
