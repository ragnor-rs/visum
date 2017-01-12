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

package io.reist.sandbox.repos.presenter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reist.sandbox.R;
import io.reist.sandbox.app.model.Repo;

/**
 * Created by Reist on 10/14/15.
 */
public class RepoListAdapter extends RecyclerView.Adapter<RepoListAdapter.ViewHolder> {

    private final List<Repo> repos;
    private ItemClickListener itemClickListener;

    public RepoListAdapter(List<Repo> repos) {
        this.repos = repos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.github_repo_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder c, final int position) {
        c.textView.setText(repos.get(position).name);
        c.textView.setBackground(c.textView.getContext().getResources().getDrawable(R.drawable.list_item_clicked));
        c.textView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.itemClicked(repos.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return repos.size();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.daggertest_repo_item_text_view)
        TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public interface ItemClickListener {
        void itemClicked(Repo repo);
    }

}
