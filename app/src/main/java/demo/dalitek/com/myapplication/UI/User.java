package demo.dalitek.com.myapplication.UI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.time.Instant;

import demo.dalitek.com.myapplication.Data.UserDataManager;
import demo.dalitek.com.myapplication.R;
import demo.dalitek.com.myapplication.Adapter.MyAdapter;

import static demo.dalitek.com.myapplication.Data.UserDataManager.USER_NAME;


public class User extends Activity implements AdapterView.OnItemClickListener {
    private Button mReturnButton;
    private TextView text;
    private ListView user_show;
    private UserDataManager userDataManager;
    private String in;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);
        userDataManager = new UserDataManager(this);
        text = findViewById(R.id.getname);
        mReturnButton = findViewById(R.id.returnback);
        user_show = findViewById(R.id.user_lv);
        showName();

        mReturnButton.setOnClickListener((v) -> {
            Intent intent = new Intent(User.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
        show();
        user_show.setOnItemClickListener(this);
    }

    private void showName() {
        if (text.getText().equals("")) {
            in = getIntent().getStringExtra("extra_data");
            text.setText(in);
            return;
        }
        else return;
    }

    public void show() {
        MyAdapter adapter = new MyAdapter(
                this, R.layout.user_item, userDataManager.getAllUserName()
        );

        user_show.setAdapter(adapter);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView textView = findViewById(R.id.user_name);
        String name = textView.getText().toString();
        in = getIntent().getStringExtra("extra_data");
        Intent intent = new Intent(User.this, Zone.class);
        intent.putExtra("userName", in);
        startActivity(intent);
    }
}