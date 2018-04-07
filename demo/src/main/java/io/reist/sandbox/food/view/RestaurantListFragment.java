package io.reist.sandbox.food.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import javax.inject.Inject;

import butterknife.BindView;
import io.reist.sandbox.R;
import io.reist.sandbox.app.view.BaseFragment;
import io.reist.sandbox.app.view.widget.LoaderView;
import io.reist.sandbox.food.model.RestaurantModel;
import io.reist.sandbox.food.presenter.RestaurantListPresenter;
import io.reist.sandbox.food.presenter.RestaurantsAdapter;

/**
 * Created by Fedorov-DA on 01.03.2018.
 */

public class RestaurantListFragment extends BaseFragment<RestaurantListPresenter> implements RestaurantListView {

    @BindView(R.id.food_list)
    RecyclerView restaurantsListView;

    @BindView(R.id.loader)
    LoaderView loaderView;

    @Inject
    RestaurantListPresenter presenter;

    private final int MY_PERMISSIONS_REQUEST = 1233;

    public RestaurantListFragment() {
        super(R.layout.fragment_food);
    }

    @Override
    public void attachPresenter() {

        super.attachPresenter();

        FragmentActivity activity = getActivity();
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(activity,
                    new String[] {
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                    },
                    MY_PERMISSIONS_REQUEST
            );
        } else{
            presenter.init();
        }

    }

    @Override
    public RestaurantListPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void setRestaurantsListAdapter(RestaurantsAdapter restaurantsAdapter) {
        restaurantsListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        restaurantsListView.setAdapter(restaurantsAdapter);
    }

    @Override
    public void showRestaurantInfo(RestaurantModel restaurantModel) {
        getFragmentController().showFragment(RestaurantFragment.newInstance(restaurantModel.getId()), false);
    }

    @Override
    public void hideLoader() {
        loaderView.hide();
    }

}