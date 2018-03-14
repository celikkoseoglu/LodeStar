package com.lodestarapp.cs491.lodestar.Adapters;

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

    public PlacesToSeeAdapter(List<Places> placesList){
        this.placesList = placesList;
    }

    static class PlacesViewHolder extends RecyclerView.ViewHolder {

        ImageButton placePictureButton;
        TextView placeNameView;
        TextView placeTypeView;
        TextView placeLocationView;

        //ImageView ??? Number of stars???

        TextView placeNumberOfReviewsView;

        public PlacesViewHolder(View itemView) {
            super(itemView);

            this.placePictureButton = itemView.findViewById(R.id.imageButtonPlace);
            this.placeNameView = itemView.findViewById(R.id.place_name);
            this.placeTypeView = itemView.findViewById(R.id.place_type);
            this.placeLocationView = itemView.findViewById(R.id.place_location);
            this.placeNumberOfReviewsView = itemView.findViewById(R.id.place_number_of_reviews);

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
