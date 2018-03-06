package io.reist.sandbox.food.presenter;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import javax.inject.Inject;

import io.reist.sandbox.food.model.GeopositionMonitor;
import io.reist.sandbox.food.model.RestaurantModel;
import io.reist.sandbox.food.model.RestaurantMonitor;
import io.reist.sandbox.food.view.RestaurantListView;
import io.reist.visum.presenter.SingleViewPresenter;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Fedorov-DA on 01.03.2018.
 */

public class RestaurantListPresenter extends SingleViewPresenter<RestaurantListView> {


    private RestaurantMonitor restaurantMonitor;
    private GeopositionMonitor geopositionMonitor;
    private RestaurantsAdapter restaurantsAdapter = new RestaurantsAdapter();

    @Inject
    public RestaurantListPresenter(RestaurantMonitor _restaurantMonitor, GeopositionMonitor _geopositionMonitor) {
        restaurantMonitor = _restaurantMonitor;
        geopositionMonitor = _geopositionMonitor;
        restaurantMonitor.getRestaurantsFound()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((restaurants) -> {
                    setRestaurantList(restaurants);
                });
    }

    @Override
    protected void onViewAttached(@NonNull RestaurantListView view) {
        super.onViewAttached(view);
    }

    public void init() {
        view().setRestaurantsListAdapter(restaurantsAdapter);
        restaurantsAdapter.setOnUserClickListener((restaurant -> {
            view().showRestaurantInfo(restaurant);
        }));
        geopositionMonitor.getLocationFound()
                .subscribe((latLng) -> findRestaurants(latLng));
    }

    private void setRestaurantList(List<RestaurantModel> restaurants) {
        // TODO: 02.03.2018 a bit weird, but makes view more passive
        restaurantsAdapter.setRestaurants(restaurants);
        view().hideLoader();
    }

    private void findRestaurants(LatLng latLng) {
        restaurantMonitor.findRestaurants(latLng);
    }
}
