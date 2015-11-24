package sooglejay.youtu.api.fuzzydetect;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class FuzzyDetectResponseBean implements Parcelable {

//    返回值说明
//    a. 返回主体包的内容
//    字段	类型	说明
//    errorcode	Int32	返回状态码,非0值为出错
//    errormsg	String	返回错误描述
//    fuzzy	bool	是否模糊
//    fuzzy_confidence	float	模糊参考值,范围 0-1的浮点数,越大置信度越高
//    seq	String	标示识别请求的序列号

    private int errorcode;
    private String errormsg;
    private boolean fuzzy;
    private float fuzzy_confidence;
    private String seq;

    @Override
    public String toString() {
        return "FuzzyDetectResponseBean{" +
                "errorcode=" + errorcode +
                ", errormsg='" + errormsg + '\'' +
                ", fuzzy=" + fuzzy +
                ", fuzzy_confidence=" + fuzzy_confidence +
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

    public boolean isFuzzy() {
        return fuzzy;
    }

    public void setFuzzy(boolean fuzzy) {
        this.fuzzy = fuzzy;
    }

    public float getFuzzy_confidence() {
        return fuzzy_confidence;
    }

    public void setFuzzy_confidence(float fuzzy_confidence) {
        this.fuzzy_confidence = fuzzy_confidence;
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
        dest.writeByte(fuzzy ? (byte) 1 : (byte) 0);
        dest.writeFloat(this.fuzzy_confidence);
        dest.writeString(this.seq);
    }

    public FuzzyDetectResponseBean() {
    }

    protected FuzzyDetectResponseBean(Parcel in) {
        this.errorcode = in.readInt();
        this.errormsg = in.readString();
        this.fuzzy = in.readByte() != 0;
        this.fuzzy_confidence = in.readFloat();
        this.seq = in.readString();
    }

    public static final Creator<FuzzyDetectResponseBean> CREATOR = new Creator<FuzzyDetectResponseBean>() {
        public FuzzyDetectResponseBean createFromParcel(Parcel source) {
            return new FuzzyDetectResponseBean(source);
        }

        public FuzzyDetectResponseBean[] newArray(int size) {
            return new FuzzyDetectResponseBean[size];
        }
    };
}
