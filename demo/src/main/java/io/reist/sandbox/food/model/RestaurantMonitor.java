package io.reist.sandbox.food.model;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reist.sandbox.food.model.dto.RestaurantDto;
import io.reist.sandbox.food.model.dto.RestaurantsDto;
import io.reist.sandbox.food.model.entity.RestaurantEntity;
import io.reist.sandbox.food.model.local.RestaurantsService;
import io.reist.sandbox.food.model.remote.RestaurantApi;
import rx.Observable;
import rx.SingleSubscriber;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

import static android.media.CamcorderProfile.get;

/**
 * Created by Fedorov-DA on 01.03.2018.
 */

public class RestaurantMonitor {
    private final String KEY = "AIzaSyA5rbx7kV-wcHYLqef3BpWTf8YiiVc6GF8";

    private RestaurantsService restaurantService;
    private RestaurantApi restaurantApi;
    private ArrayList<RestaurantModel> restaurantModels = new ArrayList<>();

    BehaviorSubject<List<RestaurantModel>> restaurantModelsSubject = BehaviorSubject.create();

    @Inject
    public RestaurantMonitor(RestaurantApi _restaurantApi, RestaurantsService _restaurantService) {
        restaurantApi = _restaurantApi;
        restaurantService = _restaurantService;

    }

    public void findRestaurants(LatLng latLng) {
        restaurantApi.searchForRestaurants(String.format("%s,%s", latLng.latitude, latLng.longitude), "bar", 1000, true, KEY)
                .subscribeOn(Schedulers.io())
                .subscribe(restaurantsFound);
    }

    public RestaurantModel getRestaurantById(String id) {
        for (RestaurantModel restaurant : restaurantModels) {
            if (restaurant.getId().equals(id)) {
                return restaurant;
            }
        }
        return null;
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
        ArrayList<RestaurantEntity> restaurantEntities = mapModelsToEntities();
        restaurantService.save(restaurantEntities)
                .subscribeOn(Schedulers.io())
                .subscribe((response) -> {
                });
    }

    private void takeCachedRestaurants() {
        restaurantService.list()
                .subscribeOn(Schedulers.io())
                .subscribe((response) -> {
                    if (!response.isSuccessful()) {
                        return;
                    }

                    restaurantModels.clear();
                    List<RestaurantEntity> restaurants = response.getResult();
                    for (RestaurantEntity restaurant : restaurants) {
                        restaurantModels.add(new RestaurantModel(restaurant));
                    }
                    restaurantModelsSubject.onNext(restaurantModels);
                });
    }

    @NonNull
    private ArrayList<RestaurantEntity> mapModelsToEntities() {
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
        return restaurantEntities;
    }

}
