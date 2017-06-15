package com.iconuim.alc_project;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

public class PDBroadcast extends BroadcastReceiver{


    public static String SEND_INPUT_DATA_ACTION = "com.iconuim.alc_project.action.INPUT_DATA_SEND";
    public PDBroadcast() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        //Uri data = intent.getData();
        String description = intent.getStringExtra(AddToDiary.DESCRIPTION);
        String title = intent.getStringExtra(AddToDiary.TITLE);
        String imgRef = intent.getStringExtra(AddToDiary.IMAGE);

          Toast toast=  Toast.makeText(context, "Build", Toast.LENGTH_LONG);
          toast.show(); toast.setGravity(Gravity.CENTER, 0, 0);



     //   MainActivity.diaryStories.add(new DiaryStory(description, title, imgRef));
      //  MainActivity.photoDiaryAdapter.notifyDataSetChanged();





    }
}
