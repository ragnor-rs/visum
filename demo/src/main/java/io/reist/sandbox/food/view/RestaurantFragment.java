package io.reist.sandbox.food.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;

import javax.inject.Inject;

import butterknife.BindView;
import io.reist.sandbox.R;
import io.reist.sandbox.app.view.BaseFragment;
import io.reist.sandbox.food.model.RestaurantComponent;
import io.reist.sandbox.food.presenter.RestaurantPresenter;

/**
 * Created by Fedorov-DA on 01.03.2018.
 */

public class RestaurantFragment extends BaseFragment<RestaurantPresenter> implements RestaurantView {

    private static final String ARG_RESTAURANT = "arg_restaurant";

    @Inject
    RestaurantPresenter presenter;

    @BindView(R.id.info_fragment)
    FrameLayout infoContainer;

    @BindView(R.id.map_fragment)
    FrameLayout mapContainer;

    public RestaurantFragment() {
        super(RestaurantPresenter.RESTAURANT_FRAGMENT, R.layout.fragment_restaurant);
    }

    public static RestaurantFragment newInstance(String restaurantId) {
        RestaurantFragment fragment = new RestaurantFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_RESTAURANT, restaurantId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public RestaurantPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void attachPresenter() {
        super.attachPresenter();
        getPresenter().setRestaurantId(getArguments().getString(ARG_RESTAURANT));
    }

    @Override
    public void showMap(String restaurantId) {
        infoContainer.setVisibility(View.VISIBLE);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.map_fragment, RestaurantMapFragment.newInstance(restaurantId))
                .commit();
    }

    @Override
    public void showInfo(String restaurantId) {
        mapContainer.setVisibility(View.VISIBLE);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.info_fragment, RestaurantInfoFragment.newInstance(restaurantId))
                .commit();
    }

    @Override
    public void inject(@NonNull Object component) {
        ((RestaurantComponent) component).inject(this);
    }

}
