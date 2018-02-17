package com.lodestarapp.cs491.lodestar.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lodestarapp.cs491.lodestar.R;

public class UserPageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    static class UserPageProfile extends RecyclerView.ViewHolder {

        ImageView profilePictureView;
        TextView usernameView;
        TextView tripLogsCountView;
        TextView tripsCountView;
        TextView lastTripToView;

        UserPageProfile(View itemView){
            super(itemView);

            this.profilePictureView = itemView.findViewById(R.id.me_profile_picture);
            this.usernameView = itemView.findViewById(R.id.me_username);
            this.tripLogsCountView = itemView.findViewById(R.id.me_trip_logs_count);
            this.tripsCountView = itemView.findViewById(R.id.me_trip_count);
            this.lastTripToView = itemView.findViewById(R.id.me_last_trip_city);

        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_me, parent, false);
        viewHolder = new UserPageProfile(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
