package com.shutup.circle.controller.login;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shutup.circle.BuildConfig;
import com.shutup.circle.R;
import com.shutup.circle.common.Constants;
import com.shutup.circle.controller.BaseActivity;
import com.shutup.circle.model.persis.User;
import com.shutup.circle.model.request.RegisterUserRequest;
import com.shutup.circle.model.response.RestInfo;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity implements Constants {

    @InjectView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.userPhone)
    TextInputEditText mUserPhone;
    @InjectView(R.id.password)
    TextInputEditText mPassword;
    @InjectView(R.id.passwordConfirm)
    TextInputEditText mPasswordConfirm;
    @InjectView(R.id.registerBtn)
    Button mRegisterBtn;
    @InjectView(R.id.userPhoneInputLayout)
    TextInputLayout mUserPhoneInputLayout;
    @InjectView(R.id.passwordInputLayout)
    TextInputLayout mPasswordInputLayout;
    @InjectView(R.id.passwordConfirmInputLayout)
    TextInputLayout mPasswordConfirmInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);
        initToolBar();
        initInputLayout();
    }



    @OnClick(R.id.registerBtn)
    public void onClick() {
        if (checkUserPhone() && checkPassword() && checkPasswordConfirm()) {
            if (checkPasswordSame()) {
                String username = mUserPhone.getEditableText().toString().trim();
                String password = mPassword.getEditableText().toString().trim();

                Call<ResponseBody> call = getCircleApi().registerUser(new RegisterUserRequest(username, password));
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        Gson gson =  getGson();
                        if (BuildConfig.DEBUG)
                            Log.d("RegisterActivity", "response.body():" + response.body());
                        if (response.isSuccessful()) {
                            User user = null;
                            try {
                                user = gson.fromJson(response.body().string(),User.class);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (user != null) {
                                Toast.makeText(RegisterActivity.this, "注册用户成功！", Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(RegisterActivity.this, "注册用户失败！", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            RestInfo info = null;
                            try {
                                info = gson.fromJson(response.errorBody().string(),RestInfo.class);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (info!= null) {
                                Toast.makeText(RegisterActivity.this, info.getMsg(), Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(RegisterActivity.this, "请求异常", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        if (BuildConfig.DEBUG)
                            Log.d("RegisterActivity", "t.getCause():" + t);

                        Toast.makeText(RegisterActivity.this, "注册用户失败，稍后尝试！", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "两次输入密码不一致！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initToolBar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
        mToolbarTitle.setText(R.string.registerActivityTitle);
    }

    private void initInputLayout() {
        mUserPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkUserPhone();

            }
        });

        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkPassword();
            }
        });
        mPasswordConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkPasswordConfirm();
            }
        });
    }

    private boolean checkUserPhone() {
        CharSequence charSequence = mUserPhone.getEditableText().toString().trim();
        if (charSequence.length() > 1 && charSequence.length() <= getResources().getInteger(R.integer.phoneNumLength)) {
            mUserPhoneInputLayout.setErrorEnabled(false);
            return true;
        } else {
            mUserPhoneInputLayout.setErrorEnabled(false);
            mUserPhoneInputLayout.setError("用户名必须为2-10位!");
            return false;
        }
    }

    private boolean checkPassword() {
        CharSequence charSequence = mPassword.getEditableText().toString().trim();
        if (charSequence.length() >= 6 && charSequence.length() <= getResources().getInteger(R.integer.maxPasswordLength)) {
            mPasswordInputLayout.setErrorEnabled(false);
            return true;
        } else {
            mPasswordInputLayout.setErrorEnabled(false);
            mPasswordInputLayout.setError("密码长度必须为6~20位!");
            return false;
        }
    }

    private boolean checkPasswordConfirm() {
        CharSequence charSequence = mPasswordConfirm.getEditableText().toString().trim();
        if (charSequence.length() >= 6 && charSequence.length() <= getResources().getInteger(R.integer.maxPasswordLength)) {
            mPasswordConfirmInputLayout.setErrorEnabled(false);
            return true;
        } else {
            mPasswordConfirmInputLayout.setErrorEnabled(false);
            mPasswordConfirmInputLayout.setError("密码长度必须为6~20位!");
            return false;
        }
    }

    private boolean checkPasswordSame() {
        if (checkPassword() && checkPasswordConfirm()) {
            return mPassword.getEditableText().toString().trim().equals(mPasswordConfirm.getEditableText().toString().trim());
        } else {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }
}
