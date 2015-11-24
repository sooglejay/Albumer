package sooglejay.youtu.api.setinfo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class SetInfoRequestBean implements Parcelable {

//    参数
//    参数名	类型	参数说明
//    必须	app_id	String	App的 API ID
//    person_id	String	相应person的id
//    可选	person_name	String	新的name
//    tag	String	备注信息

    private String app_id;
    private String person_id;
    private String person_name;
    private String tag;

    @Override
    public String toString() {
        return "SetInfoRequestBean{" +
                "app_id='" + app_id + '\'' +
                ", person_id='" + person_id + '\'' +
                ", person_name='" + person_name + '\'' +
                ", tag='" + tag + '\'' +
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.app_id);
        dest.writeString(this.person_id);
        dest.writeString(this.person_name);
        dest.writeString(this.tag);
    }

    public SetInfoRequestBean() {
    }

    protected SetInfoRequestBean(Parcel in) {
        this.app_id = in.readString();
        this.person_id = in.readString();
        this.person_name = in.readString();
        this.tag = in.readString();
    }

    public static final Creator<SetInfoRequestBean> CREATOR = new Creator<SetInfoRequestBean>() {
        public SetInfoRequestBean createFromParcel(Parcel source) {
            return new SetInfoRequestBean(source);
        }

        public SetInfoRequestBean[] newArray(int size) {
            return new SetInfoRequestBean[size];
        }
    };
}
