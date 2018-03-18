package elmeniawy.eslam.ytsag.screens.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import elmeniawy.eslam.ytsag.R;
import elmeniawy.eslam.ytsag.utils.FabricEvents;

/**
 * Created by Eslam El-Meniawy on 01-Jan-16.
 */
@SuppressWarnings("DefaultFileTemplate")
public class TabFragmentSocial extends Fragment {
    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //
        // Log fabric content view event only once.
        //

        if (savedInstanceState == null) {
            FabricEvents.logContentViewEvent(TabFragmentSocial.class.getSimpleName());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_fragment_social, container,
                false);

        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.developerWebsite)
    void websiteClicked() {
        openIntent(new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://eslamelmeniawy.github.io/")));
    }

    @OnClick(R.id.developerLinkedin)
    void linkedinClicked() {
        openIntent(new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://eg.linkedin.com/in/eslamelmeniawy")));
    }

    @OnClick(R.id.developerGithub)
    void githubClicked() {
        openIntent(new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://github.com/EslamElMeniawy")));
    }

    @OnClick(R.id.developerTwitter)
    void twitterClicked() {
        openIntent(new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://twitter.com/eslam_elmeniawy")));
    }

    @OnClick(R.id.developerFacebook)
    void facebookClicked() {
        openIntent(new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.facebook.com/eslam.elmeniawy")));
    }

    @OnClick(R.id.developerGooglePlus)
    void googlePlusClicked() {
        openIntent(new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://plus.google.com/+EslamElMeniawy/")));
    }

    private void openIntent(Intent intent) {
        startActivity(intent);
    }
}
