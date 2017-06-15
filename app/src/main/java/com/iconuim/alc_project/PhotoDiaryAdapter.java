package com.iconuim.alc_project;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.zip.Inflater;

import javax.xml.transform.URIResolver;

/**
 * Created by ogie on 6/13/2017.
 */
public class PhotoDiaryAdapter extends ArrayAdapter<DiaryStory> {
    private static LinearLayout diaryView;
    private static LinearLayout main;
    private static int resource;
    private static Context context;


    public PhotoDiaryAdapter(Context _context, int _resource, List<DiaryStory> newStory) {
        super(context, _resource);
        resource = _resource;
        context = _context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
          diaryView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater  li = (LayoutInflater) getContext().getSystemService(inflater);
             li.inflate(resource, diaryView, true);
          //  Inflater inflater1 = ((Activity)context).get;

        }

        DiaryStory diaryStory = getItem(position);


        String description = diaryStory.getTitle();
        String title = diaryStory.getTitle();
        Uri uri = Uri.parse(diaryStory.getUri());




        String[] filePathColumn = {MediaStore.Images.Media.DATA};

           String filePath;
           ContentResolver contentResolver = getContext().getContentResolver();

           Cursor cursor =  contentResolver.query(uri, filePathColumn, null, null, null);

           if(cursor.moveToFirst() ) {
                int columnIndex = cursor.getColumnIndexOrThrow(filePathColumn[0]);
               filePath = cursor.getString(columnIndex);
               cursor.close();

               Bitmap image = BitmapFactory.decodeFile(filePath);

              //  ((TextView) diaryView.findViewById(android.R.id.text1).setText(););
            // TextView descView = (TextView) diaryView.findViewById(R.id.descView);
              // ImageView imgView = (ImageView) diaryView.findViewById(R.id.imageView);

            //   titleView.setText(title);
            // descView.setText(description);
             //  imgView.setImageBitmap(image);



        // bm.compress(Bitmap.CompressFormat.PNG, 100, ostream);

           }




        return diaryView;
    }
}
