package com.dux.bbms2;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        TextView text = findViewById(R.id.textView4);
        text.setText("Hi, "+LoginActivity.mAuth.getCurrentUser().getDisplayName());
        Button signout = findViewById(R.id.button_signout123);

//        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                .setDisplayName("Shubham Tomer")
//                .build();
//
//        ActivityLogin.mAuth.getCurrentUser().updateProfile(profileUpdates)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Log.d("", "User profile updated.");
//                        }
//                    }
//                });



        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity.mAuth.signOut();
                startActivity(new Intent(Dashboard.this,LoginActivity.class));

            }
        });
    }
}
