package sooglejay.youtu.api.getpersonids;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class GetPersonIdsResponseBean implements Parcelable {

//    返回值说明
//    字段	类型	说明
//    person_ids	Array(String)	相应person的id列表
//    errorcode	Int	返回状态码
//    errormsg	String	返回错误消息

    private List<String> person_ids;
    private int errorcode;
    private String errormsg;

    @Override
    public String toString() {
        return "GetPersonIdsResponseBean{" +
                "person_ids=" + person_ids +
                ", errorcode=" + errorcode +
                ", errormsg='" + errormsg + '\'' +
                '}';
    }

    public List<String> getPerson_ids() {
        return person_ids;
    }

    public void setPerson_ids(List<String> person_ids) {
        this.person_ids = person_ids;
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
        dest.writeStringList(this.person_ids);
        dest.writeInt(this.errorcode);
        dest.writeString(this.errormsg);
    }

    public GetPersonIdsResponseBean() {
    }

    protected GetPersonIdsResponseBean(Parcel in) {
        this.person_ids = in.createStringArrayList();
        this.errorcode = in.readInt();
        this.errormsg = in.readString();
    }

    public static final Creator<GetPersonIdsResponseBean> CREATOR = new Creator<GetPersonIdsResponseBean>() {
        public GetPersonIdsResponseBean createFromParcel(Parcel source) {
            return new GetPersonIdsResponseBean(source);
        }

        public GetPersonIdsResponseBean[] newArray(int size) {
            return new GetPersonIdsResponseBean[size];
        }
    };
}
