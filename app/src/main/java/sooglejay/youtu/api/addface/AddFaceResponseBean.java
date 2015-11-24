package sooglejay.youtu.api.addface;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class AddFaceResponseBean implements Parcelable {

    //    4)
//    返回值说明
//    字段	类型	说明
//    session_id	Int	相应请求的session标识符
//    added	Int	成功加入的face数量
//    face_ids	String	增加的人脸ID列表
//    ret_codes	Array(Int)	每张图片增加人脸的返回码
//    errorcode	Int	返回状态码
//    errormsg	String	返回错误消息

    private String session_id;
    private int added;
    private List<String> face_ids;
    private List<Integer> ret_codes;
    private int errorcode;
    private String errormsg;

    @Override
    public String toString() {
        return "AddFaceResponseBean{" +
                "session_id='" + session_id + '\'' +
                ", added=" + added +
                ", face_ids=" + face_ids +
                ", ret_codes=" + ret_codes +
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

    public int getAdded() {
        return added;
    }

    public void setAdded(int added) {
        this.added = added;
    }

    public List<String> getFace_ids() {
        return face_ids;
    }

    public void setFace_ids(List<String> face_ids) {
        this.face_ids = face_ids;
    }

    public List<Integer> getRet_codes() {
        return ret_codes;
    }

    public void setRet_codes(List<Integer> ret_codes) {
        this.ret_codes = ret_codes;
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
        dest.writeInt(this.added);
        dest.writeStringList(this.face_ids);
        dest.writeList(this.ret_codes);
        dest.writeInt(this.errorcode);
        dest.writeString(this.errormsg);
    }

    public AddFaceResponseBean() {
    }

    protected AddFaceResponseBean(Parcel in) {
        this.session_id = in.readString();
        this.added = in.readInt();
        this.face_ids = in.createStringArrayList();
        this.ret_codes = new ArrayList<Integer>();
        in.readList(this.ret_codes, List.class.getClassLoader());
        this.errorcode = in.readInt();
        this.errormsg = in.readString();
    }

    public static final Creator<AddFaceResponseBean> CREATOR = new Creator<AddFaceResponseBean>() {
        public AddFaceResponseBean createFromParcel(Parcel source) {
            return new AddFaceResponseBean(source);
        }

        public AddFaceResponseBean[] newArray(int size) {
            return new AddFaceResponseBean[size];
        }
    };
}
