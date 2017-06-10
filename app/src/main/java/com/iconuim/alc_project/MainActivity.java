package com.iconuim.alc_project;

import android.app.ActionBar;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private static BackgroundUpdateService backgroundUpdateService;
    public static Intent intentUpdateService ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}
