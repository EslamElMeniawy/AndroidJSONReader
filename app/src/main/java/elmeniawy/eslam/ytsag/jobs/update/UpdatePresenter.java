package elmeniawy.eslam.ytsag.jobs.update;

import android.support.annotation.Nullable;

import elmeniawy.eslam.ytsag.api.model.UpdateResponse;
import io.reactivex.disposables.Disposable;

/**
 * UpdatePresenter
 * <p>
 * Created by Eslam El-Meniawy on 29-Mar-2018.
 * CITC - Mansoura University
 */

public class UpdatePresenter implements UpdateMVP.Presenter {
    private Disposable disposable = null;

    @Nullable
    private UpdateMVP.View view;

    private UpdateMVP.Model model;

    UpdatePresenter(UpdateMVP.Model model) {
        this.model = model;
    }

    @Override
    public void setView(@Nullable UpdateMVP.View view) {
        this.view = view;
    }

    @Override
    public void runJob() {
        disposable = model
                .checkUpdate(System.currentTimeMillis())
                .subscribe(this::handleUpdateResult);
    }

    @Override
    public void rxUnsubscribe() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    private void handleUpdateResult(UpdateResponse updateResponse) {
        if (view != null) {
            if (updateResponse.getSuccess()) {
                if (updateResponse.getVersion() > view.getVersionCode()) {
                    model.setUpdateAvailable(view.getSharedPreferences());

                    view.showNotification(2,
                            view.getTitle(),
                            view.getMessage());
                }
            }
        }
    }
}
