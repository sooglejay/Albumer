package sooglejay.youtu.api.fooddetct;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class FoodDetectResponseBean implements Parcelable {

//    errorcode	Int32	返回状态码,非0值为出错
//    errormsg	String	返回错误描述
//    food	bool	是否美食
//    food_confidence	float	模糊参考值,范围 0-1的浮点数,越大置信度越高
//    seq	String	标示识别请求的序列号

    private int errorcode;
    private String errormsg;
    private boolean food;
    private float food_confidence;
    private String seq;

    @Override
    public String toString() {
        return "FoodDetectResponseBean{" +
                "errorcode=" + errorcode +
                ", errormsg='" + errormsg + '\'' +
                ", food=" + food +
                ", food_confidence=" + food_confidence +
                ", seq='" + seq + '\'' +
                '}';
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

    public boolean isFood() {
        return food;
    }

    public void setFood(boolean food) {
        this.food = food;
    }

    public float getFood_confidence() {
        return food_confidence;
    }

    public void setFood_confidence(float food_confidence) {
        this.food_confidence = food_confidence;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.errorcode);
        dest.writeString(this.errormsg);
        dest.writeByte(food ? (byte) 1 : (byte) 0);
        dest.writeFloat(this.food_confidence);
        dest.writeString(this.seq);
    }

    public FoodDetectResponseBean() {
    }

    protected FoodDetectResponseBean(Parcel in) {
        this.errorcode = in.readInt();
        this.errormsg = in.readString();
        this.food = in.readByte() != 0;
        this.food_confidence = in.readFloat();
        this.seq = in.readString();
    }

    public static final Creator<FoodDetectResponseBean> CREATOR = new Creator<FoodDetectResponseBean>() {
        public FoodDetectResponseBean createFromParcel(Parcel source) {
            return new FoodDetectResponseBean(source);
        }

        public FoodDetectResponseBean[] newArray(int size) {
            return new FoodDetectResponseBean[size];
        }
    };
}
