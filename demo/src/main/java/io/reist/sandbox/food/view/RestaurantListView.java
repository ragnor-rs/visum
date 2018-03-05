package io.reist.sandbox.food.view;

/**
 * Created by Fedorov-DA on 01.03.2018.
 */

import java.util.ArrayList;

import io.reist.sandbox.food.model.RestaurantModel;
import io.reist.sandbox.food.presenter.RestaurantListPresenter;
import io.reist.sandbox.food.presenter.RestaurantPresenter;
import io.reist.sandbox.food.presenter.RestaurantsAdapter;
import io.reist.visum.view.VisumView;

public interface RestaurantListView extends VisumView<RestaurantListPresenter> {
    void setRestaurantsListAdapter(RestaurantsAdapter restaurantsAdapter);

    void showRestaurantInfo(RestaurantModel restaurantModel);

    void hideLoader();
}

