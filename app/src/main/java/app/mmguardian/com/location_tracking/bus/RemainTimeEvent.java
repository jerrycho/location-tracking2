package app.mmguardian.com.location_tracking.bus;

/**
 * EventBus event, will notice main activity need to update the time
 * textview on UI level
 *
 * @author  Jerry Cho
 * @version 1.0
 */

public class RemainTimeEvent {

    private int mSecond;

    public int getmSecond() {
        return mSecond;
    }

    public RemainTimeEvent(int mSecond) {
        this.mSecond = mSecond;
    }
}
