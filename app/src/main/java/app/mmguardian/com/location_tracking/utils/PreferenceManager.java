package app.mmguardian.com.location_tracking.utils;


import android.content.Context;
import android.content.SharedPreferences;

/**
 * The Android Shared Preference Manager
 *
 * @author  Jerry Cho
 * @version 1.0
 */
public class PreferenceManager {

    private static final String SHARED_PREF_NAME = "location_tracking";


    private static Context mContext;

    public PreferenceManager(Context context) {
        PreferenceManager.mContext = context;
    }

    private SharedPreferences getSharedPreferences() {
        return mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setLongPref(String key, Long value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public long getLongPref(String key){
        SharedPreferences pref = getSharedPreferences();
        return pref.getLong(key, 0);
    }
}
