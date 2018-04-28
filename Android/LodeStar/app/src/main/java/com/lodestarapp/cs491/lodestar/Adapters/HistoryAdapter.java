package com.lodestarapp.cs491.lodestar.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lodestarapp.cs491.lodestar.Models.HistoryInfo;
import com.lodestarapp.cs491.lodestar.R;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<HistoryInfo> historyInfos;

    public HistoryAdapter(ArrayList<HistoryInfo> historyInfos){
        this.historyInfos = historyInfos;
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder{

        ImageView cityFromImage;
        ImageView cityToImage;

        TextView cityFrom;
        TextView cityTo;
        TextView fromAirport;
        TextView toAirport;
        TextView departureTime;
        TextView arrivalTime;
        TextView departureDate;
        TextView arrivalDate;

        HistoryViewHolder(final View itemView){
            super(itemView);

            cityFromImage = itemView.findViewById(R.id.history_from_imageview);
            cityToImage = itemView.findViewById(R.id.history_to_imageview);

            cityFrom = itemView.findViewById(R.id.history_from_city_country_textview);
            cityTo = itemView.findViewById(R.id.history_to_city_country_textview);
            fromAirport = itemView.findViewById(R.id.history_from_airport_name_textview);
            toAirport = itemView.findViewById(R.id.history_to_airport_name_textview);
            departureTime = itemView.findViewById(R.id.history_from_time_in_CST);
            arrivalTime = itemView.findViewById(R.id.history_to_time_in_CST);
            departureDate = itemView.findViewById(R.id.history_from_time_in_GMT3);
            arrivalDate = itemView.findViewById(R.id.history_to_time_in_GMT3);

        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;

        View historyView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_history,
                parent, false);
        viewHolder = new HistoryViewHolder(historyView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((HistoryViewHolder) holder).cityFrom.setText(historyInfos.get(position).getCityFrom());
        ((HistoryViewHolder) holder).cityTo.setText(historyInfos.get(position).getCityTo());
        ((HistoryViewHolder) holder).fromAirport.setText(historyInfos.get(position).getFromAirport());
        ((HistoryViewHolder) holder).toAirport.setText(historyInfos.get(position).getToAirport());

        //departure time, arrival time
        //----

        //----

        ((HistoryViewHolder) holder).departureDate.setText(historyInfos.get(position).getDepartureDate());
        ((HistoryViewHolder) holder).arrivalDate.setText(historyInfos.get(position).getArrivalDate());

        ((HistoryViewHolder) holder).cityFromImage.setImageBitmap(historyInfos.get(position).getCityFromBitmap());
        ((HistoryViewHolder) holder).cityToImage.setImageBitmap(historyInfos.get(position).getCityToBitmap());
    }

    @Override
    public int getItemCount() {
        return historyInfos.size();
    }
}
