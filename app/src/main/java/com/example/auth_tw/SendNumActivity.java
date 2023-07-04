package com.example.auth_tw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SendNumActivity extends AppCompatActivity {

    Button NextAct;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(SendNumActivity.this, MainActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_num);

        mAuth = FirebaseAuth.getInstance();

        final EditText imputmobile = findViewById(R.id.editTextPhone);
        final ProgressBar progressBar = findViewById(R.id.progressBar);

        NextAct = findViewById(R.id.BtSendCode);
        NextAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imputmobile.getText().toString().trim().isEmpty()){
                    Toast.makeText(SendNumActivity.this, "Введите номер телефона", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                NextAct.setVisibility(View.INVISIBLE);

                mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    progressBar.setVisibility(View.GONE);
                    NextAct.setVisibility(View.VISIBLE);
                }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        progressBar.setVisibility(View.GONE);
                        NextAct.setVisibility(View.VISIBLE);
                        Toast.makeText(SendNumActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        Log.d("DT",e.getMessage());
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        progressBar.setVisibility(View.GONE);
                        NextAct.setVisibility(View.VISIBLE);
                        Toast.makeText(SendNumActivity.this, "Код для верификации отправлен",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), VerfNumActivity.class);
                        intent.putExtra("mobile",imputmobile.getText().toString());
                        intent.putExtra("verificationId",verificationId);
                        startActivity(intent);
                    }};
                    PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(mAuth)
                                .setPhoneNumber("+7" + imputmobile.getText().toString().trim())       // Phone number to verify
                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                .setActivity(SendNumActivity.this)                 // (optional) Activity for callback binding
                                // If no activity is passed, reCAPTCHA verification can not be used.
                                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });
    }
}