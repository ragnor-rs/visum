package io.reist.sandbox.food.view;

/**
 * Created by Fedorov-DA on 01.03.2018.
 */

import com.google.android.gms.maps.model.LatLng;

import io.reist.sandbox.food.presenter.RestaurantMapPresenter;
import io.reist.sandbox.food.presenter.RestaurantPresenter;
import io.reist.visum.view.VisumView;

public interface RestaurantMapView extends VisumView<RestaurantMapPresenter> {
    void setRestaurantCoordinates(double lat, double lon);

    void moveMapToUser(LatLng latLng);
}
