package sooglejay.youtu.event;

public class BusEvent {

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
