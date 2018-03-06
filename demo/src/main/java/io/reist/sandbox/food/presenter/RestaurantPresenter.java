package io.reist.sandbox.food.presenter;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import io.reist.sandbox.food.model.RestaurantModel;
import io.reist.sandbox.food.model.RestaurantMonitor;
import io.reist.sandbox.food.view.RestaurantView;
import io.reist.visum.presenter.VisumPresenter;

/**
 * Created by Fedorov-DA on 01.03.2018.
 */

public class RestaurantPresenter extends VisumPresenter<RestaurantView> {

    public static final int RESTAURANT_FRAGMENT = 20000;
    private RestaurantMonitor restaurantMonitor;
    private String restaurantId;
    private RestaurantModel restaurant;

    @Inject
    public RestaurantPresenter(RestaurantMonitor restaurantMonitor) {
        this.restaurantMonitor = restaurantMonitor;
    }

    @Override
    protected void onViewAttached(int id, @NonNull RestaurantView view) {
        super.onViewAttached(id, view);
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
        restaurant = restaurantMonitor.getRestaurantById(restaurantId);
        view(RESTAURANT_FRAGMENT).showInfo(restaurantId);
        view(RESTAURANT_FRAGMENT).showMap(restaurantId);
    }
}
