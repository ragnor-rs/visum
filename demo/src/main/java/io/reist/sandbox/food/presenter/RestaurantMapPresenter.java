package io.reist.sandbox.food.presenter;

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
    public RestaurantMapPresenter(RestaurantMonitor restaurantMonitor) {
        this.restaurantMonitor = restaurantMonitor;
    }

    public void initPresenter(String restaurantId) {
        this.restaurantId = restaurantId;
        restaurant = restaurantMonitor.getRestaurantById(restaurantId);
        withView(
                MAP_FRAGMENT,
                v -> v.setRestaurantCoordinates(new LatLng(restaurant.getLat(), restaurant.getLon()), restaurant.getName())
        );
    }

}
