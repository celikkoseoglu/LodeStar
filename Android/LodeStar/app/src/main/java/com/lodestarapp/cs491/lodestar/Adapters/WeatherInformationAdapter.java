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

    private List<WeatherInformation> weatherInformationList;
    private static final int TODAY = 0;
    private static final int OTHER = 1;

    private static final String TAG = "weatherAdapterMessage";

    public WeatherInformationAdapter(List<WeatherInformation> weatherInformationList){
        this.weatherInformationList = weatherInformationList;
    }

    static class TodaysWeatherViewHolder extends RecyclerView.ViewHolder {

        ImageView weatherTodayView;
        TextView weatherInfoToday;
        TextView temperatureView;
        TextView feelsLikeView;
        TextView humidityView;


        TodaysWeatherViewHolder(View itemView){
            super(itemView);

            this.weatherTodayView = itemView.findViewById(R.id.weather_picture_today);
            this.weatherInfoToday = itemView.findViewById(R.id.weather_info_today);
            this.temperatureView = itemView.findViewById(R.id.temperatureToday);
            this.feelsLikeView = itemView.findViewById(R.id.weather_feels_like);
            this.humidityView = itemView.findViewById(R.id.weather_humidity);
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
                ((TodaysWeatherViewHolder) holder).weatherInfoToday.setText(weatherInformationList.
                        get(0).getDescription());
                ((TodaysWeatherViewHolder) holder).temperatureView.setText(String.format("%s",
                        weatherInformationList.get(0).getFeelsLikeTemperature()));
                ((TodaysWeatherViewHolder) holder).humidityView.setText(String.format("%s",
                        weatherInformationList.get(0).getHumidity()));
                break;
            case OTHER:
                ((OtherDaysViewHolder) holder).weatherInfoView.setText(weatherInformationList.
                        get(position).getDescription());
                ((OtherDaysViewHolder) holder).humidityView.setText(String.format("%s",
                        weatherInformationList.get(position).getHumidity()));
                ((OtherDaysViewHolder) holder).temperatureView.setText(String.format("%s",
                        weatherInformationList.get(position).getFeelsLikeTemperature()));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return weatherInformationList.size();
    }

    @Override
    public int getItemViewType(int position){
        if (position == TODAY)
            return TODAY;
        return OTHER;
    }
}
