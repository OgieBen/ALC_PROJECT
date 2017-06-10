package com.iconuim.alc_project;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class PDContentProvider extends ContentProvider {
    public static final Uri CONTENT_URI = Uri.parse("content://com.iconium.pdcontentprovider/item");
    private static PDDBOpenHelper pddbOpenHelper;
    private static UriMatcher uriMatcher;
    private static final int SIMPLE_ROW = 1;
    private static final int ALL_ROWS = 2 ;
    private static  String tempTableName = PDDBOpenHelper.IMAGE_TABLE;
    private static String tempID = PDDBOpenHelper.IMAGE_ID_COLUMN;
    private static SQLiteDatabase db ;


    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("content://com.iconium.pdcontentprovider","item",ALL_ROWS);
        uriMatcher.addURI("content://com.iconium.pdcontentprovider","item/#",SIMPLE_ROW);
    }


    public PDContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
      if(db == null) {
          db = pddbOpenHelper.getWritableDatabase();
      }


        switch(uriMatcher.match(uri))
        {
            case SIMPLE_ROW:
                String rowId = uri.getPathSegments().get(1);
                if(!TextUtils.isEmpty(selection))
                    selection = tempID + "=" + rowId;
            default:break;
        }

           selection =  TextUtils.isEmpty(selection) ? "1": selection;

        int delete = db.delete(tempTableName,selection,selectionArgs);

        getContext().getContentResolver().notifyChange(uri,null);

        return delete;


    }

    @Override
    public String getType(Uri uri) {

        //return MIME type
       switch(uriMatcher.match(uri))
       {
           case SIMPLE_ROW:
               return "vnd.android.item/vnd.iconium.simplerows";
           case ALL_ROWS:
               return "vnd.android.dir/vnd.iconium.allrows";
           default: throw new IllegalArgumentException("This Uri is not supported here " + uri);
       }



    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase dbInsert = pddbOpenHelper.getWritableDatabase();

        long res = dbInsert.insert(tempTableName,null,values);

        if(res > -1)
        {
            Uri id = ContentUris.withAppendedId(CONTENT_URI,res);
            getContext().getContentResolver().notifyChange(id, null);
            return id ;
        }
        return null;
    }

    @Override
    public boolean onCreate() {


        pddbOpenHelper = new PDDBOpenHelper(getContext(),PDDBOpenHelper.DATABASE_NAME,null,PDDBOpenHelper.DATABASE_VERSION);

        try{
            db = pddbOpenHelper.getWritableDatabase();
        }catch(SQLiteException e)
        {
            db = pddbOpenHelper.getReadableDatabase();
        }


        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();



          if(uriMatcher.match(uri) == SIMPLE_ROW) {
              String rowId = uri.getPathSegments().get(1);
              sqLiteQueryBuilder.appendWhere(tempID +" = "  + rowId);

          }
        sqLiteQueryBuilder.setTables(tempTableName);

        Cursor cursor =  sqLiteQueryBuilder.query(db,projection,selection,selectionArgs,null,null,sortOrder);

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

            if (db == null)
               db = pddbOpenHelper.getWritableDatabase();


            switch(uriMatcher.match(uri))
            {
                case SIMPLE_ROW:
                    String rowId = uri.getPathSegments().get(1);
                   selection = tempID + " = " + rowId ;
                default:break;
            }

        int update = db.update(tempTableName, values, selection, selectionArgs);

        getContext().getContentResolver().notifyChange(uri,null);

        return update;
    }

    public  void setTableName(String tableName)
    {
        tempTableName = tableName;
    }

    public void setId(String id)
    {
        tempID =id;
    }

    private static void getOpenDb()
    {


    }
}
