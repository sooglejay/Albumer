package sooglejay.youtu.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by JammyQtheLab on 2015/12/1.
 */
//联系人表
@DatabaseTable(tableName = "tb_contact_name")
public class ContactBean implements Parcelable {
    @DatabaseField
    private String user_name;//用户名
    @DatabaseField
    private String image_path;//用户图片地址

    @Override
    public String toString() {
        return "ContactBean{" +
                "user_name='" + user_name + '\'' +
                ", image_path='" + image_path + '\'' +
                ", age=" + age +
                ", beauty=" + beauty +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getBeauty() {
        return beauty;
    }

    public void setBeauty(int beauty) {
        this.beauty = beauty;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @DatabaseField
    private int age;//用户年龄
    @DatabaseField
    private int beauty;//用户颜值
    @DatabaseField
    private String phoneNumber;//用户手机号码

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.user_name);
        dest.writeString(this.image_path);
        dest.writeInt(this.age);
        dest.writeInt(this.beauty);
        dest.writeString(this.phoneNumber);
    }

    public ContactBean() {
    }

    protected ContactBean(Parcel in) {
        this.user_name = in.readString();
        this.image_path = in.readString();
        this.age = in.readInt();
        this.beauty = in.readInt();
        this.phoneNumber = in.readString();
    }

    public static final Creator<ContactBean> CREATOR = new Creator<ContactBean>() {
        public ContactBean createFromParcel(Parcel source) {
            return new ContactBean(source);
        }

        public ContactBean[] newArray(int size) {
            return new ContactBean[size];
        }
    };
}
