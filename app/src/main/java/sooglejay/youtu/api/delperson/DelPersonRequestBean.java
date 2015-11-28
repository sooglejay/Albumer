package sooglejay.youtu.api.delperson;

import android.os.Parcel;
import android.os.Parcelable;

import sooglejay.youtu.model.Model;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class DelPersonRequestBean implements Model,Parcelable {

//        参数
//    参数名	类型	参数说明
//    必须	app_id	String	App的 API ID
//    person_id	String	待删除个体ID

    private String app_id;
    private String person_id;

    @Override
    public String toString() {
        return "DelPersonRequestBean{" +
                "app_id='" + app_id + '\'' +
                ", person_id='" + person_id + '\'' +
                '}';
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.app_id);
        dest.writeString(this.person_id);
    }

    public DelPersonRequestBean() {
    }

    protected DelPersonRequestBean(Parcel in) {
        this.app_id = in.readString();
        this.person_id = in.readString();
    }

    public static final Creator<DelPersonRequestBean> CREATOR = new Creator<DelPersonRequestBean>() {
        public DelPersonRequestBean createFromParcel(Parcel source) {
            return new DelPersonRequestBean(source);
        }

        public DelPersonRequestBean[] newArray(int size) {
            return new DelPersonRequestBean[size];
        }
    };
}
