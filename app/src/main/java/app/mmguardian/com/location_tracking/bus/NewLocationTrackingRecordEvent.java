package app.mmguardian.com.location_tracking.bus;

import app.mmguardian.com.location_tracking.db.model.LocationRecord;

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
