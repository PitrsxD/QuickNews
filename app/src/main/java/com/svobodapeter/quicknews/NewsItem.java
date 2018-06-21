package com.svobodapeter.quicknews;

public class NewsItem {
    private String mSectionName;
    private String mWebTitle;
    private String mDateOfPublication;
    private String mContributor;
    private String mUrlData;

    /**
     * Creating varibles and methods for adapter to obtain data from JSON response
     *
     * @param sectionName       - category of news
     * @param webTitle          - title of news
     * @param dateOfPublication - publication of news on web
     * @param urlData           - URL of news used fot intent to open detail in web browser
     * @param contributor       - author of news
     */
    public NewsItem(String sectionName, String webTitle, String dateOfPublication, String urlData, String contributor) {
        mSectionName = sectionName;
        mWebTitle = webTitle;
        mDateOfPublication = dateOfPublication;
        mUrlData = urlData;
        mContributor = contributor;
    }

    public NewsItem(String sectionName, String webTitle, String dateOfPublication, String urlData) {
        mSectionName = sectionName;
        mWebTitle = webTitle;
        mDateOfPublication = dateOfPublication;
        mUrlData = urlData;
    }

    public String getSectionName() {
        return mSectionName;
    }

    public String getWebTitle() {
        return mWebTitle;
    }

    public String getDateOfPublication() {
        return mDateOfPublication;
    }

    public String getUrlData() {
        return mUrlData;
    }

    public String getContributor() {
        return mContributor;
    }

}
