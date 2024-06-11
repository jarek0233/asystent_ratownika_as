package com.example.aystent_ratownika;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;
import android.content.Intent;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    EditText emailEditText;
    EditText passwordEditText;
    button loginButton;
    Button registerButton;
    Button exitButton;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;



        });

        emailEditText = findViewById(R.id.editTextTextEmailAddress);
        passwordEditText = findViewById(R.id.editTextTextPassword);
        loginButton = findViewById(R.id.button2);
        registerButton = findViewById(R.id.button3);
        exitButton = findViewById(R.id.button4);

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (validateLogin(email, password)) {
                Toast.makeText(MainActivity.this, "Zalogowano pomyślnie", Toast.LENGTH_SHORT).show();


            } else {
                Toast.makeText(MainActivity.this, "Niepoprawne dane logowania", Toast.LENGTH_SHORT).show();
            }

        });

        registerButton.setOnClickLisener(v -> {
            // Skrypt do rejestracji
           Intent intent = new Intent(MainActivity.this, Rejestracja.class);
           start.Activity(intent);
        });

        exitButton.setOnClickListener(v -> {
            finish();
        });

    }

    private boolean validateLogin(String email, String password) {
        // Prosta walidacja: sprawdzenie, czy pola nie są pusteee
        return !email.isEmpty() && !password.isEmpty();
    }
}
