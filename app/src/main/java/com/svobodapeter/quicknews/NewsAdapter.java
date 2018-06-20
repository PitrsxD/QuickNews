package com.svobodapeter.quicknews;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<NewsItem> {

    private List<NewsItem> models = new ArrayList<>();

    //Contructor
    public NewsAdapter(Activity context, ArrayList<NewsItem> newsItems) {
        super(context, 0, newsItems);
    }

    /**
     * Creating of listItemView, which is inflated b´with data from retrieved JSON through NewsItem class.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list, parent, false);
        }

        //Searching for Views
        TextView sectionName = listItemView.findViewById(R.id.view_section);
        TextView webTitle = listItemView.findViewById(R.id.view_title);
        TextView dateOfPublication = listItemView.findViewById(R.id.view_date);
        TextView contributor = listItemView.findViewById(R.id.view_author);
        LinearLayout newsBox = listItemView.findViewById(R.id.news_box);


        final NewsItem currentNewsObject = getItem(position);

        //Inflating view with name of category
        String mSectionName = currentNewsObject.getSectionName();
        if (mSectionName != null) {
        sectionName.setText(mSectionName);
        }
        //Inflating view with title of news
        String mWebTitle = currentNewsObject.getWebTitle();
        if (mWebTitle != null) {
        webTitle.setText(mWebTitle);
        }
        //Inflating view with name of author
        String mContributor = currentNewsObject.getContributor();
        if (mContributor != null) {
        contributor.setText(mContributor);
        }
        //Inflating view with date of publication
        String mDate = currentNewsObject.getDateOfPublication();
        if (mDate != null) {
        String updateString = mDate.replace("T"," ");
        String newDateString = updateString.replace ("Z","");
        dateOfPublication.setText(newDateString);
        }
        // Giving each item onclick listener to open in intent attached URL
        newsBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openUrl = new Intent(Intent.ACTION_VIEW);
                openUrl.setData(Uri.parse(currentNewsObject.getUrlData()));
                getContext().startActivity(openUrl);
            }
        });

        return listItemView;
    }

}
