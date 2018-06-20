package com.svobodapeter.quicknews;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

public class NewsLoader extends AsyncTaskLoader<ArrayList<NewsItem>> {

    private String mUrl;

    public NewsLoader(Context context, String urls) {
        super(context);
        mUrl = urls;
    }

    @Override
    public ArrayList<NewsItem> loadInBackground() {
        Log.i("Loader", "loaded");
        if (mUrl == null) {
            return null;
        }
        ArrayList<NewsItem> newsList = NewsHttpConnection.fetchNews(mUrl);
        return newsList;
    }
}
