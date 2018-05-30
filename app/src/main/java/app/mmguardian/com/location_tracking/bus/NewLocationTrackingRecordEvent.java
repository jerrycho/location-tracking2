package app.mmguardian.com.location_tracking.bus;

import app.mmguardian.com.location_tracking.db.model.LocationRecord;

public class NewLocationTrackingRecordEvent {

    private LocationRecord mLocationRecord;

    public LocationRecord getmLocationRecord() {
        return mLocationRecord;
    }

    public NewLocationTrackingRecordEvent(LocationRecord mLocationRecord) {
        this.mLocationRecord = mLocationRecord;
    }
}
