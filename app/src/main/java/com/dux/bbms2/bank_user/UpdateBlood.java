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

        ap.setText(bloodBankUser.getaPositive()+"");
        an.setText(bloodBankUser.getaNegative()+"");
        bp.setText(bloodBankUser.getbPositive()+"");
        bn.setText(bloodBankUser.getbNegative()+"");
        op.setText(bloodBankUser.getoPositive()+"");
        on.setText(bloodBankUser.getoNegative()+"");
        abp.setText(bloodBankUser.getAbPositive()+"");
        abn.setText(bloodBankUser.getAbNegative()+"");

        apAdd = findViewById(R.id.a_p_plus_button);
        apAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = ap.getText().toString();
                ap.setText(addButtonAction(str));
            }
        });
        anAdd = findViewById(R.id.a_n_plus_button);
        anAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = an.getText().toString().substring(0,2);
                an.setText(addButtonAction(str));
            }
        });
        opAdd = findViewById(R.id.o_p_plus_button);
        opAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = op.getText().toString().substring(0,2);
                op.setText(addButtonAction(str));
            }
        });
        onAdd = findViewById(R.id.o_n_plus_button);
        onAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = on.getText().toString().substring(0,2);
                on.setText(addButtonAction(str));
            }
        });
        bpAdd = findViewById(R.id.b_p_plus_button);
        bpAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = bp.getText().toString().substring(0,2);
                bp.setText(addButtonAction(str));
            }
        });
        bnAdd = findViewById(R.id.b_n_plus_button);
        bnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = bn.getText().toString().substring(0,2);
                bn.setText(addButtonAction(str));
            }
        });
        abpAdd = findViewById(R.id.ab_p_plus_button);
        abpAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = abp.getText().toString().substring(0,2);
                abp.setText(addButtonAction(str));
            }
        });
        abnAdd = findViewById(R.id.ab_n_plus_button);
        abnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = abn.getText().toString().substring(0,2);
                abn.setText(addButtonAction(str));
            }
        });

        apSub = findViewById(R.id.a_p_sub_button);
        apSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = ap.getText().toString().substring(0,2);
                ap.setText(subButtonAction(str));
            }
        });

        anSub = findViewById(R.id.a_n_sub_button);
        anSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = an.getText().toString().substring(0,2);
                an.setText(subButtonAction(str));
            }
        });

        opSub = findViewById(R.id.o_p_sub_button);
        opSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = op.getText().toString().substring(0,2);
                op.setText(subButtonAction(str));
            }
        });

        onSub = findViewById(R.id.o_n_sub_button);
        onSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = on.getText().toString().substring(0,2);
                on.setText(subButtonAction(str));
            }
        });

        bpSub = findViewById(R.id.b_p_sub_button);
        bpSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = bp.getText().toString().substring(0,2);
                bp.setText(subButtonAction(str));
            }
        });

        bnSub = findViewById(R.id.b_n_sub_button);
        bnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = bn.getText().toString().substring(0,2);
                bn.setText(subButtonAction(str));
            }
        });

        abnSub = findViewById(R.id.ab_n_sub_button);
        abnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = abn.getText().toString().substring(0,2);
                abn.setText(subButtonAction(str));
            }
        });

        abpSub = findViewById(R.id.ab_p_sub_button);
        abpSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = abp.getText().toString().substring(0,2);
                abn.setText(subButtonAction(str));
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
        studentData.put("aPositive",Integer.parseInt(ap.getText().toString()));
        studentData.put("aNegative",Integer.parseInt(an.getText().toString()));
        studentData.put("abPositive",Integer.parseInt(abp.getText().toString()));
        studentData.put("abNegative",Integer.parseInt(abn.getText().toString()));
        studentData.put("bPositive",Integer.parseInt(bp.getText().toString()));
        studentData.put("bNegative",Integer.parseInt(bn.getText().toString()));
        studentData.put("oPositive",Integer.parseInt(op.getText().toString()));
        studentData.put("oNegative",Integer.parseInt(on.getText().toString()));

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
