package com.svobodapeter.quicknews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/*
App which is focused on downloading latest news from The Guardian and showing them to the user.
 */

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<NewsItem>> {

    // URL for retrieving JSON from website of The Guardian / containing api key
    final String url = "https://content.guardianapis.com/search?api-key=bb5a953a-ced5-4613-85f5-b8c8fd051e95&show-tags=contributor&";

    private ListView newsListView;
    private NewsAdapter newsAdapter;
    private TextView emptyView;
    private ProgressBar loadingCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //LoadingCircle shows progress/proces, so user see activity even on blank screen
        loadingCircle = findViewById(R.id.loading_spinner);
        loadingCircle.setVisibility(View.VISIBLE);

        //Creating of custom adapter from NewsAdapter
        newsAdapter = new NewsAdapter(this, new ArrayList<NewsItem>());

        //Binding ListView from activity_main.xml
        newsListView = (ListView) findViewById(R.id.list);

        //Binding emptyView for cases when there are no data do load
        emptyView = findViewById(R.id.empty_view);
        newsListView.setEmptyView(emptyView);

        //Calling connectivity manager to control if device is connected to internet
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cm.getActiveNetworkInfo();


        if (nInfo != null && nInfo.isConnectedOrConnecting()) {
            //In case that device is connected, Loader will be initialized
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(1, null, this).forceLoad();
        } else {
            //In case that device is not connected, message will be shown
            emptyView.setText(R.string.no_internet);
            loadingCircle.setVisibility(View.GONE);
        }

    }

    @Override
    public Loader<ArrayList<NewsItem>> onCreateLoader(int i, Bundle bundle) {
        //Creating new NewsLoader to load data from URL of the Guardian
        return new NewsLoader(this, url);

    }

    /*
    Parsing data extracted from URL to Adapter
     */
    @Override
    public void onLoadFinished(Loader<ArrayList<NewsItem>> loader, ArrayList<NewsItem> newsList) {
        Log.i("Loader", "finished");
        newsAdapter.clear();
        //If there are data in ArrayList, they are parsed to adapter
        if (newsList != null && !newsList.isEmpty()) {
            newsAdapter.addAll(newsList);
            newsListView.setAdapter(newsAdapter);
            loadingCircle.setVisibility(View.GONE);
        } else {
            //In case that NewsList has no data, message about no data is shown
            loadingCircle.setVisibility(View.GONE);
            emptyView.setText(R.string.no_content);
        }
    }

    /*
    In case of activity terminating, adapter and data are cleared
     */
    @Override
    public void onLoaderReset(Loader<ArrayList<NewsItem>> loader) {
        Log.i("Loader", "reseted");
        newsAdapter.clear();
    }
}
