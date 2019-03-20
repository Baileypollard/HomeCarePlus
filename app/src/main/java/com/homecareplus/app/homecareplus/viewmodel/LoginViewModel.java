package com.homecareplus.app.homecareplus.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.homecareplus.app.homecareplus.couchbase.CBSession;
import com.homecareplus.app.homecareplus.couchbase.CouchbaseRepository;
import com.homecareplus.app.homecareplus.util.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.Response;

public class LoginViewModel extends AndroidViewModel
{
    private MutableLiveData<Boolean> loginData;
    private MutableLiveData<Response> loginResponseData;

    public LoginViewModel(@NonNull Application application)
    {
        super(application);
    }

    public void init()
    {
        loginData = new MutableLiveData<>();
        loginResponseData = new MutableLiveData<>();
    }


    public void onClickLogin(final String id, final String password)
    {
        CBSession.getSessionId(id, password).subscribe(new Observer<Response>()
        {
            @Override
            public void onSubscribe(Disposable d)
            {

            }

            @Override
            public void onNext(Response response)
            {
                loginResponseData.postValue(response);

                if (response.isSuccessful())
                {
                    try
                    {
                        startDatabaseReplication(response, id, password);
                        loginData.setValue(true);
                    }
                    catch (IOException | JSONException e)
                    {
                        loginData.setValue(false);
                    }
                }
                else
                {
                    loginData.setValue(false);
                }

            }

            @Override
            public void onError(Throwable e)
            {
                loginData.setValue(false);
            }

            @Override
            public void onComplete()
            {

            }
        });
    }

    public LiveData<Boolean> getLoginData()
    {
        return loginData;
    }

    public LiveData<Response> loginResponseData()
    {
        return loginResponseData;
    }

    private void startDatabaseReplication(Response response, String id, String password) throws IOException, JSONException
    {
        JSONObject results = new JSONObject(response.body().string());
        response.body().close();

        String sessionId = results.getString("session_id");
        CouchbaseRepository.init(getApplication().getApplicationContext(), id, sessionId);

        SharedPreference.getSharedInstance(getApplication().getApplicationContext());
        SharedPreference.getSharedInstance(getApplication().getApplicationContext()).setEmployeeId(id);
        SharedPreference.getSharedInstance(getApplication().getApplicationContext()).setEmployeePassword(password);
    }

}
