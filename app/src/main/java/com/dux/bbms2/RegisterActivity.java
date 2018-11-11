package com.dux.bbms2;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import static com.dux.bbms2.LoginActivity.mAuth;


public class RegisterActivity extends AppCompatActivity {

    private EditText fullname,email,mobile,password;
    private Button register;
    private Spinner gender,bloodGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullname = findViewById(R.id.textview_fullname);
        email = findViewById(R.id.textview_email);
        mobile = findViewById(R.id.textview_mobile);
//        password = findViewById(R.id.textview_password);

        gender = findViewById(R.id.spinner_gender);
        String[] itemGender = new String[]{"Male","Female","Other"};
        ArrayAdapter<String> adapterGender = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemGender);
        gender.setAdapter(adapterGender);

        Toast.makeText(getApplicationContext(),gender.getSelectedItem().toString(),Toast.LENGTH_LONG).show();


    }

    private void registerUser(String email,String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            Toast.makeText(getApplicationContext(),"User successfully registered",Toast.LENGTH_LONG).show();
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });

    }
}
