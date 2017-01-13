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

package io.reist.sandbox.users.presenter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reist.sandbox.R;
import io.reist.sandbox.app.model.User;

/**
 * Created by m039 on 11/12/15.
 */
public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder>
        implements View.OnClickListener {

    public interface OnUserClickListener {
        void onUserClick(User user);
    }

    private List<User> mUsers = new ArrayList<>();
    private OnUserClickListener mOnUserClickListener;

    public UserListAdapter() {
    }

    public void setUsers(List<User> users) {
        mUsers = users;
        notifyDataSetChanged();
    }

    public void setOnUserClickListener(OnUserClickListener l) {
        mOnUserClickListener = l;
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        User user = mUsers.get(position);

        vh.name.setText(user.getName());

        vh.itemView.setTag(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        ViewHolder vh = new ViewHolder(v);

        vh.itemView.setOnClickListener(this);

        return vh;
    }

    @Override
    public void onClick(View v) {
        Integer position = (Integer) v.getTag();

        if (mOnUserClickListener != null && position != null) {
            mOnUserClickListener.onUserClick(mUsers.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return (mUsers != null) ? mUsers.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        TextView name;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }
}
