package com.homecareplus.app.homecareplus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.homecareplus.app.homecareplus.R;
import com.homecareplus.app.homecareplus.callback.LoginAttemptedCallback;
import com.homecareplus.app.homecareplus.couchbase.DatabaseManager;
import com.homecareplus.app.homecareplus.util.NetworkUtil;
import com.homecareplus.app.homecareplus.util.SharedPreference;

public class LoginActivity extends AppCompatActivity
{
    private Button buttonSignIn;
    private EditText employeeIdEditText;
    private EditText employeePasswordEditText;
    private TextView loginErrorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonSignIn = (Button) findViewById(R.id.button_signin);
        employeeIdEditText = (EditText) findViewById(R.id.employeeId_edit);
        employeePasswordEditText = (EditText) findViewById(R.id.employeePassword_edit);
        loginErrorTextView = (TextView) findViewById(R.id.login_error_text);

    }

    private LoginAttemptedCallback loginAttemptedCallback = new LoginAttemptedCallback()
    {
        @Override
        public void onLoginFailed()
        {
            //Display the error
            loginErrorTextView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onLoginSuccess(String id, String sessionId)
        {
            SharedPreference.getSharedInstance(getApplicationContext()).setEmployeeId(id);
            DatabaseManager.getSharedInstance(getApplicationContext(), id);
            DatabaseManager.beginDatabaseReplication(sessionId);
            startMainActivity();
        }
    };

    private void startMainActivity()
    {
        loginErrorTextView.setVisibility(View.GONE);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClickSignIn(View view)
    {
        String id = employeeIdEditText.getText().toString().trim();
        String password =  employeePasswordEditText.getText().toString().trim();

       new NetworkUtil.getSessionId(id, password, loginAttemptedCallback).execute();
    }
}

