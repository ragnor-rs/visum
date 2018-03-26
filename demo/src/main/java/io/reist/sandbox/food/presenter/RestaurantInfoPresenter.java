package io.reist.sandbox.food.presenter;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.reist.sandbox.food.model.RestaurantModel;
import io.reist.sandbox.food.model.RestaurantMonitor;
import io.reist.sandbox.food.view.RestaurantInfoView;
import io.reist.visum.presenter.VisumPresenter;

/**
 * Created by Fedorov-DA on 01.03.2018.
 */

public class RestaurantInfoPresenter extends VisumPresenter<RestaurantInfoView> {

    public static final int INFO_FRAGMENT = 20001;
    private RestaurantMonitor restaurantMonitor;
    private String restaurantId;
    private RestaurantModel restaurant;

    @Inject
    public RestaurantInfoPresenter(RestaurantMonitor restaurantMonitor) {
        this.restaurantMonitor = restaurantMonitor;
    }

    @Override
    protected void onViewAttached(int id, @NonNull RestaurantInfoView view) {
        super.onViewAttached(id, view);
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
        restaurant=restaurantMonitor.getRestaurantById(restaurantId);
        view(INFO_FRAGMENT).setName(restaurant.getName());
        view(INFO_FRAGMENT).setRating(restaurant.getRating());
    }
}
