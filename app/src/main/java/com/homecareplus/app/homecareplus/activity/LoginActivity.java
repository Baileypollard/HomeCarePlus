package com.homecareplus.app.homecareplus.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.homecareplus.app.homecareplus.R;
import com.homecareplus.app.homecareplus.viewmodel.LoginViewModel;

import okhttp3.Response;

public class LoginActivity extends AppCompatActivity
{
    private Button buttonSignIn;
    private EditText employeeIdEditText;
    private EditText employeePasswordEditText;
    private TextView loginErrorTextView;
    private LoginViewModel viewModel;
    private Response response;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonSignIn = findViewById(R.id.button_signin);
        employeeIdEditText = findViewById(R.id.employeeId_edit);
        employeePasswordEditText = findViewById(R.id.employeePassword_edit);
        loginErrorTextView = findViewById(R.id.login_error_text);

        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        viewModel.init();

        viewModel.getLoginData().observe(this, new Observer<Boolean>()
        {
            @Override
            public void onChanged(Boolean login)
            {
                if (login)
                {
                    startMainActivity();
                }
                else
                {
                    configureErrorMessage(response.code());
                }
            }
        });

        viewModel.loginResponseData().observe(this, new Observer<Response>()
        {
            @Override
            public void onChanged(Response loginResponse)
            {
                response = loginResponse;
            }
        });
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

        viewModel.onClickLogin(id, password);
        buttonSignIn.setEnabled(false);
    }

    private void configureErrorMessage(int code)
    {
        String errorMessage;
        switch (code)
        {
            case 400:
                errorMessage = "Error logging in, Bad request";
                break;
            case 401:
                errorMessage = "Invalid username or password";
                break;
            case 403:
                errorMessage = "You do not have the necessary permissions";
                break;
            case 404:
                errorMessage = "Request resource not found";
                break;
            case 408:
                errorMessage = "Request timed out, please check your connection";
                break;
             default:
                 errorMessage = "There has been an error with your login";
                 break;
        }
        buttonSignIn.setEnabled(true);
        loginErrorTextView.setText(errorMessage);
        loginErrorTextView.setVisibility(View.VISIBLE);
    }
}

