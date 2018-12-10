package demo.dalitek.com.myapplication.UI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import demo.dalitek.com.myapplication.Data.UserData;
import demo.dalitek.com.myapplication.Data.UserDataManager;
import demo.dalitek.com.myapplication.R;
import demo.dalitek.com.myapplication.Adapter.MyAdapter;

import static demo.dalitek.com.myapplication.Data.UserDataManager.USER_NAME;

public class User extends AppCompatActivity{
    private Button mReturnButton;
    private TextView text;
    private ListView user_show;
    private UserDataManager mUserDataManager;
    private SQLiteDatabase mSQLiteDatabase;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);
        getSupportActionBar().hide();
        text = (TextView) findViewById(R.id.getname);
        mReturnButton = (Button) findViewById(R.id.returnback);
        user_show = (ListView) findViewById(R.id.user_lv);
        String in = getIntent().getStringExtra("extra_data");
        text.setText(in);
        mReturnButton.setOnClickListener((v)-> {
            Intent intent = new Intent(User.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
        List<UserData> list = Arrays.asList(
                new UserData( "今天是下雨天", "$3000"),
                new UserData("ccdd", "$3000"),
                new UserData( "aaa", "$3000")
        );
        MyAdapter adapter = new MyAdapter(
                this, R.layout.user_item, list
        );
        user_show.setAdapter(adapter);
    }
    public ArrayList<UserData> findAllUserName(){
        ArrayList<UserData> items = new ArrayList<>();
        Cursor cursor = mSQLiteDatabase.query( "users", null, USER_NAME + "=" + "?",
                new String[]{"江帆"}, null, null, null);
        while(cursor.moveToNext()){
            String name = cursor.getString(1);
            items.add(new UserData(name));
        }

        return items;
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}