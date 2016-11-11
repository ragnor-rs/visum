/*
 * Copyright (c) 2015  Zvooq LTD.
 * Authors: Renat Sarymsakov, Dmitriy Mozgin, Denis Volyntsev.
 *
 * This file is part of MVP-Sandbox.
 *
 * MVP-Sandbox is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MVP-Sandbox is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MVP-Sandbox.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.reist.sandbox.feed.presenter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reist.dali.Dali;
import io.reist.dali.ScaleMode;
import io.reist.sandbox.R;
import io.reist.sandbox.feed.model.local.Post;

/**
 * Created by 4xes on 7/11/16.
 */
public class FeedListAdapter extends RecyclerView.Adapter<FeedListAdapter.ViewHolder> {

    private final List<Post> posts;
    private ItemClickListener itemClickListener;

    public FeedListAdapter(List<Post> posts) {
        this.posts = posts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        final LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.item_post, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, final int position) {
        vh.title.setText(posts.get(position).title);
        vh.body.setText(posts.get(position).body);
        vh.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.itemClicked(posts.get(position));
            }
        });

        Dali.with(vh.itemView)
            .load("file:///android_asset/post_image.jpg")
            .inCircle(true)
            .scaleMode(ScaleMode.CENTER_CROP)
            .into(vh.image, false);
    }

    public void setItemClickListener(FeedListAdapter.ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


    public interface ItemClickListener {
        void itemClicked(Post post);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.post_image)
        ImageView image;

        @BindView(R.id.post_title)
        TextView title;

        @BindView(R.id.post_body)
        TextView body;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
