package com.example.aystent_ratownika;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
            Intent intent = new Intent(MainActivity.this, rejestracja.class);
            startActivity(intent);
        });

        exitButton.setOnClickListener(v -> finish());
    }

    private class ValidateLoginTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String email = params[0];
            String password = params[1];

            Connection connection = null;
            PreparedStatement statement = null;
            ResultSet resultSet = null;

            try {
                // Załaduj sterownik JDBC
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

                // Połączenie się z bazą danych
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

                // Zrób SQL-Inquiry do weryfikowania loginu
                String query = "SELECT COUNT(*) FROM login WHERE login = ? AND haslo = ?";
                statement = connection.prepareStatement(query);
                statement.setString(1, email);
                statement.setString(2, password);

                // Uruchom skrypt SQL i pobierz resultat
                resultSet = statement.executeQuery();
                resultSet.next();
                int count = resultSet.getInt(1);

                // Meldunek true, jeżeli dane logowania są poprawne
                return count > 0;
            } catch (Exception e) {
                e.printStackTrace();
                // Meldunek false, jeśli wystąpi błąd
                return false;
            } finally {
                try {
                    if (resultSet != null) {
                        resultSet.close();
                    }
                    if (statement != null) {
                        statement.close();
                    }
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Toast.makeText(MainActivity.this, "Zalogowano pomyślnie", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, activity_menu.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(MainActivity.this, "Niepoprawne dane logowania", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
