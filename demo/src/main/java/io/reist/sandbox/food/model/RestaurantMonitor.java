package io.reist.sandbox.food.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reist.sandbox.food.model.dto.RestaurantDto;
import io.reist.sandbox.food.model.dto.RestaurantsDto;
import io.reist.sandbox.food.model.local.RestaurantsService;
import io.reist.sandbox.food.model.remote.RestaurantApi;
import rx.Observable;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

import static android.media.CamcorderProfile.get;

/**
 * Created by Fedorov-DA on 01.03.2018.
 */

public class RestaurantMonitor {

    private RestaurantsService restaurantService;
    private RestaurantApi restaurantApi;
    private ArrayList<RestaurantModel> restaurantModels = new ArrayList<>();

    BehaviorSubject<List<RestaurantModel>> restaurantModelsSubject = BehaviorSubject.create();

    @Inject
    public RestaurantMonitor(RestaurantApi _restaurantApi, RestaurantsService _restaurantService) {
        restaurantApi = _restaurantApi;
        restaurantService = _restaurantService;

    }

    public void update() {
        restaurantApi.searchForRestaurants(String.format("%s,%s", 55.741453, 37.628418), "bar", 1000, true, "AIzaSyA5rbx7kV-wcHYLqef3BpWTf8YiiVc6GF8")
                .subscribeOn(Schedulers.io())
                .subscribe(restaurantsFound);
    }

    private void takeCachedRestaurants() {
        restaurantService.list()
                .observeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.newThread())
                .doOnError((error) -> {
                    Log.d("", "");
                })
                .subscribe((response) -> {
                    if (!response.isSuccessful()) {
                        return;
                    }
                    List<RestaurantEntity> restaurants = response.getResult();
                    restaurantModels.clear();
                    for (RestaurantEntity restaurant : restaurants) {
                        restaurantModels.add(new RestaurantModel(restaurant));
                    }
                    restaurantModelsSubject.onNext(restaurantModels);
                });
    }

    public Observable<List<RestaurantModel>> getRestaurantsFound() {
        return restaurantModelsSubject;
    }


    private SingleSubscriber<RestaurantsDto> restaurantsFound = new SingleSubscriber<RestaurantsDto>() {
        @Override
        public void onSuccess(RestaurantsDto _restaurantsDto) {
            restaurantModels.clear();
            for (RestaurantDto restaurant : _restaurantsDto.results) {
                restaurantModels.add(new RestaurantModel(restaurant));
            }
            restaurantModelsSubject.onNext(restaurantModels);
            saveToDatabase();
        }

        @Override
        public void onError(Throwable error) {
            takeCachedRestaurants();
        }
    };

    private void saveToDatabase() {
        ArrayList<RestaurantEntity> restaurantEntities = new ArrayList<>();
        for (RestaurantModel restaurant : restaurantModels) {
            RestaurantEntity restaurantEntity = new RestaurantEntity();
            restaurantEntity.id = restaurant.getId();
            restaurantEntity.name = restaurant.getName();
            restaurantEntity.rating = restaurant.getRating();
            restaurantEntity.lat = restaurant.getLat();
            restaurantEntity.lon = restaurant.getLon();
            restaurantEntities.add(restaurantEntity);
        }
        restaurantService.save(restaurantEntities)
                .subscribeOn(Schedulers.io())
                .doOnError((err) -> {
                    err.getMessage();
                })
                .subscribe((response) -> {
                    response.isSuccessful();
                });
    }

    public RestaurantModel getRestaurantById(String id) {
        for (RestaurantModel restaurant : restaurantModels) {
            if (restaurant.getId().equals(id)) {
                return restaurant;
            }
        }
        return null;
    }
}
