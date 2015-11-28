package sooglejay.youtu.api.getinfo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import sooglejay.youtu.model.Model;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class GetInfoReponseBean implements Model,Parcelable {
//    返回值说明
//    字段	类型	说明
//    person_name	String	相应person的name
//    person_id	String	相应person的id
//    group_ids	Array(String)	包含此个体的组列表
//    face_ids	Array(String)	包含的人脸列表
//    errorcode	Int	返回状态码
//    errormsg	String	返回错误消息

    private String person_id;
    private String person_name;
    private String tag;
    private List<String> group_ids;
    private List<String> face_ids;
    private int errorcode;
    private String errormsg;

    @Override
    public String toString() {
        return "GetInfoReponseBean{" +
                "person_id='" + person_id + '\'' +
                ", person_name='" + person_name + '\'' +
                ", tag='" + tag + '\'' +
                ", group_ids=" + group_ids +
                ", face_ids=" + face_ids +
                ", errorcode=" + errorcode +
                ", errormsg='" + errormsg + '\'' +
                '}';
    }

    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }

    public String getPerson_name() {
        return person_name;
    }

    public void setPerson_name(String person_name) {
        this.person_name = person_name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<String> getGroup_ids() {
        return group_ids;
    }

    public void setGroup_ids(List<String> group_ids) {
        this.group_ids = group_ids;
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
        dest.writeString(this.person_id);
        dest.writeString(this.person_name);
        dest.writeString(this.tag);
        dest.writeStringList(this.group_ids);
        dest.writeStringList(this.face_ids);
        dest.writeInt(this.errorcode);
        dest.writeString(this.errormsg);
    }

    public GetInfoReponseBean() {
    }

    protected GetInfoReponseBean(Parcel in) {
        this.person_id = in.readString();
        this.person_name = in.readString();
        this.tag = in.readString();
        this.group_ids = in.createStringArrayList();
        this.face_ids = in.createStringArrayList();
        this.errorcode = in.readInt();
        this.errormsg = in.readString();
    }

    public static final Creator<GetInfoReponseBean> CREATOR = new Creator<GetInfoReponseBean>() {
        public GetInfoReponseBean createFromParcel(Parcel source) {
            return new GetInfoReponseBean(source);
        }

        public GetInfoReponseBean[] newArray(int size) {
            return new GetInfoReponseBean[size];
        }
    };
}
