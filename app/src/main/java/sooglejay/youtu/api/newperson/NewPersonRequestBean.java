package sooglejay.youtu.api.newperson;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import sooglejay.youtu.model.Model;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class NewPersonRequestBean implements Model,Parcelable {
//    • 个体创建
//    1)
//    接口
//    http://api.youtu.qq.com/youtu/api/newperson
//            2)
//    描述
//    创建一个Person，并将Person放置到group_ids指定的组当中
//    3)
//    参数
//    参数名	类型	参数说明
//    必须	app_id	String	App的 API ID
//    必须	group_ids	Array(String)	加入到组的列表
//    必须	person_id	String	指定的个体id
//    可选	image	String(Bytes)	base64编码的二进制图片数据
//    可选	url	String	图片的url, image 和url只提供一个就可以了,如果都提供,只使用url
//    可选	person_name	String	名字
//    可选	tag	String	备注信息

    private String app_id;
    private List<String> group_ids;
    private String person_id;
    private String image;
    private String person_name;
    private String tag;

    @Override
    public String toString() {
        return "NewPersonRequestBean{" +
                "app_id='" + app_id + '\'' +
                ", group_ids=" + group_ids +
                ", person_id='" + person_id + '\'' +
                ", image='" + image + '\'' +
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

    public List<String> getGroup_ids() {
        return group_ids;
    }

    public void setGroup_ids(List<String> group_ids) {
        this.group_ids = group_ids;
    }

    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
        dest.writeStringList(this.group_ids);
        dest.writeString(this.person_id);
        dest.writeString(this.image);
        dest.writeString(this.person_name);
        dest.writeString(this.tag);
    }

    public NewPersonRequestBean() {
    }

    protected NewPersonRequestBean(Parcel in) {
        this.app_id = in.readString();
        this.group_ids = in.createStringArrayList();
        this.person_id = in.readString();
        this.image = in.readString();
        this.person_name = in.readString();
        this.tag = in.readString();
    }

    public static final Creator<NewPersonRequestBean> CREATOR = new Creator<NewPersonRequestBean>() {
        public NewPersonRequestBean createFromParcel(Parcel source) {
            return new NewPersonRequestBean(source);
        }

        public NewPersonRequestBean[] newArray(int size) {
            return new NewPersonRequestBean[size];
        }
    };
}
