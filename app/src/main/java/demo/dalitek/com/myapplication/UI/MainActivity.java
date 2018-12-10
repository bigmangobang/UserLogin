package demo.dalitek.com.myapplication.UI;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import demo.dalitek.com.myapplication.Data.UserDataManager;
import demo.dalitek.com.myapplication.R;

public class MainActivity extends Activity {                 //登录界面活动

    private long exitTime;

    private EditText mAccount;                        //用户名编辑
    private EditText mPwd;                            //密码编辑
    private Button mLoginButton;                      //登录按钮
    private Button mCancleButton;                     //注销按钮
    private CheckBox mRememberCheck;

    private SharedPreferences login_sp;

    private TextView mChangepwdText;
    private TextView registerText;
    private UserDataManager mUserDataManager;         //用户数据管理类


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //通过id找到相应的控件
        mAccount = (EditText) findViewById(R.id.login_edit_account);
        mPwd = (EditText) findViewById(R.id.login_edit_pwd);
        registerText = (TextView) findViewById(R.id.login_text_register);
        mLoginButton = (Button) findViewById(R.id.login_btn_login);
        mCancleButton = (Button) findViewById(R.id.login_btn_cancle);

        mChangepwdText = (TextView) findViewById(R.id.login_text_change_pwd);

        mRememberCheck = (CheckBox) findViewById(R.id.Login_Remember);

        login_sp = getSharedPreferences("userInfo", 0);
        boolean choseRemember = login_sp.getBoolean("mRememberCheck", false);
        //如果上次选了记住密码，那进入登录页面也自动勾选记住密码，并填上用户名和密码
        if (choseRemember) {
            String name = login_sp.getString("USER_NAME", "");
            String pwd = login_sp.getString("PASSWORD", "");
            mAccount.setText(name);
            mPwd.setText(pwd);
            mRememberCheck.setChecked(true);
        }
        registerText.setOnClickListener(mListener);                      //采用OnClickListener方法设置不同按钮按下之后的监听事件
        mLoginButton.setOnClickListener(mListener);
        mCancleButton.setOnClickListener(mListener);
        mChangepwdText.setOnClickListener(mListener);

        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();                              //建立本地数据库
        }
    }

    OnClickListener mListener = new OnClickListener() {                  //不同按钮按下的监听事件选择
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.login_text_register:                            //登录界面的注册按钮
                    Intent intent_Login_to_Register = new Intent(MainActivity.this, Register.class);    //切换Login Activity至Register Activity
                    startActivity(intent_Login_to_Register);
                    finish();
                    break;
                case R.id.login_btn_login:                              //登录界面的登录按钮
                    login();
                    break;
                case R.id.login_btn_cancle:                             //登录界面的注销按钮
                    cancel();
                    break;
                case R.id.login_text_change_pwd:                          //登录界面的忘记密码按钮
                    Intent intent_Login_to_reset = new Intent(MainActivity.this, Resetpwd.class);    //切换Login Activity至User Activity
                    startActivity(intent_Login_to_reset);
                    finish();
                    break;
            }
        }
    };

    public void login() {                                              //登录按钮监听事件
        if (isUserNameAndPwdValid()) {
            String userName = mAccount.getText().toString().trim();    //获取当前输入的用户名和密码信息
            String userPwd = mPwd.getText().toString().trim();
            SharedPreferences.Editor editor = login_sp.edit();
            int result = mUserDataManager.findUserByNameAndPwd(userName, userPwd);
            if (result == 1) {                                             //返回1说明用户名和密码均正确

                //是否记住密码
                if (mRememberCheck.isChecked()) {
                    //保存用户名和密码
                    editor.clear();
                    editor.putBoolean("mRememberCheck", true);
                    editor.putString("USER_NAME", userName);
                    editor.putString("PASSWORD", userPwd);

                } else {
                    editor.putBoolean("mRememberCheck", false);
                }
                editor.apply();

                Intent intent = new Intent(MainActivity.this, User.class);    //切换Login Activity至User Activity
                intent.putExtra("extra_data", userName);
                startActivity(intent);
                finish();
                Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();//登录成功提示
            } else if (result == 0) {
                Toast.makeText(this, "登录失败", Toast.LENGTH_SHORT).show();  //登录失败提示
            }
        }
    }

    public void cancel() {           //注销
        if (isUserNameAndPwdValid()) {
            String userName = mAccount.getText().toString().trim();    //获取当前输入的用户名和密码信息
            String userPwd = mPwd.getText().toString().trim();
            int result = mUserDataManager.findUserByNameAndPwd(userName, userPwd);
            if (result == 1) {                                             //返回1说明用户名和密码均正确
                Toast.makeText(this, "注销成功", Toast.LENGTH_SHORT).show();
                mAccount.setText("");
                mUserDataManager.deleteUserDatabyname(userName);
            } else if (result == 0) {
                Toast.makeText(this, "注销失败", Toast.LENGTH_SHORT).show();  //注销失败提示
            }
        }

    }

    public boolean isUserNameAndPwdValid() {
        if (mAccount.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.account_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (mPwd.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.pwd_empty),
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (mUserDataManager != null) {
            mUserDataManager.closeDataBase();
            mUserDataManager = null;
        }
        super.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            boolean acc = getIntent().getBooleanExtra("acc", false);
            if (acc) {
                finish();
            } else {
                exit();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);

    }

    private void exit() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }
}