package com.iconuim.alc_project;

import android.net.Uri;

/**
 * Created by ogie on 6/13/2017.
 */
public class DiaryStory {
    public static String description;
    public static String title;
    public static String uri;


    public DiaryStory()
    {
        super();
    }

    public DiaryStory(String _description,String _title, String _uri) {

        description =  _description;
        title = _title;
        uri = _uri;
    }

    public static String getDescription() {

        return description;
    }

    public static String getTitle() {
        return title;
    }

    public static String getUri() {

        return uri;
    }


}
