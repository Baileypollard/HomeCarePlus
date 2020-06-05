package com.homecareplus.app.homecareplus;

import static org.mockito.Mockito.*;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.Build;

import com.homecareplus.app.homecareplus.couchbase.CBSession;
import com.homecareplus.app.homecareplus.enumerator.LoginStatus;
import com.homecareplus.app.homecareplus.viewmodel.LoginViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.MockUtil;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import okhttp3.Response;

@Config(sdk = Build.VERSION_CODES.P, application = Application.class)
@RunWith(PowerMockRunner.class)
@PrepareForTest({CBSession.class, Response.class})
public class LoginViewModelTest {

    private LoginViewModel viewModel;
    private LiveData<LoginStatus> loginData;
    private LiveData<Response> loginResponseData;
    private Application application;


    private String testUser = "TEST";
    private String testUserPassword = "TEST_PASS";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        application = RuntimeEnvironment.application;
        viewModel = new LoginViewModel(application);

        loginData = viewModel.getLoginData();
        loginResponseData = viewModel.loginResponseData();
    }



    @Test
    public void testLogin() {

//        when(CBSession.getSessionId(testUser, testUserPassword)).thenReturn(Observable.just(response));

//        viewModel.onClickLogin(testUser, testUserPassword);
    }
}
