package com.homecareplus.app.homecareplus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.homecareplus.app.homecareplus.R;
import com.homecareplus.app.homecareplus.couchbase.CBSession;
import com.homecareplus.app.homecareplus.util.SharedPreference;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

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

    private void startMainActivity()
    {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClickSignIn(View view)
    {
        final String id = employeeIdEditText.getText().toString().trim();
        final String password = employeePasswordEditText.getText().toString().trim();
        CBSession.getSessionId(id, password, getApplicationContext()).subscribe(new Observer<String>()
        {
            @Override
            public void onSubscribe(Disposable d)
            {

            }

            @Override
            public void onNext(String sessionId)
            {
                SharedPreference.init(getApplicationContext());

                SharedPreference.getSharedInstance().setEmployeeId(id);
                SharedPreference.getSharedInstance().setEmployeePassword(password);

                startMainActivity();
            }

            @Override
            public void onError(Throwable e)
            {
                Log.d("TAG", "Error: " + e.getLocalizedMessage());
                buttonSignIn.setEnabled(true);
                loginErrorTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onComplete()
            {

            }
        });
        //Set false to prevent multiple sign in async tasks from executing
        buttonSignIn.setEnabled(false);
    }
}

