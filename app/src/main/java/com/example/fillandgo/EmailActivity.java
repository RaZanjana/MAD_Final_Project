package com.example.fillandgo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EmailActivity extends AppCompatActivity {
    private EditText mEditTextTo;
    private EditText mEditTextSubject;
    private EditText mEditTextMessage;
    String id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emaillayout);
        mEditTextTo=findViewById(R.id.edit_text_to);
        mEditTextSubject=findViewById(R.id.edit_text_subject);
        mEditTextMessage=findViewById(R.id.edit_text_message);
         id = getIntent().getStringExtra("FUEL_STATION_ID");
        Button buttonSend = findViewById(R.id.button_send);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMail();
            }
        });
    }
    private void sendMail(){

        String[] recipients= {"neranjanshalinda@gmail.com"};
        String subject = mEditTextSubject.getText().toString();
        String message = "Fuel Sation ID: "+id+"\n"+mEditTextMessage.getText().toString();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL,recipients);
        intent.putExtra(Intent.EXTRA_SUBJECT,subject);
        intent.putExtra(Intent.EXTRA_TEXT,message);
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent,"Choose an email client"));


    }
}
