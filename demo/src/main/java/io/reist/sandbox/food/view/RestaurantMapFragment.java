package io.reist.sandbox.food.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

import io.reist.sandbox.R;
import io.reist.sandbox.app.view.BaseFragment;
import io.reist.sandbox.food.presenter.RestaurantMapPresenter;

/**
 * Created by Fedorov-DA on 05.03.2018.
 */

public class RestaurantMapFragment extends BaseFragment<RestaurantMapPresenter> implements RestaurantMapView {

    //    AIzaSyDO8ouJon5tENu5sGE5OwhSTUQXllnifmo
    @Inject
    RestaurantMapPresenter presenter;

    private SupportMapFragment mapFragment;

    private static final String ARG_RESTAURANT = "arg_restaurant";

    public RestaurantMapFragment() {
        super(RestaurantMapPresenter.MAP_FRAGMENT, R.layout.fragment_restaurant_map);
    }

    // TODO: 05.03.2018 inherit from restaurant/baserestaurant fragment
    public static Fragment newInstance(String restaurantId) {
        RestaurantMapFragment fragment = new RestaurantMapFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_RESTAURANT, restaurantId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void attachPresenter() {
        super.attachPresenter();

        mapFragment = SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, mapFragment);
        fragmentTransaction.commit();

        getPresenter().initPresenter(getArguments().getString(ARG_RESTAURANT));
    }

    @Override
    public RestaurantMapPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void setRestaurantCoordinates(double lat, double lon) {

        mapFragment.getMapAsync(map ->
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(lat, lon))
                        .title("Marker")));
    }

    @Override
    public void moveMapToUser(LatLng latLng) {
        mapFragment.getMapAsync(map -> {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
        });
//                map.setMyLocationEnabled(true));

    }

}