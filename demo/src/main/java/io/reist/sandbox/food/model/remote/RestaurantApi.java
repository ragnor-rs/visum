package io.reist.sandbox.food.model.remote;

import io.reist.sandbox.food.model.dto.RestaurantsDto;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Single;

/**
 * Created by Fedorov-DA on 01.03.2018.
 */

public interface RestaurantApi {
//    https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=55.741453,37.628418&type=bar&radius=50&opennow=true&key=AIzaSyA5rbx7kV-wcHYLqef3BpWTf8YiiVc6GF8

    @GET("/maps/api/place/nearbysearch/json")
    Single<RestaurantsDto> searchForRestaurants(
            @Query("location")String location,
            @Query("type")String type,
            @Query("radius")int radius,
            @Query("opennow")Boolean opennow,
            @Query("key")String key);


}
