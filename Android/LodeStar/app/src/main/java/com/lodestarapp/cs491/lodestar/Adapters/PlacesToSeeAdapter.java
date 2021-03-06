package com.lodestarapp.cs491.lodestar.Adapters;

import android.app.ActivityOptions;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lodestarapp.cs491.lodestar.Models.Places;
import com.lodestarapp.cs491.lodestar.PlacesToSeeActivity;
import com.lodestarapp.cs491.lodestar.PlacesToSeeExpandedActivity;
import com.lodestarapp.cs491.lodestar.R;
import com.lodestarapp.cs491.lodestar.VenueActivity;

import java.util.ArrayList;
import java.util.List;

public class PlacesToSeeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Places> placesList;
    private Context context;
    private String rating;

    public PlacesToSeeAdapter(List<Places> placesList, Context context){
        this.placesList = placesList;
        this.context = context;
    }

    class PlacesViewHolder extends RecyclerView.ViewHolder {

        TextView placeTypeView;
        ImageView placePictureView;
        TextView placeNameView;
        //TextView placeTypeView;
        TextView placeLocationView;

        ImageView placeTypeIcon;
        ImageView im;

        //ImageView ??? Number of stars???
        ImageView[] placeStarImages = new ImageView[5];
        /*ImageView placeStarImage1;
        ImageView placeStarImage2;
        ImageView placeStarImage3;
        ImageView placeStarImage4;
        ImageView placeStarImage5;*/



        //TextView placeNumberOfReviewsView;

        //REFERENCE:https://stackoverflow.com/questions/27081787/onclicklistener-for-cardview
        View itemView;

        PlacesViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;

            //REFERENCE:https://stackoverflow.com/questions/27081787/onclicklistener-for-cardview
            //REFERENCE:https://stackoverflow.com/questions/768969/passing-a-bundle-on-startactivity
            //REFERENCE:https://stackoverflow.com/questions/28528009/start-new-intent-from-recyclerviewadapter
            this.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    int card = getAdapterPosition();
                    //ArrayList<String> sendInformationToNextActivity = new ArrayList<>();
                    //sendInformationToNextActivity.add(PlacesToSeeAdapter.this.placesList.get(card).getPlaceName());

                    String placeName = PlacesToSeeAdapter.this.placesList.get(card).getPlaceName();
                    String placeLocation = PlacesToSeeAdapter.this.placesList.get(card).getPlaceLocation();
                    String placeType = PlacesToSeeAdapter.this.placesList.get(card).getPlaceType();
                    String placeId = PlacesToSeeAdapter.this.placesList.get(card).getPlaceId();
                    rating  = PlacesToSeeAdapter.this.placesList.get(card).getRating();
                    Intent intent;
                    if(PlacesToSeeAdapter.this.placesList.get(card).getOriginPage().equals("landmark"))
                        intent = new Intent(view.getContext(), PlacesToSeeExpandedActivity.class);
                    else
                        intent = new Intent(view.getContext(), VenueActivity.class);



                    Bundle bundle = new Bundle();
                    bundle.putString("placeName", placeName);
                    bundle.putString("placeLocation", placeLocation);
                    bundle.putString("placeType", placeType);
                    bundle.putString("placeId", placeId);
                    bundle.putString("placeRating", rating);
                    bundle.putString("placeCoords", PlacesToSeeAdapter.this.placesList.get(card).getCoords());


                    intent.putExtras(bundle);
                    view.getContext().startActivity(intent);
                }
            });

            this.placePictureView = itemView.findViewById(R.id.imageButtonView);
            this.placeNameView = itemView.findViewById(R.id.place_name);
            this.placeTypeView = itemView.findViewById(R.id.place_type);
            this.placeLocationView = itemView.findViewById(R.id.place_location);
            //this.placeNumberOfReviewsView = itemView.findViewById(R.id.place_number_of_reviews);
            this.placeTypeIcon = itemView.findViewById(R.id.place_icon_from_api);
            this.im = itemView.findViewById(R.id.place_iconn);


            this.placeStarImages[0] = itemView.findViewById(R.id.place_stars_image1);
            this.placeStarImages[1] = itemView.findViewById(R.id.place_stars_image2);
            this.placeStarImages[2] = itemView.findViewById(R.id.place_stars_image3);
            this.placeStarImages[3] = itemView.findViewById(R.id.place_stars_image4);
            this.placeStarImages[4] = itemView.findViewById(R.id.place_stars_image5);



        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;

        View placesView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_places_to_see, parent, false);

        viewHolder = new PlacesViewHolder(placesView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //Set Place Image
        ((PlacesViewHolder) holder).placePictureView.setImageBitmap(placesList.get(position).getPlaceImage());

        //Set Place Name
        ((PlacesViewHolder) holder).placeNameView.setText(placesList.get(position).getPlaceName());

        //Set Place Type
        if(placesList.get(position).getPlaceType().length() > 20 )
            ((PlacesViewHolder) holder).placeTypeView.setText(placesList.get(position).getPlaceType().substring(0,19));
        else
            ((PlacesViewHolder) holder).placeTypeView.setText(placesList.get(position).getPlaceType());

        //Set Place Location
        ((PlacesViewHolder) holder).placeLocationView.setText(placesList.get(position).getPlaceLocation());

        //Set Place Number of Reviews
        //((PlacesViewHolder) holder).placeNumberOfReviewsView.setText(placesList.get(position)
        //        .getPlaceNumberOfReviews());

        ((PlacesViewHolder) holder).placeTypeIcon.setImageBitmap(placesList.get(position).getPlaceIconImage());

        int noOfStars = placesList.get(position).getNumberOfStars();
        boolean halfStar = placesList.get(position).isHalfStar();

        for ( int i = 0; i < noOfStars; i++) {
            //Log.d("stars", noOfStars+ "");
            ((PlacesViewHolder) holder).placeStarImages[i].setImageResource(this.context.getResources()
                    .getIdentifier("ic_star_full", "drawable", this.context.getPackageName()));
        }
        if(halfStar)
            ((PlacesViewHolder) holder).placeStarImages[noOfStars].setImageResource(this.context.getResources()
                    .getIdentifier("ic_star_half_full", "drawable", this.context.getPackageName()));

        ((PlacesToSeeAdapter.PlacesViewHolder) holder).im.setImageResource(R.drawable.ic_location);

    }

    @Override
    public int getItemCount() {
        return placesList.size();
    }
}
