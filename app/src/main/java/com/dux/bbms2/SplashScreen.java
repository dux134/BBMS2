package com.dux.bbms2;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dux.bbms2.bank_user.BloodBankDashboard;
import com.dux.bbms2.individual_user.IndividualUserDashboard;
import com.dux.bbms2.util.PrefManager;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {
    public static FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final PrefManager prefManager = new PrefManager(SplashScreen.this);

        mAuth = FirebaseAuth.getInstance();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mAuth.getCurrentUser() != null) {
                    if(prefManager.getAccountType().equalsIgnoreCase("individual_user"))
                        startActivity(new Intent(SplashScreen.this,IndividualUserDashboard.class));
                    else
                        startActivity(new Intent(SplashScreen.this,BloodBankDashboard.class));
                } else
                    startActivity(new Intent(SplashScreen.this,LoginActivity.class));
                finish();
            }
        },1000);
    }
}
