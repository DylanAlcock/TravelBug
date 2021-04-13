package com.app.travelbug.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.app.travelbug.R;
import com.app.travelbug.data.model.ClusterMarker;
import com.app.travelbug.data.model.LoggedInUser;
import com.app.travelbug.data.model.PlaceInfo;
import com.app.travelbug.data.model.UserFavorites;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.tabs.TabLayout;

import java.security.cert.TrustAnchor;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static final String COL1 = "id";
    private static final String COL2 = "name";

    private static final String DATABASE_NAME = "usersManager";

    private static final String TABLE_USERS = "user_table";
    private static final String TABLE_FAVORITES = "favorite_table";
    private static final String TABLE_PLACES = "place_table";


    //USER TABLE - column names
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_FNAME = "first_name";
    private static final String KEY_LNAME = "last_name";
    private static final String KEY_PASSWORD = "password";

    //FAVORITES TABLE - column names
    //private static final String KEY_USER_ID = "user_id";
    private static final String KEY_PLACE_LIST = "place_ids";

    //PLACES TABLE - column names
    private static final String KEY_PLACE_ID = "place_id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_SNIPPET = "snippet";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LNG = "lng";
    private static final String KEY_WEBSITE = "website";
    private static final String KEY_ADDRESS = "address";




    // Table Create Statements
    // User table create statement
    private static final String CREATE_TABLE_USER = "CREATE TABLE "
            + TABLE_USERS + "(" + KEY_EMAIL + " TEXT PRIMARY KEY," + KEY_USER_ID + "INTEGER," + KEY_FNAME
            + " TEXT," + KEY_LNAME + " TEXT," + KEY_PASSWORD + " TEXT)";


    // Favorite table create statement
    private static final String CREATE_TABLE_FAVORITES = "CREATE TABLE " + TABLE_FAVORITES
            + "(" + KEY_EMAIL + " TEXT PRIMARY KEY," + KEY_PLACE_ID + "INTEGER" + ")";

    // Places table create statement
    private static final String CREATE_TABLE_PLACES = "CREATE TABLE "
            + TABLE_PLACES + "(" + KEY_PLACE_ID + " INTEGER PRIMARY KEY," + KEY_TITLE
            + " TEXT," + KEY_SNIPPET+ " TEXT," + KEY_ADDRESS+ " TEXT," + KEY_WEBSITE+ " TEXT,"
            + KEY_LAT + " REAL," + KEY_LNG + " REAL)";





    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_FAVORITES);
        db.execSQL(CREATE_TABLE_PLACES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACES);

        onCreate(db);
    }


    public boolean addData(String item){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, item);

        Log.d(TAG, "addData: Adding " + item + " to " + TABLE_USERS);

        long result = db.insert(TABLE_USERS, null, contentValues);

        //check if data was inserted correctly
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS;
        Cursor data = db.rawQuery(query, null);
        return data;
    }



    //*****  ALL USER DB OPERATIONS *****//

    public long createUser(LoggedInUser user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_FNAME, user.getFirstName());
        values.put(KEY_LNAME, user.getLastName());
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_PASSWORD, user.getPassword());

        // insert row
        long user_id = db.insert(TABLE_USERS, null, values);

        // assigning tags to todo
        //for (long tag_id : tag_ids) {
        //    createTodoTag(todo_id, tag_id);
        //}

        return user_id;
    }

    public ArrayList<String> getAllUsers() {
        ArrayList<String> users = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USERS;

        Log.e(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                users.add((c.getString(c.getColumnIndex(KEY_EMAIL))));
            } while (c.moveToNext());
        }

        return users;
    }

    public LoggedInUser getUserByEmail(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USERS + " WHERE "
                + KEY_EMAIL + " = " + userEmail;

        Log.e(TAG, selectQuery);

        Cursor c = db.rawQuery("SELECT  * FROM " + TABLE_USERS + " WHERE "
                + KEY_EMAIL + " = ?", new String[]{userEmail});

        if (c != null)
            c.moveToFirst();
        else {
            return null;
        }

        LoggedInUser td = new LoggedInUser();
        td.setEmail(c.getString(c.getColumnIndex(KEY_EMAIL)));
        td.setFirstName((c.getString(c.getColumnIndex(KEY_FNAME))));
        td.setLastName((c.getString(c.getColumnIndex(KEY_LNAME))));

        return td;
    }


    /*
     * Updating a User
     */
    public int updateToDo(LoggedInUser user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, user.getUserId());
        values.put(KEY_EMAIL, user.getEmail());

        // updating row
        return db.update(TABLE_USERS, values, KEY_USER_ID + " = ?",
                new String[] { String.valueOf(user.getUserId()) });
    }


    /*
     * Deleting a user
     */
    public void deleteUser(String userEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, KEY_EMAIL + " = ?",
                new String[]{userEmail});
    }




    //*****  ALL FAVORITES DB OPERATIONS *****//

    public long createFavorite(PlaceInfo place) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_TITLE, place.getTitle());
        values.put(KEY_SNIPPET, place.getSnippet());
        values.put(KEY_ADDRESS, place.getAddress());
        values.put(KEY_WEBSITE, place.getWebsite());
        values.put(KEY_LAT, place.getLat());
        values.put(KEY_LNG, place.getLng());


        // insert row
        long user_id = db.insert(TABLE_PLACES, null, values);

        // assigning tags to todo
        //for (long tag_id : tag_ids) {
        //    createTodoTag(todo_id, tag_id);
        //}

        return user_id;
    }



    public ArrayList<Integer> getFavoriteByUserEmail(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_FAVORITES + " WHERE "
                + KEY_EMAIL + " = " + userEmail;

        Log.e(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {
            c.moveToFirst();
        } else {
            return null;
        }

        ArrayList<Integer> placeList = new ArrayList<Integer>();
        while(c.getString(c.getColumnIndex(KEY_EMAIL)) == userEmail){
            placeList.add(c.getColumnIndex(KEY_PLACE_ID));
        }

        return placeList;
    }



    // Deleting a favorite
    public void deleteFavorite(String userEmail, long place_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITES, KEY_EMAIL + " = ?" + " AND " + KEY_PLACE_ID + " = ?" ,
                new String[] {userEmail, String.valueOf(place_id) });
    }




    //*****  ALL PLACES DB OPERATIONS *****//

    public long createPlace(ClusterMarker place) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_TITLE, place.getTitle());
        values.put(KEY_SNIPPET, place.getSnippet());
        values.put(KEY_ADDRESS, "NONE");
        values.put(KEY_WEBSITE, "NONE");
        //values.put(KEY_ADDRESS, place.getAddress());
        //values.put(KEY_WEBSITE, place.getWebsite());
        values.put(KEY_LAT, place.getLat());
        values.put(KEY_LNG, place.getLng());


        // insert row
        long user_id = db.insert(TABLE_PLACES, null, values);

        // assigning tags to todo
        //for (long tag_id : tag_ids) {
        //    createTodoTag(todo_id, tag_id);
        //}

        return user_id;
    }



    public PlaceInfo getPlaceByID(long place_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_PLACES + " WHERE "
                + KEY_PLACE_ID + " = " + place_id;

        Log.e(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {
            c.moveToFirst();
        } else {
           return null;
        }

        PlaceInfo place = new PlaceInfo(1, c.getString(c.getColumnIndex(KEY_TITLE)),
                c.getString(c.getColumnIndex(KEY_SNIPPET)),
                new LatLng(c.getDouble(c.getColumnIndex(KEY_LAT)), c.getDouble(c.getColumnIndex(KEY_LNG))),
                c.getString(c.getColumnIndex(KEY_ADDRESS)),
                c.getString(c.getColumnIndex(KEY_WEBSITE)),
                "$$$");

        return place;
    }



    public ArrayList<ClusterMarker> getAllPlaces() {
        ArrayList<ClusterMarker> places = new ArrayList<ClusterMarker>();

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_PLACES;

        Log.e(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                ClusterMarker marker = new ClusterMarker(
                        new LatLng(c.getDouble(c.getColumnIndex(KEY_LAT)), c.getDouble(c.getColumnIndex(KEY_LNG))),
                        c.getString(c.getColumnIndex(KEY_TITLE)),
                        c.getString(c.getColumnIndex(KEY_SNIPPET)),
                        R.drawable.cathedral_of_toledo);
                places.add(marker);
            } while (c.moveToNext());
        }

        return places;
    }


    // Deleting a place
    public void deletePlace(long place_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PLACES, KEY_PLACE_ID + " = ?",
                new String[] { String.valueOf(place_id) });
    }



    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
