package com.svobodapeter.quicknews;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public final class NewsHttpConnection {

    //Tag for Log.i in case of Exception
    public static final String LOG_TAG = NewsHttpConnection.class.getSimpleName();

    public static ArrayList<NewsItem> fetchNews(String requestURL) {

        URL url = createURL(requestURL);

        String jsonResponse = null;
        try {
            //Demanding connection and data from Guardian
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error with closin inputStream", e);
        }
        //Parsing String of data to variables in NewsItem.java
        ArrayList<NewsItem> news = extractNews(jsonResponse);
        return news;
    }

    private static URL createURL(String stringURL) {
        URL url = null;
        try {
            url = new URL(stringURL);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with URL", e);
        }
        return url;
    }

    /*
    Creating connection between device and The Guardian server through HTTP protocol to download JSON data
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        int MAX_READ_TIMEOUT = 10000;
        int MAX_CONNECTION_TIMEOUT = 15000;
        int HTTP_OK = 200;

        if (jsonResponse == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(MAX_READ_TIMEOUT/*miliseconds*/);
            urlConnection.setConnectTimeout(MAX_CONNECTION_TIMEOUT/*miliseconds*/);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            System.out.println("Response code:" + String.valueOf(urlConnection.getResponseCode()));
            //Code 200 = connection was successful
            if (urlConnection.getResponseCode() == HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                //Obtained data are stored in buffer and retrieved in more readable format
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code:" + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem with retrieving JSON data", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }

        }
        return jsonResponse;
    }

    /*
    Buffer for data to translate them into more readable format (for performance)
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilderOutput = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader streamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(streamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilderOutput.append(line);
                line = bufferedReader.readLine();
            }
        }
        return stringBuilderOutput.toString();
    }

    /**
     * Reading through JSON response retrieved from StringBuilder and parsing to variable in NewsItem.java
     */
    public static ArrayList<NewsItem> extractNews(String jsonResponse) {
        ArrayList<NewsItem> newsList = new ArrayList<>();
        try {
            //Reading through response from level "response"
            JSONObject reader = new JSONObject(jsonResponse);
            JSONObject response = reader.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");
            // Loop to read through all "results" nodes
            for (int i = 0; i < results.length(); i++) {
                JSONObject o = results.getJSONObject(i);
                // Data for category
                String sectionName = o.getString("sectionName");
                // Data for title
                String webTitle = o.getString("webTitle");
                // Data for place of date of publication
                String dateOfPublication = o.getString("webPublicationDate");
                // Data for URL of news
                String urlData = o.getString("webUrl");
                // Data about contributor
                JSONArray tags = o.getJSONArray("tags");
                if (!tags.isNull(0)) {
                    JSONObject t = tags.getJSONObject(0);
                    String contributor = t.getString("webTitle");
                    // Parsing data to NewsItem class
                    newsList.add(new NewsItem(sectionName, webTitle, dateOfPublication, urlData, contributor));
                    Log.i("queryUtils", newsList.toString());
                } else {
                    newsList.add(new NewsItem(sectionName, webTitle, dateOfPublication, urlData));
                    Log.i("queryUtils", newsList.toString());
                }

            }
        } catch (JSONException e) {
            //In case of error Log will be printed
            Log.e("queryUtils", "Problemy with parsing the news JSON result", e);
        }
        return newsList;
    }
}
