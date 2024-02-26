package com.example.intouch.service;

import android.content.Context;

import com.example.intouch.api.AuthApiClient;
import com.example.intouch.model.AuthenticationRequest;
import com.example.intouch.model.AuthenticationResponse;

import java.util.concurrent.Executors;

public class AuthService {
    private AuthApiClient authApiClient;

    public AuthService(Context context) {
        authApiClient = new AuthApiClient(context);
    }

    public interface AuthServiceCallback {
        void onLoginSuccess(AuthenticationResponse response);
        void onError(String errorMessage);
    }

    public void login(AuthenticationRequest request, AuthServiceCallback callback) {
        Executors.newSingleThreadExecutor().execute(() -> {
            authApiClient.login(request, new AuthApiClient.AuthApiCallback() {

                @Override
                public void onAuthApiResponse(Object response) {
                    callback.onLoginSuccess((AuthenticationResponse) response);
                }

                @Override
                public void onAuthApiError(String errorMessage) {
                    callback.onError(errorMessage);
                }
            });
        });
    }
}