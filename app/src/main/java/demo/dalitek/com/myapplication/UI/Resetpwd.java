package demo.dalitek.com.myapplication.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import demo.dalitek.com.myapplication.Model.UserData;
import demo.dalitek.com.myapplication.Data.UserDataManager;
import demo.dalitek.com.myapplication.R;

public class Resetpwd extends AppCompatActivity {
    private EditText mAccount;                        //用户名编辑
    private EditText mPwd_old;                        //密码编辑
    private EditText mPwd_new;                        //密码编辑
    private EditText mPwdCheck;                       //密码编辑
    private UserDataManager mUserDataManager;         //用户数据管理类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resetpwd);
        getSupportActionBar().hide();
        mAccount =  findViewById(R.id.resetpwd_edit_name);
        mPwd_old =  findViewById(R.id.resetpwd_edit_pwd_old);
        mPwd_new =  findViewById(R.id.resetpwd_edit_pwd_new);
        mPwdCheck =  findViewById(R.id.resetpwd_edit_check);

        //确定按钮
        Button mSureButton =  findViewById(R.id.resetpwd_btn_sure);
        //取消按钮
        Button mCancelButton =  findViewById(R.id.resetpwd_btn_cancel);

        mSureButton.setOnClickListener(m_resetpwd_Listener);      //注册界面两个按钮的监听事件
        mCancelButton.setOnClickListener(m_resetpwd_Listener);
        if (mUserDataManager == null) {
            mUserDataManager = new UserDataManager(this);
            mUserDataManager.openDataBase();                              //建立本地数据库
        }
    }

    //不同按钮按下的监听事件选择
    View.OnClickListener m_resetpwd_Listener = v -> {
        switch (v.getId()) {
            case R.id.resetpwd_btn_sure:
                resetpwd_check();
                break;
            case R.id.resetpwd_btn_cancel:
                Intent intent_toLogin = new Intent(Resetpwd.this, MainActivity.class);
                startActivity(intent_toLogin);
                finish();
                break;
        }
    };

    public void resetpwd_check() {                                //确认按钮的监听事件
        if (isUserNameAndPwdValid()) {
            String userName = mAccount.getText().toString().trim();
            String userPwd_old = mPwd_old.getText().toString().trim();
            String userPwd_new = mPwd_new.getText().toString().trim();
            String userPwdCheck = mPwdCheck.getText().toString().trim();
            int result = mUserDataManager.findUserByNameAndPwd(userName, userPwd_old);
            if (result == 1) {                                             //返回1说明用户名和密码均正确,继续后续操作
                if (userPwd_new.equals(userPwdCheck) == false) {           //两次密码输入不一样
                    Toast.makeText(this, getString(R.string.pwd_not_the_same), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    UserData mUser = new UserData(userName, userPwd_new);
                    mUserDataManager.openDataBase();
                    boolean flag = mUserDataManager.updateUserData(mUser);
                    if (flag == false) {
                        Toast.makeText(this, getString(R.string.resetpwd_fail), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, getString(R.string.resetpwd_success), Toast.LENGTH_SHORT).show();
                        Intent intent_Register_to_Login;
                        intent_Register_to_Login = new Intent(Resetpwd.this, MainActivity.class);
                        startActivity(intent_Register_to_Login);
                        finish();
                    }
                }
            } else if (result == 0) {                                       //返回0说明用户名和密码不匹配，重新输入
                Toast.makeText(this, getString(R.string.pwd_not_fit_user), Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    public boolean isUserNameAndPwdValid() {
        String userName = mAccount.getText().toString().trim();
        //检查用户是否存在
        int count = mUserDataManager.findUserByName(userName);
        //用户不存在时返回，给出提示文字
        if (count <= 0) {
            Toast.makeText(this, getString(R.string.name_not_exist), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mAccount.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.account_empty), Toast.LENGTH_SHORT).show();
            return false;
        } else if (mPwd_old.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.pwd_empty), Toast.LENGTH_SHORT).show();
            return false;
        } else if (mPwd_new.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.pwd_new_empty), Toast.LENGTH_SHORT).show();
            return false;
        } else if (mPwdCheck.getText().toString().trim().equals("")) {
            Toast.makeText(this, getString(R.string.pwd_check_empty), Toast.LENGTH_SHORT).show();
            return false;
        }else if (mPwdCheck.getText().toString().trim().equals(mPwd_new.getText().toString().trim())) {
            Toast.makeText(this, getString(R.string.pwd_not_the_same), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}