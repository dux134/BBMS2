package com.dux.bbms2.bank_user;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dux.bbms2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

public class UpdateBlood extends AppCompatActivity {
    private Button apAdd,apSub,anAdd,anSub,abpAdd,abpSub,abnAdd,abnSub,opAdd,opSub,onAdd,onSub,bpAdd,bpSub,bnAdd,bnSub;
    private EditText ap,an,bp,bn,op,on,abp,abn;
    private FirebaseUser firebaseUser;

    private String units = " Units";
    public static BankUserDataModel bloodBankUser = null;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_blood);

        Toolbar toolbar = findViewById(R.id.toolbar_update_blood);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        ap = findViewById(R.id.editText_a_p);
        an = findViewById(R.id.editText_a_n);
        bp = findViewById(R.id.editText_b_p);
        bn = findViewById(R.id.editText_b_n);
        op = findViewById(R.id.editText_o_p);
        on = findViewById(R.id.editText_o_n);
        abp = findViewById(R.id.editText_ab_p);
        abn = findViewById(R.id.editText_ab_n);

        ap.setText(bloodBankUser.getaPositive()+units);
        an.setText(bloodBankUser.getaNegative()+units);
        bp.setText(bloodBankUser.getbPositive()+units);
        bn.setText(bloodBankUser.getbNegative()+units);
        op.setText(bloodBankUser.getoPositive()+units);
        on.setText(bloodBankUser.getoNegative()+units);
        abp.setText(bloodBankUser.getAbPositive()+units);
        abn.setText(bloodBankUser.getAbNegative()+units);

        apAdd = findViewById(R.id.a_p_plus_button);
        apAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = ap.getText().toString().substring(0,2);
                ap.setText(addButtonAction(str)+units);
            }
        });
        anAdd = findViewById(R.id.a_n_plus_button);
        anAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = an.getText().toString().substring(0,2);
                an.setText(addButtonAction(str)+units);
            }
        });
        opAdd = findViewById(R.id.o_p_plus_button);
        opAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = op.getText().toString().substring(0,2);
                op.setText(addButtonAction(str)+units);
            }
        });
        onAdd = findViewById(R.id.o_n_plus_button);
        onAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = on.getText().toString().substring(0,2);
                on.setText(addButtonAction(str)+units);
            }
        });
        bpAdd = findViewById(R.id.b_p_plus_button);
        bpAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = bp.getText().toString().substring(0,2);
                bp.setText(addButtonAction(str)+units);
            }
        });
        bnAdd = findViewById(R.id.b_n_plus_button);
        bnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = bn.getText().toString().substring(0,2);
                bn.setText(addButtonAction(str)+units);
            }
        });
        abpAdd = findViewById(R.id.ab_p_plus_button);
        abpAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = abp.getText().toString().substring(0,2);
                abp.setText(addButtonAction(str)+units);
            }
        });
        abnAdd = findViewById(R.id.ab_n_plus_button);
        abnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = abn.getText().toString().substring(0,2);
                abn.setText(addButtonAction(str)+units);
            }
        });

        apSub = findViewById(R.id.a_p_sub_button);
        apSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = ap.getText().toString().substring(0,2);
                ap.setText(subButtonAction(str)+units);
            }
        });
        anSub = findViewById(R.id.a_n_sub_button);
        anSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = an.getText().toString().substring(0,2);
                an.setText(subButtonAction(str)+units);
            }
        });
        opSub = findViewById(R.id.o_p_sub_button);
        opSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = op.getText().toString().substring(0,2);
                op.setText(subButtonAction(str)+units);
            }
        });
        onSub = findViewById(R.id.o_n_sub_button);
        onSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = on.getText().toString().substring(0,2);
                on.setText(subButtonAction(str)+units);
            }
        });
        bpSub = findViewById(R.id.b_p_sub_button);
        bpSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = bp.getText().toString().substring(0,2);
                bp.setText(subButtonAction(str)+units);
            }
        });
        bnSub = findViewById(R.id.b_n_sub_button);
        bnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = bn.getText().toString().substring(0,2);
                bn.setText(subButtonAction(str)+units);
            }
        });
        abnSub = findViewById(R.id.ab_n_sub_button);
        abnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = abn.getText().toString().substring(0,2);
                abn.setText(subButtonAction(str)+units);
            }
        });
        abpSub = findViewById(R.id.ab_p_sub_button);
        abpSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = abp.getText().toString().substring(0,2);
                abn.setText(subButtonAction(str)+units);
            }
        });


        Button update = findViewById(R.id.updateBlood_update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAction();
            }
        });
    }

    private void updateAction() {


        progressDialog = new ProgressDialog(UpdateBlood.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Map<String ,Object> studentData = new HashMap<>();
        studentData.put("aPositive",stringToInt(ap.getText().toString()));
        studentData.put("aNegative",stringToInt(an.getText().toString()));
        studentData.put("abPositive",stringToInt(abp.getText().toString()));
        studentData.put("abNegative",stringToInt(abn.getText().toString()));
        studentData.put("bPositive",stringToInt(bp.getText().toString()));
        studentData.put("bNegative",stringToInt(bn.getText().toString()));
        studentData.put("oPositive",stringToInt(op.getText().toString()));
        studentData.put("oNegative",stringToInt(on.getText().toString()));

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("blood_bank").document(firebaseUser.getUid())
                .update(studentData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                    }
                });
    }

    private String stringToInt(String str) {
        str = str + "units";
        String[] string = str.split(" ");

        return string[0];
    }

    private String addButtonAction(String str) {
        str = str + "bbms";
        String[] string = str.split(" ");
        int unit = Integer.valueOf(string[0]);
        unit++;

        return unit+"";
    }

    private String subButtonAction(String str) {
        str = str + "bbms";
        String[] string = str.split(" ");

        if(string[0].equals("0")) {
            Toast.makeText(getApplicationContext(),"Minimum units cannot be less than zero!!!",Toast.LENGTH_LONG).show();
            return "0";
        }
        int unit = Integer.valueOf(string[0]);
        unit--;

        return unit+"";
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
