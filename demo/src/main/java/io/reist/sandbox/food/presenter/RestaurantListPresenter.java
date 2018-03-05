package io.reist.sandbox.food.presenter;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

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
    private RestaurantsAdapter restaurantsAdapter = new RestaurantsAdapter();

    @Inject
    public RestaurantListPresenter(RestaurantMonitor _restaurantMonitor) {
        restaurantMonitor = _restaurantMonitor;
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
        // TODO: 02.03.2018 think if adapter is kind of presenter or smthng like a complicated view
        view.setRestaurantsListAdapter(restaurantsAdapter);
        restaurantsAdapter.setOnUserClickListener((restaurant -> {view().showRestaurantInfo(restaurant);}));
        initiateUpdate();
    }

    private void setRestaurantList(List<RestaurantModel> restaurants) {
        // TODO: 02.03.2018 wierd, but makes view more passive
        restaurantsAdapter.setRestaurants(restaurants);
        view().hideLoader();
    }

    private void initiateUpdate() {
        restaurantMonitor.update();
    }
}
