package sooglejay.youtu.api.faceidentify;

import android.os.Parcel;
import android.os.Parcelable;

import sooglejay.youtu.model.Model;

/**
 * Created by sooglejay on 2015/11/19.
 */
public class IdentifyItem implements Model,Parcelable {

//    {
//        "person_id":"person3",
//            "face_id":"1031567119985213439",
//            "confidence":54.90695571899414,
//        “tag”: “new tag”
//    },

    private String person_id;
    private String face_id;
    private String tag;
    private float confidence;

    @Override
    public String toString() {
        return "IdentifyItem{" +
                "person_id='" + person_id + '\'' +
                ", face_id='" + face_id + '\'' +
                ", tag='" + tag + '\'' +
                ", confidence=" + confidence +
                '}';
    }

    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }

    public String getFace_id() {
        return face_id;
    }

    public void setFace_id(String face_id) {
        this.face_id = face_id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public float getConfidence() {
        return confidence;
    }

    public void setConfidence(float confidence) {
        this.confidence = confidence;
    }

    public IdentifyItem() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.person_id);
        dest.writeString(this.face_id);
        dest.writeString(this.tag);
        dest.writeFloat(this.confidence);
    }

    protected IdentifyItem(Parcel in) {
        this.person_id = in.readString();
        this.face_id = in.readString();
        this.tag = in.readString();
        this.confidence = in.readFloat();
    }

    public static final Creator<IdentifyItem> CREATOR = new Creator<IdentifyItem>() {
        public IdentifyItem createFromParcel(Parcel source) {
            return new IdentifyItem(source);
        }

        public IdentifyItem[] newArray(int size) {
            return new IdentifyItem[size];
        }
    };
}
