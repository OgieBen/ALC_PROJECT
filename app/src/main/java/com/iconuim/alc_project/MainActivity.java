package com.iconuim.alc_project;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.IntentService;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

//import static com.iconuim.alc_project.R.id.displayDiaries;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>
 {

    public static ArrayList<String> titles = new ArrayList<>();
     public static ArrayList<String> images = new ArrayList<>();
     MyAdapter myAdapter;
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);

         myAdapter = new MyAdapter(this,R.layout.cards, titles, images);
         getLoaderManager().initLoader(0, null, this);

         ListView listView =  (ListView) findViewById(android.R.id.list);
         listView.setAdapter(myAdapter);

         ImageView addStories = (ImageView) findViewById(R.id.add_icon);
         final Intent intent = new Intent(this,AddToDiary.class);
         addStories.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startActivity(intent);
             }
         });

    }



    @Override
    public CursorLoader onCreateLoader(int id, Bundle args) {

        String [] projection = {PDDBOpenHelper.TITLE_COLUMN,PDDBOpenHelper.DESCRIPTION_COLUMN,PDDBOpenHelper.IMAGE_PATH_REF_COLUMN};

        CursorLoader loader = new CursorLoader(this, PDContentProvider.CONTENT_URI,projection,null,null,null);

        return loader;
    }

     @Override
     public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

         //adapterBind should be done here
         // for every cursor returned call newStory(description,title,imageUri)


         //get db column
         int title = cursor.getColumnIndexOrThrow(PDDBOpenHelper.TITLE_COLUMN);
         int description = cursor.getColumnIndexOrThrow(PDDBOpenHelper.DESCRIPTION_COLUMN);
         int imageRef = cursor.getColumnIndexOrThrow(PDDBOpenHelper.IMAGE_PATH_REF_COLUMN);


         while (cursor.moveToNext())
         {
                 cursor.getCount();
                 Log.e("title ", "Title " + cursor.getString(imageRef));
                titles.add(cursor.getString(title));
                images.add(cursor.getString(imageRef));

         }
         myAdapter.notifyDataSetChanged();
     }

     @Override
     public void onLoaderReset(Loader loader) {
     }

    private class MyAdapter extends ArrayAdapter<String> {


        private int resource;
        private ArrayList<String>  titles;
        private ArrayList<String> img;

        public MyAdapter(Context context, int _resource, ArrayList<String> _titles,ArrayList<String> _img) {
            super(context, _resource, _titles);
            resource =_resource;
           titles = _titles;
            img = _img;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(resource, container, false);
            }


            {
                Uri uri = Uri.parse(img.get(position));
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                String filePath;
                ContentResolver contentResolver = getContentResolver();

                Cursor cur = contentResolver.query(uri, filePathColumn, null, null, null);

                if (cur.moveToFirst()) {
                    int columnIndex = cur.getColumnIndexOrThrow(filePathColumn[0]);
                    filePath = cur.getString(columnIndex);
                    cur.close();

                    Bitmap image = BitmapFactory.decodeFile(filePath);
                    ((ImageView) convertView.findViewById(android.R.id.icon)).setImageBitmap(image);
                }

            }
                ((TextView) convertView.findViewById(android.R.id.text1))
                        .setText(titles.get(position));


            return convertView;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
      //  registerReceiver(pdBroadcast, intentFilter);
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
       // unregisterReceiver(pdBroadcast);
    }
}
