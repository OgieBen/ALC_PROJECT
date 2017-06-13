package com.iconuim.alc_project;

import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * Created by ogie on 6/6/2017.
 */
public class AsyncUpdate  extends AsyncTask<ContentValues, Integer ,String> {

    @Override
    protected String doInBackground(ContentValues... params) {

      PDContentProvider pdContentProvider = new PDContentProvider();
        if(AddToDiary.CONTENT_VALUES != null) {
            Uri id = pdContentProvider.insert(PDContentProvider.CONTENT_URI, params[0]);
        }else{

        }

        return null;
    }




    @Override
    protected void onProgressUpdate(Integer... values) {

        super.onProgressUpdate(values);
    }


    @Override
    protected void onPostExecute(String s) {

        super.onPostExecute(s);
    }
}
