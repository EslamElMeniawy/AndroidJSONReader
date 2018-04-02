package elmeniawy.eslam.ytsag.screens.splash;

import android.support.annotation.Nullable;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * SplashPresenter
 * <p>
 * Created by Eslam El-Meniawy on 07-Mar-2018.
 * CITC - Mansoura University
 */

public class SplashPresenter implements SplashMVP.Presenter {
    private Disposable disposable = null;

    @Nullable
    private SplashMVP.View view;

    private SplashMVP.Model model;

    SplashPresenter(SplashMVP.Model model) {
        this.model = model;
    }

    @Override
    public void setView(@Nullable SplashMVP.View view) {
        this.view = view;
    }

    @Override
    public void rxUnsubscribe() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    public void saveFromNotification() {
        if (view != null) {
            model.saveFromNotification(view.getSharedPreferences(), view.getFromNotification());
        }
    }

    @Override
    public void startWait() {
        if (view != null) {
            disposable = Observable
                    .timer(3, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(aLong -> {
                        view.openMainActivity();
                        return aLong;
                    })
                    .subscribe();
        }
    }
}
