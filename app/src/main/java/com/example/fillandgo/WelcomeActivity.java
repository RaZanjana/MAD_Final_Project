package com.example.fillandgo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager mpager;
    private int[] layouts={R.layout.first_slide, R.layout.second_slide, R.layout.third_slide};
    private LinearLayout Dots_Layout;
    private ImageView[] dots;
    private Button BnNext;
    private Button BnSkip;

private MpagerAdapter mpagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

if(new PreferenceManager(this).checkPreference()){
    loadHome();
}
        setContentView(R.layout.activity_welcome);
        mpager=(ViewPager)findViewById(R.id.viewpager);
        mpagerAdapter= new MpagerAdapter(layouts,this);
        mpager.setAdapter(mpagerAdapter);
        Dots_Layout=(LinearLayout)findViewById(R.id.dotsLayout);
        creatDots(0);
        BnNext=(Button)findViewById(R.id.bnNext);
        BnSkip=(Button)findViewById(R.id.bnSkip);
        BnNext.setOnClickListener(this);
        BnSkip.setOnClickListener(this);



        mpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                      creatDots(position);
                      if(position==layouts.length-1){
                          BnNext.setText("Start");
                          BnSkip.setVisibility(View.INVISIBLE);
                      }else{
                          BnNext.setText("Next");
                          BnSkip.setVisibility(View.VISIBLE);
                      }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }
    private void creatDots(int current_position){
        if(Dots_Layout!=null){
            Dots_Layout.removeAllViews();
            dots= new ImageView[layouts.length];
            for(int i =0;i<layouts.length;i++){
                dots[i] = new ImageView(this);
                if(i==current_position){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.active_dots));
                }else{
                    dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.default_dot));
                }
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(4,0,4,0);
                Dots_Layout.addView(dots[i],params);

            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bnNext:
                loadNextSlide();
                break;
            case R.id.bnSkip:
                loadHome();
                new PreferenceManager(this).writePreference();
                break;
        }
    }
    private void loadHome()
    {
        startActivity(new Intent(this, SplashScreen.class));
        finish();
    }
    private void loadNextSlide(){
        int nextSlide=mpager.getCurrentItem()+1;
        if(nextSlide<layouts.length){
            mpager.setCurrentItem(nextSlide);

        }else
        {
            loadHome();
            new PreferenceManager(this).writePreference();
        }
    }

}
