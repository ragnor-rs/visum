package io.reist.sandbox.food.view;

/**
 * Created by Fedorov-DA on 01.03.2018.
 */

import io.reist.sandbox.food.presenter.RestaurantPresenter;
import io.reist.visum.view.VisumView;

public interface RestaurantView extends VisumView<RestaurantPresenter> {
void showMap(String restaurantId);
void showInfo(String restaurantId);
}
