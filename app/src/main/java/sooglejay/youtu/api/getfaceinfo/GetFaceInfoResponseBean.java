package sooglejay.youtu.api.getfaceinfo;

import android.os.Parcel;
import android.os.Parcelable;

import sooglejay.youtu.api.detectface.FaceItem;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class GetFaceInfoResponseBean implements Parcelable {

//    返回值说明
//    字段	类型	说明
//    face_info	FaceItem	人脸信息
//    errorcode	Int	返回状态码
//    errormsg	String	返回错误消息

    private FaceItem face_info;
    private int errorcode;
    private String errormsg;

    @Override
    public String toString() {
        return "GetFaceInfoResponseBean{" +
                "face_info=" + face_info +
                ", errorcode=" + errorcode +
                ", errormsg='" + errormsg + '\'' +
                '}';
    }

    public FaceItem getFace_info() {
        return face_info;
    }

    public void setFace_info(FaceItem face_info) {
        this.face_info = face_info;
    }

    public int getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(int errorcode) {
        this.errorcode = errorcode;
    }

    public String getErrormsg() {
        return errormsg;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.face_info, 0);
        dest.writeInt(this.errorcode);
        dest.writeString(this.errormsg);
    }

    public GetFaceInfoResponseBean() {
    }

    protected GetFaceInfoResponseBean(Parcel in) {
        this.face_info = in.readParcelable(FaceItem.class.getClassLoader());
        this.errorcode = in.readInt();
        this.errormsg = in.readString();
    }

    public static final Creator<GetFaceInfoResponseBean> CREATOR = new Creator<GetFaceInfoResponseBean>() {
        public GetFaceInfoResponseBean createFromParcel(Parcel source) {
            return new GetFaceInfoResponseBean(source);
        }

        public GetFaceInfoResponseBean[] newArray(int size) {
            return new GetFaceInfoResponseBean[size];
        }
    };
}
