package com.dux.bbms2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dux.bbms2.bank_user.BloodBankRegister;
import com.dux.bbms2.individual_user.IndividualRegister;
import com.dux.bbms2.util.CheckNetworkConnection;
import com.dux.bbms2.util.PrefManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private EditText email,password;
    private Button login,register;
    private TextView forgotPassword;
    private ProgressDialog progressDialog;
    private CheckBox individual,bank;

    public static FirebaseAuth mAuth;
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        forgotPassword = findViewById(R.id.textView_forgotpassword);
        email = findViewById(R.id.edittext_email);
        password = findViewById(R.id.edittext_password);
        login = findViewById(R.id.button_signin);
        register = findViewById(R.id.button_register);
        individual = findViewById(R.id.checkBox_individual);
        bank = findViewById(R.id.checkBox_bank);
        
        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mEmail = email.getText().toString();
                String mPassword = password.getText().toString();

                if (!validateEmail(mEmail))
                    return;
                if (!validatePassword(mPassword))
                    return;

                if(!bank.isChecked() && !individual.isChecked()) {
                    Toast.makeText(LoginActivity.this, "Please select the user type", Toast.LENGTH_LONG).show();
                    return;
                }

                loginAction(mEmail,mPassword);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(LoginActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);
                builder.setCancelable(true);
                builder
                        .setMessage("How do you want to register? A individual or as blood bank!")
                        .setPositiveButton("bank", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int i) {
                                startActivity(new Intent(LoginActivity.this,BloodBankRegister.class));
                            }
                        })
                        .setNegativeButton("individual", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(LoginActivity.this,IndividualRegister.class));
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();


            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ForgotPassword.class));
            }
        });

        individual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bank.setChecked(false);
            }
        });
        bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                individual.setChecked(false);
            }
        });
    }

    private void loginAction(final String mEmail, final String mPassword) {

        if (!CheckNetworkConnection.isConnectionAvailable(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        mAuth.signInWithEmailAndPassword(mEmail, mPassword)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            // there was an error
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Authentication failed!", Toast.LENGTH_LONG).show();
                        } else {
                            checkUserInDatabase();
                        }
                    }
                });
    }

    private void checkUserInDatabase() {
        String accountType = "";
        PrefManager prefManager = new PrefManager(LoginActivity.this);
        if(individual.isChecked()) {
            accountType = "individual_user";
            prefManager.setAccountType(accountType);
        } else {
            accountType = "blood_bank";
            prefManager.setAccountType(accountType);

        }

        final DocumentReference docRef = db.collection(accountType).document(mAuth.getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult().exists()) {
                    DocumentSnapshot document = task.getResult();
                    progressDialog.dismiss();
                    if (document != null) {
                        if(Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified()) {
//                                createUserInDatabase(mEmail,mPassword);
                            startActivity(new Intent(LoginActivity.this,SplashScreen.class));
                            finish();
                        } else
                            alertForEmailVerification();
                    } else {
                        Toast.makeText(getApplicationContext(), "try again!", Toast.LENGTH_SHORT).show();
                                mAuth.signOut();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "User not found in database", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    mAuth.signOut();
                }
//                progressDialog.dismiss();
            }
        });
    }

//    private void createUserInDatabase(String mEmail, String mPassword) {
//        Map<String, Object> studentData = ConvetObjectIntoMap.Object(new IndividualUserDataModel("fhkjsh",));
//
//        studentData.put("fullname","");
//        studentData.put("mobile","");
//        studentData.put("email",mAuth.getCurrentUser().getEmail());
//        studentData.put("account_type","individual");
//
//        db.collection("individual_user").document(mAuth.getCurrentUser().getUid())
//                .set(studentData)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        //Log.d(TAG, "DocumentSnapshot successfully written!");
//                        Toast.makeText(LoginActivity.this, "DocumentSnapshot successfully written!", Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(LoginActivity.this,Dashboard.class));
//
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        //Log.w(TAG, "Error writing document", e);
//                        Toast.makeText(LoginActivity.this, "Error writing document", Toast.LENGTH_SHORT).show();
//
//                    }
//                });
//    }

    private void alertForEmailVerification() {
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(LoginActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);
        builder.setCancelable(false);
        builder
                .setMessage("Your email address is not verified! Press send button to send verificaion mail again")
                .setPositiveButton("exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(LoginActivity.this,"Email sent.",Toast.LENGTH_LONG).show();
                                            mAuth.signOut();
                                        }
                                    }
                                });
                        dialogInterface.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
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