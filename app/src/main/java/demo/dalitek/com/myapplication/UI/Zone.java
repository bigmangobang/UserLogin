package demo.dalitek.com.myapplication.UI;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import demo.dalitek.com.myapplication.R;

public class Zone extends Activity {
    private TextView zoneName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone);
        zoneName = (TextView) findViewById(R.id.name_show);
        zoneName.setText(getIntent().getStringExtra("useName"));
    }
}
