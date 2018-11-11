package com.dux.bbms2.individual_user;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.dux.bbms2.R;
import com.firebase.client.annotations.Nullable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

public class IndividualUserDashboard extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressDialog progressDialog;
    private FirebaseUser firebaseUser;
    private IndividualUserDataModel individualUser = null;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_user_dashboard);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_individual_user);
        setSupportActionBar(toolbar);

        progressDialog = new ProgressDialog(IndividualUserDashboard.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        textView = findViewById(R.id.textView8);
        loadUserDetails();
    }
    private void loadUserDetails() {

        final FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        db.collection("individual_user").document(firebaseUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("TaG", "DocumentSnapshot data: " + document.getData());
                                individualUser = document.toObject(IndividualUserDataModel.class);

                                assert individualUser != null;
                                textView.setText("Hi, "+individualUser.getName() +"\n\n"+individualUser.toString());
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
}
