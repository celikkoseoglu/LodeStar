package com.lodestarapp.cs491.lodestar.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lodestarapp.cs491.lodestar.R;

import java.util.ArrayList;

public class PlacesToSeeExpandedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<String> landmarkReviews;
    private ArrayList<String> reviewers;

    public PlacesToSeeExpandedAdapter(ArrayList<String> landmarkReviews, ArrayList<String> reviewers){
        this.landmarkReviews = landmarkReviews;
        this.reviewers = reviewers;
    }

    class PlacesToSeeExpandedViewHolder extends RecyclerView.ViewHolder{

        TextView reviewNameSurnameView;
        TextView reviewReviewViewHolder;

        PlacesToSeeExpandedViewHolder(View itemView) {
            super(itemView);

            this.reviewNameSurnameView = itemView.findViewById(R.id.review_name_surname);
            this.reviewReviewViewHolder = itemView.findViewById(R.id.review_review);
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;

        View reviewView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_places_to_see_reviews, parent, false);

        viewHolder = new PlacesToSeeExpandedViewHolder(reviewView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ((PlacesToSeeExpandedViewHolder) holder).reviewNameSurnameView
                .setText(reviewers.get(position));

        ((PlacesToSeeExpandedViewHolder) holder).reviewReviewViewHolder
                .setText(landmarkReviews.get(position));
    }

    @Override
    public int getItemCount() {
        return landmarkReviews.size();
    }
}
