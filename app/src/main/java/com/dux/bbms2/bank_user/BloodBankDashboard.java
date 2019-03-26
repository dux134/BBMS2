package com.dux.bbms2.bank_user;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.dux.bbms2.ChangePassword;
import com.dux.bbms2.LoginActivity;
import com.dux.bbms2.R;
import com.dux.bbms2.individual_user.IndividualUserDashboard;
import com.dux.bbms2.individual_user.IndividualUserDataModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import static com.dux.bbms2.SplashScreen.mAuth;

public class BloodBankDashboard extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressDialog progressDialog;
    private FirebaseUser firebaseUser;
    private BankUserDataModel bloodBankUser = null;
    TextView textView8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank_dashboard);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_blood_bank);
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(BloodBankDashboard.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        textView8 = findViewById(R.id.textView8);
        loadUserDetails();

        CardView updateBlood = findViewById(R.id.update_blood);
        updateBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateBlood.bloodBankUser = bloodBankUser;
                startActivity(new Intent(BloodBankDashboard.this,UpdateBlood.class));
            }
        });

        CardView changePassword = findViewById(R.id.bank_change_password);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BloodBankDashboard.this,ChangePassword.class));
            }
        });
    }

    private void loadUserDetails() {

        final FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        db.collection("blood_bank").document(firebaseUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("TaG", "DocumentSnapshot data: " + document.getData());
                                bloodBankUser = document.toObject(BankUserDataModel.class);

                                assert bloodBankUser != null;
                                textView8.setText("Hi, " + bloodBankUser.getFullname());
                            } else {
                                Log.d("TAG", "No such document");
                            }
                        } else {
                            Log.d("TAG", "get failed with ", task.getException());
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard, menu); //your file name
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.sign_out) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(BloodBankDashboard.this, LoginActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setNegativeButton("No", null).show();
    }
}
