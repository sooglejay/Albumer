package sooglejay.youtu.api.getfaceids;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class GetFaceIdsResponseBean implements Parcelable {

    //返回值说明
//    字段	类型	说明
//    face_ids	Array(String)	相应face的id列表
//    errorcode	Int	返回状态码
//    errormsg	String	返回错误消息

    private List<String> face_ids;
    private int errorcode;
    private String errormsg;

    @Override
    public String toString() {
        return "GetFaceIdsResponseBean{" +
                "face_ids=" + face_ids +
                ", errorcode=" + errorcode +
                ", errormsg='" + errormsg + '\'' +
                '}';
    }

    public List<String> getFace_ids() {
        return face_ids;
    }

    public void setFace_ids(List<String> face_ids) {
        this.face_ids = face_ids;
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
        dest.writeStringList(this.face_ids);
        dest.writeInt(this.errorcode);
        dest.writeString(this.errormsg);
    }

    public GetFaceIdsResponseBean() {
    }

    protected GetFaceIdsResponseBean(Parcel in) {
        this.face_ids = in.createStringArrayList();
        this.errorcode = in.readInt();
        this.errormsg = in.readString();
    }

    public static final Creator<GetFaceIdsResponseBean> CREATOR = new Creator<GetFaceIdsResponseBean>() {
        public GetFaceIdsResponseBean createFromParcel(Parcel source) {
            return new GetFaceIdsResponseBean(source);
        }

        public GetFaceIdsResponseBean[] newArray(int size) {
            return new GetFaceIdsResponseBean[size];
        }
    };
}
