package io.reist.sandbox.food.view;

/**
 * Created by Fedorov-DA on 01.03.2018.
 */

import io.reist.sandbox.food.presenter.RestaurantInfoPresenter;
import io.reist.sandbox.food.presenter.RestaurantPresenter;
import io.reist.visum.view.VisumView;

public interface RestaurantInfoView extends VisumView<RestaurantInfoPresenter> {
    void setName(String name);

    void setRating(String rating);

}
