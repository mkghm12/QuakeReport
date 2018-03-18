package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by manish on 11/3/18.
 */

public class EarthQuakeLoader extends AsyncTaskLoader<List<Earthquake>> {
    private static final String LOG_TAG = EarthQuakeLoader.class.getName();
    private String mUrl;
    public EarthQuakeLoader(Context context,String url) {
        super(context);
        mUrl =url;
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Earthquake> loadInBackground() {
        if (mUrl==null){
            return null;
        }
        List<Earthquake> earthquake = new ArrayList<Earthquake>();
        try {
            earthquake.addAll(QueryUtils.extractEarthquakes(mUrl));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return earthquake;
    }

}
