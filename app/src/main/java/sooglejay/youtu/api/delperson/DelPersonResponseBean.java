package sooglejay.youtu.api.delperson;

import android.os.Parcel;
import android.os.Parcelable;

import sooglejay.youtu.model.Model;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class DelPersonResponseBean implements Model,Parcelable {

    //    返回值说明
//    字段	类型	说明
//    session_id	Int	相应请求的session标识符
//    person_id	String	成功删除的person_id
//    deleted	Int	成功删除的Person数量
//    errorcode	Int	返回状态码
//    errormsg	String	返回错误消息

    private String session_id;
    private String person_id;
    private int deleted;
    private int errorcode;
    private String errormsg;

    @Override
    public String toString() {
        return "DelPersonResponseBean{" +
                "session_id='" + session_id + '\'' +
                ", person_id='" + person_id + '\'' +
                ", deleted=" + deleted +
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

    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
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
        dest.writeString(this.person_id);
        dest.writeInt(this.deleted);
        dest.writeInt(this.errorcode);
        dest.writeString(this.errormsg);
    }

    public DelPersonResponseBean() {
    }

    protected DelPersonResponseBean(Parcel in) {
        this.session_id = in.readString();
        this.person_id = in.readString();
        this.deleted = in.readInt();
        this.errorcode = in.readInt();
        this.errormsg = in.readString();
    }

    public static final Creator<DelPersonResponseBean> CREATOR = new Creator<DelPersonResponseBean>() {
        public DelPersonResponseBean createFromParcel(Parcel source) {
            return new DelPersonResponseBean(source);
        }

        public DelPersonResponseBean[] newArray(int size) {
            return new DelPersonResponseBean[size];
        }
    };
}
