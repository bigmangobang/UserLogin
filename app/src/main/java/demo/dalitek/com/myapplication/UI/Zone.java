package demo.dalitek.com.myapplication.UI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import demo.dalitek.com.myapplication.Adapter.ZoneAdapter;
import demo.dalitek.com.myapplication.Data.ZoneDataManager;
import demo.dalitek.com.myapplication.R;
import demo.dalitek.com.myapplication.Model.ZoneData;

public class Zone extends Activity implements View.OnClickListener {
    private TextView zoneImg, zoneName, editWord;
    private TextView back, back1;
    private TextView updata, phone;
    private EditText word;
    private ListView show_word;
    private ImageView imageView;

    private RelativeLayout layout2;
    private ScrollView layout1;
    private String nameIn;
    private ZoneDataManager zoneDataManager;
    private ZoneAdapter adapter;

    private static final String IMAGE_UNSPECIFIED = "image/*";
    private static final int NONE = 0;
    private static final int PHOTO_ZOOM = 2; // 缩放
    private static final int PHOTO_RESOULT = 3;// 结果

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone);
        updata = findViewById(R.id.insert_word);
        zoneDataManager = new ZoneDataManager(this);
        show_word = findViewById(R.id.word_lv);
        phone = findViewById(R.id.choose_phone);
        imageView = findViewById(R.id.image);
        phone.setOnClickListener(this);
        show_word.setFocusable(false);
        updata.setOnClickListener(this);
        if (zoneDataManager == null) {
            zoneDataManager = new ZoneDataManager(this);
            zoneDataManager.openDataBase();                              //建立本地数据库
        }
        showName();
        init_view();
        show_list();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.back1:
                showLayout1();
                imageView.setImageBitmap(null);
                imageView.setVisibility(View.GONE);
                break;
            case R.id.edit_word:
                showLayout2();
                break;
            case R.id.insert_word:
                insert();
                break;
            case R.id.choose_phone:
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
                startActivityForResult(intent, PHOTO_ZOOM);
                break;
        }
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == NONE)
            return;
        if (data == null)
            return;
        // 读取相册缩放图片
        if (requestCode == PHOTO_ZOOM) {
            startPhotoZoom(data.getData());
        }
        // 处理结果
        if (requestCode == PHOTO_RESOULT) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                assert photo != null;
                photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);// (0-100)压缩文件
                //此处可以把Bitmap保存到sd卡中
                imageView.setImageBitmap(photo); //把图片显示在ImageView控件上
                imageView.setVisibility(View.VISIBLE);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * 收缩图片
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 10000);
        intent.putExtra("outputY", 10000);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_RESOULT);
    }

}
