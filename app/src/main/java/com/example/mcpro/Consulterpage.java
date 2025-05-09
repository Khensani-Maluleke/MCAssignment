package com.example.mcpro;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.RadioButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Consulterpage extends AppCompatActivity {
    RadioButton Anxiety;
    RadioButton Addiction;
    RadioButton Abuse;
    RadioButton Academics;
    RadioButton Depression;
    RadioButton General;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_consulterpage);
        //FINDING ID
        Anxiety = findViewById(R.id.radioAnxiety);
        Addiction=findViewById(R.id.radioAddiction);
        Abuse=findViewById(R.id.radioAbuse);
        Academics=findViewById(R.id.radioAcademics);
        Depression=findViewById(R.id.radioDepression);
        General=findViewById(R.id.GeneralID);
        //CHECKING STATE
        Boolean checkAnxiety=Anxiety.isChecked();
        Boolean checkAddiction=Addiction.isChecked();
        Boolean checkAbuseState=Abuse.isChecked();
        Boolean checkAcademicsState=Academics.isChecked();
        Boolean checkDepressionState=Depression.isChecked();
        Boolean checkAnxietyState=Anxiety.isChecked();
    }
}