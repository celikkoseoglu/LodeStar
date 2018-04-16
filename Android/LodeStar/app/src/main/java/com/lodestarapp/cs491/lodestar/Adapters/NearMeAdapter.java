package com.lodestarapp.cs491.lodestar.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lodestarapp.cs491.lodestar.Models.Places;
import com.lodestarapp.cs491.lodestar.PlacesToSeeExpandedActivity;
import com.lodestarapp.cs491.lodestar.R;

import java.util.List;

public class NearMeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Places> placesList;
    private Context context;
    private String param;

    public NearMeAdapter(List<Places> placesList,String param, Context context){
        this.placesList = placesList;
        this.context = context;
    }

    class NearMeHolder extends RecyclerView.ViewHolder {

        ImageView placePicture;
        TextView placeName;
        ImageView placeIcon;
        TextView placePhotoAttribution;
        ImageView[] placeStarImages = new ImageView[5];

        /*ImageView placeStarImage1;
        ImageView placeStarImage2;
        ImageView placeStarImage3;
        ImageView placeStarImage4;
        ImageView placeStarImage5;*/

        //REFERENCE:https://stackoverflow.com/questions/27081787/onclicklistener-for-cardview
        View itemView;

        NearMeHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;

            //REFERENCE:https://stackoverflow.com/questions/27081787/onclicklistener-for-cardview
            //REFERENCE:https://stackoverflow.com/questions/768969/passing-a-bundle-on-startactivity
            //REFERENCE:https://stackoverflow.com/questions/28528009/start-new-intent-from-recyclerviewadapter
            this.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    int card = getAdapterPosition();

                    String placeName = NearMeAdapter.this.placesList.get(card).getPlaceName();
                    String placeLocation = NearMeAdapter.this.placesList.get(card).getPlaceLocation();
                    String placeType = NearMeAdapter.this.placesList.get(card).getPlaceType();

                    Intent intent = new Intent(view.getContext(), PlacesToSeeExpandedActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("placeName", placeName);
                    bundle.putString("placeLocation", placeLocation);
                    bundle.putString("placeType", placeType);


                    intent.putExtras(bundle);
                    //view.getContext().startActivity(intent);
                }
            });

            this.placePicture = itemView.findViewById(R.id.imageButtonView);
            this.placeName = itemView.findViewById(R.id.place_name);
            this.placeIcon = itemView.findViewById(R.id.place_icon);
            this.placePhotoAttribution = itemView.findViewById(R.id.attribution);

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
        View placesView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_nearme_cards, parent, false);

        viewHolder = new NearMeAdapter.NearMeHolder(placesView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //TODO: Place Image

        //Set Number of Stars Image
        int noOfStars = placesList.get(position).getNumberOfStars();
        boolean halfStar = placesList.get(position).isHalfStar();

        for ( int i = 0; i < noOfStars; i++) {
            //Log.d("stars", noOfStars+ "");
            ((NearMeHolder) holder).placeStarImages[i].setImageResource(this.context.getResources()
            .getIdentifier("ic_star_full", "drawable", this.context.getPackageName()));
        }
        if(halfStar)
            ((NearMeHolder) holder).placeStarImages[noOfStars].setImageResource(this.context.getResources()
                    .getIdentifier("ic_star_half_full", "drawable", this.context.getPackageName()));

        ((NearMeAdapter.NearMeHolder) holder).placePicture.setImageBitmap(placesList.get(position).getPlaceImage());

        if(placesList.get(position).getPlaceName().length() > 16)
            ((NearMeAdapter.NearMeHolder) holder).placeName.setText(placesList.get(position).getPlaceName().substring(0,15));
        else
            ((NearMeAdapter.NearMeHolder) holder).placeName.setText(placesList.get(position).getPlaceName());

        if(placesList.get(position).getPlaceType().length() > 17)
            ((NearMeAdapter.NearMeHolder) holder).placePhotoAttribution.setText(placesList.get(position).getPlaceType().substring(0,16));
        else
            ((NearMeAdapter.NearMeHolder) holder).placePhotoAttribution.setText(placesList.get(position).getPlaceType());


        ((NearMeAdapter.NearMeHolder) holder).placeIcon.setImageBitmap(placesList.get(position).getPlaceIconImage());

    }

    @Override
    public int getItemCount() {
        return placesList.size();
    }




}
