package sooglejay.youtu.api.imagetag;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class ImageTagItem implements Parcelable {
//    tag_name	String	返回图像标签的名字
//    tag_confidence	Int	图像标签的置信度,整形范围 0-100,越大置信度越高
//    tag_confidence_f	Float	图像标签的置信度,浮点数范围 0-1,越大置信度越高

    private String tag_name;
    private int tag_confidence;
    private float tag_confidence_f;

    @Override
    public String toString() {
        return "ImageTagItem{" +
                "tag_name='" + tag_name + '\'' +
                ", tag_confidence=" + tag_confidence +
                ", tag_confidence_f=" + tag_confidence_f +
                '}';
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public int getTag_confidence() {
        return tag_confidence;
    }

    public void setTag_confidence(int tag_confidence) {
        this.tag_confidence = tag_confidence;
    }

    public float getTag_confidence_f() {
        return tag_confidence_f;
    }

    public void setTag_confidence_f(float tag_confidence_f) {
        this.tag_confidence_f = tag_confidence_f;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tag_name);
        dest.writeInt(this.tag_confidence);
        dest.writeFloat(this.tag_confidence_f);
    }

    public ImageTagItem() {
    }

    protected ImageTagItem(Parcel in) {
        this.tag_name = in.readString();
        this.tag_confidence = in.readInt();
        this.tag_confidence_f = in.readFloat();
    }

    public static final Creator<ImageTagItem> CREATOR = new Creator<ImageTagItem>() {
        public ImageTagItem createFromParcel(Parcel source) {
            return new ImageTagItem(source);
        }

        public ImageTagItem[] newArray(int size) {
            return new ImageTagItem[size];
        }
    };
}
