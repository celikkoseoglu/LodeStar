package com.lodestarapp.cs491.lodestar.Interfaces;

import org.json.JSONArray;
import org.json.JSONObject;

public interface LodeStarServerCallback {
    void onSuccess(JSONArray jsonArray, JSONObject jsonObject);
}
