package com.dux.bbms2;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dux.bbms2.individual_user.IndividualUserDashboard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {
    private TextView details;
    private EditText oldPassword, newPassword, confirmPassword;
    private Button updateButton;
    private FirebaseUser user;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Toolbar toolbar = findViewById(R.id.toolbar_change_password);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        user = FirebaseAuth.getInstance().getCurrentUser();

        details = findViewById(R.id.change_password_details);
        oldPassword = findViewById(R.id.change_password_currentpassword);
        newPassword = findViewById(R.id.change_password_newpassword);
        confirmPassword = findViewById(R.id.change_password_confirmpass);
        updateButton = findViewById(R.id.change_password_submit);

        details.setText("Changing password for : \n"+user.getEmail());

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String oldPass = oldPassword.getText().toString().trim();
                String newPass = newPassword.getText().toString().trim();
                String confirmPass = confirmPassword.getText().toString().trim();

                if (TextUtils.isEmpty(oldPass)) {
                    oldPassword.setError("can't be empty");
                    return;
                }
                if (TextUtils.isEmpty(newPass)) {
                    newPassword.setError("can't be empty");
                    return;
                }
                if (TextUtils.isEmpty(confirmPass)) {
                    confirmPassword.setError("can't be empty");
                    return;
                }

                if (newPass.equals(confirmPass)) {
                    updatePassword(user.getEmail(), oldPass, newPass);
                } else {
                    Toast.makeText(getApplicationContext(),"Password not matched",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void updatePassword(String email, String oldPassword, final String newPassword) {
        progressDialog = new ProgressDialog(ChangePassword.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        AuthCredential credential = EmailAuthProvider.getCredential(email, oldPassword);

        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (!task.isSuccessful()) {
                                Snackbar snackbar_fail = Snackbar
                                        .make(getCurrentFocus(), "Something went wrong. Please try again later", Snackbar.LENGTH_LONG);
                                snackbar_fail.show();
                            } else {
                                Snackbar snackbar_su = Snackbar
                                        .make(getCurrentFocus(), "Password Successfully Modified", Snackbar.LENGTH_LONG);
                                snackbar_su.show();
                            }
                        }
                    });
                } else {
                    Snackbar snackbar_su = Snackbar
                            .make(getCurrentFocus(), "Authentication Failed", Snackbar.LENGTH_LONG);
                    snackbar_su.show();
                }
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
