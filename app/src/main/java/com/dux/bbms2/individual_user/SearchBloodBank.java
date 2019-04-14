package com.dux.bbms2.individual_user;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.dux.bbms2.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

public class SearchBloodBank extends AppCompatActivity {
    private EditText pincode;
    public static Spinner spinner;
    private Button searchButton;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<SearchBloodBankDataModel> list = new ArrayList<>();
    private ProgressDialog progressDialog;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_blood_bank);

        Toolbar toolbar = findViewById(R.id.toolbar_search_blood_bank);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        pincode = findViewById(R.id.search_blood_bank_pincode);
        spinner = findViewById(R.id.search_blood_bank_spinner);
        searchButton = findViewById(R.id.search_blood_bank_button);
        recyclerView = findViewById(R.id.search_blood_bank_recycler);

        String[] bloodGroupList = {"A+", "O+", "B+", "AB+", "A-", "O-", "B-", "AB-"};
        ArrayAdapter<String> bloodGroupAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, bloodGroupList);
        spinner.setAdapter(bloodGroupAdapter);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.clear();
                adapter.notifyDataSetChanged();
                loadDetails(pincode.getText().toString());
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new SearchBloodBankMyAdapter(list, new SearchBloodBankMyAdapter.RecyclerItemListener() {
            @Override
            public void onClick(View view, final int adapterPosition) {

                final AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(SearchBloodBank.this, R.style.Theme_AppCompat_DayNight_Dialog);
                builder.setCancelable(false);
                builder
                        .setMessage("Are you sure you want to Call?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @SuppressLint("MissingPermission")
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:+91"+list.get(adapterPosition).getMobile()));
                                if (ActivityCompat.checkSelfPermission(SearchBloodBank.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }, SearchBloodBank.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private void loadDetails(String pincode) {
        progressDialog = new ProgressDialog(SearchBloodBank.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        final FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        db.collection("blood_bank").whereEqualTo("pincode",pincode)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot querySnapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen error", e);
                            return;
                        }
                        assert querySnapshot != null;
                        for (DocumentChange change : querySnapshot.getDocumentChanges()) {
                            if (change.getType() == DocumentChange.Type.ADDED) {
                                SearchBloodBankDataModel bankUserDataModel = new SearchBloodBankDataModel();
                                bankUserDataModel.setFullname(change.getDocument().get("fullname").toString());
                                bankUserDataModel.setMobile(change.getDocument().get("mobile").toString());
                                bankUserDataModel.setEmail(change.getDocument().get("email").toString());
                                bankUserDataModel.setAccount_type(change.getDocument().get("account_type").toString());
                                bankUserDataModel.setState(change.getDocument().get("state").toString());
                                bankUserDataModel.setCity(change.getDocument().get("city").toString());
                                bankUserDataModel.setAddress(change.getDocument().get("address").toString());
                                bankUserDataModel.setPincode(change.getDocument().get("pincode").toString());
                                bankUserDataModel.setFoundedYear(change.getDocument().get("foundedYear").toString());
                                bankUserDataModel.setAbPositive(change.getDocument().get("abPositive").toString());
                                bankUserDataModel.setAbNegative(change.getDocument().get("abNegative").toString());
                                bankUserDataModel.setaNegative(change.getDocument().get("aNegative").toString());
                                bankUserDataModel.setoNegative(change.getDocument().get("oNegative").toString());
                                bankUserDataModel.setbNegative(change.getDocument().get("bNegative").toString());
                                bankUserDataModel.setaPositive(change.getDocument().get("aPositive").toString());
                                bankUserDataModel.setbPositive(change.getDocument().get("bPositive").toString());
                                bankUserDataModel.setoPositive(change.getDocument().get("oPositive").toString());
                                bankUserDataModel.setUid(change.getDocument().get("uid").toString());

                                if(spinner.getSelectedItem().toString().equalsIgnoreCase("A+"))
                                    bankUserDataModel.setBloodShown(bankUserDataModel.getaPositive());
                                if(spinner.getSelectedItem().toString().equalsIgnoreCase("B+"))
                                    bankUserDataModel.setBloodShown(bankUserDataModel.getbPositive());
                                if(spinner.getSelectedItem().toString().equalsIgnoreCase("AB+"))
                                    bankUserDataModel.setBloodShown(bankUserDataModel.getAbPositive());
                                if(spinner.getSelectedItem().toString().equalsIgnoreCase("O+"))
                                    bankUserDataModel.setBloodShown(bankUserDataModel.getoPositive());
                                if(spinner.getSelectedItem().toString().equalsIgnoreCase("A-"))
                                    bankUserDataModel.setBloodShown(bankUserDataModel.getaNegative());
                                if(spinner.getSelectedItem().toString().equalsIgnoreCase("B-"))
                                    bankUserDataModel.setBloodShown(bankUserDataModel.getbNegative());
                                if(spinner.getSelectedItem().toString().equalsIgnoreCase("AB-"))
                                    bankUserDataModel.setBloodShown(bankUserDataModel.getAbNegative());
                                if(spinner.getSelectedItem().toString().equalsIgnoreCase("O-"))
                                    bankUserDataModel.setBloodShown(bankUserDataModel.getoNegative());

                                list.add(bankUserDataModel);
                                adapter.notifyDataSetChanged();
                            }

                            String source = querySnapshot.getMetadata().isFromCache() ?
                                    "local cache" : "server";
                            Log.d(TAG, "Data fetched from " + source);
                        }
                        if(list.isEmpty())
                            Toast.makeText(SearchBloodBank.this,"No bank found in this pincode",Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
