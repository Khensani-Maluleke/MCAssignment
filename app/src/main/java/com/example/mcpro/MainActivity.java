package com.example.mcpro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    Button councellorbtn;
    Button counsulterbtn;
    String username;
    TextView heading;
    TextView hi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //FINDING ID
        councellorbtn=findViewById(R.id.CounsellorBTN);
        counsulterbtn=findViewById(R.id.ConsulterBTN);
        heading=findViewById(R.id.textView);
        hi=findViewById(R.id.hellouserview);
        //including user's name on page
        username=getIntent().getStringExtra("name");
        hi.setText("Hello "+ username);
        heading.setText(" Welcome TO Mobile Counselling");

        counsulterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), Consulterpage.class);
                startActivity(i);
            }
        });
        councellorbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), Consellorpage.class);
                startActivity(i);

            }
        });




    }
}