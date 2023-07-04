package com.example.auth_tw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;

public class VerfNumActivity extends AppCompatActivity {

    private EditText codeNum1, codeNum2, codeNum3, codeNum4, codeNum5, codeNum6;
    private TextView textMobile, ResetPin;
    private Button CheckPin;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    String verificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verf_num);

        progressBar = findViewById(R.id.progressBar);
        CheckPin = findViewById(R.id.BtCheckCode);
        CheckPin.setOnClickListener( v -> CheckPincode());
        ResetPin = findViewById(R.id.ResendCode);
        ResetPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VerfNumActivity.this, "Код повротно отправлен",Toast.LENGTH_SHORT).show();
                mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    }
                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                    }
                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    }};
                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(mAuth)
                                .setPhoneNumber("+7" + getIntent().getStringExtra("mobile"))       // Phone number to verify
                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                .setActivity(VerfNumActivity.this)                 // (optional) Activity for callback binding
                                // If no activity is passed, reCAPTCHA verification can not be used.
                                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });
        textMobile = findViewById(R.id.textMobile);
        textMobile.setText(String.format(
                "+7-%s", getIntent().getStringExtra("mobile")
        ));
        codeNum1 = findViewById(R.id.codeNum1);
        codeNum2 = findViewById(R.id.codeNum2);
        codeNum3 = findViewById(R.id.codeNum3);
        codeNum4 = findViewById(R.id.codeNum4);
        codeNum5 = findViewById(R.id.codeNum5);
        codeNum6 = findViewById(R.id.codeNum6);
        verificationId = getIntent().getStringExtra("verificationId");

        setupCodeInputs();
    }
    private void setupCodeInputs(){
        codeNum1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    codeNum2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        codeNum2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    codeNum3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        codeNum3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    codeNum4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        codeNum4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    codeNum5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        codeNum5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    codeNum6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void CheckPincode(){
        progressBar.setVisibility(View.VISIBLE);
        CheckPin.setVisibility(View.INVISIBLE);
        if (codeNum1.getText().toString().trim().isEmpty() ||
                codeNum2.getText().toString().trim().isEmpty() ||
                codeNum3.getText().toString().trim().isEmpty() ||
                codeNum4.getText().toString().trim().isEmpty() ||
                codeNum5.getText().toString().trim().isEmpty() ||
                codeNum6.getText().toString().trim().isEmpty()){
            Toast.makeText(VerfNumActivity.this, "Код неверный",Toast.LENGTH_SHORT).show();
        }else{
            if (verificationId != null) {
                String code = codeNum1.getText().toString().trim() +
                        codeNum2.getText().toString().trim() +
                        codeNum3.getText().toString().trim() +
                        codeNum4.getText().toString().trim() +
                        codeNum5.getText().toString().trim() +
                        codeNum6.getText().toString().trim();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
                FirebaseAuth
                        .getInstance()
                        .signInWithCredential(credential)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressBar.setVisibility(View.VISIBLE);
                            CheckPin.setVisibility(View.INVISIBLE);
                            Intent intent = new Intent(VerfNumActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            progressBar.setVisibility(View.GONE);
                            CheckPin.setVisibility(View.VISIBLE);
                            Toast.makeText(VerfNumActivity.this, "Код неверный", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }
}