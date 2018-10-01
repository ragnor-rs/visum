package io.reist.sandbox.food.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import io.reist.sandbox.R;
import io.reist.sandbox.app.view.BaseFragment;
import io.reist.sandbox.food.model.RestaurantComponent;
import io.reist.sandbox.food.presenter.RestaurantInfoPresenter;

/**
 * Created by Fedorov-DA on 01.03.2018.
 */

public class RestaurantInfoFragment extends BaseFragment<RestaurantInfoPresenter> implements RestaurantInfoView{

    private static final String ARG_RESTAURANT = "arg_restaurant";

    @Inject
    RestaurantInfoPresenter presenter;

    @BindView(R.id.restaurant_info_name)
    TextView restaurantNameText;

    @BindView(R.id.restaurant_info_rating)
    TextView restaurantRatingText;

    public RestaurantInfoFragment() {
        super(RestaurantInfoPresenter.INFO_FRAGMENT, R.layout.fragment_restaurant_info);
    }

    public static Fragment newInstance(String restaurantId) {
        RestaurantInfoFragment fragment = new RestaurantInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_RESTAURANT, restaurantId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void attachPresenter() {
        super.attachPresenter();
        getPresenter().setRestaurantId(getArguments().getString(ARG_RESTAURANT));
    }

    @Override
    public RestaurantInfoPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void setName(String name) {
        restaurantNameText.setText(name);
    }

    @Override
    public void setRating(String rating) {
        restaurantRatingText.setText(rating);
    }

    @Override
    public void inject(@NonNull Object component) {
        ((RestaurantComponent) component).inject(this);
    }

}
