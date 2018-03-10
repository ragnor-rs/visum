package io.reist.sandbox.food.presenter;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;

import io.reist.sandbox.food.model.RestaurantModel;
import io.reist.sandbox.food.model.RestaurantMonitor;
import io.reist.sandbox.food.view.RestaurantMapView;
import io.reist.visum.presenter.VisumPresenter;

/**
 * Created by Fedorov-DA on 01.03.2018.
 */

public class RestaurantMapPresenter extends VisumPresenter<RestaurantMapView> {

    public static final int MAP_FRAGMENT = 20002;
    private RestaurantMonitor restaurantMonitor;
    private String restaurantId;
    private RestaurantModel restaurant;

    @Inject
    public RestaurantMapPresenter(RestaurantMonitor _restaurantMonitor) {
        restaurantMonitor = _restaurantMonitor;
    }

    @Override
    protected void onViewAttached(int id, @NonNull RestaurantMapView view) {
        super.onViewAttached(id, view);
    }

    public void initPresenter(String _restaurantId) {
        restaurantId = _restaurantId;
        restaurant = restaurantMonitor.getRestaurantById(restaurantId);
        view(MAP_FRAGMENT).moveMapToUser(new LatLng(restaurant.getLat(), restaurant.getLon()));
        view(MAP_FRAGMENT).setRestaurantCoordinates(restaurant.getLat(),restaurant.getLon());
    }
}
