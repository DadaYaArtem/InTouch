package com.example.intouch.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.intouch.R;
import com.example.intouch.model.AuthenticationRequest;
import com.example.intouch.model.AuthenticationResponse;
import com.example.intouch.service.AuthService;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.remove("token");
        myEdit.apply();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        authService = new AuthService(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
                } else {
                    AuthenticationRequest request = new AuthenticationRequest();
                    request.setUsername(username);
                    request.setPassword(password);

                    authService.login(request, new AuthService.AuthServiceCallback() {
                        @Override
                        public void onLoginSuccess(AuthenticationResponse response) {
                            // Save the JWT token for future use
                            // You can use SharedPreferences or any other method to save the token
                            // For now, just start MainActivity
                            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                            SharedPreferences.Editor myEdit = sharedPreferences.edit();
                            myEdit.putString("token", response.getToken());
                            myEdit.apply();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();  // Close LoginActivity
                            Toast.makeText(LoginActivity.this, "DONE: ", Toast.LENGTH_SHORT).show();
                            System.out.println(response.getToken());
                        }

                        @Override
                        public void onError(String errorMessage) {
                            // Show an error message
                            Toast.makeText(LoginActivity.this, "Login failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}