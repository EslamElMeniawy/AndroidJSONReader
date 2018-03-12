package elmeniawy.eslam.ytsag.screens.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import elmeniawy.eslam.ytsag.R;
import elmeniawy.eslam.ytsag.utils.FabricEvents;

/**
 * Created by Eslam El-Meniawy on 01-Jan-16.
 */
@SuppressWarnings("DefaultFileTemplate")
public class TabFragmentAbout extends Fragment {
    private static final String TAG = TabFragmentAbout.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //
        // Log fabric content view event only once.
        //

        if (savedInstanceState == null) {
            FabricEvents.logContentViewEvent(TAG);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fragment_about, container, false);
    }
}
