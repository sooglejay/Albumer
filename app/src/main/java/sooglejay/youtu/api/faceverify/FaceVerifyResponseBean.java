package sooglejay.youtu.api.faceverify;

import android.os.Parcel;
import android.os.Parcelable;

import sooglejay.youtu.model.Model;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class FaceVerifyResponseBean implements Model,Parcelable {
//
//    返回值说明
//    字段	类型	说明
//    ismatch	bool	两个输入是否为同一人的判断
//    confidence	float	系统对这个判断的置信度。
//    session_id	String	相应请求的session标识符，可用于结果查询
//    errorcode	Int	返回状态码
//    errormsg	String	返回错误消息

    private String session_id;
    private String errormsg;
    private int errorcode;
    private float confidence;
    private boolean ismatch;

    @Override
    public String toString() {
        return "FaceVerifyResponseBean{" +
                "session_id='" + session_id + '\'' +
                ", errormsg='" + errormsg + '\'' +
                ", errorcode=" + errorcode +
                ", confidence=" + confidence +
                ", ismatch=" + ismatch +
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

    public float getConfidence() {
        return confidence;
    }

    public void setConfidence(float confidence) {
        this.confidence = confidence;
    }

    public boolean ismatch() {
        return ismatch;
    }

    public void setIsmatch(boolean ismatch) {
        this.ismatch = ismatch;
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
        dest.writeFloat(this.confidence);
        dest.writeByte(ismatch ? (byte) 1 : (byte) 0);
    }

    public FaceVerifyResponseBean() {
    }

    protected FaceVerifyResponseBean(Parcel in) {
        this.session_id = in.readString();
        this.errormsg = in.readString();
        this.errorcode = in.readInt();
        this.confidence = in.readFloat();
        this.ismatch = in.readByte() != 0;
    }

    public static final Creator<FaceVerifyResponseBean> CREATOR = new Creator<FaceVerifyResponseBean>() {
        public FaceVerifyResponseBean createFromParcel(Parcel source) {
            return new FaceVerifyResponseBean(source);
        }

        public FaceVerifyResponseBean[] newArray(int size) {
            return new FaceVerifyResponseBean[size];
        }
    };
}
