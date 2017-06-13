package com.iconuim.alc_project;

import android.app.ActionBar;
import android.app.IntentService;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks
{

    private static BackgroundUpdateService backgroundUpdateService;
    public static Intent intentUpdateService ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        getLoaderManager().initLoader(0,null,this);


        ActionBar bar = getActionBar();
        if(bar != null)
            bar.setDisplayShowHomeEnabled(false);

        ImageView add = (ImageView) findViewById(R.id.add_icon);
        PackageManager man = getPackageManager();
        final Intent it = new Intent(this,AddToDiary.class);

       // intentUpdateService = new Intent(this,BackgroundUpdateService.class);

       // backgroundUpdateService = new BackgroundUpdateService();


         //BackgroundUpdateService.startActionOpen(this);
      //  backgroundUpdateService.s


        // check if there is any activity that could resolve this intent
        boolean resolve = it.resolveActivity(man) != null ;
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(it);
                //refresh activity usung the new uri returned
            }
        });



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
    public Loader onCreateLoader(int id, Bundle args) {

        CursorLoader loader = new CursorLoader(this, PDContentProvider.CONTENT_URI,null,null,null,null);


        return loader;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

       //adapterBind should be done here
        // for every cursor returned call newStory(description,title,imageUri)


        //get db column
      //  int keyTaskIndex = cursor.getColumnIndexOrThrow(ToDoContentProvider.KEY_TASK);

        //clear refrence to prevoius array object
        //todoItems.clear();

        //iterate through the cursor
      /*  while (cursor.moveToNext()) {
            ToDoItem newItem = new ToDoItem(cursor.getString(keyTaskIndex));
            todoItems.add(newItem);
        }

        */

        //notify adapter about change


    }

    @Override
    public void onLoaderReset(Loader loader) {

    }


    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(0,null, this);
    }
}
