package com.madmensoftware.com.features.detail;

import javax.inject.Inject;

import com.madmensoftware.com.data.DataManager;
import com.madmensoftware.com.features.base.BasePresenter;
import com.madmensoftware.com.injection.ConfigPersistent;
import com.madmensoftware.com.util.rx.scheduler.SchedulerUtils;

@ConfigPersistent
public class DetailPresenter extends BasePresenter<DetailMvpView> {

    private final DataManager dataManager;

    @Inject
    public DetailPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(DetailMvpView mvpView) {
        super.attachView(mvpView);
    }

    public void getBar(String name) {
        checkViewAttached();
        getView().showProgress(true);
        dataManager
                .getBar(name)
                .compose(SchedulerUtils.ioToMain())
                .subscribe(
                        bar -> {
                            // It should be always checked if MvpView (Fragment or Activity) is attached.
                            // Calling showProgress() on a not-attached fragment will throw a NPE
                            // It is possible to ask isAdded() in the fragment, but it's better to ask in the presenter
                            getView().showProgress(false);
                            getView().showBar(bar);
                            getView().showQrCode(bar.getObjectId());
                        },
                        throwable -> {
                            getView().showProgress(false);
                            getView().showError(throwable);
                        });
    }
}