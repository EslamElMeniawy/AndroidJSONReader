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

    public void putData(String key, boolean data) {
        mSharedPreferences.edit().putBoolean(key, data).apply();
    }

    public boolean getData(String key) {
        return mSharedPreferences.getBoolean(key, false);
    }
}
