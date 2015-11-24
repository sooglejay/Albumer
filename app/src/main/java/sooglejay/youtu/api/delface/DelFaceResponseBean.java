package sooglejay.youtu.api.delface;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class DelFaceResponseBean implements Parcelable {

    //返回值说明
//    字段	类型	说明
//    session_id	Int	相应请求的session标识符
//    deleted	Int	成功删除的face数量
//    face_ids	Array(String)	成功删除的face_id列表
//    errorcode	Int	返回状态码
//    errormsg	String	返回错误消息

    private String session_id;
    private int deleted;
    private List<String> face_ids;
    private int errorcode;
    private String errormsg;

    @Override
    public String toString() {
        return "DelFaceResponseBean{" +
                "session_id='" + session_id + '\'' +
                ", deleted=" + deleted +
                ", face_ids=" + face_ids +
                ", errorcode=" + errorcode +
                ", errormsg='" + errormsg + '\'' +
                '}';
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
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
        dest.writeString(this.session_id);
        dest.writeInt(this.deleted);
        dest.writeStringList(this.face_ids);
        dest.writeInt(this.errorcode);
        dest.writeString(this.errormsg);
    }

    public DelFaceResponseBean() {
    }

    protected DelFaceResponseBean(Parcel in) {
        this.session_id = in.readString();
        this.deleted = in.readInt();
        this.face_ids = in.createStringArrayList();
        this.errorcode = in.readInt();
        this.errormsg = in.readString();
    }

    public static final Creator<DelFaceResponseBean> CREATOR = new Creator<DelFaceResponseBean>() {
        public DelFaceResponseBean createFromParcel(Parcel source) {
            return new DelFaceResponseBean(source);
        }

        public DelFaceResponseBean[] newArray(int size) {
            return new DelFaceResponseBean[size];
        }
    };
}
