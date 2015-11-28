package sooglejay.youtu.api.getfaceinfo;

import android.os.Parcel;
import android.os.Parcelable;

import sooglejay.youtu.model.Model;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class GetFaceInfoRequestBean implements Model,Parcelable {

//    参数
//    参数名	类型	参数说明
//    必须	app_id	String	App的 API ID
//    face_id	String	人脸id

    private String app_id;
    private String face_id;

    @Override
    public String toString() {
        return "GetFaceInfoRequestBean{" +
                "app_id='" + app_id + '\'' +
                ", face_id='" + face_id + '\'' +
                '}';
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getFace_id() {
        return face_id;
    }

    public void setFace_id(String face_id) {
        this.face_id = face_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.app_id);
        dest.writeString(this.face_id);
    }

    public GetFaceInfoRequestBean() {
    }

    protected GetFaceInfoRequestBean(Parcel in) {
        this.app_id = in.readString();
        this.face_id = in.readString();
    }

    public static final Creator<GetFaceInfoRequestBean> CREATOR = new Creator<GetFaceInfoRequestBean>() {
        public GetFaceInfoRequestBean createFromParcel(Parcel source) {
            return new GetFaceInfoRequestBean(source);
        }

        public GetFaceInfoRequestBean[] newArray(int size) {
            return new GetFaceInfoRequestBean[size];
        }
    };
}
