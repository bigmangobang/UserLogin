package demo.dalitek.com.myapplication.UI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import demo.dalitek.com.myapplication.Adapter.ZoneAdapter;
import demo.dalitek.com.myapplication.Data.ZoneDataManager;
import demo.dalitek.com.myapplication.R;
import demo.dalitek.com.myapplication.Util.ZoneData;

public class Zone extends Activity implements View.OnClickListener {
    private TextView zoneImg, zoneName, editWord;
    private TextView back, back1;
    private TextView updata;
    private EditText word;
    private ListView show_word;

    private RelativeLayout layout2;
    private ScrollView layout1;
    private String nameIn;
    private ZoneDataManager zoneDataManager;
    private ZoneAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone);
        updata = findViewById(R.id.insert_word);
        zoneDataManager = new ZoneDataManager(this);
        show_word = findViewById(R.id.word_lv);
        show_word.setFocusable(false);
        updata.setOnClickListener(this);
        showName();
        init_view();

        if (zoneDataManager == null) {
            zoneDataManager = new ZoneDataManager(this);
            zoneDataManager.openDataBase();                              //建立本地数据库
        }

        show_list();

    }

    private void show_list() {
        adapter = new ZoneAdapter(
                this, R.layout.zone_item, zoneDataManager.getAllUserWord()
        );
        show_word.setAdapter(adapter);
        setListViewHeightBasedOnChildren(show_word);
        autoFlash();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ZoneAdapter listAdapter = (ZoneAdapter) listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    //自动刷新listView中的数据
    private void autoFlash() {
        Thread thread = new Thread(new Runnable() {
            Boolean isopen = false;

            public void run() {
                while (isopen) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
        thread.start();
    }

    //显示头像和用户名
    private void showName() {
        zoneImg = findViewById(R.id.user_image);
        zoneName = (TextView) findViewById(R.id.name_show);
        nameIn = getIntent().getStringExtra("userName");
        zoneName.setText(nameIn);
        zoneImg.setText(nameIn.subSequence(0, 1));
    }

    //初始化界面
    @SuppressLint("WrongViewCast")
    private void init_view() {
        layout1 = (ScrollView) findViewById(R.id.word_show);
        layout2 = (RelativeLayout) findViewById(R.id.ly_2);
        back = (TextView) findViewById(R.id.back);
        back1 = (TextView) findViewById(R.id.back1);
        editWord = (TextView) findViewById(R.id.edit_word);
        showLayout1();
        back.setOnClickListener(this);
        back1.setOnClickListener(this);
        editWord.setOnClickListener(this);
    }

    private void showLayout1() {
        // TODO Auto-generated method stub
        layout1.setVisibility(View.VISIBLE);
        layout2.setVisibility(View.GONE);
    }

    private void showLayout2() {
        // TODO Auto-generated method stub
        layout1.setVisibility(View.GONE);
        layout2.setVisibility(View.VISIBLE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.back1:
                showLayout1();
                break;
            case R.id.edit_word:
                showLayout2();
                break;
            case R.id.insert_word:
                insert();
                break;
        }
    }

    private void insert() {
        word = findViewById(R.id.wenzi);
        String word1 = word.getText().toString();
        nameIn = getIntent().getStringExtra("userName");
        if (word.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.word_empty), Toast.LENGTH_SHORT).show();
            return;
        }
        ZoneData zoneData = new ZoneData(nameIn, word1);
        zoneDataManager.openDataBase();
        zoneDataManager.insertData(zoneData);
        word.setText("");
        Toast.makeText(this, getString(R.string.word_success), Toast.LENGTH_SHORT).show();
        showLayout1();
    }

}
