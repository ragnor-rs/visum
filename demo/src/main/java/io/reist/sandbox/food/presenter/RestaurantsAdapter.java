package io.reist.sandbox.food.presenter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.reist.sandbox.R;
import io.reist.sandbox.app.model.User;
import io.reist.sandbox.food.model.RestaurantModel;
import io.reist.sandbox.food.view.RestaurantFragment;
import io.reist.sandbox.food.view.RestaurantViewHolder;
import io.reist.sandbox.users.presenter.UserListAdapter;
import io.reist.visum.view.VisumFragmentManager;

/**
 * Created by Fedorov-DA on 02.03.2018.
 */

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantViewHolder> {

    public interface OnRestaurantClickListener {
        void onClick(RestaurantModel restaurant);
    }

    private List<RestaurantModel> restaurants = new ArrayList<>();

    private OnRestaurantClickListener onRestaurantClickListener;

    public void setOnUserClickListener(OnRestaurantClickListener _onRestaurantClickListener) {
        onRestaurantClickListener = _onRestaurantClickListener;
    }

    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant, parent, false);
        RestaurantViewHolder viewHolder = new RestaurantViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RestaurantViewHolder holder, int position) {
        RestaurantModel restaurant = restaurants.get(position);
        holder.setName(restaurant.getName());
        holder.setRating(restaurant.getRating());
        holder.onClick(()-> onRestaurantClickListener.onClick(restaurant));
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }


    public void setRestaurants(List<RestaurantModel> _restaurants) {
        restaurants = _restaurants;
        notifyDataSetChanged();
    }
}
