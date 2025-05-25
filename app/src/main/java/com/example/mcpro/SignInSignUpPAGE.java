package com.example.mcpro;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class SignInSignUpPAGE extends AppCompatActivity {
    Button sign_up;
    Button login;
    Button resetBtn;
    EditText pass;
    EditText user;
    EditText mail;

    // Your PHP endpoint
    String serverURL = "https://lamp.ms.wits.ac.za/home/s2815983/userSignin.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup_or_signin);

        sign_up = findViewById(R.id.BtnSignUp);
        login = findViewById(R.id.BtnLogin);
        user = findViewById(R.id.usernametext);
        pass = findViewById(R.id.passwordtext);
        mail=findViewById(R.id.emailedittext);
        resetBtn = findViewById(R.id.resetButton);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = user.getText().toString().trim();
                String password = pass.getText().toString().trim();

                if (username.equals("") || password.equals("")) {
                    Toast.makeText(getApplicationContext(), "ENTER BOTH THE USERNAME AND PASSWORD.", Toast.LENGTH_SHORT).show();
                } else {
                    setLogin(username, password);
                }
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Directly go to MainActivity (you can implement actual login logic here)
                Intent i = new Intent(getApplicationContext(), SignUpPage.class);
                startActivity(i);
            }
        });

        resetBtn.setOnClickListener(v -> {
            String email = mail.getText().toString().trim();
            if (!email.isEmpty()) {
                sendResetRequest(email);
            } else {
                Toast.makeText(this, "Please enter your email.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setLogin(String username, String password) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(serverURL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);

                    String postData = "username=" + URLEncoder.encode(username, "UTF-8")
                            + "&password=" + URLEncoder.encode(password, "UTF-8");

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(postData);
                    writer.flush();
                    writer.close();
                    os.close();

                    int responseCode = conn.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        String inputLine;
                        StringBuilder response = new StringBuilder();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        String serverResponse = response.toString();

                        runOnUiThread(() -> {
                            Toast.makeText(getApplicationContext(), serverResponse, Toast.LENGTH_SHORT).show();

                            if (serverResponse.toLowerCase().contains("success")) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.putExtra("name", username);
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else {
                        runOnUiThread(() -> {
                            Toast.makeText(getApplicationContext(), "Server Error: " + responseCode, Toast.LENGTH_SHORT).show();
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
                }
            }
        });
        thread.start();
    }

    private void sendResetRequest(String email) {
        String URL = "https://lamp.ms.wits.ac.za/home/s2815983/request_password_reset.php";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, URL,
                response -> Toast.makeText(this, response, Toast.LENGTH_LONG).show(),
                error -> Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }
        };
        queue.add(request);
    }
}