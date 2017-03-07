package elmeniawy.eslam.ytsag;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Eslam El-Meniawy on 01-Jan-16.
 */
@SuppressWarnings("DefaultFileTemplate")
public class TabFragmentSocial extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_fragment_social, container, false);
        LinearLayout linkedin = (LinearLayout) rootView.findViewById(R.id.developerLinkedin);
        linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://eg.linkedin.com/in/eslamelmeniawy"));
                startActivity(browserIntent);
            }
        });
        LinearLayout github = (LinearLayout) rootView.findViewById(R.id.developerGithub);
        github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/EslamEl-Meniawy"));
                startActivity(browserIntent);
            }
        });
        LinearLayout twitter = (LinearLayout) rootView.findViewById(R.id.developerTwitter);
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/eslam_elmeniawy"));
                startActivity(browserIntent);
            }
        });
        LinearLayout facebook = (LinearLayout) rootView.findViewById(R.id.developerFacebook);
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/eslam.elmeniawy"));
                startActivity(browserIntent);
            }
        });
        LinearLayout googlePlus = (LinearLayout) rootView.findViewById(R.id.developerGooglePlus);
        googlePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/+EslamElMeniawy/"));
                startActivity(browserIntent);
            }
        });
        return rootView;
    }
}
