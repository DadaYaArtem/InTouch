package com.example.intouch.api;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.intouch.model.AuthenticationRequest;
import com.example.intouch.model.AuthenticationResponse;
import com.example.intouch.model.ToDoEntity;

import java.util.List;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthApiClient {
    private static final String BASE_URL = "http://10.0.2.2:8080/api/";
    private OkHttpClient client;
    private MyApiService apiService;
    private Context context;

    public AuthApiClient(Context context) {
        this.context = context;

        client = new OkHttpClient().newBuilder()
                .addInterceptor(new TokenInterceptor(this.context))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        apiService = retrofit.create(MyApiService.class);
    }

    public interface AuthApiCallback {
        void onAuthApiResponse(Object response);
        void onAuthApiError(String errorMessage);
    }

    private void saveUserId(long userId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("userId", userId);
        editor.apply();
    }

    public void login(AuthenticationRequest request, AuthApiCallback callback) {
        Call<AuthenticationResponse> call = apiService.login(request);

        call.enqueue(new Callback<AuthenticationResponse>() {
            @Override
            public void onResponse(Call<AuthenticationResponse> call, Response<AuthenticationResponse> response) {
                if (response.isSuccessful()) {
                    callback.onAuthApiResponse(response.body());

                    long userId = response.body().getUserId();
                    saveUserId(userId);

                    System.out.println("response code apiclient: " + response.code() );
                    System.out.println("userId apiclient: " + response.body().getUserId());

                } else {
                    callback.onAuthApiError("Failed to login. Server returned " + response.code());
                }
            }

            @Override
            public void onFailure(Call<AuthenticationResponse> call, Throwable t) {
                t.printStackTrace();
                callback.onAuthApiError("Network error: " + t.getMessage());
            }
        });
    }

}