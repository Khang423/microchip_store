package com.example.microchip.activity.auth;

import androidx.biometric.BiometricManager;

import android.hardware.biometrics.BiometricManager;
import android.os.Bundle;

import com.example.microchip.R;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);

        BiometricManager biometricManager = BiometricManager.from(getApplicationContext());
    }
}