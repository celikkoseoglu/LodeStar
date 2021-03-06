package com.lodestarapp.cs491.lodestar.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lodestarapp.cs491.lodestar.Models.WeatherInformation;
import com.lodestarapp.cs491.lodestar.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class WeatherInformationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<WeatherInformation> weatherInformationList;
    private static final int TODAY = 0;
    private static final int OTHER = 1;

    private Context context;

    private SparseArray weatherIconMap;

    private static final String TAG = "weatherAdapterMessage";

    public WeatherInformationAdapter(SparseArray weatherIconMap, Context context, List<WeatherInformation> weatherInformationList) {
        this.weatherIconMap = weatherIconMap;
        this.context = context;
        this.weatherInformationList = weatherInformationList;
    }

    static class TodaysWeatherViewHolder extends RecyclerView.ViewHolder {

        ImageView weatherTodayView;
        TextView weatherInfoToday;
        TextView temperatureView;
        TextView feelsLikeView;
        TextView humidityView;
        TextView cityNameView;


        TodaysWeatherViewHolder(View itemView) {
            super(itemView);

            this.weatherTodayView = itemView.findViewById(R.id.weather_picture_today);
            this.weatherInfoToday = itemView.findViewById(R.id.weather_info_today);
            this.temperatureView = itemView.findViewById(R.id.temperatureToday);
            this.feelsLikeView = itemView.findViewById(R.id.weather_feels_like);
            this.humidityView = itemView.findViewById(R.id.weather_humidity);
            this.cityNameView = itemView.findViewById(R.id.weather_city);

        }
    }

    static class OtherDaysViewHolder extends RecyclerView.ViewHolder {

        ImageView weatherView;
        TextView weatherInfoView;
        TextView temperatureView;
        TextView humidityView;
        TextView feelsLikeView;


        OtherDaysViewHolder(View itemView) {
            super(itemView);

            this.weatherView = itemView.findViewById(R.id.weather_picture_today);
            this.weatherInfoView = itemView.findViewById(R.id.weather_info);
            this.temperatureView = itemView.findViewById(R.id.temperatureToday);
            this.feelsLikeView = itemView.findViewById(R.id.weather_feels_like);
            this.humidityView = itemView.findViewById(R.id.weather_humidity);

        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType) {
            case TODAY:
                View todayView = LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.card_weather_today,
                                parent, false);
                viewHolder = new TodaysWeatherViewHolder(todayView);
                break;
            case OTHER:
                View otherDaysView = LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.cards_weather,
                                parent, false);
                viewHolder = new OtherDaysViewHolder(otherDaysView);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TODAY:
                ((TodaysWeatherViewHolder) holder).weatherInfoToday.setText(weatherInformationList.
                        get(0).getDescription());
                ((TodaysWeatherViewHolder) holder).temperatureView.setText(String.format("%s°C",
                        String.format("%s",
                                (int) weatherInformationList.get(0).getTemperature())));
                ((TodaysWeatherViewHolder) holder).feelsLikeView.setText(String.format("%s°C",
                        String.format("%s", "feels like " +
                                (int) weatherInformationList.get(0).getFeelsLikeTemperature())));
                ((TodaysWeatherViewHolder) holder).humidityView.setText(String.format("%s%%",
                        String.format("%s",
                                (int) weatherInformationList.get(0).getHumidity())));
                ((TodaysWeatherViewHolder) holder).cityNameView.
                        setText(String.format("in %s", weatherInformationList.get(0).getCity()));
                if(weatherInformationList.get(0).getDescription().toLowerCase().contains("clear") || weatherInformationList.get(0).getDescription().toLowerCase().contains("sun"))
                    ((TodaysWeatherViewHolder) holder).weatherTodayView.setImageResource(R.drawable.sun);
                else
                    ((TodaysWeatherViewHolder) holder).weatherTodayView.setImageResource(this.context.
                            getResources().getIdentifier(weatherIconMap.get(weatherInformationList
                                    .get(0).getWeatherId() / 100).toString(),
                            "drawable", this.context.getPackageName()));

                break;
            case OTHER:
                ((OtherDaysViewHolder) holder).weatherInfoView.setText(String.format("%s  -  %s",
                        weatherInformationList.get(position).getDate(), weatherInformationList.
                                get(position).getDescription()));
                ((OtherDaysViewHolder) holder).humidityView.setText(String.format("%s%%",
                        String.format("%s", (int) weatherInformationList.get(position).getHumidity())));
                ((OtherDaysViewHolder) holder).temperatureView.setText(String.format("%s°C",
                        String.format("%s",
                                (int)weatherInformationList.get(position).getTemperature())));
                ((OtherDaysViewHolder) holder).feelsLikeView.setText(String.format("%s",
                        String.format("feels like %s",
                                (int)weatherInformationList.get(position).getFeelsLikeTemperature())));

                if(weatherInformationList.get(position).getDescription().toLowerCase().contains("clear") || weatherInformationList.get(position).getDescription().toLowerCase().contains("sun"))
                    ((OtherDaysViewHolder) holder).weatherView.setImageResource(R.drawable.sun);
                else
                    ((OtherDaysViewHolder) holder).weatherView.setImageResource(this.context.
                            getResources().getIdentifier(weatherIconMap.get(weatherInformationList
                                    .get(position).getWeatherId() / 100).toString(),
                            "drawable", this.context.getPackageName()));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return weatherInformationList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == TODAY)
            return TODAY;
        return OTHER;
    }
}
