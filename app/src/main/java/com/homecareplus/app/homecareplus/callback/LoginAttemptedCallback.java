package com.homecareplus.app.homecareplus.callback;

public interface LoginAttemptedCallback
{
    void onLoginFailed();

    void onLoginSuccess(String id, String password, String sessionId);
}
