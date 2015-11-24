package sooglejay.youtu.api.facecompare;

import android.os.Parcel;
import android.os.Parcelable;

/**
 返回值说明
 字段	类型	说明
 session_id	String	相应请求的session标识符，保留
 similarity	String	两个face的相似度  优图接口说明文档 熟悉诶
 errormsg	String	返回错误消息
 errorcode	Int	返回状态码

 */
public class FaceCompareResponseBean implements Parcelable {
 private float similarity;
 private String errormsg;
 private String session_id;
 private int errorcode;

 @Override
 public String toString() {
  return "DetectFaceResponseBean{" +
          "similarity=" + similarity +
          ", errormsg='" + errormsg + '\'' +
          ", session_id=" + session_id +
          ", errorcode=" + errorcode +
          '}';
 }

 public float getSimilarity() {
  return similarity;
 }

 public void setSimilarity(float similarity) {
  this.similarity = similarity;
 }

 public String getErrormsg() {
  return errormsg;
 }

 public void setErrormsg(String errormsg) {
  this.errormsg = errormsg;
 }

 public String getSession_id() {
  return session_id;
 }

 public void setSession_id(String session_id) {
  this.session_id = session_id;
 }

 public int getErrorcode() {
  return errorcode;
 }

 public void setErrorcode(int errorcode) {
  this.errorcode = errorcode;
 }

 @Override
 public int describeContents() {
  return 0;
 }

 @Override
 public void writeToParcel(Parcel dest, int flags) {
  dest.writeFloat(this.similarity);
  dest.writeString(this.errormsg);
  dest.writeString(this.session_id);
  dest.writeInt(this.errorcode);
 }

 public FaceCompareResponseBean() {
 }

 protected FaceCompareResponseBean(Parcel in) {
  this.similarity = in.readFloat();
  this.errormsg = in.readString();
  this.session_id = in.readString();
  this.errorcode = in.readInt();
 }

 public static final Creator<FaceCompareResponseBean> CREATOR = new Creator<FaceCompareResponseBean>() {
  public FaceCompareResponseBean createFromParcel(Parcel source) {
   return new FaceCompareResponseBean(source);
  }

  public FaceCompareResponseBean[] newArray(int size) {
   return new FaceCompareResponseBean[size];
  }
 };
}
