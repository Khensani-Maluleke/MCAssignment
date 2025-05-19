package com.example.mcpro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Consellorpage extends AppCompatActivity {
    CheckBox Anxiety;
    CheckBox Addiction;
    CheckBox Abuse;
    CheckBox Academics;
    CheckBox Depression;
    CheckBox General;
    Button sub;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_consellorpage);
        //FINDING ID
        Anxiety=findViewById(R.id.AnxietyID);
        Addiction=findViewById(R.id.AddictionID);
        Abuse=findViewById(R.id.AbuseID);
        Academics=findViewById(R.id.AcademicID);
        Depression=findViewById(R.id.DeprissionID);
        General=findViewById(R.id.GeneralID);
        sub=findViewById(R.id.consellorsubbtn);
        //CHECKING STATE
        Boolean checkAnxiety=Anxiety.isChecked();
        Boolean checkAddiction=Addiction.isChecked();
        Boolean checkAbuseState=Abuse.isChecked();
        Boolean checkAcademicsState=Academics.isChecked();
        Boolean checkDepressionState=Depression.isChecked();
        Boolean checkAnxietyState=Anxiety.isChecked();

        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(i);
            }
        });
    }
}