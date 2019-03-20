package com.example.hebun.piggybank;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import pl.droidsonroids.gif.GifImageView;


public class MainActivity extends AppCompatActivity {
    ImageView shines;
    Fragment loginScreen;
    GifImageView piggys;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginScreen = new LoginScreenFragment();



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


               goStartScreen();



                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_left_to_right,R.anim.exit_to_right);
                fragmentTransaction.replace(R.id.main_frame, loginScreen);
                fragmentTransaction.commit();

            }
        },5000);


    }
    private void goStartScreen(){
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.animations);
        shines = findViewById(R.id.shine);
        piggys = findViewById(R.id.piggy);
        animation.reset();
        shines.clearAnimation();
        piggys.clearAnimation();

        shines.startAnimation(animation);
        piggys.startAnimation(animation);

    }



    }


