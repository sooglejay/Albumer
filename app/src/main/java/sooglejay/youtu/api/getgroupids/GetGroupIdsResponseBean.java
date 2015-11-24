package sooglejay.youtu.api.getgroupids;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class GetGroupIdsResponseBean implements Parcelable {

//    * group_ids	Array(String)	相应app_id的group_id列表
//    * errorcode	Int	返回状态码
//    * errormsg	String	返回错误消息

    private List<String>group_ids;
    private int errorcode;
    private String errormsg;

    @Override
    public String toString() {
        return "GetGroupIdsResponseBean{" +
                "group_ids=" + group_ids +
                ", errorcode=" + errorcode +
                ", errormsg='" + errormsg + '\'' +
                '}';
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
        dest.writeStringList(this.group_ids);
        dest.writeInt(this.errorcode);
        dest.writeString(this.errormsg);
    }

    public GetGroupIdsResponseBean() {
    }

    protected GetGroupIdsResponseBean(Parcel in) {
        this.group_ids = in.createStringArrayList();
        this.errorcode = in.readInt();
        this.errormsg = in.readString();
    }

    public static final Creator<GetGroupIdsResponseBean> CREATOR = new Creator<GetGroupIdsResponseBean>() {
        public GetGroupIdsResponseBean createFromParcel(Parcel source) {
            return new GetGroupIdsResponseBean(source);
        }

        public GetGroupIdsResponseBean[] newArray(int size) {
            return new GetGroupIdsResponseBean[size];
        }
    };
}
