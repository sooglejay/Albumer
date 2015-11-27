package sooglejay.youtu.api.detectface;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by JammyQtheLab on 2015/11/26.
 */
public class AsyncBean implements Parcelable {
    private List<FaceItem>faces;

    public List<FaceItem> getFaces() {
        return faces;
    }

    public void setFaces(List<FaceItem> faces) {
        this.faces = faces;
    }


    public AsyncBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(faces);
    }

    protected AsyncBean(Parcel in) {
        this.faces = in.createTypedArrayList(FaceItem.CREATOR);
    }

    public static final Creator<AsyncBean> CREATOR = new Creator<AsyncBean>() {
        public AsyncBean createFromParcel(Parcel source) {
            return new AsyncBean(source);
        }

        public AsyncBean[] newArray(int size) {
            return new AsyncBean[size];
        }
    };
}
