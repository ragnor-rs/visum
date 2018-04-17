package io.reist.sandbox.cryptocurrency.view;

import java.util.List;

import io.reist.sandbox.cryptocurrency.model.local.CryptoCurrencyItem;
import io.reist.sandbox.cryptocurrency.presenter.CryptoCurrencyPresenter;
import io.reist.visum.view.VisumView;

/**
 * Created by Sergey on 02/11/2017.
 */
public interface CryptoCurrencyView extends VisumView<CryptoCurrencyPresenter> {

    void showLoading(boolean show);

    void showData(List<CryptoCurrencyItem> items);

    void showError();

}