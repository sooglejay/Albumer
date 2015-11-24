package sooglejay.youtu.api.detectface;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 返回值说明
 * 字段	类型	说明
 * session_id	String	相应请求的session标识符，可用于结果查询
 * image_id	String	系统中的图片标识符，用于标识用户请求中的图片
 * image_width	Int	请求图片的宽度
 * image_height	Int	请求图片的高度
 * face	Array(FaceItem)	被检测出的人脸FaceItem的列表
 * errorcode	Int	返回状态值
 * errormsg	String	返回错误消息
 */
public class DetectFaceResponseBean implements Parcelable {

//    Response:
//    {
//        "session_id":"xxxx",
//            "image_id":"xxxx",
//            "image_height":584,
//            "image_width":527,
//            "face":[
//        {
//            "face_id":"1001344647426015231",
//                "x":145,
//                "y":147,
//                "height":305.0,
//                "width":305.0,
//                "pitch":3,
//                "roll":0,
//                "yaw":0,
//                "age":34,
//                "gender":99,
//                "glass":true,
//                "expression":27,
//                "beauty":81
//        }
//        ],
//        "errorcode":0,
//            "errormsg":"ok"
//    }


    private String session_id;
    private String image_id;
    private int image_height;
    private int image_width;
    private List<FaceItem> face;
    private int errorcode;
    private String errormsg;

    @Override
    public String toString() {
        return "DetectFaceResponseBean{" +
                "session_id='" + session_id + '\'' +
                ", image_id='" + image_id + '\'' +
                ", image_height=" + image_height +
                ", image_width=" + image_width +
                ", face=" + face +
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

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public int getImage_height() {
        return image_height;
    }

    public void setImage_height(int image_height) {
        this.image_height = image_height;
    }

    public int getImage_width() {
        return image_width;
    }

    public void setImage_width(int image_width) {
        this.image_width = image_width;
    }

    public List<FaceItem> getFace() {
        return face;
    }

    public void setFace(List<FaceItem> face) {
        this.face = face;
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
        dest.writeString(this.image_id);
        dest.writeInt(this.image_height);
        dest.writeInt(this.image_width);
        dest.writeTypedList(face);
        dest.writeInt(this.errorcode);
        dest.writeString(this.errormsg);
    }

    public DetectFaceResponseBean() {
    }

    protected DetectFaceResponseBean(Parcel in) {
        this.session_id = in.readString();
        this.image_id = in.readString();
        this.image_height = in.readInt();
        this.image_width = in.readInt();
        this.face = in.createTypedArrayList(FaceItem.CREATOR);
        this.errorcode = in.readInt();
        this.errormsg = in.readString();
    }

    public static final Creator<DetectFaceResponseBean> CREATOR = new Creator<DetectFaceResponseBean>() {
        public DetectFaceResponseBean createFromParcel(Parcel source) {
            return new DetectFaceResponseBean(source);
        }

        public DetectFaceResponseBean[] newArray(int size) {
            return new DetectFaceResponseBean[size];
        }
    };
}
