package com.dux.bbms2.bank_user;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dux.bbms2.R;
import com.dux.bbms2.individual_user.IndividualRegister;
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

import static com.dux.bbms2.SplashScreen.mAuth;

public class BloodBankRegister extends AppCompatActivity {

    private EditText fullName,mobile,email,address,pincode,city,state,password,confirmPassword;
    private Button register;
    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank_register);

        fullName = findViewById(R.id.bank_register_fullname);
        mobile = findViewById(R.id.bank_register_mobile);
        email = findViewById(R.id.bank_register_email);
        address = findViewById(R.id.bank_register_address);
        pincode = findViewById(R.id.bank_register_pincode);
        city = findViewById(R.id.bank_register_city);
        state = findViewById(R.id.bank_register_state);
        password = findViewById(R.id.bank_register_password);
        confirmPassword = findViewById(R.id.bank_register_confirm_password);
        register = findViewById(R.id.bank_register_registerbutton);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerAction();
            }
        });
    }

    private void registerAction() {
        if(!isEditTextValid(fullName))
            return;
        if (!isEditTextValid(mobile))
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

        if(!CheckNetworkConnection.isConnectionAvailable(BloodBankRegister.this)) {
            Toast.makeText(BloodBankRegister.this,"Please check your Intenet connection!!",Toast.LENGTH_LONG).show();
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

    private void createUser(String mEmail, String mPassword) {
        progressDialog = new ProgressDialog(BloodBankRegister.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(mEmail, mPassword)
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
                            Toast.makeText(BloodBankRegister.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                        // ...
                    }
                });
    }

    private void createUserInDatabase(String uid) {
        Map<String, Object> studentData = new HashMap<>();

        studentData.put("fullname",fullName.getText().toString());
        studentData.put("mobile",mobile.getText().toString());
        studentData.put("email",email.getText().toString());
        studentData.put("account_type","blood bank");
        studentData.put("state",state.getText().toString());
        studentData.put("city",city.getText().toString());
        studentData.put("address",address.getText().toString());
        studentData.put("pincode",pincode.getText().toString());
        studentData.put("foundedYear","");
        studentData.put("aPositive","0");
        studentData.put("aNegative","0");
        studentData.put("abPositive","0");
        studentData.put("abNegative","0");
        studentData.put("bPositive","0");
        studentData.put("bNegative","0");
        studentData.put("oPositive","0");
        studentData.put("oNegative","0");
        studentData.put("uid",uid);

        db.collection("blood_bank").document(uid)
                .set(studentData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.d(TAG, "DocumentSnapshot successfully written!");
                        Toast.makeText(BloodBankRegister.this, "User successfully registered!!!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(BloodBankRegister.this, "Error while registering!!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        onBackPressed();

                    }
                });

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
