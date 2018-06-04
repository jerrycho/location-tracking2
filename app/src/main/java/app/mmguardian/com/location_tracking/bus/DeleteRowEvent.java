package app.mmguardian.com.location_tracking.bus;


public class DeleteRowEvent {

    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public DeleteRowEvent(int count) {
        this.count = count;
    }
}
