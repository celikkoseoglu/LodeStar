package com.lodestarapp.cs491.lodestar.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lodestarapp.cs491.lodestar.Models.WeatherInformation;
import com.lodestarapp.cs491.lodestar.R;
import org.json.JSONObject;

import java.util.List;

public class WeatherInformationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private JSONObject weatherInformationFromServer;
    private WeatherInformation[] weatherInformation;

    private List<String> description;
    private List<Double> temperature;
    private List<Double> humidity;

    private static final int TODAY = 0;
    private static final int OTHER = 1;

    private static final String TAG = "weatherAdapterMessage";

    public WeatherInformationAdapter(List<String> description, List<Double> temperature,
                                     List<Double> humidity){

        this.description = description;
        this.temperature = temperature;
        this.humidity = humidity;

        this.weatherInformationFromServer = weatherInformationFromServer;

        weatherInformation = new WeatherInformation[5];
        //parseTheJSONResponse(weatherInformationFromServer);
    }

    static class TodaysWeatherViewHolder extends RecyclerView.ViewHolder {

        ImageView weatherTodayView;
        TextView weatherInfoToday;

        TodaysWeatherViewHolder(View itemView){
            super(itemView);

            this.weatherTodayView = itemView.findViewById(R.id.weather_picture_today);
            this.weatherInfoToday = itemView.findViewById(R.id.weather_info_today);
        }
    }

    static class OtherDaysViewHolder extends RecyclerView.ViewHolder {

        ImageView weatherView;
        TextView weatherInfoView;
        TextView temperatureView;
        TextView humidityView;

        OtherDaysViewHolder(View itemView) {
            super(itemView);

            this.weatherView = itemView.findViewById(R.id.weather_picture);
            this.weatherInfoView = itemView.findViewById(R.id.weather_info);
            this.temperatureView = itemView.findViewById(R.id.weather_feels_like);
            this.humidityView = itemView.findViewById(R.id.weather_humidity);

        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType){
            case TODAY:
                View todayView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_weather_today,
                        parent, false);
                viewHolder = new TodaysWeatherViewHolder(todayView);
                break;
            case OTHER:
                View otherDaysView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_weather,
                        parent, false);
                viewHolder = new OtherDaysViewHolder(otherDaysView);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case TODAY:
                ((TodaysWeatherViewHolder) holder).weatherInfoToday.setText(description.get(0));
                break;
            case OTHER:
                ((OtherDaysViewHolder) holder).weatherInfoView.setText(description.get(position));
                ((OtherDaysViewHolder) holder).humidityView.setText(String.format("%s",
                        humidity.get(position)));
                ((OtherDaysViewHolder) holder).temperatureView.setText(String.format("%s",
                        temperature.get(position)));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return weatherInformation.length;
    }

    @Override
    public int getItemViewType(int position){
        if (position == TODAY)
            return TODAY;
        return OTHER;
    }
}
