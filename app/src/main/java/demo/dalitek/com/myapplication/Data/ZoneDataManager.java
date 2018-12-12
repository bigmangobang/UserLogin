package demo.dalitek.com.myapplication.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import demo.dalitek.com.myapplication.Util.UserData;
import demo.dalitek.com.myapplication.Util.ZoneData;

import static android.content.ContentValues.TAG;

public class ZoneDataManager {

    private Context mContext = null;
    private static final int DB_VERSION = 2;
    private static final String CREATE_ZONE = "CREATE TABLE" + " zone" + " (" + " username text," + " word text" + " )";
    private static final String DB_ZONE = "db_zone";

    private SQLiteDatabase mSQLiteDatabase = null;
    private DataBaseManagementHelper mDatabaseHelper = null;

    //DataBaseManagementHelper继承自SQLiteOpenHelper
    public static class DataBaseManagementHelper extends SQLiteOpenHelper {

        DataBaseManagementHelper(Context context) {
            super(context, DB_ZONE, null, DB_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_ZONE);
            ContentValues contentValues = new ContentValues();
            contentValues.put("username","杜甫");
            contentValues.put("word","安得广厦千万间，大批天下寒士俱欢颜");
            db.insert("zone","",contentValues);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onCreate(db);
        }
    }

    //打开数据库
    public void openDataBase() throws SQLException {
        mDatabaseHelper = new DataBaseManagementHelper(mContext);
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
    }

    //关闭数据库
    public void closeDataBase() throws SQLException {
        mDatabaseHelper.close();
    }

    public ZoneDataManager(Context context) {
        this.mContext = context;
        Log.i(TAG, "UserDataManager construction!");
    }
//增加朋友圈数据
    public void insertData(ZoneData zoneData) {
        String userName = zoneData.getUserName();
        String word = zoneData.getZoneWord();
        ContentValues values = new ContentValues();
        values.put("username", userName);
        values.put("word", word);
        mSQLiteDatabase.insert("zone", null, values);
    }

    //返回所有用户名列表
    public List<ZoneData> getAllUserWord() {
        String sql = "select *  from  zone";
        List<ZoneData> items = new ArrayList<>();
        try{
            openDataBase();
        }catch (Exception e){
            e.printStackTrace();
        }
        Cursor cursor = mSQLiteDatabase.rawQuery(sql,null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("username"));
            String word = cursor.getString(cursor.getColumnIndex("word"));
            String image = (String) name.subSequence(0,1);
            items.add(new ZoneData(name, word,image ));
        }
        cursor.close();
        return items;
    }
}
