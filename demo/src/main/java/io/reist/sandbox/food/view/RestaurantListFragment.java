package io.reist.sandbox.food.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.TextureView;
import android.widget.TextView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reist.sandbox.R;
import io.reist.sandbox.app.view.BaseFragment;
import io.reist.sandbox.app.view.widget.LoaderView;
import io.reist.sandbox.food.model.RestaurantModel;
import io.reist.sandbox.food.presenter.RestaurantListPresenter;
import io.reist.sandbox.food.presenter.RestaurantsAdapter;
import io.reist.sandbox.time.view.TimeView;
import io.reist.sandbox.users.view.UserReposFragment;

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

    public RestaurantListFragment() {
        super(R.layout.fragment_food);
    }

    @Override
    public void attachPresenter() {
        super.attachPresenter();
    }

    @Override
    public RestaurantListPresenter getPresenter() {
        return presenter;
    }


    @Override
    public void setRestaurantsListAdapter(RestaurantsAdapter _restaurantsAdapter) {
        restaurantsListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        restaurantsListView.setAdapter(_restaurantsAdapter);
    }

    @Override
    public void showRestaurantInfo(RestaurantModel restaurantModel) {
        getFragmentController().showFragment(RestaurantFragment.newInstance(restaurantModel.getId()), false);
    }

    @Override
    public void hideLoader(){
        loaderView.hide();
    }
}