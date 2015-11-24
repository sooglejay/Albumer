package sooglejay.youtu.api.faceshape;

import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class FaceShapeResponseBean implements Parcelable {

//    返回值说明
//    字段	类型	说明
//    session_id	String	相应请求的session标识符，可用于结果查询
//    face_shape	Array(Point)	人脸轮廓结构体，包含所有人脸的轮廓点
//    face_profile	Array(Point)	描述脸型轮廓的21点
//    left_eye	Array(Point)	描述左眼轮廓的8点
//    right_eye	Array(Point)	描述右眼轮廓的8点
//    left_eyebrow	Array(Point)	描述左眉轮廓的8点
//    right_eyebrow	Array(Point)	描述右眉轮廓的8点
//    mouth	Array(Point)	描述嘴巴轮廓的22点
//    nose	Array(Point)	描述鼻子轮廓的13点
//    image_width	Int	请求图片的宽度
//    image_height	Int	请求图片的高度
//    errorcode	Int	返回状态值
//    errormsg	String	返回错误消息

    private String session_id;
    private List<Point> face_shape;
    private List<Point> face_profile;
    private List<Point> left_eye;
    private List<Point> right_eye;
    private List<Point> left_eyebrow;
    private List<Point> right_eyebrow;
    private List<Point> mouth;
    private List<Point> nose;
    private int image_width;
    private int image_height;
    private int errorcode;
    private String errormsg;

    @Override
    public String toString() {
        return "FaceShapeResponseBean{" +
                "session_id='" + session_id + '\'' +
                ", face_shape=" + face_shape +
                ", face_profile=" + face_profile +
                ", left_eye=" + left_eye +
                ", right_eye=" + right_eye +
                ", left_eyebrow=" + left_eyebrow +
                ", right_eyebrow=" + right_eyebrow +
                ", mouth=" + mouth +
                ", nose=" + nose +
                ", image_width=" + image_width +
                ", image_height=" + image_height +
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

    public List<Point> getFace_shape() {
        return face_shape;
    }

    public void setFace_shape(List<Point> face_shape) {
        this.face_shape = face_shape;
    }

    public List<Point> getFace_profile() {
        return face_profile;
    }

    public void setFace_profile(List<Point> face_profile) {
        this.face_profile = face_profile;
    }

    public List<Point> getLeft_eye() {
        return left_eye;
    }

    public void setLeft_eye(List<Point> left_eye) {
        this.left_eye = left_eye;
    }

    public List<Point> getRight_eye() {
        return right_eye;
    }

    public void setRight_eye(List<Point> right_eye) {
        this.right_eye = right_eye;
    }

    public List<Point> getLeft_eyebrow() {
        return left_eyebrow;
    }

    public void setLeft_eyebrow(List<Point> left_eyebrow) {
        this.left_eyebrow = left_eyebrow;
    }

    public List<Point> getRight_eyebrow() {
        return right_eyebrow;
    }

    public void setRight_eyebrow(List<Point> right_eyebrow) {
        this.right_eyebrow = right_eyebrow;
    }

    public List<Point> getMouth() {
        return mouth;
    }

    public void setMouth(List<Point> mouth) {
        this.mouth = mouth;
    }

    public List<Point> getNose() {
        return nose;
    }

    public void setNose(List<Point> nose) {
        this.nose = nose;
    }

    public int getImage_width() {
        return image_width;
    }

    public void setImage_width(int image_width) {
        this.image_width = image_width;
    }

    public int getImage_height() {
        return image_height;
    }

    public void setImage_height(int image_height) {
        this.image_height = image_height;
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
        dest.writeTypedList(face_shape);
        dest.writeTypedList(face_profile);
        dest.writeTypedList(left_eye);
        dest.writeTypedList(right_eye);
        dest.writeTypedList(left_eyebrow);
        dest.writeTypedList(right_eyebrow);
        dest.writeTypedList(mouth);
        dest.writeTypedList(nose);
        dest.writeInt(this.image_width);
        dest.writeInt(this.image_height);
        dest.writeInt(this.errorcode);
        dest.writeString(this.errormsg);
    }

    public FaceShapeResponseBean() {
    }

    protected FaceShapeResponseBean(Parcel in) {
        this.session_id = in.readString();
        this.face_shape = in.createTypedArrayList(Point.CREATOR);
        this.face_profile = in.createTypedArrayList(Point.CREATOR);
        this.left_eye = in.createTypedArrayList(Point.CREATOR);
        this.right_eye = in.createTypedArrayList(Point.CREATOR);
        this.left_eyebrow = in.createTypedArrayList(Point.CREATOR);
        this.right_eyebrow = in.createTypedArrayList(Point.CREATOR);
        this.mouth = in.createTypedArrayList(Point.CREATOR);
        this.nose = in.createTypedArrayList(Point.CREATOR);
        this.image_width = in.readInt();
        this.image_height = in.readInt();
        this.errorcode = in.readInt();
        this.errormsg = in.readString();
    }

    public static final Creator<FaceShapeResponseBean> CREATOR = new Creator<FaceShapeResponseBean>() {
        public FaceShapeResponseBean createFromParcel(Parcel source) {
            return new FaceShapeResponseBean(source);
        }

        public FaceShapeResponseBean[] newArray(int size) {
            return new FaceShapeResponseBean[size];
        }
    };
}
