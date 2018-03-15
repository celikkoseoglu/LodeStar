package com.lodestarapp.cs491.lodestar.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lodestarapp.cs491.lodestar.Models.Places;
import com.lodestarapp.cs491.lodestar.R;

import java.util.List;

public class PlacesToSeeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Places> placesList;
    private Context context;

    public PlacesToSeeAdapter(List<Places> placesList, Context context){
        this.placesList = placesList;
        this.context = context;
    }

    static class PlacesViewHolder extends RecyclerView.ViewHolder {

        ImageButton placePictureButton;
        TextView placeNameView;
        TextView placeTypeView;
        TextView placeLocationView;

        //ImageView ??? Number of stars???
        ImageView[] placeStarImages = new ImageView[5];
        /*ImageView placeStarImage1;
        ImageView placeStarImage2;
        ImageView placeStarImage3;
        ImageView placeStarImage4;
        ImageView placeStarImage5;*/

        TextView placeNumberOfReviewsView;

        public PlacesViewHolder(View itemView) {
            super(itemView);

            this.placePictureButton = itemView.findViewById(R.id.imageButtonPlace);
            this.placeNameView = itemView.findViewById(R.id.place_name);
            this.placeTypeView = itemView.findViewById(R.id.place_type);
            this.placeLocationView = itemView.findViewById(R.id.place_location);
            this.placeNumberOfReviewsView = itemView.findViewById(R.id.place_number_of_reviews);

            this.placeStarImages[0] = itemView.findViewById(R.id.place_stars_image1);
            this.placeStarImages[1] = itemView.findViewById(R.id.place_stars_image2);
            this.placeStarImages[2] = itemView.findViewById(R.id.place_stars_image3);
            this.placeStarImages[3] = itemView.findViewById(R.id.place_stars_image4);
            this.placeStarImages[4] = itemView.findViewById(R.id.place_stars_image5);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;

        View placesView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_places_to_see, parent, false);

        viewHolder = new PlacesViewHolder(placesView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //TODO: Place Image and Number of Stars Image

        //Set Number of Stars Image
        int noOfStars = placesList.get(position).getNumberOfStars();
        boolean halfStar = placesList.get(position).isHalfStar();
        int i;

        for ( i = 0; i < noOfStars; i++) {
            ((PlacesViewHolder) holder).placeStarImages[i].setImageResource(this.context.getResources()
            .getIdentifier("ic_star_full", "drawable", this.context.getPackageName()));
        }
        if(halfStar)
            ((PlacesViewHolder) holder).placeStarImages[i].setImageResource(this.context.getResources()
                    .getIdentifier("ic_star_half_full", "drawable", this.context.getPackageName()));

        //Set Place Name
        ((PlacesViewHolder) holder).placeNameView.setText(placesList.get(position)
                .getPlaceName());

        //Set Place Type
        ((PlacesViewHolder) holder).placeTypeView.setText(placesList.get(position)
                .getPlaceType());

        //Set Place Location
        ((PlacesViewHolder) holder).placeLocationView.setText(placesList.get(position)
                .getPlaceLocation());

        //Set Place Number of Reviews
        ((PlacesViewHolder) holder).placeNumberOfReviewsView.setText(placesList.get(position)
                .getPlaceNumberOfReviews());
    }

    @Override
    public int getItemCount() {
        return placesList.size();
    }
}
