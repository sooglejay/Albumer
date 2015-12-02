package sooglejay.youtu.event;

public class BusEvent {

    public static final int MSG_EDIT_FACE_INFO = 1000;
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
