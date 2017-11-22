package com.example.babacarkadams.myapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private final String ACTIVITY = "Login";

    private EditText mLoginEmailField;
    private EditText mLoginPasswordField;
    private Button mLoginBtn;
    private Button mCreateAccountBnt;

    private ProgressDialog mProgress;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabase.setPersistenceEnabled(true);

        if (mAuth.getCurrentUser() != null) {
            startMainActivity();
        }

        setContentView(R.layout.activity_login);

        mProgress = new ProgressDialog(this);
        mLoginEmailField = (EditText) findViewById(R.id.loginEmailField);
        mLoginPasswordField = (EditText) findViewById(R.id.loginPasswordField);
        mLoginBtn = (Button) findViewById(R.id.loginBtn);
        mCreateAccountBnt = (Button) findViewById(R.id.btnCreateAccount);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkLogin();
            }


        });

        mCreateAccountBnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    private void createAccount() {
        String email = mLoginEmailField.getText().toString().trim();
        String password = mLoginPasswordField.getText().toString().trim();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            mProgress.setMessage("Registering...");
            mProgress.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
//                        Toast.makeText(LoginActivity.this, "Register successful", Toast.LENGTH_LONG).show();
//                        checkUserExist();
                        startMainActivity();
                    } else {
                        Toast.makeText(LoginActivity.this, "error Registering", Toast.LENGTH_LONG).show();
                        Log.e(ACTIVITY, task.getException().getMessage());
                    }
                    mProgress.dismiss();
                }
            });
        }
    }

    private void checkLogin() {
        String email = mLoginEmailField.getText().toString().trim();
        String password = mLoginPasswordField.getText().toString().trim();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

            mProgress.setMessage("cheking Login...");
            mProgress.show();

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
//                        Toast.makeText(LoginActivity.this, "Register successful", Toast.LENGTH_LONG).show();
//                        checkUserExist();

                        startMainActivity();
                    } else {
                        Toast.makeText(LoginActivity.this, "error Login", Toast.LENGTH_LONG).show();
                    }
                    mProgress.dismiss();
                }


            });
        }
    }

    private void startMainActivity() {
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        this.finish();
    }


}
