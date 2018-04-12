package com.lodestarapp.cs491.lodestar.Interfaces;

import android.graphics.Bitmap;
import android.media.Image;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public interface LodeStarServerCallback {
    void onSuccess(JSONArray jsonArray, JSONObject jsonObject) throws JSONException;
    void onPlaceImageSuccess(Bitmap bitmap);
}
