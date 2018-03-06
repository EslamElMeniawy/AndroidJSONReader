package elmeniawy.eslam.ytsag;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import elmeniawy.eslam.ytsag.root.MyApplication;

/**
 * Created by Eslam El-Meniawy on 02-Jan-16.
 */
@SuppressWarnings("DefaultFileTemplate")
class VolleySingleton {
    private static VolleySingleton sInstance = null;
    private RequestQueue mRequestQueue;

    private VolleySingleton() {
        mRequestQueue = Volley.newRequestQueue(MyApplication.getAppContext());
    }

    static VolleySingleton getInstance() {
        if (sInstance == null) {
            sInstance = new VolleySingleton();
        }
        return sInstance;
    }

    RequestQueue getRequestQueue() {
        return mRequestQueue;
    }
}
