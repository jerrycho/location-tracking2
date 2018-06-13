package app.mmguardian.com.location_tracking.bus;

import app.mmguardian.com.location_tracking.db.model.LocationRecord;

/**
 * EventBus event, will notice main activity need to update the list on UI level
 *
 * @author  Jerry Cho
 * @version 1.0
 */
public class NewLocationTrackingRecordEvent {

    private LocationRecord mLocationRecord;
    private long beforeDate;

    public LocationRecord getmLocationRecord() {
        return mLocationRecord;
    }

    public long getBeforeDate() {
        return beforeDate;
    }

    public NewLocationTrackingRecordEvent(LocationRecord mLocationRecord, long beforeDate) {
        this.mLocationRecord = mLocationRecord;
        this.beforeDate = beforeDate;
    }
}
