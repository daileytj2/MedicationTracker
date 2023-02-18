package com.medicationtracker.app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.login.*
import java.util.concurrent.Executor

class Login : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {


            super.onCreate(savedInstanceState)
            setContentView(R.layout.login)
            var executor: Executor = ContextCompat.getMainExecutor(this)
            var biometricPrompt: BiometricPrompt = BiometricPrompt(this@Login, executor,
                object : BiometricPrompt.AuthenticationCallback() {


                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        loginnote.text = "Authentication Success!"
                        Toast.makeText(this@Login, "Authentication Success!", Toast.LENGTH_SHORT)
                            .show()
                        val intent = Intent(this@Login, MainActivity::class.java)
                        // start your next activity
                        startActivity(intent)

                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        loginnote.text = "Authentication Failed"
                        Toast.makeText(this@Login, "Authentication Failed", Toast.LENGTH_SHORT)
                            .show()
                    }
                })

        if (BiometricManager.from(this).canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL) == BiometricManager.BIOMETRIC_SUCCESS) {
            login.setOnClickListener()
            {
                var promptInfo: BiometricPrompt.PromptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Biometric Authentication")
                    .setSubtitle("Login using fingerprint authentication")
                    .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                    .setConfirmationRequired(false)
                    .build()
                biometricPrompt.authenticate(promptInfo)


            }
        }
        else{
            login.setOnClickListener()
            {
                val login = Intent(this@Login, MainActivity::class.java)
                startActivity(login)
            }
        }
    }
}