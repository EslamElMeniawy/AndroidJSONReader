/*
 * Copyright (c) 2015.
 *
 * Dear maintainer:
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * So, good luck maintaining the code :D
 *
 * Eslam El-Meniawy
 * 04-Jun-2015 21:18
 */

package elmeniawy.eslam.yts.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andexert.library.RippleView;

import elmeniawy.eslam.yts.R;
import elmeniawy.eslam.yts.receivers.AlarmReceiver;

public class NavigationDrawerFragment extends Fragment {

    public static final String PREF_FILE_NAME = "YTSPref";
    public static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer", KEY_NOTIFICATIONS_ENABLED = "notifications_enabled";
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private boolean mUserLearnedDrawer, mNotificationsEnabled;
    private View containerView;
    private SwitchCompat notificationsSwitch;
    private RippleView aboutApp, rateApp, aboutDeveloper, developerSocial;
    private AlarmReceiver alarm = new AlarmReceiver();

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = readFromPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, false);
        mNotificationsEnabled = readFromPreferences(getActivity(), KEY_NOTIFICATIONS_ENABLED, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        notificationsSwitch = (SwitchCompat) layout.findViewById(R.id.notificationsSwitch);
        notificationsSwitch.setChecked(mNotificationsEnabled);
        notificationsSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNotificationsEnabled = !mNotificationsEnabled;
                saveToPreferences(getActivity(), KEY_NOTIFICATIONS_ENABLED, mNotificationsEnabled);
                if (mNotificationsEnabled) {
                    alarm.setAlarm(getActivity());
                    Toast.makeText(getActivity(), R.string.notifications_enabled, Toast.LENGTH_SHORT).show();
                } else {
                    alarm.cancelAlarm(getActivity());
                    Toast.makeText(getActivity(), R.string.notifications_disabled, Toast.LENGTH_SHORT).show();
                }
            }
        });
        aboutApp = (RippleView) layout.findViewById(R.id.aboutApp);
        aboutApp.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.dialog_about_app, null));
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        rateApp = (RippleView) layout.findViewById(R.id.rateApp);
        rateApp.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
                }
            }
        });
        aboutDeveloper = (RippleView) layout.findViewById(R.id.aboutDeveloper);
        aboutDeveloper.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.dialog_about_developer, null));
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        developerSocial = (RippleView) layout.findViewById(R.id.developerSocial);
        developerSocial.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View socialLayout = getActivity().getLayoutInflater().inflate(R.layout.dialog_developer_social, null);
                RippleView linkedin = (RippleView) socialLayout.findViewById(R.id.developerLinkedin);
                linkedin.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://eg.linkedin.com/in/eslamelmeniawy"));
                        startActivity(browserIntent);
                    }
                });
                RippleView github = (RippleView) socialLayout.findViewById(R.id.developerGithub);
                github.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/EslamEl-Meniawy"));
                        startActivity(browserIntent);
                    }
                });
                RippleView twitter = (RippleView) socialLayout.findViewById(R.id.developerTwitter);
                twitter.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/eslam_elmeniawy"));
                        startActivity(browserIntent);
                    }
                });
                RippleView facebook = (RippleView) socialLayout.findViewById(R.id.developerFacebook);
                facebook.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/eslam.elmeniawy"));
                        startActivity(browserIntent);
                    }
                });
                RippleView googlePlus = (RippleView) socialLayout.findViewById(R.id.developerGooglePlus);
                googlePlus.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/+EslamElMeniawy/"));
                        startActivity(browserIntent);
                    }
                });
                builder.setView(socialLayout);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        return layout;
    }

    public void setUp(int fragmentID, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentID);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    saveToPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, mUserLearnedDrawer);
                }
                getActivity().supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().supportInvalidateOptionsMenu();
            }
        };
        if (!mUserLearnedDrawer) {
            mDrawerLayout.openDrawer(containerView);
        }
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    public static void saveToPreferences(Context context, String preferenceName, boolean preferenceValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(preferenceName, preferenceValue);
        editor.apply();
    }

    public static boolean readFromPreferences(Context context, String preferencesName, boolean defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(preferencesName, defaultValue);
    }

}
