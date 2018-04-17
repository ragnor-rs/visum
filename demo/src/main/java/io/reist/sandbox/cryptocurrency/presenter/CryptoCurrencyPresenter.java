package io.reist.sandbox.cryptocurrency.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reist.sandbox.app.model.SandboxResponse;
import io.reist.sandbox.cryptocurrency.model.CryptoCurrencyBaseService;
import io.reist.sandbox.app.model.CryptoCurrencyItem;
import io.reist.sandbox.cryptocurrency.view.CryptoCurrencyView;
import io.reist.visum.presenter.SingleViewPresenter;
import rx.Observer;

/**
 * Created by Sergey on 02/11/2017.
 */
@Singleton
public final class CryptoCurrencyPresenter extends SingleViewPresenter<CryptoCurrencyView> {

    private CryptoCurrencyBaseService service;

    @Inject
    CryptoCurrencyPresenter(CryptoCurrencyBaseService service) {
        this.service = service;
    }

    @Override
    protected void onViewAttached(@NonNull CryptoCurrencyView view) {

        view.showLoading(true);

        loadData();

    }

    private void loadData() {
        subscribe(service.list(), new CryptoCurrencyObserver());
    }

    private class CryptoCurrencyObserver implements Observer<SandboxResponse<List<CryptoCurrencyItem>>> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

            Log.d("TAG", "error: " + e.toString());

            view().showError();

        }

        @Override
        public void onNext(SandboxResponse<List<CryptoCurrencyItem>> listSandboxResponse) {

            if (listSandboxResponse == null) {

                view().showError();

                return;

            }

            view().showLoading(false);

            view().showData(listSandboxResponse.getResult());

        }

    }

}