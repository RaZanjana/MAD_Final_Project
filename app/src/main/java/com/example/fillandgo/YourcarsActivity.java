package com.example.fillandgo;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class YourcarsActivity  extends AppCompatActivity {
    private DatabaseManager dbManager;

    private ScrollView scrollView;
    private int buttonWidth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewcars);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbManager = new DatabaseManager( this );

        updateView( );
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.other, menu);
        return super.onCreateOptionsMenu(menu);
    }
    protected void onResume( ) {
        super.onResume( );
        updateView( );
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            startActivity( new Intent(YourcarsActivity.this,MainActivity.class));
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
    public void updateView() {
         Button backButton = new Button( this );
        backButton.setText( "Back");

        backButton.setOnClickListener( new View.OnClickListener( ) {
            public void onClick(View v) {


              Intent intent = new Intent(YourcarsActivity.this,MainActivity.class);
              startActivity(intent);
            }
        });

        ArrayList<Car_get_set> cars = dbManager.selectAll();
        RelativeLayout layout = new RelativeLayout(this);
        ScrollView scrollView = new ScrollView(this);
        RadioGroup group = new RadioGroup(this);
        for (Car_get_set car : cars) {
            RadioButton rb = new RadioButton(this);
            rb.setId(car.getId());
            rb.setText(car.getCarName()+" " +car.getFuelType());

            group.addView(rb);
        }
        // set up event handling
        RadioButtonHandler rbh = new RadioButtonHandler();
        group.setOnCheckedChangeListener(rbh);

        // create a back button

        scrollView.addView(group);
        layout.addView(scrollView);

        // add back button at bottom
        RelativeLayout.LayoutParams params
                = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.setMargins(0, 0, 0, 50);
        layout.addView(backButton, params);

        setContentView(layout);
    }
    private class RadioButtonHandler
            implements RadioGroup.OnCheckedChangeListener {
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // delete candy from database
            dbManager.deleteById(checkedId);
            Toast.makeText(YourcarsActivity.this, "Car deleted",
                    Toast.LENGTH_SHORT).show();

            // update screen
            updateView();
        }
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

}
