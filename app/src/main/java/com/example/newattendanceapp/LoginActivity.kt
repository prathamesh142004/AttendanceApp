package com.example.newattendanceapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.newattendanceapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private val binding :ActivityLoginBinding by lazy{
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth=FirebaseAuth.getInstance()
        binding.LoginBtn.setOnClickListener {
            val username=binding.userName.text.toString()
            val password=binding.password.text.toString()

            if(username.isEmpty()||password.isEmpty()){
                Toast.makeText(this, "Fill all detials", Toast.LENGTH_SHORT).show()
            }else {
                auth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Sign-In Successfull", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(
                                this,
                                "Sign-In Failed: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
        binding.signUpButton.setOnClickListener {
            startActivity(Intent(this,RegistrationActivity::class.java))
        }
    }
}