package com.iconuim.alc_project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ogie on 6/6/2017.
 */
public class PDDBOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "PHOTO_DIARY.db";
    public static final int DATABASE_VERSION = 1;

    public static final String IMAGE_TABLE ="ImageTable";
    public static final String TAGGED_PERSONS_TABLE = "TaggedPerson";

    private static final String DROP_DATABASE = "DROP IF IT EXISTS "+ IMAGE_TABLE;

    public static final String IMAGE_ID_COLUMN ="_id";
    public static final String TITLE_COLUMN = "TITLE";
    public static final String DESCRIPTION_COLUMN ="DESCRIPTION";
    public static final String IMAGE_PATH_REF_COLUMN ="IMAGE_PATH_REF";


    public static final String TAGGED_PERSONS_ID = "_id";
    public static final String FIRST_NAME_COLUMN ="FIRST_NAME";
    public static final String LAST_NAME_COLUMN = "LAST_NAME";
    public static final String DATA_COLUMN = "DATA";
    public static final String DATA_X_POSITION_COLUMN = "DATA_X_POSITION";
    public static final String DATA_Y_POSITION_COLUMN = "DATA_Y_POSITION";
    public static final String SUB_WINDOW_SIZE_COLUMN = "SUB_WINDOW_SIZE";

    private static final String CREATE_IMAGE_TABLE =
            "create table " + IMAGE_TABLE + " ("
            + IMAGE_ID_COLUMN + " integer primary key autoincrement, "
            + TITLE_COLUMN + "text not null, "
            + DESCRIPTION_COLUMN + "text not null,  "
            + IMAGE_PATH_REF_COLUMN + "text not null ); ";


    //TODO: (Technical)Table should be normalized further for data training aggregation

    private static final String CREATE_TAGGED_PERSON_TABLE =
            "create table "+ TAGGED_PERSONS_TABLE + " ("
            + TAGGED_PERSONS_ID + "integer primary key autoincrement, "
            + FIRST_NAME_COLUMN + "text not null, "
            + LAST_NAME_COLUMN + "text not null, "
            + DATA_COLUMN + "text "
            + DATA_X_POSITION_COLUMN + "integer, "
            + DATA_Y_POSITION_COLUMN + "integer,  "
            + SUB_WINDOW_SIZE_COLUMN + "integer);";


    public PDDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
          db.execSQL(CREATE_IMAGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
     //TODO: upgrade to newer db by coping data to new :::never drop database without copying
     // db.execSQL(DROP_DATABASE);
        //onCreate(db);
    }




}
