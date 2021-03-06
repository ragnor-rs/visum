package io.reist.sandbox.food.model;

import io.reist.sandbox.food.model.dto.RestaurantDto;
import io.reist.sandbox.food.model.entity.RestaurantEntity;

/**
 * Created by Fedorov-DA on 01.03.2018.
 */

public class RestaurantModel {
    RestaurantEntity entity;
    RestaurantDto dto;
    private boolean isLocal;

    public RestaurantModel(RestaurantDto dto) {
        this.dto = dto;
        isLocal = false;
    }

    public RestaurantModel(RestaurantEntity entity) {
        entity = entity;
        isLocal = true;
    }

    public String getName() {
        return isLocal
                ? entity.name
                : dto.name;
    }

    public String getRating() {
        return isLocal
                ? entity.rating
                : dto.rating;
    }

    public String getId() {
        return isLocal
                ? entity.id
                : dto.id;
    }

    public double getLon() {
        return isLocal
                ? entity.lon
                : dto.geometry.location.lng;
    }

    public double getLat() {
        return isLocal
                ? entity.lat
                : dto.geometry.location.lat;
    }
}
