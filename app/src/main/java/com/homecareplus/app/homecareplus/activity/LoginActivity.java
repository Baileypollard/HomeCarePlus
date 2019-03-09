package com.homecareplus.app.homecareplus.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.homecareplus.app.homecareplus.R;
import com.homecareplus.app.homecareplus.callback.LoginAttemptedCallback;
import com.homecareplus.app.homecareplus.couchbase.CBSession;
import com.homecareplus.app.homecareplus.couchbase.CouchbaseRepository;
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

        buttonSignIn = findViewById(R.id.button_signin);
        employeeIdEditText = findViewById(R.id.employeeId_edit);
        employeePasswordEditText = findViewById(R.id.employeePassword_edit);
        loginErrorTextView = findViewById(R.id.login_error_text);

    }

    private LoginAttemptedCallback loginAttemptedCallback = new LoginAttemptedCallback()
    {
        @Override
        public void onLoginFailed()
        {
            //Display the error
            buttonSignIn.setEnabled(true);
            loginErrorTextView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onLoginSuccess(String id, String password, String sessionId)
        {
            SharedPreference.getSharedInstance(getApplicationContext()).setEmployeeId(id);
            SharedPreference.getSharedInstance(getApplicationContext()).setEmployeePassword(password);
            CouchbaseRepository.init(getApplicationContext(), id, sessionId);
            startMainActivity();
        }
    };

    private void startMainActivity()
    {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClickSignIn(View view)
    {
        String id = employeeIdEditText.getText().toString().trim();
        String password = employeePasswordEditText.getText().toString().trim();
        CBSession.getSessionId sessionIdTask =  new CBSession.getSessionId(id, password, loginAttemptedCallback);
        sessionIdTask.execute();

        //Set false to prevent multiple sign in async tasks from executing
        buttonSignIn.setEnabled(false);
    }
}

