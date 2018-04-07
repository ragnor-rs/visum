package io.reist.sandbox.food.presenter;

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
    public RestaurantListPresenter(RestaurantMonitor restaurantMonitor, GeopositionMonitor geopositionMonitor) {

        this.restaurantMonitor = restaurantMonitor;
        this.geopositionMonitor = geopositionMonitor;

        restaurantMonitor.getRestaurantsFound()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setRestaurantList);

    }

    public void init() {
        withView(
                view -> {
                    view.setRestaurantsListAdapter(restaurantsAdapter);
                    restaurantsAdapter.setOnUserClickListener((view::showRestaurantInfo));
                }
        );
        geopositionMonitor.getLocationFound().subscribe(this::findRestaurants);
    }

    private void setRestaurantList(List<RestaurantModel> restaurants) {
        // TODO: 02.03.2018 a bit weird, but makes view more passive
        restaurantsAdapter.setRestaurants(restaurants);
        withView(RestaurantListView::hideLoader);
    }

    private void findRestaurants(LatLng latLng) {
        restaurantMonitor.findRestaurants(latLng);
    }

}
