package io.reist.sandbox.food.model;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Locale;
import java.util.Observable;

import rx.Single;
import rx.SingleSubscriber;
import rx.subjects.BehaviorSubject;

/**
 * Created by Fedorov-DA on 06.03.2018.
 */

public class GeopositionMonitor {
    private FusedLocationProviderClient locationProviderClient;
private Context context;
private BehaviorSubject<LatLng> location = BehaviorSubject.create();
    public GeopositionMonitor(Context _context) {
        context=_context;
        locationProviderClient = LocationServices.getFusedLocationProviderClient(_context);
        initiateLocation();
    }

    public BehaviorSubject<LatLng> getLocationFound(){
        initiateLocation();
        return location;
    }

    public void initiateLocation() {
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationProviderClient.getLastLocation()
                .addOnCompleteListener(task -> {
                    Location _location = task.getResult();
                    if (task.isSuccessful() && _location != null) {
                        location.onNext(new LatLng(_location.getLatitude(), _location.getLongitude()));
                    }
                });
    }
}
