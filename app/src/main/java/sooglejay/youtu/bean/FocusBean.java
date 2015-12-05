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
        return "FocusBean{" +
                "imagePath='" + imagePath + '\'' +
                ", person_id='" + person_id + '\'' +
                ", isSelected=" + isSelected +
                ", isVisible=" + isVisible +
                ", id=" + id +
                '}';
    }

    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }

    @DatabaseField
    private String person_id;//用户id

    private boolean isSelected;//在adapter 中辅助显示
    private boolean isVisible;//在adapter 中辅助显示

    public boolean isVisible() {
        return isVisible;
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public static Creator<FocusBean> getCREATOR() {
        return CREATOR;
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

    public FocusBean() {
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
        dest.writeByte(isVisible ? (byte) 1 : (byte) 0);
        dest.writeInt(this.id);
    }

    protected FocusBean(Parcel in) {
        this.imagePath = in.readString();
        this.person_id = in.readString();
        this.isSelected = in.readByte() != 0;
        this.isVisible = in.readByte() != 0;
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
