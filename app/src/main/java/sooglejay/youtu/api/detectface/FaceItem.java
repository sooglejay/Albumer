package sooglejay.youtu.api.detectface;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class FaceItem implements Parcelable {
    //说明

//    face_id	String	人脸标识
//    x	object	人脸框左上角x
//    y	object	人脸框左上角y
//    width	object	人脸框宽度
//    height	object	人脸框高度
//    gender	object	性别 [0/(female)~100(male)]
//    age	object	年龄 [0~100]
//    expression	object	微笑[0(normal)~50(smile)~100(laugh)]
//    beauty	object	魅力 [0~100]
//    glass	Bool	是否有眼镜 [true,false]
//    pitch	Int	上下偏移[-30,30]
//    yaw	Int	左右偏移[-30,30]
//    roll	Int	平面旋转[-180,180]

//    {
//        "face_id":"1001344647426015231",
//            "x":145,
//            "y":147,
//            "height":305.0,
//            "width":305.0,
//            "pitch":3,
//            "roll":0,
//            "yaw":0,
//            "age":34,
//            "gender":99,
//            "glass":true,
//            "expression":27,
//            "beauty":81
//    }

    private String face_id;
    private int x;
    private int y;
    private float height;
    private float width;
    private int pitch;
    private int roll;
    private int yaw;
    private int age;
    private int gender;
    private boolean glass;
    private int expression;
    private int beauty;

    @Override
    public String toString() {
        return "FaceItem{" +
                "face_id='" + face_id + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", height=" + height +
                ", width=" + width +
                ", pitch=" + pitch +
                ", roll=" + roll +
                ", yaw=" + yaw +
                ", age=" + age +
                ", gender=" + gender +
                ", glass=" + glass +
                ", expression=" + expression +
                ", beauty=" + beauty +
                '}';
    }

    public String getFace_id() {
        return face_id;
    }

    public void setFace_id(String face_id) {
        this.face_id = face_id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public int getPitch() {
        return pitch;
    }

    public void setPitch(int pitch) {
        this.pitch = pitch;
    }

    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

    public int getYaw() {
        return yaw;
    }

    public void setYaw(int yaw) {
        this.yaw = yaw;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public boolean isGlass() {
        return glass;
    }

    public void setGlass(boolean glass) {
        this.glass = glass;
    }

    public int getExpression() {
        return expression;
    }

    public void setExpression(int expression) {
        this.expression = expression;
    }

    public int getBeauty() {
        return beauty;
    }

    public void setBeauty(int beauty) {
        this.beauty = beauty;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.face_id);
        dest.writeInt(this.x);
        dest.writeInt(this.y);
        dest.writeFloat(this.height);
        dest.writeFloat(this.width);
        dest.writeInt(this.pitch);
        dest.writeInt(this.roll);
        dest.writeInt(this.yaw);
        dest.writeInt(this.age);
        dest.writeInt(this.gender);
        dest.writeByte(glass ? (byte) 1 : (byte) 0);
        dest.writeInt(this.expression);
        dest.writeInt(this.beauty);
    }

    public FaceItem() {
    }

    protected FaceItem(Parcel in) {
        this.face_id = in.readString();
        this.x = in.readInt();
        this.y = in.readInt();
        this.height = in.readFloat();
        this.width = in.readFloat();
        this.pitch = in.readInt();
        this.roll = in.readInt();
        this.yaw = in.readInt();
        this.age = in.readInt();
        this.gender = in.readInt();
        this.glass = in.readByte() != 0;
        this.expression = in.readInt();
        this.beauty = in.readInt();
    }

    public static final Creator<FaceItem> CREATOR = new Creator<FaceItem>() {
        public FaceItem createFromParcel(Parcel source) {
            return new FaceItem(source);
        }

        public FaceItem[] newArray(int size) {
            return new FaceItem[size];
        }
    };
}
