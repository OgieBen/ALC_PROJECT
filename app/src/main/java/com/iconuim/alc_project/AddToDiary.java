package com.iconuim.alc_project;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import static android.app.PendingIntent.getActivities;
import static android.app.PendingIntent.getActivity;

public class AddToDiary extends AppCompatActivity  {

    private static final int PICK_IMAGE = 1;
    private static Bitmap selectedImage;
    private static ImageView addImage;
    private static EditText title;
    private static EditText description;
    private static Button addButton;
    public static ContentValues CONTENT_VALUES ;

    private static String imageRef;

    private static PDContentProvider pdContentProvider = new PDContentProvider();

   // private static String [] TABLE_COLUMS = {}
    private static ContentValues contentValues = new ContentValues();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_diary);


         addImage = (ImageView) findViewById(R.id.add_icon);
        title = (EditText)findViewById(R.id.titleEntry);
        description = (EditText) findViewById(R.id.description);

        addButton = (Button) findViewById(R.id.addButton);


        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, PICK_IMAGE);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleText = title.getText().toString();
                String descriptionText = description.getText().toString();
                int trck =0;

                if(!TextUtils.isEmpty(titleText)) {
                    contentValues.put(PDDBOpenHelper.TITLE_COLUMN, titleText);
                    trck++;
                }else {
                    Toast toast = Toast.makeText(getApplication(), "Title cannot be Empty", Toast.LENGTH_LONG);
                    toast.show();
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    return;
                }


                if(!TextUtils.isEmpty(descriptionText)) {
                    contentValues.put(PDDBOpenHelper.TITLE_COLUMN, descriptionText);
                    trck++;
                }else {
                    Toast toast = Toast.makeText(getApplication(), "Description cannot be Empty", Toast.LENGTH_LONG);
                    toast.show();
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    return;
                }


                if(imageRef != null) {
                    contentValues.put(PDDBOpenHelper.IMAGE_PATH_REF_COLUMN, imageRef);
                    trck++;
                }else{
                 Toast toast=  Toast.makeText(getApplication(), "No Image selected", Toast.LENGTH_LONG);

                    toast.show();toast.setGravity(Gravity.CENTER,0,0);
                    return;
                }


                if(trck == 3)
                {
                 ///   CONTENT_VALUES = contentValues;
                     AsyncUpdate asyncUpdate =new AsyncUpdate();
                    asyncUpdate.execute(contentValues);

                }


            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_to_diary, menu);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case PICK_IMAGE :
                if (resultCode == RESULT_OK) {
                    Uri dataUri = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(dataUri, filePathColumn, null, null, null);
                    imageRef = dataUri.toString();
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();

                    BitmapFactory.Options opt = new BitmapFactory.Options();
                    opt.inScreenDensity = 60;





                   // bm.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                    selectedImage = BitmapFactory.decodeFile(filePath, opt);
                    //int x  = addImage.getMeasuredWidth();
                   // int y  = addImage.getMeasuredHeight();
                 //   Log.e("width", "" + x);
                   // Log.e("  ", ""+y);
                   // selectedImage = Bitmap.createScaledBitmap(selectedImage, addImage., y, true);
                          //selectedImage =
                     addImage.setImageBitmap(selectedImage);

                }

        }


    }


    public AddToDiary() {
        super();
    }


}
