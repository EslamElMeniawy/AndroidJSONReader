package elmeniawy.eslam.ytsag.storage.preferences;

import android.content.SharedPreferences;

import javax.inject.Inject;

/**
 * MySharedPreferences
 * <p>
 * Created by Eslam El-Meniawy on 06-Mar-2018.
 * CITC - Mansoura University
 */

public class MySharedPreferences {
    private SharedPreferences mSharedPreferences;

    @Inject
    public MySharedPreferences(SharedPreferences mSharedPreferences) {
        this.mSharedPreferences = mSharedPreferences;
    }

    public void putBoolean(String key, boolean data) {
        mSharedPreferences.edit().putBoolean(key, data).apply();
    }

    public boolean getBoolean(String key) {
        return mSharedPreferences.getBoolean(key, false);
    }

    public void putLong(String key, long data) {
        mSharedPreferences.edit().putLong(key, data).apply();
    }

    public long getLong(String key) {
        return mSharedPreferences.getLong(key, 0);
    }

    public long getLong(String key, long defaultValue) {
        return mSharedPreferences.getLong(key, defaultValue);
    }
}
