package sooglejay.youtu.api.detectface;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by JammyQtheLab on 2015/11/26.
 */
public class AsyncBean implements Parcelable {
    private List<FaceItem>faces;
    private Bitmap bitmap;

    public List<FaceItem> getFaces() {
        return faces;
    }

    public void setFaces(List<FaceItem> faces) {
        this.faces = faces;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(faces);
        dest.writeParcelable(this.bitmap, 0);
    }

    public AsyncBean() {
    }

    protected AsyncBean(Parcel in) {
        this.faces = in.createTypedArrayList(FaceItem.CREATOR);
        this.bitmap = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Parcelable.Creator<AsyncBean> CREATOR = new Parcelable.Creator<AsyncBean>() {
        public AsyncBean createFromParcel(Parcel source) {
            return new AsyncBean(source);
        }

        public AsyncBean[] newArray(int size) {
            return new AsyncBean[size];
        }
    };
}
