package sooglejay.youtu.event;

public class BusEvent {

    public static final int MSG_EDIT_FACE_INFO = 1000;//编辑 top 5中俄一个记录
    public static final int MSG_DELETE_IMAGE_FILE = 1001;// 删除图片文件
    public static final int MSG_REFRESH = 1002;// 删除图片文件
    public static final int MSG_IS_DETECT_FACE = 1003;// 删除图片文件
    public static final int MSG_IS_IDENTIFY_FACE = 1004;// 删除图片文件
    private int msg=0;
    public int getMsg() {
        return msg;
    }
    public void setMsg(int msg) {
        this.msg = msg;
    }
    public BusEvent(int msg) {
        this.msg = msg;
    }
}
