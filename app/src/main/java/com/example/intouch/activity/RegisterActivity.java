package com.example.intouch.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.intouch.R;
import com.example.intouch.model.AuthenticationResponse;
import com.example.intouch.model.RegisterRequest;
import com.example.intouch.service.AuthService;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button registerButton;
    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        usernameEditText = findViewById(R.id.registerUsername);
        emailEditText = findViewById(R.id.registerEmail);
        passwordEditText = findViewById(R.id.registerPassword);
        registerButton = findViewById(R.id.registerButton);
        authService = new AuthService(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                authService.register(new RegisterRequest(username, email, password), new AuthService.AuthServiceCallback() {
                    @Override
                    public void onLoginSuccess(AuthenticationResponse response) {
                        // Save the JWT token for future use
                        // You can use SharedPreferences or any other method to save the token
                        // For now, just start MainActivity
                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
                        myEdit.putString("token", response.getToken());
                        myEdit.apply();
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });

                // TODO: Implement your registration logic here
                // For example, you can call your API to register the new user

                Toast.makeText(RegisterActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }
}