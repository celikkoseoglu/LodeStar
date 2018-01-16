package com.lodestarapp.cs491.lodestar.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lodestarapp.cs491.lodestar.Models.WeatherInformation;
import com.lodestarapp.cs491.lodestar.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class WeatherInformationAdapter extends RecyclerView.Adapter<WeatherInformationAdapter.ViewHolder> {

    private JSONObject weatherInformationFromServer;
    private WeatherInformation[] weatherInformation;

    private List<String> description;
    private List<Double> temperature;
    private List<Double> humidity;

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

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView weatherView;
        TextView weatherInfoView;
        TextView temperatureView;
        TextView humidityView;

        ViewHolder(View itemView) {
            super(itemView);

            this.weatherView = itemView.findViewById(R.id.weather_picture);
            this.weatherInfoView = itemView.findViewById(R.id.weather_info);
            this.temperatureView = itemView.findViewById(R.id.weather_feels_like);
            this.humidityView = itemView.findViewById(R.id.weather_humidity);

        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_weather,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.weatherInfoView.setText(description.get(0));
        holder.humidityView.setText("" + humidity.get(0));
        holder.temperatureView.setText("" + temperature.get(0));
        holder.weatherView = null;
    }

    @Override
    public int getItemCount() {
        return weatherInformation.length;
    }
}
