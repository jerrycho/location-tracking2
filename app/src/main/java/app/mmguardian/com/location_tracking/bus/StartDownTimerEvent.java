package app.mmguardian.com.location_tracking.bus;


public class StartDownTimerEvent {

    private int from;

    public int getFrom() {
        return from;
    }

    public StartDownTimerEvent(int from) {
        this.from = from;
    }
}
