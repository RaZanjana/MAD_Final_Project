package com.example.fillandgo;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Add_car extends AppCompatActivity {
    RadioGroup radioGroup;
    RadioButton radiobutton;
    EditText editText;
    private DatabaseManager dbManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


  editText=findViewById(R.id.carname);
        dbManager = new DatabaseManager( this );
        setContentView(R.layout.add_car);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.other, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void addCar(View v){
        radioGroup=findViewById(R.id.radioGroup2);
        int radioId= radioGroup.getCheckedRadioButtonId();
        radiobutton=findViewById(radioId);
        editText=findViewById(R.id.carname);
        String carName= editText.getText().toString();
        String fueltype=radiobutton.getText().toString();
        if(carName.matches("")){
            Toast.makeText(this,"Please enter your car name",Toast.LENGTH_SHORT).show();
        }else{
            try {

               Car_get_set friend = new Car_get_set( 0, carName, fueltype );
                dbManager.insert( friend );
                Toast.makeText( this, "Car added", Toast.LENGTH_SHORT ).show( );
            } catch( Exception nfe ) {
                Toast.makeText( this, "error", Toast.LENGTH_LONG ).show( );
            }

            // clear data
            editText.setText( "" );
            Toast.makeText(this,"Successfully added your car",Toast.LENGTH_SHORT);
        }


    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            startActivity( new Intent(Add_car.this,MainActivity.class));
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.addcar:
                Intent addIntent = new Intent(this,Add_car.class);
                this.startActivity(addIntent);
                return true;
            case R.id.delete:
                Intent deleteIntent = new Intent(this,YourcarsActivity.class);
                this.startActivity(deleteIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);


        }
    }
    public void goBack( View v ) {
        Intent intent = new Intent(Add_car.this,MainActivity.class);
        startActivity(intent);
    }
        }
