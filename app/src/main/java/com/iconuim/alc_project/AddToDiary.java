package com.iconuim.alc_project;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.FileProvider;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.PendingIntent.getActivities;
import static android.app.PendingIntent.getActivity;

public class AddToDiary extends AppCompatActivity  {

    public static final String DESCRIPTION = "description";
    public static final String TITLE = "title";
    public static final String IMAGE = "image_url";
    private static final int PICK_IMAGE = 1;
    private static final int PIC_CAPTURE_INTENT = 300;
    private static final int SAVE_FILE = 400;
    public static ContentValues CONTENT_VALUES ;
    public static String asyncString ="None";
    private static Bitmap selectedImage;
    private static ImageView addImageView;
    private static EditText title;
    private static EditText description;
    private static Button addButton;
    private static String imageRef;
    private static PDContentProvider pdContentProvider = new PDContentProvider();
   // private static String [] TABLE_COLUMS = {}
    private static ContentValues contentValues = new ContentValues();
   private static File imageFile;
    private static  String path;
    SoundPool   soundPool;
    int notification;

    public AddToDiary() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_diary);


         addImageView = (ImageView) findViewById(R.id.add_icon);
        title = (EditText)findViewById(R.id.titleEntry);
        description = (EditText) findViewById(R.id.description);

        addButton = (Button) findViewById(R.id.addButton);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                    .build();

            soundPool = new SoundPool.Builder()
                                .setAudioAttributes(audioAttributes)
                                .setMaxStreams(1)
                                .build();
            notification  = soundPool.load(AddToDiary.this, R.raw.arpeggio, 1);
                                soundPool.play(notification,1,1,0,0,1);

        }else{
            soundPool = new SoundPool(1, AudioManager.STREAM_NOTIFICATION,1);
            notification  = soundPool.load(this,R.raw.arpeggio,1);
        }

        addImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                if(galleryIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(galleryIntent, PICK_IMAGE);
                }
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
                    contentValues.put(PDDBOpenHelper.DESCRIPTION_COLUMN, descriptionText);
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

                //    TextView text = (TextView) findViewById(R.id.testView); //for debug purposes
                     AsyncUpdate asyncUpdate = new AsyncUpdate();
                     asyncUpdate.execute(contentValues);

                    soundPool.play(notification, 1, 1, 0, 0, 1);

                  //  Log.e("async ", asyncString);

                    Toast.makeText(AddToDiary.this, "Memory Added to Diary", Toast.LENGTH_LONG).show();

                    // sendBroadcast(resIntent);
                    // sendBroadcast(resIntent);
                    // MainActivity.diaryStories.add(new DiaryStory(descriptionText, titleText, imageRef));
                   //  MainActivity.photoDiaryAdapter.notifyDataSetChanged();

                       finish();
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

        switch(id)
        {
            case  R.id.action_settings:
                return true;
            case R.id.cam:


                // if(hasSystemFeature(PackageManager.FEATURE_CAMERA))
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(intent.resolveActivity(getPackageManager())!= null) {

                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String imageFileName = "JPEG_" + timeStamp + "_";
                    try {
                        File storage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                        imageFile = File.createTempFile(imageFileName, ".jpg", storage);
                         path = imageFile.getAbsolutePath();
                      //  Log.e(" ", "++++++" + path);


                    }catch (IOException e)
                    {

                    }


                    if(imageFile != null) {
                        Uri imageUri = FileProvider.getUriForFile(this, "com.iconuim.alc_project.fileprovider", imageFile);
                        Log.e(""," -----"+imageUri);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        //  startActivityForResult(intent, SAVE_FILE);

                              /*  Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                File openImagefile = new File(path);
                                Uri imageFileUri = Uri.fromFile(openImagefile);
                                mediaScanIntent.setData(imageFileUri);
                                this.sendBroadcast(mediaScanIntent);
                                */

                    }
                    startActivityForResult(intent, PIC_CAPTURE_INTENT);

                }

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                      Uri dataUri;
                  /*  if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){

                        dataUri = (Uri) data.getParcelableExtra(Intent.EXTRA_INTENT);

                    }else {
                        dataUri = data.getData();
                    } */


                   // dataUri = (Uri) data.getParcelableExtra(Intent.EXTRA_STREAM);
                       dataUri = data.getData();
                     if( dataUri != null) {
                         //
                         String[] filePathColumn = {MediaStore.Images.Media.DATA};

                         Cursor cursor = getContentResolver().query(dataUri, filePathColumn, null, null, null);
                         imageRef = dataUri.toString();
                         cursor.moveToFirst();

                         int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                         String filePath = cursor.getString(columnIndex);
                         cursor.close();


                         BitmapFactory.Options opt = new BitmapFactory.Options();
                       //  opt.inScreenDensity = 60;
                         //  int picWidth = opt.outWidth;
                         // int picHeight = opt.outHeight;
                         //int scaleFactor = Math.min(picWidth / targetW, picHeight / targetH);
                         // opt.inSampleSize = scaleFactor;
                         opt.inJustDecodeBounds= false;

                         // bm.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                         try {
                             InputStream input = this.getContentResolver().openInputStream(dataUri);
                             selectedImage = BitmapFactory.decodeStream(input, null, opt);
                         }catch(FileNotFoundException e)
                         {
                             Toast toast = Toast.makeText(AddToDiary.this,"Image File could not be retrieved",Toast.LENGTH_SHORT);
                             toast.setGravity(Gravity.CENTER,0,0);
                             toast.show();
                         }



                         //int x  = addImage.getMeasuredWidth();
                         // int y  = addImage.getMeasuredHeight();
                         //   Log.e("width", "" + x);
                         // Log.e("  ", ""+y);
                         // selectedImage = Bitmap.createScaledBitmap(selectedImage, addImage., y, true);
                         //selectedImage =
                         if(dataUri != null) {
                             addImageView.setImageBitmap(selectedImage);

                         }

                     }else{
                         Toast toast = Toast.makeText(AddToDiary.this,"System Error Please try again",Toast.LENGTH_SHORT);
                         toast.setGravity(Gravity.CENTER,0,0);
                         toast.show();
                     }

                } else{
            Toast toast = Toast.makeText(AddToDiary.this,"Please Pick an Image file",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
                break;
            case PIC_CAPTURE_INTENT:


                if (resultCode == RESULT_OK) {
                   String dat = data.getStringExtra(MediaStore.EXTRA_OUTPUT);
                    if (path == null) {
                        Log.e("","Path: "+ dat);
                        return;
                    }
                    Log.e("","Path: "+ path);
                    int targetW = addImageView.getWidth();
                    int targetH = addImageView.getHeight();

                    // Get the dimensions of the bitmap
                    BitmapFactory.Options Opt = new BitmapFactory.Options();
                    Opt.inScreenDensity = 60;
                    BitmapFactory.decodeFile(path, Opt);
                    int picWidth = Opt.outWidth;
                    int picHeight = Opt.outHeight;

                    // Scale image
                   // int scaleFactor = Math.min(picWidth / targetW, picHeight / targetH);

                    // Decode the image file into a Bitmap sized to fill the View
                   // bmOptions.inJustDecodeBounds = false;
                   // bmOptions.inSampleSize = scaleFactor;


                    Bitmap bitmap = BitmapFactory.decodeFile(path, Opt);
                    addImageView.setImageBitmap(bitmap);


                }
                break;
        }

    }




}
