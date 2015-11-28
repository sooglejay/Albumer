package sooglejay.youtu.api.delface;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import sooglejay.youtu.model.Model;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class DelFaceRequestBean implements Model,Parcelable {

    /**
     * 参数
     参数名	类型	参数说明
     必须	app_id	String	App的 API ID
     person_id	String	待删除人脸的person ID
     face_ids	Array(String)	删除人脸id的列表
     */

    private String app_id;
    private String person_id;
    private List<String> face_ids;

    @Override
    public String toString() {
        return "DelFaceRequestBean{" +
                "app_id='" + app_id + '\'' +
                ", person_id='" + person_id + '\'' +
                ", face_ids=" + face_ids +
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

    public List<String> getFace_ids() {
        return face_ids;
    }

    public void setFace_ids(List<String> face_ids) {
        this.face_ids = face_ids;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.app_id);
        dest.writeString(this.person_id);
        dest.writeStringList(this.face_ids);
    }

    public DelFaceRequestBean() {
    }

    protected DelFaceRequestBean(Parcel in) {
        this.app_id = in.readString();
        this.person_id = in.readString();
        this.face_ids = in.createStringArrayList();
    }

    public static final Creator<DelFaceRequestBean> CREATOR = new Creator<DelFaceRequestBean>() {
        public DelFaceRequestBean createFromParcel(Parcel source) {
            return new DelFaceRequestBean(source);
        }

        public DelFaceRequestBean[] newArray(int size) {
            return new DelFaceRequestBean[size];
        }
    };
}
