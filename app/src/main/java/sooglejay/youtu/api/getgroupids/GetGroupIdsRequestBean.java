package sooglejay.youtu.api.getgroupids;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class GetGroupIdsRequestBean implements Parcelable {
//    * 参数
//    * 参数名	类型	参数说明
//    * 必须	app_id	String	App的 API ID
    private String app_id;

    @Override
    public String toString() {
        return "GetGroupIdsRequestBean{" +
                "app_id='" + app_id + '\'' +
                '}';
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.app_id);
    }

    public GetGroupIdsRequestBean() {
    }

    protected GetGroupIdsRequestBean(Parcel in) {
        this.app_id = in.readString();
    }

    public static final Creator<GetGroupIdsRequestBean> CREATOR = new Creator<GetGroupIdsRequestBean>() {
        public GetGroupIdsRequestBean createFromParcel(Parcel source) {
            return new GetGroupIdsRequestBean(source);
        }

        public GetGroupIdsRequestBean[] newArray(int size) {
            return new GetGroupIdsRequestBean[size];
        }
    };
}
