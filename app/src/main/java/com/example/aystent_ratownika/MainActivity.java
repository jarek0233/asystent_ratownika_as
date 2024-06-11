package com.example.aystent_ratownika;

import android.content.Intent;
import android.os.AsyncTask;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MainActivity extends AppCompatActivity {
    EditText emailEditText;
    EditText passwordEditText;
    Button loginButton;
    Button registerButton;
    Button exitButton;
    private static final String DB_URL = "jdbc:sqlserver://51.75.53.42:1433;databaseName=login";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "QlBnFa2020##";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EdgeToEdge.enable(this);

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

            new ValidateLoginTask().execute(email, password);
        });


        registerButton.setOnClickListener(v -> {
            // Startuj aktywność rejestracji
            Intent intent = new Intent(MainActivity.this, rejestracja.class);
            startActivity(intent);
        });

        exitButton.setOnClickListener(v -> {
            // Zamknij  aktualną aktywność
            finish();
        });
    }
private class ValidateLoginTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            // Sprawdź dostarczone parametry (e-Mail i Hasło)

            String email = params[0];
            String password = params[1];

            try {
                // Załaduj sterownik JDBC
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                // Połączenie się z bazą danych
                Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

                // Zrób SQL-Inquiry do weryfikowania loginu
                String query = "SELECT COUNT(*) FROM login WHERE login = ? AND haslo = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, email);
                statement.setString(2, password);

                // Uruchom skrypt SQL i pobierz resultat
                ResultSet resultSet = statement.executeQuery();
                resultSet.next();
                int count = resultSet.getInt(1);

                // Zamknij zasoby
                resultSet.close();
                statement.close();
                connection.close();

                // Meldunek true, jeżeli dane logowania są poprawne
                return count > 0;
            }
            catch (Exception e) {
                e.printStackTrace();
                // Meldunek false, jeśli wystąpi błąd
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // Pokaż wiadomość za pomocą modułu Toast bazującą na wyniku walidacji loginu
            if (result) {
                Toast.makeText(MainActivity.this, "Zalogowano pomyślnie", Toast.LENGTH_SHORT).show();
                // Tutaj może stać kod która otwiera następną aktywność
                Intent intent = new Intent(MainActivity.this, activity_menu.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(MainActivity.this, "Niepoprawne dane logowania", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
