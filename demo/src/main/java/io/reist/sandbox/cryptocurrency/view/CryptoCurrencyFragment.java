package io.reist.sandbox.cryptocurrency.view;

import android.support.annotation.NonNull;
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
import io.reist.sandbox.app.model.CryptoCurrencyItem;
import io.reist.sandbox.cryptocurrency.CryptoCurrencyComponent;
import io.reist.sandbox.cryptocurrency.presenter.CryptoCurrencyPresenter;

/**
 * Created by Sergey on 02/11/2017.
 */
public final class CryptoCurrencyFragment extends BaseFragment<CryptoCurrencyPresenter> implements CryptoCurrencyView {

    @Inject
    CryptoCurrencyPresenter presenter;

    @BindView(R.id.cryptocurrency_recycler_view)
    RecyclerView recycler;

    @BindView(R.id.cryptocurrency_loader_view)
    LoaderView loader;

    private ListItemAdapter adapter;

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

        recycler.setHasFixedSize(true);

        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new ListItemAdapter();

        adapter
            .addViewCreator(CryptoCurrencyItem.class, parent -> LayoutInflater.from(getActivity()).inflate(R.layout.item_cryptocurrency, parent, false))
            .addViewBinder((view, item) -> {

                ((TextView) view.findViewById(R.id.item_cryptocurrency_label)).setText(item.label);
                ((TextView) view.findViewById(R.id.item_cryptocurrency_price)).setText(item.price);

                ImageView image = view.findViewById(R.id.item_cryptocurrency_image);

                Dali.with(image).load(item.image).inCircle(false).scaleMode(ScaleMode.CENTER_CROP).into(image, false);

        });

        recycler.setAdapter(adapter);

    }

    @Override
    public void showLoading(boolean show) {
        loader.showLoading(show);
    }

    @Override
    public void showData(List<CryptoCurrencyItem> items) {

        loader.hide();

        for (CryptoCurrencyItem item : items) {
            adapter.addItem(item);
        }

        adapter.notifyDataSetChanged();

    }

    @Override
    public void showError() {

        loader.hide();

        Toast.makeText(this.getContext(), "Error", Toast.LENGTH_LONG).show();

    }

    @Override
    public void inject(@NonNull Object component) {
        ((CryptoCurrencyComponent) component).inject(this);
    }

}