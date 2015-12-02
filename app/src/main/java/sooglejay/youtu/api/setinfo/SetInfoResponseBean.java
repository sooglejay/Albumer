package sooglejay.youtu.api.setinfo;

import android.os.Parcel;
import android.os.Parcelable;

import sooglejay.youtu.model.Model;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class SetInfoResponseBean implements Model,Parcelable {

    //    返回值说明
//    字段	类型	说明
//    session_id	Int	相应请求的session标识符
//    person_id	String	相应person的id
//    errorcode	Int	返回状态码
//    errormsg	String	返回错误消息

    private String session_id;
    private String person_id;
    private String errorcode;
    private String errormsg;

    @Override
    public String toString() {
        return "SetInfoResponseBean{" +
                "session_id='" + session_id + '\'' +
                ", person_id='" + person_id + '\'' +
                ", errorcode='" + errorcode + '\'' +
                ", errormsg='" + errormsg + '\'' +
                '}';
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }

    public String getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(String errorcode) {
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
        dest.writeString(this.person_id);
        dest.writeString(this.errorcode);
        dest.writeString(this.errormsg);
    }

    public SetInfoResponseBean() {
    }

    protected SetInfoResponseBean(Parcel in) {
        this.session_id = in.readString();
        this.person_id = in.readString();
        this.errorcode = in.readString();
        this.errormsg = in.readString();
    }

    public static final Creator<SetInfoResponseBean> CREATOR = new Creator<SetInfoResponseBean>() {
        public SetInfoResponseBean createFromParcel(Parcel source) {
            return new SetInfoResponseBean(source);
        }

        public SetInfoResponseBean[] newArray(int size) {
            return new SetInfoResponseBean[size];
        }
    };
}
