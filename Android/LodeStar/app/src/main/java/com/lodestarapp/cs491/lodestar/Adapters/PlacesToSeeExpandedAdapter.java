package com.lodestarapp.cs491.lodestar.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lodestarapp.cs491.lodestar.R;

import java.util.ArrayList;

public class PlacesToSeeExpandedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<String> landmarkReviews;
    private ArrayList<String> reviewers;
    private ArrayList<Double> ratings;


    public PlacesToSeeExpandedAdapter(ArrayList<String> landmarkReviews, ArrayList<String> reviewers,ArrayList<Double> ratings){
        this.landmarkReviews = landmarkReviews;
        this.reviewers = reviewers;
        this.ratings = ratings;
    }

    class PlacesToSeeExpandedViewHolder extends RecyclerView.ViewHolder{

        TextView reviewNameSurnameView;
        TextView reviewReviewViewHolder;
        TextView rating;
        ImageView[] placeStarImages = new ImageView[5];

        PlacesToSeeExpandedViewHolder(View itemView) {
            super(itemView);

            this.reviewNameSurnameView = itemView.findViewById(R.id.review_name_surname);
            this.reviewReviewViewHolder = itemView.findViewById(R.id.review_review);
            this.rating = itemView.findViewById(R.id.place_number_of_reviews_expanded);

            this.placeStarImages[0] = itemView.findViewById(R.id.place_stars_image1);
            this.placeStarImages[1] = itemView.findViewById(R.id.place_stars_image2);
            this.placeStarImages[2] = itemView.findViewById(R.id.place_stars_image3);
            this.placeStarImages[3] = itemView.findViewById(R.id.place_stars_image4);
            this.placeStarImages[4] = itemView.findViewById(R.id.place_stars_image5);

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

        ((PlacesToSeeExpandedViewHolder) holder).reviewNameSurnameView.setText(reviewers.get(position));

        ((PlacesToSeeExpandedViewHolder) holder).reviewReviewViewHolder.setText(landmarkReviews.get(position));

        if(ratings.size() > 0) {
            double r = ratings.get(position);
            int numberOfStars = (int) r;
            boolean halfStar;
            if (r % 1 >= 0.5)
                halfStar = true;
            else
                halfStar = false;

            for (int i = 0; i < numberOfStars; i++)
                ((PlacesToSeeExpandedViewHolder) holder).placeStarImages[i].setImageResource(R.drawable.ic_star_full);

            if (halfStar)
                ((PlacesToSeeExpandedViewHolder) holder).placeStarImages[numberOfStars].setImageResource(R.drawable.ic_star_half_full);

            for (int i = numberOfStars; i < 5; i++)
                ((PlacesToSeeExpandedViewHolder) holder).placeStarImages[i].setVisibility(View.GONE);

            ((PlacesToSeeExpandedViewHolder) holder).rating.setText(numberOfStars + "/5");

        }
    }

    @Override
    public int getItemCount() {
        return landmarkReviews.size();
    }
}
