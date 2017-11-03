package io.reist.sandbox.cryptocurrency.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.m039.el_adapter.ListItemAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.reist.dali.Dali;
import io.reist.dali.ScaleMode;
import io.reist.sandbox.R;
import io.reist.sandbox.app.view.BaseFragment;
import io.reist.sandbox.app.view.widget.LoaderView;
import io.reist.sandbox.cryptocurrency.model.local.CryptoCurrencyItem;
import io.reist.sandbox.cryptocurrency.presenter.CryptoCurrencyPresenter;

/**
 * Created by Sergey on 02/11/2017.
 */

public final class CryptoCurrencyFragment extends BaseFragment<CryptoCurrencyPresenter> implements CryptoCurrencyView {

    @Inject
    CryptoCurrencyPresenter presenter;

    @BindView(R.id.cryptocurrency_recycler_view)
    RecyclerView recycler_view;

    @BindView(R.id.cryptocurrency_loader_view)
    LoaderView loader_view;

    private ListItemAdapter list_adapter;

    public CryptoCurrencyFragment() {
        super(R.layout.fragment_cryptocurrency);
    }

    @Override
    public CryptoCurrencyPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void attachPresenter() {
        super.attachPresenter();

        recycler_view.setHasFixedSize(true);

        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));

        list_adapter = new ListItemAdapter();

        list_adapter
            .addViewCreator(CryptoCurrencyItem.class, parent -> LayoutInflater.from(getActivity()).inflate(R.layout.item_cryptocurrency, parent, false))
            .addViewBinder((view, item) -> {

                ((TextView) view.findViewById(R.id.item_cryptocurrency_label)).setText(item.label);
                ((TextView) view.findViewById(R.id.item_cryptocurrency_price)).setText(item.price);

                ImageView image = ((ImageView) view.findViewById(R.id.item_cryptocurrency_image));

                Dali.with(image).load(item.image).inCircle(false).scaleMode(ScaleMode.CENTER_CROP).into(image, false);

        });

        recycler_view.setAdapter(list_adapter);

    }

    @Override
    public void showLoading(boolean show) {
        loader_view.showLoading(show);
    }

    @Override
    public void showData(List<CryptoCurrencyItem> items) {

        loader_view.hide();

        for (CryptoCurrencyItem item : items) list_adapter.addItem(item);

        list_adapter.notifyDataSetChanged();

    }

    @Override
    public void showError() {

        loader_view.hide();

        Toast.makeText(this.getContext(), "Error", Toast.LENGTH_LONG).show();

    }

}