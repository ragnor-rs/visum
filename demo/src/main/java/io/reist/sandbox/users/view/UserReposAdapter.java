/*
 * Copyright (C) 2017 Renat Sarymsakov.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.reist.sandbox.users.view;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reist.sandbox.R;
import io.reist.sandbox.app.model.Repo;

/**
 * Created by Reist on 10/14/15.
 */
public class UserReposAdapter extends RecyclerView.Adapter<UserReposAdapter.ViewHolder>
        implements View.OnClickListener {

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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_repos_item, parent, false);
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
        return repos == null ? 0 : repos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.daggertest_repo_item_text_view)
        TextView textView;

        @BindView(R.id.like)
        Button like;

        @BindView(R.id.like_count)
        TextView likeCount;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
