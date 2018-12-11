package demo.dalitek.com.myapplication.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ZoneDataManager extends SQLiteOpenHelper {
private static final String CREATE_ZONE = "CREATE TABLE" + " zone" + " (" + " username text," + " word text" + " )";
    private static final String DB_ZONE = "db_zone";

    ZoneDataManager(Context context) {
        super(context, DB_ZONE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ZONE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void insertData(SQLiteDatabase db, ZoneData zoneData){
        String userName = zoneData.getUserName();
        String word = zoneData.getZoneWord();
        ContentValues values = new ContentValues();
        values.put("username", userName);
        values.put("zone", word);
        db.insert("zone", null, values);
    }
}
