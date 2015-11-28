package sooglejay.youtu.api.getpersonids;

import android.os.Parcel;
import android.os.Parcelable;

import sooglejay.youtu.model.Model;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class GetPersonIdsRequestBean implements Model,Parcelable {

//    参数
//    参数名	类型	参数说明
//    必须	app_id	String	App的 API ID
//    group_id	String	组id

    private String app_id;
    private String group_id;

    @Override
    public String toString() {
        return "GetPersonIdsRequestBean{" +
                "app_id='" + app_id + '\'' +
                ", group_id='" + group_id + '\'' +
                '}';
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.app_id);
        dest.writeString(this.group_id);
    }

    public GetPersonIdsRequestBean() {
    }

    protected GetPersonIdsRequestBean(Parcel in) {
        this.app_id = in.readString();
        this.group_id = in.readString();
    }

    public static final Creator<GetPersonIdsRequestBean> CREATOR = new Creator<GetPersonIdsRequestBean>() {
        public GetPersonIdsRequestBean createFromParcel(Parcel source) {
            return new GetPersonIdsRequestBean(source);
        }

        public GetPersonIdsRequestBean[] newArray(int size) {
            return new GetPersonIdsRequestBean[size];
        }
    };
}
