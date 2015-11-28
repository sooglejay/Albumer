package sooglejay.youtu.api.newperson;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import sooglejay.youtu.model.Model;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class NewPersonResponseBean implements Model,Parcelable {

//    返回值说明
//    字段	类型	说明
//    session_id	Int	相应请求的session标识符
//    suc_group	Int	成功被加入的group数量
//    suc_face	Int	成功加入的face数量
//    person_id	String	相应person的id
//    face_id	String	创建所用图片生成的face_id
//    group_ids	Array(String)	加入成功的组id
//    errorcode	Int	返回码
//    errormsg	String	返回错误消息

    private String session_id;
    private int suc_group;
    private int suc_face;
    private String person_id;
    private String face_id;
    private List<String> group_ids;
    private int errorcode;
    private String errormsg;

    @Override
    public String toString() {
        return "NewPersonResponseBean{" +
                "session_id='" + session_id + '\'' +
                ", suc_group=" + suc_group +
                ", suc_face=" + suc_face +
                ", person_id='" + person_id + '\'' +
                ", face_id='" + face_id + '\'' +
                ", group_ids=" + group_ids +
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

    public int getSuc_group() {
        return suc_group;
    }

    public void setSuc_group(int suc_group) {
        this.suc_group = suc_group;
    }

    public int getSuc_face() {
        return suc_face;
    }

    public void setSuc_face(int suc_face) {
        this.suc_face = suc_face;
    }

    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }

    public String getFace_id() {
        return face_id;
    }

    public void setFace_id(String face_id) {
        this.face_id = face_id;
    }

    public List<String> getGroup_ids() {
        return group_ids;
    }

    public void setGroup_ids(List<String> group_ids) {
        this.group_ids = group_ids;
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
        dest.writeInt(this.suc_group);
        dest.writeInt(this.suc_face);
        dest.writeString(this.person_id);
        dest.writeString(this.face_id);
        dest.writeStringList(this.group_ids);
        dest.writeInt(this.errorcode);
        dest.writeString(this.errormsg);
    }

    public NewPersonResponseBean() {
    }

    protected NewPersonResponseBean(Parcel in) {
        this.session_id = in.readString();
        this.suc_group = in.readInt();
        this.suc_face = in.readInt();
        this.person_id = in.readString();
        this.face_id = in.readString();
        this.group_ids = in.createStringArrayList();
        this.errorcode = in.readInt();
        this.errormsg = in.readString();
    }

    public static final Creator<NewPersonResponseBean> CREATOR = new Creator<NewPersonResponseBean>() {
        public NewPersonResponseBean createFromParcel(Parcel source) {
            return new NewPersonResponseBean(source);
        }

        public NewPersonResponseBean[] newArray(int size) {
            return new NewPersonResponseBean[size];
        }
    };
}
