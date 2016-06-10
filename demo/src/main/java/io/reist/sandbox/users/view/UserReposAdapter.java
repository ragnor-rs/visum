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

package io.reist.sandbox.users.view;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reist.sandbox.R;
import io.reist.sandbox.app.model.Repo;

/**
 * Created by Reist on 10/14/15.
 */
public class UserReposAdapter extends RecyclerView.Adapter<UserReposAdapter.ViewHolder>
        implements View.OnClickListener
{

    private List<Repo> repos = new ArrayList<>();

    public interface OnLikeRepoClickListener {
        void onLikeRepoClick(Repo repo);
        void onUnlikeRepoClick(Repo repo);
    }

    public UserReposAdapter() {
    }

    public void setRepos(List<Repo> repos) {
        this.repos = repos;
        notifyDataSetChanged();
    }

    private OnLikeRepoClickListener mOnLikeRepoClickListener;

    public void setOnLikeRepoClickListener(OnLikeRepoClickListener l) {
        mOnLikeRepoClickListener = l;
    }

    @Override
    public void onClick(View v) {
        Integer position = (Integer) v.getTag();

        if (mOnLikeRepoClickListener != null && position != null) {
            Repo repo = repos.get(position);
            if (repo.isLiked()) {
                mOnLikeRepoClickListener.onUnlikeRepoClick(repo);
            } else {
                mOnLikeRepoClickListener.onLikeRepoClick(repo);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        final LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.user_repos_item, parent, false);
        ViewHolder vh = new ViewHolder(v);

        vh.like.setOnClickListener(this);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder c, int position) {
        Resources res = c.itemView.getResources();

        Repo repo = repos.get(position);
        c.textView.setText(repo.name);

        if (repo.isLiked()) {
            c.like.setText(R.string.repo_button_unlike);
        } else {
            c.like.setText(R.string.repo_button_like);
        }

        c.like.setTag(position);
        c.likeCount.setText(res.getString(R.string.repo_like_count, repo.likeCount));
    }

    @Override
    public int getItemCount() {
        return repos == null? 0 : repos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.daggertest_repo_item_text_view)
        TextView textView;

        @Bind(R.id.like)
        Button like;

        @Bind(R.id.like_count)
        TextView likeCount;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
