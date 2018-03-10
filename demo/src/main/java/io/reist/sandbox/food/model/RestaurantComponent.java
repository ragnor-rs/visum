package io.reist.sandbox.food.model;

import dagger.Subcomponent;
import io.reist.sandbox.food.view.RestaurantFragment;
import io.reist.sandbox.food.view.RestaurantInfoFragment;
import io.reist.sandbox.food.view.RestaurantListFragment;
import io.reist.sandbox.food.view.RestaurantMapFragment;

/**
 * Created by Fedorov-DA on 01.03.2018.
 */

@Subcomponent
public interface RestaurantComponent {
    void  inject (RestaurantListFragment restaurantListFragment);
    void  inject (RestaurantFragment restaurantFragment);
    void  inject (RestaurantMapFragment restaurantMapFragment);
    void  inject (RestaurantInfoFragment restaurantInfoFragment);
}
