package io.reist.sandbox.food.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reist.sandbox.R;

/**
 * Created by Fedorov-DA on 02.03.2018.
 */

public class RestaurantViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.restaurant_name_text)
    TextView name;
    @BindView(R.id.restaurant_rating_text)
    TextView rating;
    @BindView(R.id.restaurant__item)
    LinearLayout item;

    private Runnable onClickRunner;

    public RestaurantViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
        item.setOnClickListener(view1 -> onClickRunner.run());
    }

    public void setName(String _name) {
        name.setText(_name);
    }

    public void setRating(String _rating) {
        rating.setText(_rating);
    }

    public void onClick(Runnable _onClickRunner) {
        onClickRunner = _onClickRunner;
    }
}
