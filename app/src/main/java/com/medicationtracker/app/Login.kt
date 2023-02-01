package com.medicationtracker.app

import android.content.Intent
import android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG
import android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.login.*
import java.util.concurrent.Executor

class Login : AppCompatActivity(){
    lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this@Login, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                //override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                //    super.onAuthenticationError(errorCode, errString)
                //    setContentView(R.layout.login)
                //    loginnote.text = "Authentication Error"
                //    Toast.makeText(this@Login,
                //        "Authentication Error $errString",
                //        Toast.LENGTH_SHORT
                //    )
                //        .show()
                //}

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
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Login using fingerprint authentication")
            //.setNegativeButtonText("Use Password instead")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
            .setConfirmationRequired(false)
            .build()

        login.setOnClickListener()
        {
            biometricPrompt.authenticate(promptInfo)


        }
    }
}