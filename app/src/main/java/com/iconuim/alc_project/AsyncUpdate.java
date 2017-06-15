package com.iconuim.alc_project;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;

/**
 * Created by ogie on 6/6/2017.
 */
public class AsyncUpdate  extends AsyncTask<ContentValues, Integer ,String> {

     TextView view;
    public AsyncUpdate(TextView _view) {
       view =_view;
    }

    @Override
    protected String doInBackground(ContentValues... params) {



        PDContentProvider pdContentProvider = new PDContentProvider();


             try {
                 Uri id = pdContentProvider.insert(PDContentProvider.CONTENT_URI, params[0]);
                 return id.toString();

             }catch(SQLiteException e)
             {
                 Log.e("Error",""+e);
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

       if(!TextUtils.isEmpty(s)){
            AddToDiary.asyncString = s;
           view.setText(s);

        }

    }
}
