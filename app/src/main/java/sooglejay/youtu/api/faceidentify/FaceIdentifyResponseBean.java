package sooglejay.youtu.api.faceidentify;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class FaceIdentifyResponseBean implements Parcelable {
//
//    返回值说明
//    字段	类型	说明
//    session_id	String	相应请求的session标识符，可用于结果查询
//    candidates	Array(IdentifyItem)	识别出的top5候选人
//    errorcode	Int	返回状态码
//    errormsg	String	返回错误消息

    private String session_id;
    private String errormsg;
    private int errorcode;
    private List<IdentifyItem> candidates;

    @Override
    public String toString() {
        return "FaceIdentifyResponseBean{" +
                "session_id='" + session_id + '\'' +
                ", errormsg='" + errormsg + '\'' +
                ", errorcode=" + errorcode +
                ", candidates=" + candidates +
                '}';
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getErrormsg() {
        return errormsg;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }

    public int getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(int errorcode) {
        this.errorcode = errorcode;
    }

    public List<IdentifyItem> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<IdentifyItem> candidates) {
        this.candidates = candidates;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.session_id);
        dest.writeString(this.errormsg);
        dest.writeInt(this.errorcode);
        dest.writeList(this.candidates);
    }

    public FaceIdentifyResponseBean() {
    }

    protected FaceIdentifyResponseBean(Parcel in) {
        this.session_id = in.readString();
        this.errormsg = in.readString();
        this.errorcode = in.readInt();
        this.candidates = new ArrayList<IdentifyItem>();
        in.readList(this.candidates, List.class.getClassLoader());
    }

    public static final Creator<FaceIdentifyResponseBean> CREATOR = new Creator<FaceIdentifyResponseBean>() {
        public FaceIdentifyResponseBean createFromParcel(Parcel source) {
            return new FaceIdentifyResponseBean(source);
        }

        public FaceIdentifyResponseBean[] newArray(int size) {
            return new FaceIdentifyResponseBean[size];
        }
    };
}
