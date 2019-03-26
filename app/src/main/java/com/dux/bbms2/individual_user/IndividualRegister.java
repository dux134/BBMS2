package com.dux.bbms2.individual_user;

import android.app.ProgressDialog;
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

import com.dux.bbms2.R;
import com.dux.bbms2.util.CheckNetworkConnection;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class IndividualRegister extends AppCompatActivity {

    private EditText fullname,mobile,email,password,confirmPassword,dob,address,state,city,pincode;
    private Spinner gender,bloodGroup;
    private Button register;
    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_register);

        fullname = findViewById(R.id.in_register_fullname);
        mobile = findViewById(R.id.in_register_mobile);
        email = findViewById(R.id.in_register_email);
        password = (EditText) findViewById(R.id.in_register_password);
        confirmPassword = (EditText) findViewById(R.id.in_register_confirm_password);
        dob = findViewById(R.id.in_register_dob);
        address = findViewById(R.id.in_register_address);
        state = findViewById(R.id.in_register_state);
        city = findViewById(R.id.in_register_city);
        pincode = findViewById(R.id.in_register_pincode);
        gender = findViewById(R.id.spinner_gender);
        bloodGroup = findViewById(R.id.spinner_bloodGroup);
        register = findViewById(R.id.in_register_registerbutton);

        String[] genderList = {"Male","Female","other"};
        String[] bloodGroupList = {"A+","O+","B+","AB+","A-","O-","B-","AB-"};

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genderList);
        ArrayAdapter<String> bloodGroupAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, bloodGroupList);

        gender.setAdapter(genderAdapter);
        bloodGroup.setAdapter(bloodGroupAdapter);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerAction();
            }
        });

    }

    private void registerAction() {
        if(!isEditTextValid(fullname))
            return;
        if (!isEditTextValid(mobile))
            return;
        if (!isEditTextValid(dob))
            return;
        if (!isEditTextValid(address))
            return;
        if (!isEditTextValid(city))
            return;
        if (!isEditTextValid(pincode))
            return;
        if (!isEditTextValid(state))
            return;
        if (!isEditTextValid(password))
            return;


        if(!CheckNetworkConnection.isConnectionAvailable(IndividualRegister.this)) {
            Toast.makeText(IndividualRegister.this,"Please check your Intenet connection!!",Toast.LENGTH_LONG).show();
            return;
        }

        String mEmail = this.email.getText().toString();
        String mPassword = this.password.getText().toString();
        String mConfirmPassword = this.confirmPassword.getText().toString();

        if (!validateEmail(mEmail))
            return;

        if(!mPassword.equals(mConfirmPassword)) {
            confirmPassword.setError("Password does not match!!");
        }

        if (!validatePassword(mPassword))
            return;

        createUser(mEmail,mPassword);

    }

    private boolean isEditTextValid(EditText text) {
        String str = text.getText().toString();

        if(str.isEmpty()) {
            text.setError("Field is Empty!!");
            text.setFocusable(true);
            return false;
        }
        return true;
    }

    private void createUserInDatabase(String uid) {
        Map<String, Object> studentData = new HashMap<>();

        studentData.put("fullname",fullname.getText().toString());
        studentData.put("mobile",mobile.getText().toString());
        studentData.put("email",email.getText().toString());
        studentData.put("account_type","individual");
        studentData.put("state",state.getText().toString());
        studentData.put("city",city.getText().toString());
        studentData.put("address",address.getText().toString());
        studentData.put("pincode",pincode.getText().toString());
        studentData.put("gender",gender.getSelectedItem().toString());
        studentData.put("blood_group",bloodGroup.getSelectedItem().toString());
        studentData.put("dob",dob.getText().toString());
        studentData.put("uid",uid);

        db.collection("individual_user").document(uid)
                .set(studentData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.d(TAG, "DocumentSnapshot successfully written!");
                        Toast.makeText(IndividualRegister.this, "User successfully registered!!!", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(LoginActivity.this,Dashboard.class));

                        mAuth.getCurrentUser().sendEmailVerification();
                        mAuth.signOut();
                        progressDialog.dismiss();
                        onBackPressed();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error writing document", e);
                        mAuth.signOut();
                        Toast.makeText(IndividualRegister.this, "Error while registering!!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        onBackPressed();

                    }
                });

    }

    private void createUser(String email,String password) {

        progressDialog = new ProgressDialog(IndividualRegister.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            createUserInDatabase(user.getUid());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(IndividualRegister.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                        // ...
                    }
                });
    }

    private boolean validateEmail(String string) {
        if((!string.contains("@")) || (!string.contains("."))) {
            email.setError("Please enter a valid mail!");
            return false;
        }
        return true;
    }

    private boolean validatePassword(String string) {
        if(string.length() < 8) {
            password.setError("Password should be greater than 8 character");
            return false;
        }
        return true;
    }
}
