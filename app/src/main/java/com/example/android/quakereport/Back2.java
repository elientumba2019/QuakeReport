package com.example.android.quakereport;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import org.json.JSONException;

import java.util.List;

public class Back2 extends AsyncTaskLoader<List<EarthQuake>> {

    private String URL;

    public Back2(Context context , String URL) {
        super(context);
        this.URL = URL;
    }


    @Override
    public List<EarthQuake> loadInBackground() {
        List<EarthQuake> list = null;
        try {
            list = NetWorkUtils.getEarthQuakeList(URL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }


    @Override
    protected void onStartLoading() {
        //super.onStartLoading();
        forceLoad();
    }
}