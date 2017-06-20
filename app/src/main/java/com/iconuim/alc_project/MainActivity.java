package com.iconuim.alc_project;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.IntentService;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.app.ProgressDialog;
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
import android.graphics.Path;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Handler;
import java.util.zip.Inflater;

import static android.app.PendingIntent.getActivity;

//import static com.iconuim.alc_project.R.id.displayDiaries;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>
 {

     private static ArrayList<String> titles = new ArrayList<>();
     private static ArrayList<String> images = new ArrayList<>();
     private static ArrayList<String> description = new ArrayList<>();

     private static int PIC_CAPTURE_INTENT = 300;
     private static int SAVE_FILE = 400;
     private static ProgressDialog dialog;
     MyAdapter myAdapter;


     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);

         dialog = new ProgressDialog(MainActivity.this);
         dialog.setMessage("Loading");
         dialog.setCancelable(false);
         dialog.setInverseBackgroundForced(false);
         dialog.show();

        // AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
         // Get the layout inflater
        // LayoutInflater inflater = getActivity().getLayoutInflater();


         myAdapter = new MyAdapter(this,R.layout.cards, titles, images,description);
         getLoaderManager().initLoader(0, null, this);

         ListView listView =  (ListView) findViewById(android.R.id.list);



         listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                 final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                 LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                 View  dView = inflater.inflate(R.layout.details_dialog,null);
                 builder.create();
                 builder.setView(dView);


                 Uri uri = Uri.parse(images.get(position));

                 if( images.size() != 0 && description.size() != 0 && titles.size() != 0 ) {

                     try {
                         InputStream inputStream = getContentResolver().openInputStream(uri);
                         Bitmap image = BitmapFactory.decodeStream(inputStream);

                         ((TextView) dView.findViewById(R.id.dialogTitleText))
                                 .setText(titles.get(position));
                         ((TextView) dView.findViewById(R.id.dialogDescriptionText))
                                 .setText(description.get(position));
                         ((ImageView) dView.findViewById(R.id.dialogImage)).setImageBitmap(image);
                         builder.show();
                     } catch (FileNotFoundException e) {
                         Toast toast = Toast.makeText(MainActivity.this, "Image File could not be retrieved", Toast.LENGTH_SHORT);
                         toast.setGravity(Gravity.CENTER, 0, 0);
                         toast.show();
                     }
                 }



             /*    Toast toast = Toast.makeText(MainActivity.this," "+ images.size() +" "+ description.size()  +" "+ titles.size(), Toast.LENGTH_SHORT);
                 toast.setGravity(Gravity.CENTER, 0, 0);
                 toast.show(); */





             }
         });
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
         int _description = cursor.getColumnIndexOrThrow(PDDBOpenHelper.DESCRIPTION_COLUMN);
         int imageRef = cursor.getColumnIndexOrThrow(PDDBOpenHelper.IMAGE_PATH_REF_COLUMN);

            images.clear();
            titles.clear();
         while (cursor.moveToNext())
         {
                 cursor.getCount();
                 Log.e("title ", "Title " + cursor.getString(imageRef));
                titles.add(cursor.getString(title));
                images.add(cursor.getString(imageRef));
                description.add(cursor.getString(_description));

         }
         myAdapter.notifyDataSetChanged();
         dialog.hide();
     }

     @Override
     public void onLoaderReset(Loader loader) {
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


        switch(id)
        {
            case  R.id.action_settings:
                return true;
            case R.id.cam:

                File imageFile;
                   // if(hasSystemFeature(PackageManager.FEATURE_CAMERA))
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if(intent.resolveActivity(getPackageManager())!= null) {

                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        String imageFileName = "JPEG_" + timeStamp + "_";
                        try {
                            File storage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                            imageFile = File.createTempFile( imageFileName, ".jpg", storage );
                            String path = imageFile.getAbsolutePath();

                            if(imageFile != null) {
                                Uri imageUri = FileProvider.getUriForFile(this, "com.iconuim.alc_project.fileprovider", imageFile);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                startActivityForResult(intent, SAVE_FILE);

                              /*  Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                File openImagefile = new File(path);
                                Uri imageFileUri = Uri.fromFile(openImagefile);
                                mediaScanIntent.setData(imageFileUri);
                                this.sendBroadcast(mediaScanIntent);
                                */

                            }
                        }catch (IOException e)
                        {

                        }





                        startActivityForResult(intent, PIC_CAPTURE_INTENT);
                    }




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

     @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);

         if(requestCode == PIC_CAPTURE_INTENT && resultCode == RESULT_OK )
         {
             //final Intent intent = new Intent(this,AddToDiary.class);
            // intent.putExtra("")
           //  startActivity(intent);
         }

     }

    private class MyAdapter extends ArrayAdapter<String> {


        Boolean bool = false;
        private int resource;
        private ArrayList<String>  titles;
        private ArrayList<String> img;
        private ArrayList<String> description;


        public MyAdapter(Context context, int _resource, ArrayList<String> _titles,ArrayList<String> _img, ArrayList<String> _description) {
            super(context, _resource, _titles);
            resource =_resource;
           titles = _titles;
            img = _img;
            description = _description;


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

                    try {
                        InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
                        Bitmap image = BitmapFactory.decodeStream(inputStream);
                        ImageView addImage = ((ImageView) convertView.findViewById(android.R.id.icon));
                        addImage.setImageBitmap(image);

                    }catch (FileNotFoundException e){

                        Toast toast = Toast.makeText(MainActivity.this,"Image File could not be retrieved",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();

                    }



                }

            }




                ((TextView) convertView.findViewById(android.R.id.text1))
                        .setText(titles.get(position));

            return convertView;
        }
    }
 }
